package dev.nmarulo.ahorraco_api.app.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllPaymentRes {
    
    private List<PaymentRes> payments;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentRes {
        
        private LocalDate month;
        
        private boolean marked;
        
    }
    
}
