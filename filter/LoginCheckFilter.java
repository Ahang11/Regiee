package com.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.reggie.common.BaseContext;
import com.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")//urlPatterns表示要拦截哪些请求路径，/*表示所有的请求都要拦截
@Component
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();//处理类似于 backend/index.html的路径也使用通配符来匹配
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();

        String[] urls = new String[]{//把不需要处理的请求放在这里
                "/employee/login",
                "/employee/logout",
                "/backend/**",//静态页面无需处理
                "/front/**",//静态页面无需处理
                "/common/**",
                "/department/**",
                "/employee/query"
        };

        log.info("拦截到请求： {}", requestURI);

        //2.判断本次请求是否需要处理（是否完成登录）；也就是判断请求的路径是否在以上的路径中
        boolean check = check(urls, requestURI);

        //3.如果不需要处理，则直接放行
        if(check){
            log.info("本次请求：{}，不需要处理",requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4.判断登陆状态，如果已经登陆，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录， id为 {}",request.getSession().getAttribute("employee"));

            Long empID = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empID);

            //在这里获取一下线程id
            long id = Thread.currentThread().getId();
            log.info("doFilter的线程id为：{}", id);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5.如果未登录则返回未登录结果，通过输出流的方式向客户端页面响应数据
        log.info("用户id{}",request.getSession().getAttribute("employee"));
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI){
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
