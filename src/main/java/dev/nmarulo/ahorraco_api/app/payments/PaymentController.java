package dev.nmarulo.ahorraco_api.app.payments;

import dev.nmarulo.ahorraco_api.app.payments.dtos.FindAllPaymentRes;
import dev.nmarulo.ahorraco_api.app.payments.dtos.MarkPaidPaymentReq;
import dev.nmarulo.ahorraco_api.app.payments.dtos.MarkPaidPaymentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pools/{poolPublicId}/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/mark-paid")
    public ResponseEntity<MarkPaidPaymentRes> markPaid(@PathVariable UUID poolPublicId,
                                                       @RequestBody MarkPaidPaymentReq request) {
        return ResponseEntity.ok(this.paymentService.markPaid(poolPublicId, request));
    }
    
    @GetMapping("/participant/{participantPublicId}")
    public ResponseEntity<FindAllPaymentRes> findAllByParticipant(@PathVariable UUID poolPublicId,
                                                                  @PathVariable UUID participantPublicId) {
        return ResponseEntity.ok(this.paymentService.findAllByParticipant(poolPublicId, participantPublicId));
    }
    
}
