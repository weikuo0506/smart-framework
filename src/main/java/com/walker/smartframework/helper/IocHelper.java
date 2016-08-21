package com.walker.smartframework.helper;

import com.walker.smartframework.annotation.Inject;
import com.walker.smartframework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by wk on 2015/11/26.
 */
public final class IocHelper {
    static{
        for (Map.Entry<Class<?>,Object> entry : BeanHelper.getBeanMap().entrySet()) {
            Class<?> clazz = entry.getKey();
            Object targetObj = entry.getValue();
            for(Field field : clazz.getDeclaredFields()){
                if(field.isAnnotationPresent(Inject.class)){
                    Object fieldObj = BeanHelper.getBean(field.getType());
                    if(null != fieldObj){
                        ReflectionUtil.setField(targetObj,field,fieldObj);
                    }
                }
            }
        }
    }
}
