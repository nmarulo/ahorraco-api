package dev.nmarulo.ahorraco_api.app.pools.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FindPublicIdPoolRes {
    
    private String publicId;
    
    private String name;
    
    private BigDecimal monthlyFee;
    
    private Integer numParticipants;
    
    private LocalDate startDate;
    
    private Integer paymentDueDay;
    
    private String notes;
    
    private String managementCode;
    
    private String invitationToken;
    
    /**
     * Total de participantes que ya han entrado.
     */
    private Long joinedCount;
    
}
