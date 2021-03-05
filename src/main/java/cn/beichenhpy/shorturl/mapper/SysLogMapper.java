package cn.beichenhpy.shorturl.mapper;

import cn.beichenhpy.shorturl.model.SysLogModal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 日志mapper
 * @since 2021/3/5 20:59
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLogModal> {
}
