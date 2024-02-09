package com.levelup.crawler.presentation.controller.v1.dto;

import com.levelup.crawler.domain.enumeration.Company;
import com.levelup.crawler.domain.model.Job;

import java.time.LocalDateTime;

public class JobDto {

    public record Response(
            String title,
            Company company,
            String url,
            String noticeEndDate,
            LocalDateTime createdAt
    ) {
        public static Response from(Job job) {
            return new Response(
                    job.title(),
                    job.company(),
                    job.url(),
                    job.noticeEndDate(),
                    job.createdAt()
            );
        }
    }
}
