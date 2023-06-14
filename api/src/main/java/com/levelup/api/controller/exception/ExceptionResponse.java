package com.levelup.api.controller.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {

    private String code;
    private String message;
    private int httpStatus;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timeStamp;

    protected ExceptionResponse() {}

    protected ExceptionResponse(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
        this.timeStamp = LocalDateTime.now();
    }

    public static ExceptionResponse of(String code, String message, int status) {
        return new ExceptionResponse(code, message, status);
    }

    public String toString() {
        return "{\n" +
                "\"code\":" + "\"" + this.code + "\"\n\t" +
                "\"status\":" + "\"" + this.httpStatus + "\"\n\t" +
                "\"message\":" + "\"" + this.message + "\"\n\t" +
                "\"timeStamp\":" + "\"" + this.timeStamp + "\"" +
                "\n}";
    }
}