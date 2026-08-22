package dev.nmarulo.ahorraco_api.app.payments.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MarkPaidPaymentReq {
    
    /**
     * Participante al que se le marcara la cuota como pagada.
     */
    private String participantPublicId;
    
    /**
     * Mes de la cuota.
     */
    private LocalDate month;
    
}
