package dev.nmarulo.ahorraco_api.app.participants.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllParticipantRes {
    
    private List<ParticipantRes> participants;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ParticipantRes {
        
        private String publicId;
        
        private String fullName;
        
        /**
         * Solo lo ve el organizador. Al resto del grupo no le hace falta y no es suyo.
         */
        private String phone;
        
    }
    
}
