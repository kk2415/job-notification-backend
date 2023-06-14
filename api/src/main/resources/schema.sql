drop table if exists `hibernate_sequence`;
drop table if exists `job`;
drop table if exists notification;
drop table if exists fcm_topic;
drop table if exists fcm_device_token;
drop table if exists `admin`;

create table hibernate_sequence (
    next_val bigint null
) engine=InnoDB;
insert into hibernate_sequence values (1);

create table `job` (
    job_id bigint not null auto_increment primary key,
    title text not null,
    url text not null,
    company varchar(50) not null,
    notice_end_date varchar(50) not null,
    job_group varchar(50) not null,
    active boolean not null,
    created_at datetime not null default '2022-01-01 00:00:00',
    updated_at datetime not null default '2022-01-01 00:00:00'
) engine=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

create table if not exists notification (
    notification_id bigint not null auto_increment primary key,
    title text not null,
    body text not null,
    receiver_id bigint not null,
    activator_id bigint not null,
    read_at datetime null,
    is_read boolean not null,
    created_at datetime not null default '2022-01-01 00:00:00',
    updated_at datetime not null default '2022-01-01 00:00:00'
) engine=InnoDB default charset=utf8 collate=utf8_general_ci;

create table if not exists fcm_topic (
    fcm_topic_id bigint not null auto_increment primary key,
    topic_name varchar(255) not null,
    created_at datetime not null default '2022-01-01 00:00:00',
    updated_at datetime not null default '2022-01-01 00:00:00'
) engine=InnoDB default charset=utf8 collate=utf8_general_ci;

insert into fcm_topic value (1, 'JOB', CURRENT_TIMESTAMP, current_timestamp);

create table if not exists fcm_device_token (
    fcm_device_token_id bigint not null auto_increment primary key,
    token varchar(255) not null,
    fcm_topic_id bigint null,
    created_at datetime not null default '2022-01-01 00:00:00',
    updated_at datetime not null default '2022-01-01 00:00:00'
) engine=InnoDB default charset=utf8 collate=utf8_general_ci;

create table if not exists `admin` (
    admin_id bigint not null auto_increment primary key,
    username varchar(255) not null,
    `password`  varchar(255) not null,
    created_at datetime not null default '2022-01-01 00:00:00',
    updated_at datetime not null default '2022-01-01 00:00:00'
) engine=InnoDB default charset=utf8 collate=utf8_general_ci;
insert into `admin` (username, `password`, created_at, updated_at) value ('admin', '{noop}Aa123456!', current_timestamp, current_timestamp);
