package com.levelup.api.controller.v1.notification;

import com.levelup.api.controller.v1.dto.NotificationDto.NotificationRequest;
import com.levelup.notification.domain.service.fcm.SendMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "알람 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationController {

    private final SendMessageService fcmSendMessageService;

    @Operation(summary = "새로운 공고 알람 발생")
    @PostMapping
    public ResponseEntity<Void> pushNewJobsNotification(NotificationRequest request) {
        return ResponseEntity.ok().build();
    }
}
