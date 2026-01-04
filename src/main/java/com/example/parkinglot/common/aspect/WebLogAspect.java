package com.example.parkinglot.common.aspect;

import com.example.parkinglot.common.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Pointcut("execution(public * com.example.parkinglot.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("======================================================================================");
            log.info("URL      : {}", request.getRequestURL().toString());
            log.info("Method   : {}", request.getMethod());
            log.info("IP       : {}", request.getRemoteAddr());
            log.info("Class    : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            log.info("Args     : {}", Arrays.toString(joinPoint.getArgs()));
        }
    }

    /**
     * 在方法返回后：记录响应结果 (仅成功时打印 Response 内容)
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        if (ret instanceof ApiResponse<?> apiResponse) {
            Object data = apiResponse.getData();
            if (data instanceof java.util.Collection<?>) {
                log.info("Response : ApiResponse(code={}, msg={}, data=List(size={}))",
                        apiResponse.getCode(), apiResponse.getMessage(), ((java.util.Collection<?>) data).size());
            } else{
                log.info("Response : {}", ret);
            }
        } else {
            log.info("Response : {}", ret);
        }
    }

    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        // 1. 打印异常日志（确保在线内）
        log.error("Exception: {} - {}", e.getClass().getSimpleName(), e.getMessage());

        // 2. 【关键】获取 Request 并打上标记：表示由于进入了切面，错误日志已记录
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.getRequest().setAttribute("EXCEPTION_LOGGED_BY_AOP", true);
        }
    }

    @After("webLog()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("======================================================================================");
    }
}