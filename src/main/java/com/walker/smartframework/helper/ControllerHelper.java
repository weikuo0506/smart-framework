package com.walker.smartframework.helper;

import com.walker.smartframework.bean.Handler;
import com.walker.smartframework.annotation.Action;
import com.walker.smartframework.bean.Request;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wk on 2015/11/26.
 */
public final class ControllerHelper {

    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static{
        for (Class<?> clazz : ClassHelper.getControllerClassSet()) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Action.class)) {
                    //Annotation[] annotations = method.getDeclaredAnnotationsByType(Action.class);
                    Action action = method.getAnnotation(Action.class);
//                    if(ArrayUtils.isNotEmpty(annotations)){
//                        Annotation annotation = annotations[0];
//                        String annoStr = annotation.toString();
//                        String[] tmp = annoStr.split(":");
//                        Request requst = new Request(tmp[0],tmp[1].replace("/",""));
//                        Handler handler = new Handler(clazz,method);
//                        ACTION_MAP.put(requst, handler);
//                    }
                    String mapping = action.value();
                    if(mapping.matches("\\w+:/\\w*")){
                        String[] array= mapping.split(":");
                        if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                            Request requst = new Request(array[0],array[1]);
                            Handler handler = new Handler(clazz,method);
                            ACTION_MAP.put(requst, handler);
                        }
                    }
                }
            }
        }
    }

    public static Map<Request,Handler> getActionMap(){
        return ACTION_MAP;
    }

    public static Handler getHandler(String reqMethod, String reqPath){
        return ACTION_MAP.get(new Request(reqMethod, reqPath));
    }
}
