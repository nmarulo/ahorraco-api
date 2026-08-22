package dev.nmarulo.ahorraco_api.app.participants;

import dev.nmarulo.ahorraco_api.app.participants.dtos.FindAllParticipantRes;
import dev.nmarulo.ahorraco_api.app.participants.dtos.JoinPoolReq;
import dev.nmarulo.ahorraco_api.app.participants.dtos.JoinPoolRes;
import dev.nmarulo.ahorraco_api.commons.constant.ApiHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/pools/{poolPublicId}/participants")
@RequiredArgsConstructor
public class ParticipantController {
    
    private final ParticipantService participantService;
    
    @PostMapping
    public ResponseEntity<JoinPoolRes> join(@PathVariable UUID poolPublicId, @RequestBody JoinPoolReq request) {
        final var response = this.participantService.join(poolPublicId, request);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                        .path("/{participant-public-id}")
                                                        .buildAndExpand(response.getPublicId())
                                                        .toUri();
        
        return ResponseEntity.created(location)
                             .body(response);
    }
    
    @GetMapping
    public ResponseEntity<FindAllParticipantRes> findAll(@PathVariable UUID poolPublicId,
                                                         @RequestHeader(name = ApiHeaders.MANAGEMENT_CODE,
                                                                        required = false) String managementCode) {
        return ResponseEntity.ok(this.participantService.findAll(poolPublicId, managementCode));
    }
    
}
