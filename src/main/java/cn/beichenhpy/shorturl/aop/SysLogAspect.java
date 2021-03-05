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
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
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
        doLog(startTime,point);
        return point.proceed();
    }

    private void doLog(long startTime,ProceedingJoinPoint point){
        //获取注解修饰的方法
        String value = "";
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String methodName = signature.getName();
        //获取request
        HttpServletRequest httpServletRequest = SpringContextUtils.getHttpServletRequest();
        //获取真实ip
        String ipAddr = IpUtil.getIpAddr(httpServletRequest);
        //获取注解信息
        SysLog sysLog = method.getAnnotation(SysLog.class);
        if (sysLog != null){
            value = sysLog.value();
        }
        //获取参数
        String params = getParams(httpServletRequest, point);
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

    public String getParams(HttpServletRequest request,ProceedingJoinPoint joinPoint){
        String httpMethod = request.getMethod();
        //转换成enum类型
        HttpMethod httpMethodEnum = HttpMethod.valueOf(httpMethod);
        StringBuilder params = new StringBuilder();
        //如果为有可能用到request和response的请求 理论上是 post/put/patch 但是这里我用了get
        if (HttpMethod.POST.equals(httpMethodEnum) || HttpMethod.PUT.equals(httpMethodEnum) || HttpMethod.PATCH.equals(httpMethodEnum) || HttpMethod.GET.equals(httpMethodEnum)) {
            //通过切面获取所有参数
            Object[] paramsArray = joinPoint.getArgs();
            // java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
            //  https://my.oschina.net/mengzhang6/blog/2395893
            //放在中间变量中
            Object[] arguments  = new Object[paramsArray.length];
            for (int i = 0; i < paramsArray.length; i++) {
                if (paramsArray[i] instanceof ServletRequest || paramsArray[i] instanceof ServletResponse || paramsArray[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = paramsArray[i];
            }
            //update-begin-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
            PropertyFilter profiler = (o, name, value) -> {
                return value == null || value.toString().length() <= 500;
            };
            params = new StringBuilder(JSONObject.toJSONString(arguments, profiler));
            //update-end-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        } else {
            //不是get/post/put/patch请求则 获取method对象
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            if (args != null && paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
                }
            }
        }
        return params.toString();
    }
}
