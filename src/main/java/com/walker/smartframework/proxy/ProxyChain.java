package com.walker.smartframework.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wk on 2015/11/28.
 */
public class ProxyChain {

    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final Object[] methodParams;
    private final MethodProxy methodProxy;
    private List<Proxy> proxyList = new ArrayList<>();
    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodParams = methodParams;
        this.methodProxy = methodProxy;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Object doProxyChain() throws Throwable {
        Object result;
        if (proxyIndex < proxyList.size()) {
            result = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            result = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return result;
    }
}
