package cn.beichenhpy.shorturl.filter;

import cn.beichenhpy.shorturl.constant.ResponseConstant;
import cn.beichenhpy.shorturl.exception.NoSuchUrlException;
import cn.beichenhpy.shorturl.utils.HexUtil;
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
            这里的判断 对已经在parseFilterMap中的path
            相当于是对一些不需要过滤的进行判断或者一些已经确定一定要过滤的进行过滤
         *  parseMap.containsKey(contextPath) 进行查询path是否需要进行判断 第一个满足才会执行
         *  parseMap.get(contextPath) 对需要进行判断的path判断是否放行（根据value的值）
         */
        if (parseFilterMap.containsKey(contextPath)) {
            if (parseFilterMap.get(contextPath)) {
                log.info("\n<<<<<<<<<<<<<过滤Filter By Map记录开始执行>>>>>>>>>>>>>\n" +
                        "请求ip：{} \n" +
                        "请求关键路径：{} \n" +
                        "<<<<<<<<<<<<<过滤Filter By Map记录结束执行>>>>>>>>>>>>>", ipAddr, contextPath);
                ResponseTo301.return301((HttpServletResponse) servletResponse, ResponseConstant.NOT_FOUND_URL);
            } else {
                log.info("<<<<<<<<<<<<<通过Map检测|允许通过>>>>>>>>>>>>>");
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            /* 条件是：Map为空/请求的path不包含在key中
              这里防止DDos对数据库负载
              增加对短链接的逆向判断
              如果为雪花算法生成，转换为10进制长度应该为18/19
             */
            //去除prefix /api
            String pureContext = contextPath.substring(contextPath.substring(contextPath.indexOf("/") + 1).indexOf("/") + 2);
            //尝试decode 62to10
            try {
                long decodeNum = HexUtil.revertToLong(pureContext, 62);
                if (String.valueOf(decodeNum).length() == 13) {
                    log.info("<<<<<<<<<<<<<长度满足|允许通过>>>>>>>>>>>>>");
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    log.info("\n<<<<<<<<<<<<<过滤Filter By decode记录开始执行>>>>>>>>>>>>>\n" +
                            "请求ip：{} \n" +
                            "请求关键路径：{} \n" +
                            "<<<<<<<<<<<<<过滤Filter By decode记录结束执行>>>>>>>>>>>>>", ipAddr, contextPath);
                    ResponseTo301.return301((HttpServletResponse) servletResponse, ResponseConstant.NOT_FOUND_URL);
                }
            } catch (Exception e) {
                ResponseTo301.return301((HttpServletResponse) servletResponse, ResponseConstant.NOT_FOUND_URL);
            }
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
