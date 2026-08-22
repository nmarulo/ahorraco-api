package dev.nmarulo.ahorraco_api.app.payments.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ConfirmReceivedPaymentReq {
    
    private String participantPublicId;
    
    private LocalDate month;
    
}
