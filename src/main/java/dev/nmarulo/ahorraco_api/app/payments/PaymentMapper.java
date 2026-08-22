package dev.nmarulo.ahorraco_api.app.payments;

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

    public static FindAllPaymentRes toFindAllPaymentRes(final List<Payment> payments) {
        final var paymentResList = payments.stream()
                                           .map(PaymentMapper::toPaymentRes)
                                           .toList();

        return new FindAllPaymentRes(paymentResList);
    }

    public static FindAllPaymentRes.PaymentRes toPaymentRes(final Payment payment) {
        return new FindAllPaymentRes.PaymentRes(payment.getMonth(), payment.isMarked());
    }

}
