package com.seyitkoc.handler;

import com.seyitkoc.entity.RootEntity;
import com.seyitkoc.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@ControllerAdvice // exceptionları global olarak yakalamak için kullanılır
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BaseException.class}) // BaseException sınıfından türetilen hataları yakalar
    public ResponseEntity<RootEntity<Void>> handleBaseException(BaseException exception, WebRequest request) throws UnknownHostException {
        RootEntity<Void> rootEntity = RootEntity.error(createApiError(exception.getMessage(),request));

        return ResponseEntity.badRequest().body(rootEntity);
    }

    public <E> ApiError<E> createApiError(E message, WebRequest request) throws UnknownHostException {
        ApiError<E> apiError = new ApiError<>();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());

        Exception<E> exception = new Exception<>();
        exception.setCreateTime(new Date());
        exception.setHostName(getHostName());
        exception.setPath(request.getDescription(false).substring(4));
        exception.setMessage(message);

        apiError.setException(exception);

        return apiError;
    }

    private String getHostName() throws UnknownHostException {
        try{
            return InetAddress.getLocalHost().getHostName();
        } catch (RuntimeException e) {
            System.out.println("Error while getting hostname: " + e.getMessage());
        }
        return "";
    }
}
