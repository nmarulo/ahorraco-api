package dev.nmarulo.ahorraco_api.app.pools.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePoolRes {
    
    /**
     * Identificador público de la porra.
     */
    private String publicId;
    
    /**
     * Clave del organizador.
     */
    private String managementCode;
    
    /**
     * Token de invitación.
     */
    private String invitationToken;
    
}
