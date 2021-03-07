package cn.beichenhpy.shorturl.service;

import cn.beichenhpy.shorturl.model.UrlInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author beichenhpy
 * @version 1.0
 * @description
 * @since 2021/3/1 13:59
 */
public interface IShortUrlService extends IService<UrlInfo> {
    /**
     * 获取原始url
     * @param shortUrl 短url
     * @return 返回url信息
     */
    String getOriginUrl(String shortUrl);

    /**
     * 新增短链接信息
     * @param originUrl 原始链接
     * @return 返回是否添加成功
     */
    String addUrlInfo(String originUrl);
}
