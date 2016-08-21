package com.walker.smartframework.proxy;

/**
 * Created by wk on 2015/11/28.
 */
public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
