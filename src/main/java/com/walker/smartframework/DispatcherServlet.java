package com.walker.smartframework;

import com.walker.smartframework.bean.Data;
import com.walker.smartframework.bean.Handler;
import com.walker.smartframework.bean.View;
import com.walker.smartframework.helper.*;
import com.walker.smartframework.util.JsonUtil;
import com.walker.smartframework.util.ReflectionUtil;
import com.walker.smartframework.bean.Param;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by wk on 2015/11/27.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletHelper.init(req, resp);
        try {
            //从request中获取请求方法和路径
            String reqMethod = req.getMethod().toLowerCase();
            String reqPath = req.getPathInfo();

            if (StringUtils.equals("/favicon.ico", reqPath)) {
                return;
            }

            //获取handler
            Handler handler = ControllerHelper.getHandler(reqMethod, reqPath);
            if (null == handler) return;
            //获取controller的class、method、实例bean
            Class<?> controllerClass = handler.getControllerClass();
            Method controllerMethod = handler.getControllerMethod();
            Object controllerBean = BeanHelper.getBean(controllerClass);

        /*
        这段是生成参数的
        //从request创建请求参数对象
        Map<String,Object> paramMap = new HashMap<String,Object>();
        //获取get请求参数
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = req.getParameter(paramName);
            paramMap.put(paramName, paramValue);
        }
        //paramMap.putAll(req.getParameterMap());

        //获取Post请求参数
        String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
        if (StringUtil.isNotEmpty(body)) {
            String[] params = body.split("&");
            for (String param : params) {
                String[] nameValue = param.split("=");
                if (ArrayUtils.isNotEmpty(nameValue) && nameValue.length == 2) {
                    paramMap.put(nameValue[0], nameValue[1]);
                }
            }
        }
        //构建param
        Param methodParam = new Param(paramMap);
        */

            //重构后的param生成：
            Param param;
            if (ServletFileUpload.isMultipartContent(req)) {
                param = UploadHelper.createParam(req);
            } else {
                param = RequestHelper.createParam(req);
            }

            //调用Action方法:方法中是否有参数？
            Object result;
//        //这都思路是判断req中是否有参数；如果有，那么对应的方法就是有参数；如果没有，那么对应的方法就是没参数；
//        //但是如果方法不需要参数，而req中带了参数，算错吗？
//        if(methodParam.isEmpty()){
//            result = ReflectionUtil.invokeMethod(controllerBean, controllerMethod);
//        }else{
//            result = ReflectionUtil.invokeMethod(controllerBean, controllerMethod, methodParam);
//        }

            //判断目标方法是否真的需要参数
            int paramNum = controllerMethod.getParameterCount();
            if (0 == paramNum) {
                result = ReflectionUtil.invokeMethod(controllerBean, controllerMethod);
            } else {   //if(1==paramNum)
                result = ReflectionUtil.invokeMethod(controllerBean, controllerMethod, param);
            }
            //根据返回类型处理方法返回值
            if (result instanceof View) {
                //返回JSP页面
                handleViewResult((View) result, req, resp);
            } else if (result instanceof Data) {
                //返回JSON数据
                handleDataResult((Data) result, req, resp);
            }
        } finally {
            ServletHelper.destroy();
        }
    }

    private void handleDataResult(Data data, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object model = data.getModelData();
        if (null != model) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            //转化为json字符串
            String json = JsonUtil.toJson(model);
            PrintWriter pw = resp.getWriter();
            pw.write(json);
            pw.flush();
            pw.close();
        }
    }

    private void handleViewResult(View view, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String viewPath = view.getViewPath();                   //路径
        Map<String, Object> modelParam = view.getModelParam();   //参数
        if (StringUtils.isNotEmpty(viewPath)) {
            if (viewPath.startsWith("/")) {
                //客户端转发
                resp.sendRedirect(req.getContextPath() + viewPath);
            } else {
                //服务器端转发
                for (Map.Entry<String, Object> paramEntry : modelParam.entrySet()) {
                    //在req上加上负载参数
                    req.setAttribute(paramEntry.getKey(), paramEntry.getValue());
                }
                req.getRequestDispatcher(ConfigHelper.getAppJspPath() + viewPath).forward(req, resp);
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();
        //get ServletContext, to register Servlet
        ServletContext servletContext = config.getServletContext();
        //register JSP servlet
        ServletRegistration jspRegistration = servletContext.getServletRegistration("jsp");
        jspRegistration.addMapping(ConfigHelper.getAppJspPath()+"*");
        //register default servlet
        ServletRegistration defaultRegistration = servletContext.getServletRegistration("default");
        defaultRegistration.addMapping(ConfigHelper.getAppAssetPath() + "*");

        //初始化上传功能
        UploadHelper.init(servletContext);
    }
}
