package com.reggie.common;

/**
 * 自定义业务异常
 */
public class CustomerException extends RuntimeException{
    public CustomerException(String message){//把提示信息传进来
        super(message);
    }
}
