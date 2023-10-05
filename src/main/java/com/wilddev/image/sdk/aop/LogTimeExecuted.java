package com.wilddev.image.sdk.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTimeExecuted {

    String value();
}
