package cn.beichenhpy.shorturl.anno;

import java.lang.annotation.*;

/**
 * @author beichenhpy
 * @version 1.0
 * @description 方法系统日志记录
 * @since 2021/3/5 10:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
