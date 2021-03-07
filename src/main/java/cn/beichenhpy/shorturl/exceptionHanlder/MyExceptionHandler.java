package cn.beichenhpy.shorturl.exceptionHanlder;

import cn.beichenhpy.shorturl.exception.NoSuchUrlException;
import cn.beichenhpy.shorturl.utils.ResponseToCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 异常处理类
 * @since 2021/3/1 15:54
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler{
    @ExceptionHandler(NoSuchUrlException.class)
    public void globalException(HttpServletResponse response, HttpServletRequest request, NoSuchUrlException ex){
        ResponseToCode.return404(response, request);
    }
}
