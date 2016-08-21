package com.walker.smartframework.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by wk on 2015/11/27.
 */
public final class StreamUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);
    public static String getString(InputStream is){
        StringBuilder sb = new StringBuilder();

        //try with resource
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            LOGGER.error("get String failure", e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
