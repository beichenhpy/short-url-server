package cn.beichenhpy.shorturl.mapper;

import cn.beichenhpy.shorturl.model.UrlInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 短链接mapper层
 * @since 2021/3/1 13:59
 */
@Mapper
public interface ShortUrlMapper extends BaseMapper<UrlInfo> {
}
