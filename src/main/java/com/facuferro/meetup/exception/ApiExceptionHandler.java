package com.facuferro.meetup.exception;


import com.facuferro.meetup.api.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {



    @ExceptionHandler({MeetupException.class})
    public ResponseEntity<ErrorResponse> handleMeetupNotFoundException(MeetupException ex,
                                                                       WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getLocalizedMessage()).build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }


    @ExceptionHandler({DataIntegrity.class})
    public ResponseEntity<ErrorResponse> handleMeetupNotFoundException(DataIntegrity ex,
                                                                            WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getLocalizedMessage()).build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({WrongUserOrPassword.class})
    public ResponseEntity<ErrorResponse> handleMeetupNotFoundException(WrongUserOrPassword ex,
                                                                            WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(ex.getLocalizedMessage()).build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> otherExcption(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("internal server error").build();
        logger.error("unexpected error", ex);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({WrongState.class})
    public ResponseEntity<ErrorResponse> handleMeetupNotFoundException(WrongState ex,
                                                                       WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getLocalizedMessage()).build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }


}
