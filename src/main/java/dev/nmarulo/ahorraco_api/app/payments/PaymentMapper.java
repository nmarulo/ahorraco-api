package dev.nmarulo.ahorraco_api.app.payments;

import dev.nmarulo.ahorraco_api.app.participants.Participant;
import dev.nmarulo.ahorraco_api.app.payments.dtos.ConfirmReceivedPaymentRes;
import dev.nmarulo.ahorraco_api.app.payments.dtos.FindAllByMonthPaymentRes;
import dev.nmarulo.ahorraco_api.app.payments.dtos.FindAllPaymentRes;
import dev.nmarulo.ahorraco_api.app.payments.dtos.MarkPaidPaymentRes;

import java.util.List;

public final class PaymentMapper {
    
    private PaymentMapper() {
    }
    
    public static MarkPaidPaymentRes toMarkPaidPaymentRes(final Payment payment) {
        final var participantPublicId = payment.getParticipant()
                                               .getPublicId()
                                               .toString();
        
        return new MarkPaidPaymentRes(participantPublicId, payment.getMonth(), payment.isMarked());
    }
    
    public static ConfirmReceivedPaymentRes toConfirmReceivedPaymentRes(final Payment payment) {
        final var participantPublicId = payment.getParticipant()
                                               .getPublicId()
                                               .toString();
        
        return new ConfirmReceivedPaymentRes(participantPublicId,
                                             payment.getMonth(),
                                             payment.isMarked(),
                                             payment.isConfirmed());
    }
    
    public static FindAllPaymentRes toFindAllPaymentRes(final List<Payment> payments) {
        final var paymentResList = payments.stream()
                                           .map(PaymentMapper::toPaymentRes)
                                           .toList();
        
        return new FindAllPaymentRes(paymentResList);
    }
    
    public static FindAllPaymentRes.PaymentRes toPaymentRes(final Payment payment) {
        return new FindAllPaymentRes.PaymentRes(payment.getMonth(), payment.isMarked(), payment.isConfirmed());
    }
    
    /**
     * NOTA: La cuota puede ser nula, cuando no se ha realizado ningún pago.
     */
    public static FindAllByMonthPaymentRes.PaymentRes toByMonthPaymentRes(final Participant participant,
                                                                          final Payment payment) {
        final var paymentRes = new FindAllByMonthPaymentRes.PaymentRes();
        final var participantPublicId = participant.getPublicId()
                                                   .toString();
        
        paymentRes.setParticipantPublicId(participantPublicId);
        paymentRes.setFullName(participant.getFullName());
        paymentRes.setMarked(false);
        paymentRes.setConfirmed(false);
        
        if (payment != null) {
            paymentRes.setMarked(payment.isMarked());
            paymentRes.setConfirmed(payment.isConfirmed());
        }
        
        return paymentRes;
    }
    
}
