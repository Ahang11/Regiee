package com.reggie.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装此对象
 */
@Data
public class R<T> {

    private Integer code; //编码：返回结果，1成功，0和其它数字为失败

    private String msg; //错误信息，登陆的密码错误无法登录成功，通过msg设置的值告诉登陆失败

    private T data; //数据，把员工实体放到此属性上，前端页面就能将其转成json存储在浏览器当中

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
