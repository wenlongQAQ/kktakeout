package com.example.kktakeout.filter;


import com.alibaba.fastjson.JSON;
import com.example.kktakeout.common.R;
import com.example.kktakeout.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

/**
 * 检查用户是否已经完成了登录
 */

@WebFilter(filterName = "loginCheckFilter" ,urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();//路径匹配器
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)  servletRequest;
        HttpServletResponse response = (HttpServletResponse)  servletResponse;
        log.info("拦截到请求: {}",request.getRequestURI());
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "employee/logout",
                "/backend/**", //静态资源放行
                "/front/**",
                "/"
        };
        String requestURI = request.getRequestURI();
        if (check(urls,requestURI)) {
            filterChain.doFilter(request,response);
            log.info("被拦截且被放行的请求: {}",request.getRequestURI());
            return;
        }

        if(request.getSession().getAttribute("employee")!=null){
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            log.info("被拦截且被放行的请求: {}",request.getRequestURI());
            return;
        }else {
            response.getWriter().write(com.alibaba.fastjson.JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }
    }

    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            if (PATH_MATCHER.match(url,requestURI)) {
                return true;
            }
        }
        return false;
    }

}
