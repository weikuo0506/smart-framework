package com.walker.smartframework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wk on 2015/11/27.
 */
public class View {
    private String viewPath;
    private Map<String,Object> modelParam;

    public View(String viewPath) {
        this.viewPath = viewPath;
        this.modelParam = new HashMap<String, Object>();
    }

    public View addModel(String key, Object value) {
        modelParam.put(key, value);
        return this;
    }

    public String getViewPath() {
        return viewPath;
    }

    public Map<String, Object> getModelParam() {
        return modelParam;
    }

}
