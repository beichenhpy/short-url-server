package cn.beichenhpy.shorturl.exceptionHanlder;

import cn.beichenhpy.shorturl.exception.NoSuchUrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/3/1 15:54
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler{
    @ExceptionHandler(NoSuchUrlException.class)
    public void globalException(HttpServletResponse response, NoSuchUrlException ex){
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        //todo 使用别人的模板演示地址，有空自己部署一个？
        response.setHeader("Location","https://www.undi.cn/404/dynamic-cartoon-bug/");
    }
}
