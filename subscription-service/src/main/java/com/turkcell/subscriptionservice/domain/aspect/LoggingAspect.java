package com.turkcell.subscriptionservice.domain.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(com.turkcell.subscriptionservice.domain.aspect) || @within(com.turkcell.subscriptionservice.domain.aspect)")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        var method = sig.getMethod();

        Loggable cfg = method.getAnnotation(Loggable.class);
        if (cfg == null) cfg = method.getDeclaringClass().getAnnotation(Loggable.class);

        String className = sig.getDeclaringType().getSimpleName();
        String methodName = method.getName();

        long start = System.currentTimeMillis();

        if (cfg != null && cfg.logArgs()) {
            String args = Arrays.stream(pjp.getArgs())
                    .map(a -> a == null ? "null" : safeToString(a))
                    .collect(Collectors.joining(", "));
            log.info("{}.{}({})", className, methodName, args);
        } else {
            log.info("{}.{}()", className, methodName);
        }

        try {
            Object result = pjp.proceed();

            if (cfg != null && cfg.logResult()) {
                log.info("SUCCESS {}.{} result={}", className, methodName, safeToString(result));
            }

            if (cfg != null && cfg.logTime()) {
                long took = System.currentTimeMillis() - start;
                log.info("USETIME {}.{} took={}ms", className, methodName, took);
            }
            return result;

        } catch (Throwable ex) {
            long took = System.currentTimeMillis() - start;
            log.error("FAIL {}.{} failed after {}ms: {}", className, methodName, took, ex.toString(), ex);
            throw ex;
        }
    }

    private String safeToString(Object o) {
        String s = String.valueOf(o);
        return s.length() > 500 ? s.substring(0, 500) + "...(truncated)" : s;
    }
}
