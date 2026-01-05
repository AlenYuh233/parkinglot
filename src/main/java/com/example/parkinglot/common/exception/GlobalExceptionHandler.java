package com.example.parkinglot.common.exception;

import com.example.parkinglot.common.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //拦截自定义业务异常
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBusinessException(BusinessException e) {
        //log.warn("业务异常: {}", e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    //消除NoResourceFoundException噪音
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoResourceFound(NoResourceFoundException e) {
        return ApiResponse.error("NOT_FOUND", "资源不存在");
    }
    //拦截404
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoHandlerFoundException(NoHandlerFoundException e){
        log.error("接口不存在 - 请求方法：{}，请求路径：{}", e.getHttpMethod(), e.getRequestURL(), e);
        return ApiResponse.error("NOT_FOUND", "接口不存在（Not Found）");
    }

    //拦截其他系统异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e, HttpServletRequest request) {

        // 检查 AOP 是否已经处理过这个异常的日志
        Object isLogged = request.getAttribute("EXCEPTION_LOGGED_BY_AOP");

        if (isLogged == null) {
            // AOP 没触发, 补打日志
            log.error("系统异常 (Framework/Filter Error)", e);
        } else {
            // AOP 已经打过日志了（在线内），什么都不用做，避免重复
        }

        return ApiResponse.error("INTERNAL_SERVER_ERROR", "系统繁忙，请稍后重试");
    }
}