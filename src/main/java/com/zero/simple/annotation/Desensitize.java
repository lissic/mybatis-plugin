package com.zero.simple.annotation;

import com.zero.simple.plugins.DesensitizeStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zero
 * @description Desensitize
 * @date 2022/4/1 10:30
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitize {

    DesensitizeStrategy strategy();

}
