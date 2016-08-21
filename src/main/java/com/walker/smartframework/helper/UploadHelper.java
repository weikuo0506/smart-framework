package com.walker.smartframework.helper;

import com.walker.smartframework.bean.FileParam;
import com.walker.smartframework.bean.FormParam;
import com.walker.smartframework.bean.Param;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wk on 2015/11/30.
 */
public final class UploadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    private static ServletFileUpload servletFileUpload;

    public static void init(ServletContext servletContext) {
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        FileItemFactory fileItemFactory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
        servletFileUpload = new ServletFileUpload(fileItemFactory);
        int upLoadLimit = ConfigHelper.getAppLoadLimit();
        if (upLoadLimit != 0) {
            servletFileUpload.setFileSizeMax(upLoadLimit * 1024 * 1024);
        }
    }

//    public static boolean isMultiPart(HttpServletRequest request) {
//        return ServletFileUpload.isMultipartContent(request);
//    }

    public static Param createParam(HttpServletRequest req) {
        List<FormParam> formParamList = new ArrayList<>();
        List<FileParam> fileParamList = new ArrayList<>();
        try {
            //这是apache的方法
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(req);
            if (MapUtils.isNotEmpty(fileItemListMap)) {
                for (Map.Entry<String, List<FileItem>> entry : fileItemListMap.entrySet()) {
                    String fieldName = entry.getKey();
                    List<FileItem> fileItemList = entry.getValue();
                    //apache这个判断真方便啊
                    for (FileItem fileItem : fileItemList) {
                        if (fileItem.isFormField()) {
                            //普通表单输入域
                            String fieldValue = fileItem.getString("UTF-8");
                            formParamList.add(new FormParam(fieldName, fieldValue));
                        } else {
                            //文件上传域
                            String fileName = fileItem.getName();
                            if (StringUtils.isNotEmpty(fileName)) {
                                long fileSize = fileItem.getSize();
                                String contentType = fileItem.getContentType();
                                InputStream inputStream = fileItem.getInputStream();
                                fileParamList.add(new FileParam(fieldName, fileName, fileSize, contentType, inputStream));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("create param failure", e);
            throw new RuntimeException(e);
            //throw e;
        }
        return new Param(formParamList, fileParamList);
    }

    public static void uploadFile(String basePath, FileParam fileParam) {
        try {
            if (null != fileParam) {
                String filePath = basePath + fileParam.getFileName();
                File file = new File(filePath);
                BufferedInputStream bis = new BufferedInputStream(fileParam.getInputStream());
                //BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                FileUtils.copyInputStreamToFile(bis, file);
            }
        } catch (Exception e) {
            LOGGER.error("upload file failure", e);
            throw new RuntimeException(e);
//            throw e;
        }
    }
}
