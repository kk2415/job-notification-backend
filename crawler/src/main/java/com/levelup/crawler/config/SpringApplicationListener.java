package com.levelup.crawler.config;

import com.levelup.job.crawler.Crawler;
import com.levelup.job.domain.model.Job;
import com.levelup.job.domain.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 스프링 컨테이너 초기화 직후 실행됨
 * 필요에 따라 ContextStartedEvent 대신 상황에 맞게 다른 이벤트를 넣어줄 수도 있다
 * */
@Slf4j
@RequiredArgsConstructor
@Component
public class SpringApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final List<Crawler> crawlers;
    private final JobService jobService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Starting ContextRefreshedEvent of SpringApplicationListener");
        log.error("hello");
        crawlers.forEach(crawler -> {
            List<Job> crawledJobs = crawler.crawling();
            jobService.saveIfAbsent(crawledJobs, crawler.getCompany());

            List<Job> deleteJobs = jobService.getNotMatched(crawledJobs, crawler.getCompany());
            jobService.deleteAll(deleteJobs);
        });
    }
}