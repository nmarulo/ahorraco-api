package dev.nmarulo.ahorraco_api.app.turns.dtos;

import lombok.Data;

@Data
public class CreateDrawReq {
    
    /**
     * Establece si se debe reservar el turno 1 para el organizador.
     */
    private Boolean organizerFirst;
    
    private String organizerPublicId;
    
}
