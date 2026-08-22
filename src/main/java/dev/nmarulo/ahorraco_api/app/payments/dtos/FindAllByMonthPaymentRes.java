package dev.nmarulo.ahorraco_api.app.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllByMonthPaymentRes {
    
    private LocalDate month;
    
    private List<PaymentRes> payments;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentRes {
        
        private String participantPublicId;
        
        private String fullName;
        
        /**
         * Indica si el participante ha dicho que ya pagó.
         */
        private boolean marked;
        
        /**
         * Indica si el organizador ha dado el pago por recibido.
         */
        private boolean confirmed;
        
    }
    
}
