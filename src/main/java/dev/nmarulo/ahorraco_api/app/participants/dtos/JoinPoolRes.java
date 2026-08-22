package dev.nmarulo.ahorraco_api.app.participants.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinPoolRes {

    /**
     * Identificador público del participante. Es lo que guarda el cliente para saber quién está
     * usando la porra desde ese navegador; no es una credencial, porque la porra es abierta.
     */
    private String publicId;

}
