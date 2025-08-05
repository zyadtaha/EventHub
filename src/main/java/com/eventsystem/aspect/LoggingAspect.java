package com.eventsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(public * com.eventsystem.service.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Method: {} with Arguments: {} will execute.", methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        log.info("Method: {} executed with Result: {}.", methodName, result);
        return result;
    }
}