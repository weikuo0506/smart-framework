package com.walker.smartframework.helper;


import com.walker.smartframework.annotation.Controller;
import com.walker.smartframework.annotation.Service;
import com.walker.smartframework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wk on 2015/11/26.
 */
public final class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;
    static{
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> serviceClassSet = new HashSet<>();
        for (Class clazz : CLASS_SET) {
            if(clazz.isAnnotationPresent(Service.class)){
                serviceClassSet.add(clazz);
            }
        }
        return serviceClassSet;
    }

    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> controllerClassSet = new HashSet<>();
        for (Class clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllerClassSet.add(clazz);
            }
        }
        return controllerClassSet;
    }

    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet = new HashSet<>();
        beanClassSet.addAll(getControllerClassSet());
        beanClassSet.addAll(getServiceClassSet());
        return beanClassSet;
    }

    @SuppressWarnings("unchecked")
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> childClassSet = new HashSet<>();
        for (Class clazz : CLASS_SET) {
            if (clazz.isAssignableFrom(superClass)) {
                childClassSet.add(clazz);
            }
        }
        return childClassSet;
    }

    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> annotationClassSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                annotationClassSet.add(clazz);
            }
        }
        return annotationClassSet;
    }
}
