package cn.beichenhpy.shorturl.filter;

import cn.beichenhpy.shorturl.constant.ResponseConstant;
import cn.beichenhpy.shorturl.utils.ResponseTo301;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class NoParseUrlFilter implements Filter {
    /**
     * 不解析的servletPath
     * todo 后续可能放入数据库？
     */
    private static final String FAV = "/api/favicon.ico";
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String contextPath = req.getServletPath();
        if (FAV.equals(contextPath)){
            ResponseTo301.return301((HttpServletResponse) servletResponse, ResponseConstant.NOT_FOUND_URL);
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
