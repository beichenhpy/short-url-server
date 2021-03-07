package cn.beichenhpy.shorturl.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 返回301
 * @since 2021/3/5 10:48
 */
public class ResponseToCode {
    /**
     * 设置返回301
     * 设置返回值 可能后续有方法使用返回值
     *
     * @param response http响应
     * @param request  request
     * @return 返回http响应
     */
    public static HttpServletResponse return404(HttpServletResponse response, HttpServletRequest request) {
        String server = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/404";
        return301(response, server);
        return response;
    }

    public static HttpServletResponse return301(HttpServletResponse response, String originUrl) {
        //禁用浏览器缓存
        response.setHeader("Cache-control", "no-cache");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", -1);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", originUrl);
        return response;
    }
}
