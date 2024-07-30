package com.tri0mphe.filter.common.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import com.fasterxml.jackson.databind.ObjectMapper;


@Order(1) // 表示执行过滤顺序，值越小，越先执行
@WebFilter(filterName = "loginFilter", urlPatterns = "/*") // 执行filter
public class LoginFilter implements Filter {

    private static final String ACCESS_TOKEN="Tri0mphe";


    private static final String[] VERIFICATION_PREFIX = { "/api/v1/user",
            "/api/v1/projects/getProjects" };
            
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("==>LoginFilter启动");
       
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

         logger.info("==>LoginFilter拦截请求");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String path = httpRequest.getRequestURI()
                .substring(httpRequest.getContextPath().length()).replaceAll("[/]+$", "");
        String servletPath=httpRequest.getServletPath();

        logger.info("servletPath is : "+servletPath);
        logger.info(" path is : " + path);
        logger.info("==>LoginFilter Request URL:" + httpRequest.getRequestURL());


        if (Arrays.stream(VERIFICATION_PREFIX).anyMatch(path::startsWith)) {
            if (httpRequest.getHeader("accessToken") != null
                    && httpRequest.getHeader("accessToken").equals(ACCESS_TOKEN)) {
                chain.doFilter(httpRequest, httpResponse);
            } else {
                servletResponse.getWriter().write("on authentiocation");
            }
            return;
        }

        chain.doFilter(httpRequest, httpResponse);
        // TODO Auto-generated method stub
    }

    @Override
    public void destroy() {

        logger.info("==>LoginFilter销毁");
        
    }

    

}
