package dev.nmarulo.ahorraco_api.app.payments;

import dev.nmarulo.ahorraco_api.app.participants.Participant;
import dev.nmarulo.ahorraco_api.app.participants.ParticipantRepository;
import dev.nmarulo.ahorraco_api.app.payments.dtos.FindAllPaymentRes;
import dev.nmarulo.ahorraco_api.app.payments.dtos.MarkPaidPaymentReq;
import dev.nmarulo.ahorraco_api.app.payments.dtos.MarkPaidPaymentRes;
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
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

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
        final var uuid = UuidUtils.toUuid(request.getParticipantPublicId(),
                                          () -> new BadRequestException(
                                              "El identificador del participante no tiene un formato válido."));
        final var participant = getParticipant(pool, uuid);
        
        checkDrawIsDone(pool);
        
        final var monthWithFirstDay = DateUtils.withFirstDayMonth(request.getMonth());
        final var turn = getTurn(pool, monthWithFirstDay);
        
        checkIsNotTheBeneficiary(turn, participant);
        
        Supplier<Payment> paymentSupplier = () -> newPayment(pool, participant, monthWithFirstDay);
        final var payment = this.paymentRepository.findByPoolAndParticipantAndMonth(pool,
                                                                                    participant,
                                                                                    monthWithFirstDay)
                                                  .orElseGet(paymentSupplier);
        
        payment.setMarked(true);
        
        return PaymentMapper.toMarkPaidPaymentRes(this.paymentRepository.save(payment));
    }
    
    private void validateRequest(MarkPaidPaymentReq request) {
        if (StringUtils.isBlank(request.getParticipantPublicId())) {
            throw new BadRequestException("El participante no puede estar vacío.");
        }
        
        if (request.getMonth() == null) {
            throw new BadRequestException("El mes no puede estar vacío.");
        }
        
        //Solo se pueden pagar cuotas del mes actual o anterior.
        if (!DateUtils.isDateEqualOrBeforeCurrentMonth(request.getMonth())) {
            throw new BadRequestException("Ese mes todavía no ha llegado.");
        }
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
    
}
