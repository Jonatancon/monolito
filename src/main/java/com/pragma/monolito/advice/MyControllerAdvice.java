package com.pragma.monolito.advice;

import com.pragma.monolito.domain.Response;
import com.pragma.monolito.exception.CoreException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<Response> handleExceptionCore(CoreException coreException) {
        Response res = Response.builder().developerMessage(coreException.getMessage())
                .userMessage(coreException.getUserMessage()).status(coreException.getStatus())
                .errorCode(coreException.getCodeError()).moreInfo(coreException.getLocalizedMessage()).build();

        return new ResponseEntity<>(res, HttpStatus.valueOf(coreException.getStatus()));
    }


}
