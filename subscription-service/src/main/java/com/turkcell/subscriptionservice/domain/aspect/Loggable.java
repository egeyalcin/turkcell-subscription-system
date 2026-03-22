package com.turkcell.subscriptionservice.domain.aspect;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Loggable {
    boolean logArgs() default true;
    boolean logResult() default false;
    boolean logTime() default true;
}