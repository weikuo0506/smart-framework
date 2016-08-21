package com.walker.smartframework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wk on 2015/11/30.
 */
public final class ServletHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletHelper.class);

    //各个线程独享一份servletHelper实例
    private static final ThreadLocal<ServletHelper> SERVLET_HELPER_HOLDER = new ThreadLocal<>();

    private HttpServletRequest request;
    private HttpServletResponse response;

    public ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static void init(HttpServletRequest request, HttpServletResponse response) {
        SERVLET_HELPER_HOLDER.set(new ServletHelper(request, response));
    }

    public static void destroy() {
        SERVLET_HELPER_HOLDER.remove();
    }

    private static HttpServletRequest getRequest() {
        return SERVLET_HELPER_HOLDER.get().request;
    }

    private static HttpServletResponse getResponse() {
        return SERVLET_HELPER_HOLDER.get().response;
    }

    public static void setAttribute(String key, Object value) {
        getRequest().setAttribute(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(String key) {
        return (T) getRequest().getAttribute(key);
    }

    public static void removeAttribute(String key) {
        getRequest().removeAttribute(key);
    }

    public static void sendRedirect(String location) {
        try {
            getResponse().sendRedirect(getRequest().getContextPath() + location);
        } catch (IOException e) {
            LOGGER.error("redirect failure", e);
        }
    }

    public static void invalidateSession() {
        getRequest().getSession().invalidate();
    }

    public static void setSessionAttribute(String key, Object value) {
        getRequest().getSession().setAttribute(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttribute(String key) {
        return (T) getRequest().getSession().getAttribute(key);
    }

    public static void removeSessionAttribute(String key) {
        getRequest().getSession().removeAttribute(key);
    }
}
