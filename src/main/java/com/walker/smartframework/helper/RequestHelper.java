package com.walker.smartframework.helper;

import com.walker.smartframework.util.StringUtil;
import com.walker.smartframework.bean.FormParam;
import com.walker.smartframework.bean.Param;
import com.walker.smartframework.util.CodecUtil;
import com.walker.smartframework.util.StreamUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wk on 2015/11/30.
 */
public final class RequestHelper {

    //这里封装的是请求参数；分别用req.getParameter()方式 和 req.getInputStream()的方式读取
    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        formParamList.addAll(parseFromParameterNames(request));
        formParamList.addAll(parseFromInputStream(request));
        return new Param(formParamList);
    }

    private static List<FormParam> parseFromParameterNames(HttpServletRequest request) {
        List<FormParam> formParamList = new ArrayList<>();
        Map<String, String[]> paramMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = StringUtils.join(entry.getValue(), StringUtil.SEPARATOR);
            formParamList.add(new FormParam(fieldName, fieldValue));
        }
        return null;
    }

    private static List<FormParam> parseFromInputStream(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtils.isNotEmpty(body)) {
            String[] kvs = StringUtils.split(body, "&");
            for (String kv : kvs) {
                String[] tmp = StringUtils.split(kv, "=");
                String fieldName = tmp[0];
                String fieldValue = tmp[1];
                formParamList.add(new FormParam(fieldName, fieldValue));
            }
        }
        return formParamList;
    }

}
