package com.xxx.exception.advice;

import com.xxx.exception.pojo.CustomException;
import com.xxx.exception.pojo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandleController {
    @ExceptionHandler(CustomException.class) //监听指定的异常
    public ResponseEntity<ExceptionResult> lyExceptionHandle(CustomException e){
        return ResponseEntity.status(e.getStatus()).body(new ExceptionResult(e));
    }

}
