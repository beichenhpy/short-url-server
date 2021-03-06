package cn.beichenhpy.shorturl.exceptionHanlder;

import cn.beichenhpy.shorturl.constant.ResponseConstant;
import cn.beichenhpy.shorturl.exception.NoSuchUrlException;
import cn.beichenhpy.shorturl.utils.ResponseTo301;
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
        //清缓存
        ResponseTo301.return301(response, ResponseConstant.NOT_FOUND_URL);
    }
}
