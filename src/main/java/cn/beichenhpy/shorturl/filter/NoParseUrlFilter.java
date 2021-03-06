package cn.beichenhpy.shorturl.filter;

import cn.beichenhpy.shorturl.constant.ResponseConstant;
import cn.beichenhpy.shorturl.utils.ResponseTo301;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 过滤某些请求
 * @since 2021/3/1 15:23
 */
@Component
@Slf4j
@WebFilter(filterName = "blackUrlFilter",urlPatterns = "/*")
public class NoParseUrlFilter implements Filter {
    private final ConcurrentHashMap<String, Boolean> parseMap = new ConcurrentHashMap<>();
    /**初始化筛选Map*/
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        putInMap();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String contextPath = req.getServletPath();
        //先判断链接是否在需要筛选的Map中 并且 能get出来，这里主要针对 短链接请求可能会有多种不在map中的
        if (parseMap.containsKey(contextPath) && parseMap.get(contextPath)){
            ResponseTo301.return301((HttpServletResponse) servletResponse, ResponseConstant.NOT_FOUND_URL);
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 将是否进行判断的url放入
     */
    private void putInMap(){
       parseMap.put("/api/favicon.ico",true);
       parseMap.put("/api/robots.txt",true);
       parseMap.put("/api/wcm",true);
       parseMap.put("/api/phpmyadmin",true);
       parseMap.put("/api/phpMyAdmin",true);
       parseMap.put("/api/4e5e5d7364f443e28fbf0d3ae744a59a",true);
       parseMap.put("/api/env",true);
       parseMap.put("/api/add",false);
    }
}
