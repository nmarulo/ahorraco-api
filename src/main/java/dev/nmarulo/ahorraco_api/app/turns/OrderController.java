package dev.nmarulo.ahorraco_api.app.turns;

import dev.nmarulo.ahorraco_api.app.turns.dtos.FindOrderRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pools/{poolPublicId}/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final TurnService turnService;
    
    @GetMapping
    public ResponseEntity<FindOrderRes> findOrder(@PathVariable UUID poolPublicId) {
        return ResponseEntity.ok(this.turnService.findOrder(poolPublicId));
    }
    
}
