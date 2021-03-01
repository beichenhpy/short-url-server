package cn.beichenhpy.shorturl.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 返回结果参数枚举类
 * @author A51398
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResultEnum {
    /**
     * 请求成功
     */
    REQUEST_OK(true,200,"请求成功"),
    REQUEST_ERROR(false,500,"请求失败");
    private Boolean type;
    private Integer code;
    private String msg;
}
