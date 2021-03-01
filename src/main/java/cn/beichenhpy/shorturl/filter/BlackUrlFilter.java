package cn.beichenhpy.shorturl.filter;

import cn.beichenhpy.shorturl.config.BlackUrlLoad;
import cn.beichenhpy.shorturl.constant.UnParsingPath;
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
        if (UnParsingPath.FAV.equals(contextPath)){
            log.warn("不解析无用短链接");
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
