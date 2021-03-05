package cn.beichenhpy.shorturl.service.Impl;

import cn.beichenhpy.shorturl.mapper.SysLogMapper;
import cn.beichenhpy.shorturl.model.SysLogModal;
import cn.beichenhpy.shorturl.service.ISysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 系统日志实现
 * @since 2021/3/1 13:59
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLogModal> implements ISysLogService {
}
