package com.ecommerce.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler  {



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> res = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String code = err.getCode();
            String msg = err.getDefaultMessage();

            String fieldName = ((FieldError)err).getField();

            res.put(fieldName, msg);
            res.put("errorCode", code);

        });
        return new  ResponseEntity<Map<String, String>>(res, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> myResourceNotFoundException(ResourceNotFoundException e){
        return null;
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> myGenericException(Exception e){
//
//        Map<String, String> res = new HashMap<>();
//        res.put("message", e.getMessage());
//
//        return new ResponseEntity<Map<String, String>>(res, HttpStatus.BAD_REQUEST);
//    }


}
