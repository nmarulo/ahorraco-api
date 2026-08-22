package dev.nmarulo.ahorraco_api.app.turns.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Orden de cobro sorteado.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDrawRes {
    
    private List<TurnRes> turns;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TurnRes {
        
        /**
         * Posición en el orden, empezando en 1.
         */
        private Integer position;
        
        private String participantPublicId;
        
        private String fullName;
        
        /**
         * Mes en que cobra; siempre el día 1.
         */
        private LocalDate month;
        
        /**
         * Establece si la posición se reservó en vez de sortearse.
         */
        private boolean pinned;
        
    }
    
}
