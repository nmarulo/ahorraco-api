package dev.nmarulo.ahorraco_api.app.payments;

import dev.nmarulo.ahorraco_api.app.payments.dtos.*;
import dev.nmarulo.ahorraco_api.commons.constant.ApiHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    
    @PostMapping("/confirm-received")
    public ResponseEntity<ConfirmReceivedPaymentRes> confirmReceived(@PathVariable UUID poolPublicId,
                                                                     @RequestHeader(name = ApiHeaders.MANAGEMENT_CODE,
                                                                                    required = false) String managementCode,
                                                                     @RequestBody ConfirmReceivedPaymentReq request) {
        return ResponseEntity.ok(this.paymentService.confirmReceived(poolPublicId, managementCode, request));
    }
    
    @GetMapping
    public ResponseEntity<FindAllByMonthPaymentRes> findAllByMonth(@PathVariable UUID poolPublicId,
                                                                   @RequestHeader(name = ApiHeaders.MANAGEMENT_CODE,
                                                                                  required = false) String managementCode,
                                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        return ResponseEntity.ok(this.paymentService.findAllByMonth(poolPublicId, managementCode, month));
    }
    
}
