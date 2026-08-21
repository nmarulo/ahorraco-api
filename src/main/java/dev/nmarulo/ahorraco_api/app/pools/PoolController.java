package dev.nmarulo.ahorraco_api.app.pools;

import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolReq;
import dev.nmarulo.ahorraco_api.app.pools.dtos.CreatePoolRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    
}
