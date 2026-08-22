package dev.nmarulo.ahorraco_api.app.turns;

import dev.nmarulo.ahorraco_api.app.turns.dtos.CreateDrawReq;
import dev.nmarulo.ahorraco_api.app.turns.dtos.CreateDrawRes;
import dev.nmarulo.ahorraco_api.commons.constant.ApiHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pools/{poolPublicId}/draw")
@RequiredArgsConstructor
public class TurnController {
    
    private final TurnService turnService;
    
    @PostMapping
    public ResponseEntity<CreateDrawRes> createDraw(@PathVariable UUID poolPublicId,
                                                    @RequestHeader(name = ApiHeaders.MANAGEMENT_CODE,
                                                                   required = false) String managementCode,
                                                    @RequestBody CreateDrawReq request) {
        return ResponseEntity.ok(this.turnService.createDraw(poolPublicId, managementCode, request));
    }
    
}
