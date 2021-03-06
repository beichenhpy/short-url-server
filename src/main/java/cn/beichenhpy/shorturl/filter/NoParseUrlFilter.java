package cn.beichenhpy.shorturl.filter;

import cn.beichenhpy.shorturl.constant.ResponseConstant;
import cn.beichenhpy.shorturl.utils.IpUtil;
import cn.beichenhpy.shorturl.utils.ResponseTo301;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 过滤某些请求
 * 不会记录在数据库中，相当于跳过某些请求，不进入controller
 * 这样可以节省数据库资源或者一些不必要的风险
 * @since 2021/3/1 15:23
 */
@Component
@Slf4j
@WebFilter(filterName = "blackUrlFilter", urlPatterns = "/*")
public class NoParseUrlFilter implements Filter {
    /**
     * 是否过滤的Map
     * key:path
     * value: true:过滤 false:不过滤
     */
    private final Map<String, Boolean> parseFilterMap = new HashMap<>();

    /**
     * 初始化筛选Map
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        initParseMap();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String contextPath = req.getServletPath();
        String ipAddr = IpUtil.getIpAddr(req);
        /*  条件说明
         *  parseMap.size() > 0 用于判断Map是否为空，不为空才能进行下一步判断
         *  parseMap.containsKey(contextPath) 进行查询path是否需要进行判断 第一个满足才会执行
         *  parseMap.get(contextPath) 对需要进行判断的path判断是否放行（根据value的值） 前两个满足才会执行
         */
        if (parseFilterMap.size() > 0 && parseFilterMap.containsKey(contextPath) && parseFilterMap.get(contextPath)) {
            log.info("\n<<<<<<<<<<<<<过滤Filter记录开始执行>>>>>>>>>>>>>\n" +
                    "请求ip：{} \n"+
                    "请求关键路径：{} \n"+
                    "<<<<<<<<<<<<<过滤Filter记录结束执行>>>>>>>>>>>>>",ipAddr,contextPath);
            ResponseTo301.return301((HttpServletResponse) servletResponse, ResponseConstant.NOT_FOUND_URL);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 将是否进行过滤的url放入
     */
    private void initParseMap() {
        /*过滤*/
        parseFilterMap.put("/api/favicon.ico", true);
        parseFilterMap.put("/api/robots.txt", true);
        parseFilterMap.put("/api/wcm", true);
        parseFilterMap.put("/api/phpmyadmin", true);
        parseFilterMap.put("/api/phpMyAdmin", true);
        parseFilterMap.put("/api/4e5e5d7364f443e28fbf0d3ae744a59a", true);
        parseFilterMap.put("/api/env", true);
        /*不过滤*/
        parseFilterMap.put("/api/add", false);
    }
}
