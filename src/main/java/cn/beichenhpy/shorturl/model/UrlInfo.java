package cn.beichenhpy.shorturl.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author beichenhpy
 * @version 1.0
 * @description url 实体类
 * @since 2021/3/1 11:05
 */
@Data
@TableName("url_info")
public class UrlInfo implements Serializable {
    /**
     *
     *
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 短url
     *
     *@since 2021-03-01 13:57:34
     */
    private String shortUrl;

    /**
     * 原始url
     *
     *@since  2021-03-01 13:57:34
     */
    private String originUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table url_info
     *
     * @since  2021-03-01 13:57:34
     */
    private static final long serialVersionUID = 1L;
}
