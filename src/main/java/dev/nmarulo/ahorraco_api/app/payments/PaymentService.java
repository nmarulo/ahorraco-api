package dev.nmarulo.ahorraco_api.app.payments;

import dev.nmarulo.ahorraco_api.app.participants.Participant;
import dev.nmarulo.ahorraco_api.app.participants.ParticipantRepository;
import dev.nmarulo.ahorraco_api.app.payments.dtos.*;
import dev.nmarulo.ahorraco_api.app.pools.Pool;
import dev.nmarulo.ahorraco_api.app.turns.Turn;
import dev.nmarulo.ahorraco_api.app.turns.TurnRepository;
import dev.nmarulo.ahorraco_api.commons.exception.BadRequestException;
import dev.nmarulo.ahorraco_api.commons.services.AccessPoolService;
import dev.nmarulo.ahorraco_api.commons.util.DateUtils;
import dev.nmarulo.ahorraco_api.commons.util.UuidUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Las cuotas del mes.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    private final ParticipantRepository participantRepository;
    
    private final TurnRepository turnRepository;
    
    private final AccessPoolService accessPoolService;
    
    /**
     * Marcar una cuota como pagada.
     */
    @Transactional
    public MarkPaidPaymentRes markPaid(final UUID poolPublicId, final MarkPaidPaymentReq request) {
        validateRequest(request);
        
        final var pool = this.accessPoolService.getByPublicId(poolPublicId);
        final var payment = findOrNewPaymentByPoolAndParticipantAndMonth(request.getParticipantPublicId(),
                                                                         pool,
                                                                         request.getMonth());
        
        payment.setMarked(true);
        
        return PaymentMapper.toMarkPaidPaymentRes(this.paymentRepository.save(payment));
    }
    
    private void validateRequest(MarkPaidPaymentReq request) {
        validateRequest(request.getParticipantPublicId(), request.getMonth());
    }
    
    /**
     * Las cuotas registradas del participante.
     */
    @Transactional(readOnly = true)
    public FindAllPaymentRes findAllByParticipant(final UUID poolPublicId, final UUID participantPublicId) {
        final var pool = this.accessPoolService.getByPublicId(poolPublicId);
        final var participant = getParticipant(pool, participantPublicId);
        final var paymentList = this.paymentRepository.findAllByPoolAndParticipantOrderByMonthAsc(pool, participant);
        
        return PaymentMapper.toFindAllPaymentRes(paymentList);
    }
    
    /**
     * El organizador confirma el pago de la cuota de un participante.
     */
    @Transactional
    public ConfirmReceivedPaymentRes confirmReceived(final UUID poolPublicId,
                                                     final String managementCode,
                                                     final ConfirmReceivedPaymentReq request) {
        validateRequest(request);
        
        final var pool = this.accessPoolService.getByPublicId(poolPublicId);
        
        this.accessPoolService.requireOrganizer(pool, managementCode);
        
        final var payment = findOrNewPaymentByPoolAndParticipantAndMonth(request.getParticipantPublicId(),
                                                                         pool,
                                                                         request.getMonth());
        
        payment.setConfirmed(true);
        
        return PaymentMapper.toConfirmReceivedPaymentRes(this.paymentRepository.save(payment));
    }
    
    /**
     * Consultar las cuotas de un mes.
     */
    @Transactional(readOnly = true)
    public FindAllByMonthPaymentRes findAllByMonth(final UUID poolPublicId,
                                                   final String managementCode,
                                                   final LocalDate month) {
        monthValidateRequest(month);
        
        final var pool = this.accessPoolService.getByPublicId(poolPublicId);
        
        this.accessPoolService.requireOrganizer(pool, managementCode);
        
        checkDrawIsDone(pool);
        
        final var monthWithFirstDay = DateUtils.withFirstDayMonth(month);
        Collector<Payment, ?, Map<Long, Payment>> toMap = Collectors.toMap(payment -> payment.getParticipant()
                                                                                             .getId(),
                                                                           payment -> payment);
        final var paymentMap = this.paymentRepository.findAllByPoolAndMonth(pool, monthWithFirstDay)
                                                     .stream()
                                                     .collect(toMap);
        
        final var beneficiary = getTurn(pool, monthWithFirstDay).getParticipant();
        Function<Participant, FindAllByMonthPaymentRes.PaymentRes> mapper = participant -> PaymentMapper.toByMonthPaymentRes(
            participant,
            paymentMap.get(participant.getId()));
        final var participantList = this.participantRepository.findAllByPoolOrderByIdAsc(pool)
                                                              .stream()
                                                              .filter(participant -> !Objects.equals(participant.getId(),
                                                                                                     beneficiary.getId()))
                                                              .map(mapper)
                                                              .toList();
        
        return new FindAllByMonthPaymentRes(monthWithFirstDay, participantList);
    }
    
    private Participant getParticipant(final Pool pool, final UUID participantPublicId) {
        return this.participantRepository.findByPoolAndPublicId(pool, participantPublicId)
                                         .orElseThrow(() -> new BadRequestException(
                                             "El participante no existe en la lista de participantes."));
    }
    
    private void checkDrawIsDone(final Pool pool) {
        if (!this.turnRepository.existsByPool(pool)) {
            throw new BadRequestException("Todavía no se ha sorteado el orden de cobro.");
        }
    }
    
    /**
     * Quien cobra ese mes no paga su cuota.
     */
    private void checkIsNotTheBeneficiary(final Turn turn, final Participant participant) {
        final var participantTurn = turn.getParticipant();
        
        if (Objects.equals(participantTurn.getId(), participant.getId())) {
            throw new BadRequestException("Quien cobra este mes no paga cuota.");
        }
    }
    
    private Turn getTurn(final Pool pool, final LocalDate month) {
        return this.turnRepository.findByPoolAndMonth(pool, month)
                                  .orElseThrow(() -> new BadRequestException("Ese mes no es de esta porra."));
    }
    
    private Payment newPayment(final Pool pool, final Participant participant, final LocalDate month) {
        final var payment = new Payment();
        
        payment.setPool(pool);
        payment.setParticipant(participant);
        payment.setMonth(month);
        
        return payment;
    }
    
    private void validateRequest(ConfirmReceivedPaymentReq request) {
        validateRequest(request.getParticipantPublicId(), request.getMonth());
    }
    
    private void validateRequest(String participantPublicId, LocalDate month) {
        if (StringUtils.isBlank(participantPublicId)) {
            throw new BadRequestException("El participante no puede estar vacío.");
        }
        
        monthValidateRequest(month);
    }
    
    private void monthValidateRequest(LocalDate month) {
        if (month == null) {
            throw new BadRequestException("El mes no puede estar vacío.");
        }
        
        if (!DateUtils.isDateEqualOrBeforeCurrentMonth(month)) {
            throw new BadRequestException("Ese mes todavía no ha llegado.");
        }
    }
    
    private Payment findOrNewPaymentByPoolAndParticipantAndMonth(String participantPublicId,
                                                                 Pool pool,
                                                                 LocalDate month) {
        final var uuid = UuidUtils.toUuid(participantPublicId,
                                          () -> new BadRequestException(
                                              "El identificador del participante no tiene un formato válido."));
        final var participant = getParticipant(pool, uuid);
        
        checkDrawIsDone(pool);
        
        final var monthWithFirstDay = DateUtils.withFirstDayMonth(month);
        final var turn = getTurn(pool, monthWithFirstDay);
        
        checkIsNotTheBeneficiary(turn, participant);
        
        Supplier<Payment> paymentSupplier = () -> newPayment(pool, participant, monthWithFirstDay);
        
        return this.paymentRepository.findByPoolAndParticipantAndMonth(pool, participant, monthWithFirstDay)
                                     .orElseGet(paymentSupplier);
    }
    
}
