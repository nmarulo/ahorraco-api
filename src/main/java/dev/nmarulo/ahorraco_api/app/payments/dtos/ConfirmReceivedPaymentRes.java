package dev.nmarulo.ahorraco_api.app.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmReceivedPaymentRes {

    private String participantPublicId;

    private LocalDate month;

    private boolean marked;

    private boolean confirmed;

}
