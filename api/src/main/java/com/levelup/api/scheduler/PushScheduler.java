package com.levelup.api.scheduler;

import com.levelup.job.domain.service.JobService;
import com.levelup.job.domain.vo.JobVO;
import com.levelup.notification.domain.service.JobNotificationService;
import com.levelup.notification.enumeration.FcmTopicName;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableScheduling
public class PushScheduler {

    private final JobService jobService;
    private final JobNotificationService jobNotificationService;

    @Scheduled(cron = "0 0 */1 * * *")
    public void crawlingJobs() {
        List<JobVO> notPushedJobs = jobService.getNotPushedJobs();
        List<JobVO> jobs = jobService.push(notPushedJobs);

        jobNotificationService.pushNewJobsNotification(FcmTopicName.JOB, jobs.stream()
                .map(JobVO::getSubject)
                .toList());
    }
}
