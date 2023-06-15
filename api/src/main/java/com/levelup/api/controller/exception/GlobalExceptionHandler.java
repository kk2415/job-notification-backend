package com.levelup.api.controller.exception;

import com.levelup.common.exception.BusinessException;
import com.levelup.common.exception.EntityNotFoundException;
import com.levelup.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("{} - request uri: {}, message: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        e.printStackTrace();

        return ResponseEntity.status(500).body(ExceptionResponse.of(ExceptionCode.EXCEPTION.name(), e.getMessage(), ExceptionCode.EXCEPTION.getHttpStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("{} - request uri: {}, message: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        e.printStackTrace();

        return ResponseEntity.status(ExceptionCode.INVALID_REQUEST_BODY.getHttpStatus()).body(FieldExceptionResponse.of(ExceptionCode.INVALID_REQUEST_BODY, e.getBindingResult()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.error("{} - request uri: {}, message: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        e.printStackTrace();

        return ResponseEntity.status(ExceptionCode.FILE_SIZE_LIMIT_EXCEEDED.getHttpStatus()).body(ExceptionResponse.of(
                ExceptionCode.FILE_SIZE_LIMIT_EXCEEDED.name(),
                ExceptionCode.FILE_SIZE_LIMIT_EXCEEDED.getMessage(),
                ExceptionCode.FILE_SIZE_LIMIT_EXCEEDED.getHttpStatus())
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("{} - request uri: {}, message: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        e.printStackTrace();

        return ResponseEntity.status(e.getHttpStatus()).body(ExceptionResponse.of(e.getCode(), e.getMessage(), e.getHttpStatus()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        log.error("{} - request uri: {}, message: {}", request.getMethod(), request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(ExceptionResponse.of(e.getCode(), e.getMessage(), e.getHttpStatus()));
    }
}
