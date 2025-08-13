package com.eventhub.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.eventhub.constant.AspectConstant.*;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(public * com.eventhub.service.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info(BEFORE_EXECUTING_MESSAGE, methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        log.info(AFTER_EXECUTING_MESSAGE, methodName, result);
        return result;
    }
}