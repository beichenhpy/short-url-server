package cn.beichenhpy.shorturl.controller;

import cn.beichenhpy.shorturl.anno.SysLog;
import cn.beichenhpy.shorturl.constant.Result;
import cn.beichenhpy.shorturl.exception.NoSuchUrlException;
import cn.beichenhpy.shorturl.service.IShortUrlService;
import cn.beichenhpy.shorturl.utils.ResponseToCode;
import cn.beichenhpy.shorturl.utils.UrlValid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 控制层
 *
 *
 *
 * @since 2021/3/1 11:05
 */
@Controller
public class ShortUrlController {

    @Resource(name = "defaultShortUrlServiceImpl")
    private IShortUrlService defaultShortUrlServiceImpl;
    /**
     * 首页
     * @return 返回index.html
     */
    @GetMapping("")
    public String index(){
        return "index";
    }

    /**
     * 首页
     * @return 返回index.html
     */
    @GetMapping("/404")
    public String notFound(){
        return "404";
    }

    @SysLog("请求添加短链接")
    @ResponseBody
    @PostMapping("/api/add")
    public Result<?> add(String originUrl){
        boolean isLegal = UrlValid.checkUrl(originUrl);
        if (isLegal){
            String shortUrl = defaultShortUrlServiceImpl.addUrlInfo(originUrl);
            return Result.ok(shortUrl);
        }else {
            return Result.error("短链接不合法");
        }
    }

    /**
     * 如果已有的path中没出现的path才会执行这个请求
     * @param path 短链接
     * @param response response
     */
    @SysLog(value = "请求短链接api")
    @GetMapping("/{path}")
    public void returnShort(@PathVariable("path") String path, HttpServletResponse response, HttpServletRequest request){
        String originUrl = defaultShortUrlServiceImpl.getOriginUrl(path);
        if (originUrl == null){
            throw new NoSuchUrlException();
        }
        ResponseToCode.return301(response,originUrl);
    }
}
