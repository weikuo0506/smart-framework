package com.walker.smartframework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wk on 2015/11/27.
 */
public final class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static <T> T fromJson(String json,Class<T> clazz){
        T pojo;
        try {
            pojo = OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            LOGGER.error("convert json to pojo failure",e);
            throw new RuntimeException(e);
        }
        return pojo;
    }

    public static String toJson(Object obj){
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            LOGGER.error("convert pojo to json failure",e);
            throw new RuntimeException(e);
        }
        return json;
    }
}
