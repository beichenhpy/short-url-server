package cn.beichenhpy.shorturl.controller;

import cn.beichenhpy.shorturl.constant.Result;
import cn.beichenhpy.shorturl.model.UrlInfo;
import cn.beichenhpy.shorturl.service.IShortUrlService;
import cn.beichenhpy.shorturl.util.HexUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/3/1 11:05
 */
@Controller
public class ShortUrlController {

    private final IShortUrlService defaultShortUrlServiceImpl;

    public ShortUrlController(@Qualifier(value = "defaultShortUrlServiceImpl") IShortUrlService defaultShortUrlServiceImpl){
        this.defaultShortUrlServiceImpl = defaultShortUrlServiceImpl;
    }

    @RequestMapping("/{path}")
    public void returnShort(@PathVariable("path") String path, HttpServletResponse response){
        UrlInfo originUrl = defaultShortUrlServiceImpl.getOriginUrl(path);
        response.setStatus(301);
        response.setHeader("Location",originUrl.getOriginUrl());
    }

    @PostMapping("/add")
    @ResponseBody
    public Result<?> add(@RequestBody UrlInfo urlInfo){
        String shortUrl = defaultShortUrlServiceImpl.addUrlInfo(urlInfo);
        return Result.ok(shortUrl);
    }

}
