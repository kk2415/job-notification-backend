package com.careers.job.domain.service;

import com.careers.common.exception.EntityNotFoundException;
import com.careers.common.exception.ExceptionCode;
import com.careers.job.domain.model.CreateJob;
import com.careers.job.infrastructure.jpaentity.JobEntity;
import com.careers.job.infrastructure.enumeration.Company;
import com.careers.job.infrastructure.enumeration.OrderBy;
import com.careers.job.infrastructure.repository.JobRepository;
import com.careers.job.domain.model.JobSearchCondition;
import com.careers.job.domain.model.Job;
import com.careers.job.domain.model.PagingJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobService {

    private final JobRepository jobRepository;

    @Transactional
    public List<Job> saveIfAbsentAndDrop(List<CreateJob> createJobs) {
        final Map<Company, List<CreateJob>> createJobsByCompany = createJobs.stream()
                .collect(Collectors.groupingBy(CreateJob::getCompany));

        final List<Job> result = new ArrayList<>();
        createJobsByCompany.forEach((Company company, List<CreateJob> jobs) -> {
                    result.addAll(saveIfAbsent(jobs, company));

                    final List<Job> notMatchedJobs = findNotMatchedWith(jobs, company);
                    deleteAll(notMatchedJobs);
                }
        );

        return result;
    }

    @Transactional
    public List<Job> saveIfAbsent(List<CreateJob> createJobs) {
        final Map<Company, List<CreateJob>> createJobsByCompany = createJobs.stream()
                .collect(Collectors.groupingBy(CreateJob::getCompany));

        final List<Job> result = new ArrayList<>();
        createJobsByCompany.forEach((Company company, List<CreateJob> jobs) ->
                result.addAll(saveIfAbsent(jobs, company))
        );

        return result;
    }

    @Transactional
    public List<Job> saveIfAbsent(List<CreateJob> creationJobs, Company company) {
        List<Job> savedJobs = jobRepository.findByCompany(company).stream()
                .map(Job::from)
                .toList();

        List<JobEntity> saveJobs = creationJobs.stream()
                .filter(createJob -> savedJobs.stream().noneMatch(savedJob ->
                                savedJob.title().equals(createJob.getTitle()) &&
                                savedJob.url().equals(createJob.getUrl()))
                )
                .map(CreateJob::toEntity)
                .toList();

        jobRepository.saveAll(saveJobs);

        return saveJobs.stream()
                .map(Job::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagingJob search(
            JobSearchCondition filterCondition,
            OrderBy orderBy,
            Integer page,
            Integer size
    ) {
        final PageImpl<JobEntity> pageJobs = jobRepository.search(
                filterCondition,
                page,
                size
        );

        return PagingJob.of(
                pageJobs.stream()
                        .map(Job::from)
                        .toList(),
                pageJobs.getTotalElements(),
                pageJobs.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public List<Job> getCreatedTodayByCompany(Company company, int page, int size) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        Pageable pageable = PageRequest.of(page, size);

        if (company == null) {
            return jobRepository.findByCreatedAt(startOfDay, endOfDay, pageable).stream()
                    .map(Job::from)
                    .filter(Job::active)
                    .toList();
        }

        return jobRepository.findByCompanyAndCreatedAt(company, startOfDay, endOfDay, pageable).stream()
                .map(Job::from)
                .filter(Job::active)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Job> findNotMatchedWith(List<CreateJob> createJobs, Company company) {
        List<Job> savedJobs = jobRepository.findByCompany(company).stream()
                .map(Job::from)
                .toList();

        return savedJobs.stream()
                .filter(savedJob -> createJobs.stream().noneMatch(createJob ->
                        createJob.getTitle().equals(savedJob.title()) &&
                        createJob.getUrl().equals(savedJob.url()))
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Job> findNotPushedJobs() {
        return jobRepository.findByIsPushSent(false).stream()
                .map(Job::from)
                .toList();
    }

    @Transactional
    public List<Job> push(List<Job> notPushedJobs) {
        List<Long> jobIds = notPushedJobs.stream().map(Job::id).toList();

        List<JobEntity> jobs = jobRepository.findAllById(jobIds);
        jobs.forEach(JobEntity::push);

        return jobs.stream()
                .map(Job::from)
                .toList();
    }

    @Transactional
    public void update(Long findJobId, Job updateJob) {
        JobEntity findJob = jobRepository.findById(findJobId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionCode.JOB_NOT_FOUND));

        findJob.update(
                updateJob.title(),
                updateJob.url(),
                updateJob.company(),
                updateJob.noticeEndDate(),
                updateJob.jobGroup(),
                updateJob.active(),
                updateJob.isPushSent()
        );
    }

    @Transactional
    public void deleteAll(List<Job> jobs) {
        jobRepository.deleteAllById(jobs.stream()
                .map(Job::id)
                .toList());
    }
}
