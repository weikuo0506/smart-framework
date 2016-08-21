package com.walker.smartframework.annotation;

import java.lang.annotation.*;

/**
 * Created by wk on 2015/11/28.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
