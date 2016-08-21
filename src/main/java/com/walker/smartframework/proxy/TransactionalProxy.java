package com.walker.smartframework.proxy;

import com.walker.smartframework.annotation.Transactional;
import com.walker.smartframework.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by wk on 2015/11/28.
 */
public class TransactionalProxy implements Proxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionalProxy.class);
    private static ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Method targetMethod = proxyChain.getTargetMethod();
        Object result = null;
        boolean flag = FLAG_HOLDER.get();
        if (!flag && targetMethod.isAnnotationPresent(Transactional.class)) {
            FLAG_HOLDER.set(true);   //not necessary?
            try {
                DatabaseHelper.beginTransaction();
                LOGGER.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            } catch (Exception e) {
                DatabaseHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
            } finally {
                FLAG_HOLDER.remove();
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
