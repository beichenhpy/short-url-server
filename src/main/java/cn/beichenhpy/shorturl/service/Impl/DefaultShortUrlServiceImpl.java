package cn.beichenhpy.shorturl.service.Impl;

import cn.beichenhpy.shorturl.exception.NoSuchUrlException;
import cn.beichenhpy.shorturl.model.UrlInfo;
import cn.beichenhpy.shorturl.mapper.ShortUrlMapper;
import cn.beichenhpy.shorturl.service.IShortUrlService;
import cn.beichenhpy.shorturl.util.HexUtil;
import cn.beichenhpy.shorturl.util.SnowFlake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/3/1 14:00
 */
@Service
public class DefaultShortUrlServiceImpl extends ServiceImpl<ShortUrlMapper,UrlInfo> implements IShortUrlService {

    private final ShortUrlMapper shortUrlMapper;
    private final SnowFlake idWorker;

    public DefaultShortUrlServiceImpl(ShortUrlMapper shortUrlMapper,@Qualifier(value = "idWorker") SnowFlake idWorker){
        this.shortUrlMapper = shortUrlMapper;
        this.idWorker = idWorker;
    }

    @Override
    @Cacheable(value = "cache:url",key = "#shortUrl")
    public UrlInfo getOriginUrl(String shortUrl) {
        QueryWrapper<UrlInfo> query = new QueryWrapper<>();
        query.eq("short_url",shortUrl);
        UrlInfo urlInfo = shortUrlMapper.selectOne(query);
        if (urlInfo != null){
            return urlInfo;
        }else {
            throw new NoSuchUrlException();
        }
    }

    @Override
    public String addUrlInfo(UrlInfo urlInfo) {
        //查询是否重复
        List<UrlInfo> repeatList = shortUrlMapper.selectList(new QueryWrapper<UrlInfo>().eq("origin_url", urlInfo.getOriginUrl()));
        if (repeatList.size() > 0){
            //重复返回第一个
            return repeatList.get(0).getShortUrl();
        }else {
            String shortUrl = HexUtil.convertTo(idWorker.nextId(), 62);
            urlInfo.setShortUrl(shortUrl);
            shortUrlMapper.insert(urlInfo);
            return shortUrl;
        }
    }
}
