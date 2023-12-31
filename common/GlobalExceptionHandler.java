package com.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})//指定拦截哪些Controller
@ResponseBody//下边方法需要返回Json数据
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @return
     */
        @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
        public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
            log.error(ex.getMessage());
            if(ex.getMessage().contains("Duplicate entry")){
                String[] split = ex.getMessage().split(" ");
                String msg = split[2] + "已存在";
                return R.error(msg);
            }
            return R.error("未知错误");
        }


    @ExceptionHandler(CustomerException.class)
    public R<String> exceptionHandler(CustomerException ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }
}
