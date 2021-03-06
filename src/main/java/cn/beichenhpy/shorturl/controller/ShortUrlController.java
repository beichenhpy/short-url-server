package cn.beichenhpy.shorturl.controller;

import cn.beichenhpy.shorturl.anno.SysLog;
import cn.beichenhpy.shorturl.constant.Result;
import cn.beichenhpy.shorturl.exception.NoSuchUrlException;
import cn.beichenhpy.shorturl.model.UrlInfo;
import cn.beichenhpy.shorturl.service.IShortUrlService;
import cn.beichenhpy.shorturl.utils.ResponseTo301;
import cn.beichenhpy.shorturl.utils.UrlValid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 控制层 可以在nginx反向代理一下 使用域名 proxy_pass localhost:9999/api
 * nginx配置:
    server {
        listen      80;
        server_name   你的域名;
        charset utf-8;
        location / {
            proxy_set_header   X-Real-IP $remote_addr;
            proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header   Host      $http_host;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
            proxy_pass         http://0.0.0.0:9999/api/;
        }
    }
 *
 *
 * @since 2021/3/1 11:05
 */
@Controller
@RequestMapping("/api")
public class ShortUrlController {

    @Resource(name = "defaultShortUrlServiceImpl")
    private IShortUrlService defaultShortUrlServiceImpl;

    @SysLog(value = "请求短链接api")
    @GetMapping("/{path}")
    public void returnShort(@PathVariable("path") String path, HttpServletResponse response){
        String originUrl = defaultShortUrlServiceImpl.getOriginUrl(path);
        if (originUrl == null){
            throw new NoSuchUrlException();
        }
        ResponseTo301.return301(response,originUrl);
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
