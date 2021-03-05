package cn.beichenhpy.shorturl.service.Impl;

import cn.beichenhpy.shorturl.model.UrlInfo;
import cn.beichenhpy.shorturl.mapper.ShortUrlMapper;
import cn.beichenhpy.shorturl.service.IShortUrlService;
import cn.beichenhpy.shorturl.utils.HexUtil;
import cn.beichenhpy.shorturl.utils.RedisUtil;
import cn.beichenhpy.shorturl.utils.SnowFlake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/3/1 14:00
 */
@Slf4j
@Service
public class DefaultShortUrlServiceImpl extends ServiceImpl<ShortUrlMapper, UrlInfo> implements IShortUrlService {

    private static final String CACHE_PATH = "url:";
    private final ShortUrlMapper shortUrlMapper;
    private final SnowFlake idWorker;
    private final RedisUtil redisUtil;

    public DefaultShortUrlServiceImpl(ShortUrlMapper shortUrlMapper, @Qualifier(value = "idWorker") SnowFlake idWorker, RedisUtil redisUtil) {
        this.shortUrlMapper = shortUrlMapper;
        this.idWorker = idWorker;
        this.redisUtil = redisUtil;
    }

    @Override
    public String getOriginUrl(String shortUrl) {
        String originUrl = null;
        //判断是否存在key
        boolean isExistKey = redisUtil.hasKey(CACHE_PATH + shortUrl);
        if (isExistKey) {
            //读取key
            log.info("存在Key:直接读取redis缓存");
            originUrl = String.valueOf(redisUtil.get(CACHE_PATH + shortUrl));
        } else {
            QueryWrapper<UrlInfo> query = new QueryWrapper<>();
            query.eq("short_url", shortUrl);
            UrlInfo urlInfo = shortUrlMapper.selectOne(query);
            //放入redis
            if (urlInfo != null) {
                originUrl = urlInfo.getOriginUrl();
                redisUtil.set(CACHE_PATH + shortUrl, originUrl,60*30);
                log.info("未缓存:设置key");
            }
        }
        return originUrl;
    }

    @Override
    public String addUrlInfo(UrlInfo urlInfo) {
        //查询是否重复
        List<UrlInfo> repeatList = shortUrlMapper.selectList(new QueryWrapper<UrlInfo>().eq("origin_url", urlInfo.getOriginUrl()));
        if (repeatList.size() > 0) {
            //重复返回第一个
            return repeatList.get(0).getShortUrl();
        } else {
            String shortUrl = HexUtil.convertTo(idWorker.nextId(), 62);
            urlInfo.setShortUrl(shortUrl);
            shortUrlMapper.insert(urlInfo);
            return shortUrl;
        }
    }
}
