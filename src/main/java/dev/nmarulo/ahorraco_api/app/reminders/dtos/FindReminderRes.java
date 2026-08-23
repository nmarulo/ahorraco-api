package dev.nmarulo.ahorraco_api.app.reminders.dtos;

import lombok.Data;

import java.time.LocalDate;

/**
 * Mensajes recordatorios.
 */
@Data
public class FindReminderRes {
    
    private LocalDate month;
    
    /**
     * Saludo con el mes y el nombre de la porra.
     */
    private String greeting;
    
    /**
     * Quién cobra este mes, su turno y la cuota que toca pagar.
     */
    private String beneficiary;
    
    /**
     * Quién falta por pagar y a quién le falta la confirmación del ingreso.
     */
    private String debtors;
    
    /**
     * Enlace a la información de la cuota.
     */
    private String link;
    
    /**
     * Notas del organizador.
     */
    private String paymentDetails;
    
}
