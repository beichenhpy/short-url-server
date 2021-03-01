package cn.beichenhpy.shorturl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/3/1 11:05
 */
@Controller
public class ShortUrlController {

    @RequestMapping("/{path}")
    public void returnShort(@PathVariable("path") String path, HttpServletResponse response){
        String fullUrl = null;
        if ("1".equals(path)){
            fullUrl = "https://www.baidu.com";
        }
        response.setStatus(301);
        response.setHeader("Location",fullUrl);
    }
}
