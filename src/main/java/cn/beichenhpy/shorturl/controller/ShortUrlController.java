package cn.beichenhpy.shorturl.controller;

import cn.beichenhpy.shorturl.anno.SysLog;
import cn.beichenhpy.shorturl.constant.Result;
import cn.beichenhpy.shorturl.exception.NoSuchUrlException;
import cn.beichenhpy.shorturl.model.UrlInfo;
import cn.beichenhpy.shorturl.service.IShortUrlService;
import cn.beichenhpy.shorturl.utils.UrlValid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 控制层 可以在nginx反向代理一下 使用域名 proxy_pass localhost:9999/api
 * @since 2021/3/1 11:05
 */
@Controller
@RequestMapping("/api")
public class ShortUrlController {

    private final IShortUrlService defaultShortUrlServiceImpl;

    public ShortUrlController(@Qualifier(value = "defaultShortUrlServiceImpl") IShortUrlService defaultShortUrlServiceImpl){
        this.defaultShortUrlServiceImpl = defaultShortUrlServiceImpl;
    }
    @SysLog(value = "请求短链接api")
    @GetMapping("/{path}")
    public void returnShort(@PathVariable("path") String path, HttpServletResponse response){
        String originUrl = defaultShortUrlServiceImpl.getOriginUrl(path);
        if (originUrl == null){
            throw new NoSuchUrlException();
        }
        //禁用浏览器缓存
        response.setHeader("Cache-control", "no-cache");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", -1);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location",originUrl);
    }

    @SysLog(value = "添加短链接")
    @PostMapping("/add")
    @ResponseBody
    public Result<?> add(@RequestBody UrlInfo urlInfo){
        //验证url是否合法
        boolean isLegal = UrlValid.checkUrl(urlInfo.getOriginUrl());
        if (isLegal){
            String shortUrl = defaultShortUrlServiceImpl.addUrlInfo(urlInfo);
            return Result.ok(shortUrl);
        }else {
            return Result.error("错误的链接或被禁用的链接");
        }
    }
}
