package cn.beichenhpy.shorturl.constant;

import lombok.Data;

import java.io.Serializable;

/**
 * @author beichenhpy
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 返回处理消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code;
    /**
     * 返回数据对象 data
     */
    private T result;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public Result() {

    }

    public Result<T> success(String message) {
        this.message = message;
        this.code = ResultEnum.REQUEST_OK.getCode();
        this.success = ResultEnum.REQUEST_OK.getType();
        return this;
    }

    /**
     * 不需要参数
     * @return 请求成功
     */
    public static Result<Object> ok() {
        Result<Object> r = new Result<Object>();
        r.setSuccess(ResultEnum.REQUEST_OK.getType());
        r.setCode(ResultEnum.REQUEST_OK.getCode());
        r.setMessage(ResultEnum.REQUEST_OK.getMsg());
        return r;
    }

    /**
     * 请求成功
     * @param msg 消息
     * @return 请求成功
     */
    public static Result<Object> ok(String msg) {
        Result<Object> r = new Result<Object>();
        r.setSuccess(ResultEnum.REQUEST_OK.getType());
        r.setCode(ResultEnum.REQUEST_OK.getCode());
        r.setMessage(msg);
        return r;
    }
    /**
     * 请求成功
     * @param data 请求结果
     * @return 请求成功
     */
    public static Result<Object> ok(Object data) {
        Result<Object> r = new Result<Object>();
        r.setSuccess(ResultEnum.REQUEST_OK.getType());
        r.setCode(ResultEnum.REQUEST_OK.getCode());
        r.setResult(data);
        r.setMessage(ResultEnum.REQUEST_OK.getMsg());
        return r;
    }

    /**
     * 请求错误，不需要对应code
     * @param msg 错误信息
     * @return 返回结果
     */
    public static Result<Object> error(String msg) {
        return error(ResultEnum.REQUEST_ERROR.getCode(), msg);
    }

    /**
     * 请求后台报错
     * code可能后续进行补充定义？
     * @param code 错误码，后续在枚举中定义
     * @param msg 错误信息
     * @return 返回结果
     */
    public static Result<Object> error(int code, String msg) {
        Result<Object> r = new Result<Object>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(ResultEnum.REQUEST_ERROR.getType());
        return r;
    }

    /**
     * 服务器500错误
     * @param message 错误信息
     * @return 返回结果
     */
    public Result<T> error500(String message) {
        this.message = message;
        this.code = (ResultEnum.REQUEST_ERROR.getCode());
        this.success = ResultEnum.REQUEST_ERROR.getType();
        return this;
    }
}
