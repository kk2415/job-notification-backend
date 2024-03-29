package com.careers.job.infrastructure.jpaentity;

import com.careers.job.infrastructure.enumeration.Company;
import com.careers.job.infrastructure.jpaentity.base.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "job")
@Entity
public class JobEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Company company;

    private String noticeEndDate;
    private String jobGroup;
    private Boolean active;
    private Boolean isPushSent;

    public static JobEntity of(
            Long id,
            String title,
            Company company,
            String url,
            String noticeEndDate,
            String jobGroup,
            Boolean active,
            Boolean isPushSent
    ) {
        return new JobEntity(
                id,
                title,
                url,
                company,
                noticeEndDate,
                jobGroup,
                active,
                isPushSent
        );
    }

    public void update(
            String title,
            String url,
            Company company,
            String noticeEndDate,
            String jobGroup,
            Boolean active,
            Boolean isPushSent
    ) {
        this.title = title;
        this.url = url;
        this.company = company;
        this.noticeEndDate = noticeEndDate;
        this.jobGroup = jobGroup;
        this.active = active;
        this.isPushSent = isPushSent;
    }

    public void push() {
        isPushSent = Boolean.TRUE;
    }
}
