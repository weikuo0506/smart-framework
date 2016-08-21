package com.walker.smartframework.bean;

import com.walker.smartframework.util.CastUtil;
import com.walker.smartframework.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wk on 2015/11/27.
 */
public class Param {
    //    private Map<String,Object> paramMap;
//
//    public Param(Map<String, Object> paramMap) {
//        this.paramMap = paramMap;
//    }
//
//    public Map<String,Object> getParamMap(){
//        return this.paramMap;
//    }
//
//    public long getLong(String name){
//        return CastUtil.castLong(paramMap.get(name));
//    }
//    public int getInt(String name){
//        return CastUtil.castInt(paramMap.get(name));
//    }
//    public double getDouble(String name){
//        return CastUtil.castDouble(paramMap.get(name));
//    }
//    public String getString(String name){
//        return CastUtil.castString(paramMap.get(name));
//    }
//    public boolean getBoolean(String name){
//        return CastUtil.castBoolean(paramMap.get(name));
//    }
//
//    public boolean isEmpty(){
//        return MapUtils.isEmpty(paramMap);
//    }
    //所有参数的序列
    private List<FormParam> formParamList;
    private List<FileParam> fileParamList;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    //相当于按照fieldName分组；
    //应该返回<field, value > <field, (value1,value2,value3)>
    public Map<String, Object> getFieldMap() {
        Map<String, Object> fieldMap = new HashMap<>();
        for (FormParam param : formParamList) {
            //fieldMap.put(param.getFieldName(),param.getFieldValue());
            String fieldName = param.getFieldName();
            Object fieldValue = param.getFieldValue();
            if (fieldMap.containsKey(fieldName)) {
                //在之前基础上append
                //fieldValue = fieldMap.get(fieldName)+","+fieldValue;
                fieldMap.put(fieldName, fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue);
            } else {
                fieldMap.put(fieldName, param.getFieldValue());
            }
        }
        return fieldMap;
    }

    //相当于按照fieldName分组；
    //应该返回<photo,List<pic1,pic2>>
    public Map<String, List<FileParam>> getFileMap() {
        Map<String, List<FileParam>> fileMap = new HashMap<>();
        for (FileParam param : fileParamList) {
            String filedName = param.getFieldName();
            if (fileMap.containsKey(filedName)) {
                fileMap.get(filedName).add(param);    //仅此而已，足以！
            } else {
                fileMap.put(filedName, new ArrayList<FileParam>() {{
                    add(param);
                }});
            }
        }
        return fileMap;
    }

    public List<FileParam> getFileList(String fieldName) {
        return getFileMap().get(fieldName);
    }

    public FileParam getFile(String fieldName) {
        List<FileParam> fileParamList = getFileList(fieldName);
        if (CollectionUtils.isNotEmpty(fileParamList) && fileParamList.size() == 1) {
            return fileParamList.get(0);
        }
        return null;
    }

    public long getLong(String name){
        return CastUtil.castLong(getFieldMap().get(name));
    }
    public int getInt(String name){
        return CastUtil.castInt(getFieldMap().get(name));
    }
    public double getDouble(String name){
        return CastUtil.castDouble(getFieldMap().get(name));
    }
    public String getString(String name){
        return CastUtil.castString(getFieldMap().get(name));
    }
    public boolean getBoolean(String name){
        return CastUtil.castBoolean(getFieldMap().get(name));
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(fileParamList) && CollectionUtils.isEmpty(formParamList);
    }
}
