package cn.beichenhpy.shorturl.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 日志实体类
 * @since 2021/3/5 11:05
 */
@TableName("sys_log")
@Data
@Builder
public class SysLogModal {
    @TableId(value = "id")
    private Long id;
    /**请求者ip*/
    private String ip;
    /**请求方法名*/
    private String methodName;
    /**请求备注*/
    private String methodRemark;
    /**请求参数*/
    private String requestParams;
    /**方法花费时间*/
    private Long costTime;
    /**创建时间*/
    private Date createTime;
}
