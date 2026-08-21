package dev.nmarulo.ahorraco_api.app.pools.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreatePoolReq {
    
    private String name;
    
    /**
     * Cuota mensual.
     */
    private BigDecimal monthlyFee;
    
    /**
     * Número de participantes.
     */
    private Integer numParticipants;
    
    /**
     * Fecha de inicio.
     */
    private LocalDate startDate;
    
    /**
     * Dia de vencimiento.
     */
    private Integer paymentDueDay;
    
    /**
     * Nota libre para el grupo (por ejemplo, cómo se paga).
     */
    private String notes;
    
    /**
     * Código de gestión elegido por el organizador. Si no se envía, lo genera
     * el servidor y llega en la respuesta.
     */
    private String managementCode;
    
}
