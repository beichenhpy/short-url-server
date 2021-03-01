package cn.beichenhpy.shorturl.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 黑名单处理
 * @since 2021/3/1 15:23
 */
@Component
@Slf4j
@WebFilter(filterName = "blackUrlFilter",urlPatterns = "/*")
public class BlackUrlFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String contextPath = req.getServletPath();
        //todo 将会使用黑名单配置类代替
        if ("/favicon.ico".equals(contextPath)){
            log.warn("跳过执行");
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
