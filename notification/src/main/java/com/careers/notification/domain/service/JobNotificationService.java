package com.careers.notification.domain.service;

import com.careers.notification.infrastructure.enumeration.FcmTopicName;
import com.careers.notification.domain.service.fcm.SendMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JobNotificationService {

    public static final String NEW_JOB_NOTIFICATION_TITLE = "신규 공고 알림";

    private final SendMessageService fcmSendMessageService;

    public void pushNewJobsNotification(FcmTopicName topicName, List<String> bodies) {
        if (bodies.isEmpty()) {
            return ;
        }

        fcmSendMessageService.sendMessageToTopic(topicName.name(), NEW_JOB_NOTIFICATION_TITLE, createNotificationBody(bodies));
    }

    private String createNotificationBody(List<String> bodies) {
        if (bodies.size() == 1) {
            return bodies.get(0) + " 공고가 등록됐어요";
        }

        return bodies.get(0) + " 공고 외 " + (bodies.size() - 1) + "건 신규 공고가 등록됐어요";
    }
}
