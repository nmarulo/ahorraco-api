package dev.nmarulo.ahorraco_api.app.pools;

import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolReq;
import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolRes;
import dev.nmarulo.ahorraco_api.app.pools.dtos.FindInvitationPoolRes;
import dev.nmarulo.ahorraco_api.app.pools.dtos.FindPublicIdPoolRes;
import dev.nmarulo.ahorraco_api.commons.constant.ApiHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/pools")
@RequiredArgsConstructor
public class PoolController {
    
    private final PoolService poolService;
    
    @PostMapping
    public ResponseEntity<CreatePoolRes> create(@RequestBody CreatePoolReq request) {
        final var response = this.poolService.create(request);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                        .path("/{pool-public-id}")
                                                        .buildAndExpand(response.getPublicId())
                                                        .toUri();
        
        return ResponseEntity.created(location)
                             .body(response);
    }
    
    @GetMapping("/{poolPublicId}")
    public ResponseEntity<FindPublicIdPoolRes> findByPublicId(@PathVariable UUID poolPublicId,
                                                              @RequestHeader(name = ApiHeaders.MANAGEMENT_CODE,
                                                                             required = false) String managementCode) {
        return ResponseEntity.ok(this.poolService.findByPublicId(poolPublicId, managementCode));
    }
    
    @GetMapping("/invitation/{invitationToken}")
    public ResponseEntity<FindInvitationPoolRes> findByInvitationToken(@PathVariable String invitationToken) {
        return ResponseEntity.ok(this.poolService.findByInvitationToken(invitationToken));
    }
    
}
