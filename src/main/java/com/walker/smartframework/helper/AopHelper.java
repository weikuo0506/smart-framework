package com.walker.smartframework.helper;

import com.walker.smartframework.annotation.Aspect;
import com.walker.smartframework.annotation.Service;
import com.walker.smartframework.proxy.AspectProxy;
import com.walker.smartframework.proxy.Proxy;
import com.walker.smartframework.proxy.ProxyManager;
import com.walker.smartframework.proxy.TransactionalProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by wk on 2015/11/28.
 */
public final class AopHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    //这里执行目标类替代任务
    static {
        try {
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap();
            for (Map.Entry<Class<?>, List<Proxy>> entry : targetMap.entrySet()) {
                Class<?> targetClass = entry.getKey();
                List<Proxy> proxyList = entry.getValue();
                Proxy proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("aop failure", e);
        }
    }

    //找出目标类集合：Aspect注解中设置的注解类集合
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Set<Class<?>> targetClassSet = new HashSet<>();
        Class<? extends Annotation> annotation = aspect.value();
        if (null != annotation && !annotation.equals(Aspect.class)) {
            targetClassSet = ClassHelper.getClassSetByAnnotation(annotation);
        }
        return targetClassSet;
    }

    //建立代理类与目标类集合之间的映射
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        addAspectProxy(proxyMap);
        addTransactionalProxy(proxyMap);
        return proxyMap;
    }

    //建立代理类（继承自AspectProxy，也就是切面）与目标类集合（Aspect的value()中指明的多个类）之间的映射
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        //找出所有切面类,即代理类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            //判断代理类上是否有@Aspect注解，如果有，目标类是哪些
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    //建立Transactional代理类（即TransactionalProxy.class这一个)与目标类集合
    // （这里的情况只可能是Service类，因为假设了@Transactional注解的方法只存在于@Service注解的类中）之间的映射
    private static void addTransactionalProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionalProxy.class, serviceClassSet);
    }


    //建立目标类与代理列表（代理对象集合）之间的映射
    private static Map<Class<?>, List<Proxy>> createTargetMap() throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        //遍历目标类集合
        Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
        for (Map.Entry<Class<?>, Set<Class<?>>> entry : proxyMap.entrySet()) {
            Class<?> proxyClass = entry.getKey();
            Set<Class<?>> targetClassSet = entry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                //创建代理类对象
                Proxy proxyObject = (Proxy) proxyClass.newInstance();
                //加入代理列表，加入map中
                //1.map中不存在该key，不存在该列表
                if (!targetMap.containsKey(targetClass)) {
                    List<Proxy> proxyList = new ArrayList<Proxy>() {{
                        add(proxyObject);
                    }};
                    targetMap.put(targetClass, proxyList);
                }
                //2.map中已经存在该key，存在该列表了
                else {
                    targetMap.put(targetClass, targetMap.get(targetClass));
                }
            }
        }
        return targetMap;
    }
}
