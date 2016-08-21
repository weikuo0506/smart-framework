package com.walker.smartframework.helper;


import com.walker.smartframework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by wk on 2015/11/26.
 */
public class BeanHelper {
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();
    static{
        for (Class<?> clazz : ClassHelper.getBeanClassSet()) {
            Object instance = ReflectionUtil.newInstance(clazz);
            BEAN_MAP.put(clazz,instance);
        }
    }

    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz){
        if(!BEAN_MAP.containsKey(clazz)){
            throw new RuntimeException("cannot get bean by class: "+clazz);
        }
        return (T) BEAN_MAP.get(clazz);
    }

    public static void setBean(Class<?> clazz, Object instance) {
        BEAN_MAP.put(clazz, instance);
    }
}
