package dev.nmarulo.ahorraco_api.app.reminders;

import dev.nmarulo.ahorraco_api.app.reminders.dtos.FindReminderRes;
import dev.nmarulo.ahorraco_api.commons.constant.ApiHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/pools/{poolPublicId}/reminder")
@RequiredArgsConstructor
public class ReminderController {
    
    private final ReminderService reminderService;
    
    @GetMapping
    public ResponseEntity<FindReminderRes> findReminder(@PathVariable UUID poolPublicId,
                                                        @RequestHeader(name = ApiHeaders.MANAGEMENT_CODE,
                                                                       required = false) String managementCode,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        return ResponseEntity.ok(this.reminderService.findReminder(poolPublicId, managementCode, month));
    }
    
}
