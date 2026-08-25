package dev.nmarulo.ahorraco_api.app.reminders;

import dev.nmarulo.ahorraco_api.app.participants.Participant;
import dev.nmarulo.ahorraco_api.app.participants.ParticipantRepository;
import dev.nmarulo.ahorraco_api.app.payments.Payment;
import dev.nmarulo.ahorraco_api.app.payments.PaymentRepository;
import dev.nmarulo.ahorraco_api.app.pools.Pool;
import dev.nmarulo.ahorraco_api.app.reminders.dtos.FindReminderRes;
import dev.nmarulo.ahorraco_api.app.turns.Turn;
import dev.nmarulo.ahorraco_api.app.turns.TurnRepository;
import dev.nmarulo.ahorraco_api.commons.exception.BadRequestException;
import dev.nmarulo.ahorraco_api.commons.services.AccessPoolService;
import dev.nmarulo.ahorraco_api.commons.util.BigDecimalUtils;
import dev.nmarulo.ahorraco_api.commons.util.DateUtils;
import dev.nmarulo.ahorraco_api.configuration.AppProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderService {
    
    private static final Locale SPANISH_LOCALE = Locale.forLanguageTag("es-ES");
    
    private final ParticipantRepository participantRepository;
    
    private final PaymentRepository paymentRepository;
    
    private final TurnRepository turnRepository;
    
    private final AccessPoolService accessPoolService;
    
    private final AppProperties appProperties;
    
    @Transactional(readOnly = true)
    public FindReminderRes findReminder(final UUID poolPublicId, final String managementCode, final LocalDate month) {
        validateMonth(month);
        
        final var pool = this.accessPoolService.getByPublicId(poolPublicId);
        
        this.accessPoolService.requireOrganizer(pool, managementCode);
        
        checkDrawIsDone(pool);
        
        final var monthWithFirstDay = DateUtils.withFirstDayMonth(month);
        final var turn = getTurn(pool, monthWithFirstDay);
        final var response = new FindReminderRes();
        
        response.setMonth(monthWithFirstDay);
        
        final var greetingMsg = greeting(pool, monthWithFirstDay);
        
        response.setGreeting(greetingMsg);
        
        final var beneficiaryMsg = beneficiary(pool, turn, monthWithFirstDay);
        
        response.setBeneficiary(beneficiaryMsg);
        
        final var debtorsMsg = debtors(pool, turn, monthWithFirstDay);
        
        response.setDebtors(debtorsMsg);
        
        final var paymentLink = paymentLink(pool);
        
        response.setLink(paymentLink);
        response.setPaymentDetails(StringUtils.trimToNull(pool.getNotes()));
        
        return response;
    }
    
    private void validateMonth(final LocalDate month) {
        if (month == null) {
            throw new BadRequestException("El mes no puede estar vacío.");
        }
        
        if (!DateUtils.isDateEqualOrBeforeCurrentMonth(month)) {
            throw new BadRequestException("Ese mes todavía no ha llegado.");
        }
    }
    
    private void checkDrawIsDone(final Pool pool) {
        if (!this.turnRepository.existsByPool(pool)) {
            throw new BadRequestException("Todavía no se ha sorteado el orden de cobro.");
        }
    }
    
    private Turn getTurn(final Pool pool, final LocalDate month) {
        return this.turnRepository.findByPoolAndMonth(pool, month)
                                  .orElseThrow(() -> new BadRequestException("Ese mes no es de esta porra."));
    }
    
    private String greeting(final Pool pool, final LocalDate month) {
        final var name = DateUtils.getFullDisplayNameMonth(month, SPANISH_LOCALE);
        
        return "¡Hola! Toca la cuota de %s de la porra «%s».".formatted(name, pool.getName());
    }
    
    private String beneficiary(final Pool pool, final Turn turn, final LocalDate month) {
        final var fullName = turn.getParticipant()
                                 .getFullName();
        final var monthName = DateUtils.getFullDisplayNameMonth(month, SPANISH_LOCALE);
        final var money = BigDecimalUtils.twoFractionDigitsNumberFormat(pool.getMonthlyFee(), SPANISH_LOCALE);
        
        return """
            Este mes cobra %s (turno %d de %d).
            Cuota: %s € · Vence el día %d de %s.""".formatted(fullName,
                                                              turn.getPosition(),
                                                              pool.getNumParticipants(),
                                                              money,
                                                              pool.getPaymentDueDay(),
                                                              monthName);
    }
    
    /**
     * Pendientes de pagar o confirmar. Se omiten los confirmados.
     */
    private String debtors(final Pool pool, final Turn turn, final LocalDate month) {
        final var beneficiaryId = turn.getParticipant()
                                      .getId();
        final var paymentByParticipantId = paymentByParticipantId(pool, month);
        final Predicate<Participant> participantPredicate = participant -> !Objects.equals(participant.getId(),
                                                                                           beneficiaryId);
        final var owing = this.participantRepository.findAllByPoolOrderByIdAsc(pool)
                                                    .stream()
                                                    .filter(participantPredicate)
                                                    .toList();
        final var unpaid = namesByMarked(owing, paymentByParticipantId, false);
        final var unconfirmed = namesByMarked(owing, paymentByParticipantId, true);
        final var lines = new LinkedList<String>();
        
        if (!unpaid.isEmpty()) {
            lines.add("Todavía faltan por pagar: %s.".formatted(String.join(", ", unpaid)));
        }
        
        if (!unconfirmed.isEmpty()) {
            lines.add("Pendientes de que confirme el ingreso: %s.".formatted(String.join(", ", unconfirmed)));
        }
        
        if (lines.isEmpty()) {
            lines.add("¡Ya ha pagado todo el mundo! Gracias.");
        }
        
        return String.join("\n", lines);
    }
    
    /**
     * Lista de nombres de quienes han marcado o no el pago de su cuota.
     */
    private List<String> namesByMarked(final List<Participant> owing,
                                       final Map<Long, Payment> paymentByParticipantId,
                                       final boolean marked) {
        return owing.stream()
                    .filter(participant -> {
                        final var payment = paymentByParticipantId.get(participant.getId());
                        
                        if (payment != null && payment.isConfirmed()) {
                            return false;
                        }
                        
                        return marked == (payment != null && payment.isMarked());
                    })
                    .map(Participant::getFullName)
                    .toList();
    }
    
    private Map<Long, Payment> paymentByParticipantId(final Pool pool, final LocalDate month) {
        final Function<Payment, Long> paymentLongFunction = payment -> payment.getParticipant()
                                                                              .getId();
        final Collector<Payment, ?, Map<Long, Payment>> mapCollector = Collectors.toMap(paymentLongFunction,
                                                                                        payment -> payment);
        
        return this.paymentRepository.findAllByPoolAndMonth(pool, month)
                                     .stream()
                                     .collect(mapCollector);
    }
    
    private String paymentLink(final Pool pool) {
        final var baseUrl = Strings.CI.removeEnd(StringUtils.trimToEmpty(this.appProperties.getWebBaseUrl()), "/");
        
        return """
            Podéis marcar vuestro pago aquí:
            %s/pools/%s/simple""".formatted(baseUrl, pool.getPublicId());
    }
    
}
