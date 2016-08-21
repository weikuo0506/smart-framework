package com.walker.smartframework.proxy;

import com.walker.smartframework.annotation.Aspect;
import com.walker.smartframework.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by wk on 2015/11/28.
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);
    private long beginTime;

    @Override
    public void begin() {
        LOGGER.debug("begin...");
    }

    @Override
    public void before(Class<?> clazz, Method method, Object[] params) throws Throwable {
        LOGGER.debug("before...");
        LOGGER.debug("class name: " + clazz.getName());
        LOGGER.debug("method name: " + method.getName());
        beginTime = System.currentTimeMillis();
        LOGGER.debug("before over...");
    }

    @Override
    public void after(Class<?> clazz, Method method, Object[] params) throws Throwable {
        LOGGER.debug("after...");
        long endTime = System.currentTimeMillis();
        LOGGER.debug(String.format("time used: %dms", endTime - beginTime));
        LOGGER.debug("after over...");
    }

    @Override
    public void end() {
        LOGGER.debug("end...");
    }
}
