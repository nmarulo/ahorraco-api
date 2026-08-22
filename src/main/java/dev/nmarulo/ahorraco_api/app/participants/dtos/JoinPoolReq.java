package dev.nmarulo.ahorraco_api.app.participants.dtos;

import lombok.Data;

@Data
public class JoinPoolReq {
    
    private String invitationToken;
    
    private String fullName;
    
    private String phone;
    
}
