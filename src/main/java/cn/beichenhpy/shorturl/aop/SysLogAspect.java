package cn.beichenhpy.shorturl.aop;

import cn.beichenhpy.shorturl.anno.SysLog;
import cn.beichenhpy.shorturl.mapper.SysLogMapper;
import cn.beichenhpy.shorturl.model.SysLogModal;
import cn.beichenhpy.shorturl.utils.IpUtil;
import cn.beichenhpy.shorturl.utils.SpringContextUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author beichenhpy
 * @version 1.0
 * @description sysLog 切面
 * @since 2021/3/5 10:52
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {

    @Resource
    private SysLogMapper sysLogMapper;
    @Pointcut("@annotation(cn.beichenhpy.shorturl.anno.SysLog)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取request
        HttpServletRequest httpServletRequest = SpringContextUtils.getHttpServletRequest();
        SysLogAspect currentBean = SpringContextUtils.getBean(this.getClass());
        currentBean.doLog(startTime,point,httpServletRequest);
        return point.proceed();
    }

    @Async
    public void doLog(long startTime, ProceedingJoinPoint point, HttpServletRequest httpServletRequest){
        //获取注解修饰的方法
        String value = "";
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String methodName = signature.getName();
        //获取真实ip
        String ipAddr = IpUtil.getIpAddr(httpServletRequest);
        //获取注解信息
        SysLog sysLog = method.getAnnotation(SysLog.class);
        if (sysLog != null){
            value = sysLog.value();
        }
        //获取参数
        String params = getParamByReflect(point);
        Date createTime = new Date();
        long entTime = System.currentTimeMillis();
        //新建日志实体类
        SysLogModal sysLogModal = SysLogModal.builder()
                .createTime(createTime)
                .costTime(entTime - startTime).ip(ipAddr)
                .methodRemark(value)
                .methodName(methodName)
                .requestParams(params)
                .build();
        //todo 插入数据库
        sysLogMapper.insert(sysLogModal);
        log.info("\n<<<<<<<<<<<<<日志记录开始执行>>>>>>>>>>>>>\n" +
                "请求ip：{} \n"+
                "正在执行：{} 方法\n" +
                "sysLog记录备注内容：{} \n" +
                "方法耗时：{} ms\n" +
                "方法参数为：{} \n"+
                "请求时间：{}\n"+
                "<<<<<<<<<<<<<日志记录执行结束>>>>>>>>>>>>>",ipAddr,methodName,value,entTime - startTime,params,createTime);
    }
    /**
     * 直接通过反射获取参数
     * @param point 切点
     * @return 返回String构造器
     */
    private String getParamByReflect(ProceedingJoinPoint point){
        StringBuilder params;
        //通过切面获取所有参数
        Object[] paramsArray = point.getArgs();
        // java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
        //  https://my.oschina.net/mengzhang6/blog/2395893
        //放在中间变量中
        Object[] arguments  = new Object[paramsArray.length];
        for (int i = 0; i < paramsArray.length; i++) {
            Object param = paramsArray[i];
            if (param instanceof ServletRequest || param instanceof ServletResponse || param instanceof MultipartFile || param instanceof BindingResult) {
                //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                // BindingResult在 fastJson >1.2.75 会报错
                continue;
            }
            arguments[i] = param;
        }
        //update-begin-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        PropertyFilter profiler = (o, name, value) -> value == null || value.toString().length() <= 500;
        params = new StringBuilder(JSONObject.toJSONString(arguments, profiler));
        //update-end-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        return params.toString();
    }
}
