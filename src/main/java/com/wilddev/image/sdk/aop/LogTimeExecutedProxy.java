package com.wilddev.image.sdk.aop;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
@Component
public class LogTimeExecutedProxy {

    private Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    private Object cover(ProceedingJoinPoint joinPoint, String name) throws Throwable {

        long start = System.currentTimeMillis();
        Object value = execute(joinPoint);
        long elapsed = System.currentTimeMillis() - start;

        log.debug("{} executed in {}ms", name, elapsed);

        return value;
    }

    @Around("@annotation(logAnnotation)")
    public Object run(ProceedingJoinPoint joinPoint, LogTimeExecuted logAnnotation) throws Throwable {
        return log.isDebugEnabled() ? cover(joinPoint, logAnnotation.value()) : execute(joinPoint);
    }
}
