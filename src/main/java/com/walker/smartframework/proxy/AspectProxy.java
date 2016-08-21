package com.walker.smartframework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by wk on 2015/11/28.
 */
public abstract class AspectProxy implements Proxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class<?> clazz = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try {
            if (intercept(clazz, method, params)) {
                before(clazz, method, params);
                result = proxyChain.doProxyChain();
                after(clazz, method, params);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            LOGGER.error("proxy failure", e);
            error(clazz, method, params);
            throw e;
        } finally {
            end();
        }

        return result;
    }

    public void begin() {

    }

    public boolean intercept(Class<?> clazz, Method method, Object[] params) throws Throwable {
        return true;
    }

    public void before(Class<?> clazz, Method method, Object[] params) throws Throwable {

    }

    public void after(Class<?> clazz, Method method, Object[] params) throws Throwable {

    }

    public void error(Class<?> clazz, Method method, Object[] params) throws Throwable {

    }

    public void end() {

    }
}
