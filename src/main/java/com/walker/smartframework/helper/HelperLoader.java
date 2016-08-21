package com.walker.smartframework.helper;


import com.walker.smartframework.util.ClassUtil;

/**
 * Created by wk on 2015/11/27.
 */
public final class HelperLoader {
    public static void init(){
        Class<?>[] clazzList = {
                ClassHelper.class, BeanHelper.class, AopHelper.class, IocHelper.class, ControllerHelper.class
        };
        for(Class<?> clazz:clazzList){
            ClassUtil.loadClass(clazz.getName(),true);
        }
    }
}
