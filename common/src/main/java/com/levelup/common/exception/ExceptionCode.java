package com.levelup.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    EXCEPTION(500, "서버 작업 중 예외가 발생하였습니다."),

    EMAIL_NOT_FOUND(400, "존재하지 않는 이메일입니다."),
    PASSWORD_NOT_FOUND(400, "패스워드를 잘못 입력하셨습니다."),

    JOB_NOT_FOUND(400, "존재하지 않는 채용 공고입니다."),
    ADMIN_NOT_FOUND(400, "존재하지 않는 관리자입니다."),
    USER_NOT_FOUND(400, "존재하지 않는 사용자입니다."),
    IMAGE_NOT_FOUND(400, "이미지를 찾을 수 없습니다."),
    FILE_NOT_FOUND(400, "파일을 찾을 수 없습니다."),

    VOTE_DUPLICATION(400, "추천은 한 번만 가능합니다."),
    EMAIL_DUPLICATION(400, "이미 존재하는 이메일입니다."),
    NICKNAME_DUPLICATION(400, "이미 존재하는 닉네임입니다."),
    CHANNEL_MEMBER_DUPLICATION(400, "이미 채널에 등록된 회원입니다."),
    FILE_DUPLICATION(400, "중복된 파일입니다."),

    SECURITY_CODE_EXPIRED(400, "인증코드가 만료되었습니다."),
    NOT_MATCH_SECURITY_CODE(400, "인증코드가 일치하지 않습니다."),

    NO_PLACE_CHANNEL(400, "스터디/프로젝트 정원이 다 찼습니다."),

    ARTICLE_AUTHORITY_EXCEPTION(403, "작성자 본인이 아닙니다."),
    CHANNEL_AUTHORITY_EXCEPTION(403, "채널 매니저가 아닙니다."),

    INVALID_REQUEST_BODY(400, "HTTP 리퀘스트 바디 유효성 체크 에러"),
    BAD_REQUEST(400, "잘못된 접근입니다"),

    JSOUP_FAIL_CONNECTING(500, "커넥팅에 실패하였습니다."),
    JSOUP_FAIL_HTML_PARSING(500, "HTML 파싱에 실패하였습니다."),

    FCM_TOPIC_NOT_FOUND(400, "존재하지 않는 FCM 토픽입니다."),
    FCM_DEVICE_TOKEN_NOT_FOUND(400, "존재하지 않는 FCM 디바이스 토큰입니다."),

    FILE_EXCEPTION(500,"파일 처리 중 예외가 발생하였습니다."),
    FILE_DELETE_FAILURE_EXCEPTION(500, "파일 삭제에 실패하였습니다."),
    FILE_SIZE_LIMIT_EXCEEDED(500, "요청하신 파일은 크기가 너무 큽니다."),
    ;

    private final int httpStatus;
    private final String message;

    ExceptionCode(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
