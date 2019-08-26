package com.swjt.fileManagement.utils.excel.standardModel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;

import com.swjt.fileManagement.utils.excel.model.ReadModel3;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 该类包括 signal 表动态生成model{getReadModel和changeExcelPropertyIndex}
 * 基础signal表 表头格式检查，
 * 基础signal表 数据格式检查。
 */
public class Model {
    @Getter
    @Setter
    private String number;

    @Getter
    @Setter
    private String stationName;

    @Getter
    @Setter
    private String signalPointName;

    @Getter
    @Setter
    private String actualMileage;

    @Getter
    @Setter
    private String signalPointType;

    @Getter
    @Setter
    private String insulationSectionType;

    @Getter
    @Setter
    private String trackSectionName;

    @Getter
    @Setter
    private String codingSystem;

    @Getter
    @Setter
    private String trackSectionFrequency;

    @Getter
    @Setter
    private String actualLength;

    @Getter
    @Setter
    private String sectionProperties;

    @Getter
    @Setter
    private String remarks;

    /**
     * 数据格式检查发现的问题记录
     *  需要修改的问题描述
     */
    @Getter
    @Setter
    private String problem;

    /**
     * Sheet间数据一致性检查发现的问题记录
     */
    @Getter
    @Setter
    private String Uniformity;

    /**
     * 与CAD图数据一致性检查发现的问题记录
     */
    @Getter
    @Setter
    private String CADProblem;


    /**
     * 根据表的列中关键字，获取对应元素对应的index 值
     *
     * @param filename  文件地址名
     * @param sheet     excel表的单个sheet
     * @param Row       实际excel表头占用的行数
     * @param readModel 基础model
     * @return
     * @throws IOException
     */
    public static Map<String, ReadModel3> getReadModel(String filename, Sheet sheet, int Row, ReadModel3 readModel) throws IOException {
        Map<String, Integer> indexMap = new HashMap<>();
        Map<String, ReadModel3> model3Map = new HashMap<>();
        //model 元素名和indexMap 关联
        Map<String, String> indexNameMap = new HashMap<>();
        indexNameMap.put("序号", "number");
        indexNameMap.put("车站名", "stationName");
        indexNameMap.put("备注", "remarks");
        indexNameMap.put("信号点名称", "signalPointName");
        indexNameMap.put("实际里程", "actualMileage");
        indexNameMap.put("信号点类型", "signalPointType");
        indexNameMap.put("绝缘节类型", "insulationSectionType");
        indexNameMap.put("轨道区段名称", "trackSectionName");
        indexNameMap.put("编码制式", "codingSystem");
        indexNameMap.put("载频", "trackSectionFrequency");
        indexNameMap.put("实际长度", "actualLength");
        indexNameMap.put("区段属性", "sectionProperties");
        List<List<String>> list = new ArrayList<>();
//        for (Sheet sheet : sheets) {
        InputStream inputStreamS = new BufferedInputStream(new FileInputStream(filename));
        List<Object> data = EasyExcelFactory.read(inputStreamS, new Sheet(sheet.getSheetNo(), sheet.getHeadLineMun()));
        inputStreamS.close();
        indexMap.clear();
        //实际有效的表头
        for (int i = 0; i < Row; i++) {
            if (i == 0) {
                continue;
            }
            list.clear();
            list.add((List<String>) data.subList(i, i + 1).get(0));
//                System.out.println(sheet.getSheetName() + i + "list= " + list);
            if (i == 1) {
                indexMap.put("序号", (list.get(0)).indexOf("序号"));
                indexMap.put("车站名", (list.get(0)).indexOf("车站名"));
                indexMap.put("备注", (list.get(0)).indexOf("备注"));
            } else if (i == 2) {
                indexMap.put("信号点名称", (list.get(0)).indexOf("名称"));
                indexMap.put("实际里程", (list.get(0)).indexOf("实际里程"));
                indexMap.put("信号点类型", (list.get(0)).indexOf("信号点类型"));
                indexMap.put("绝缘节类型", (list.get(0)).indexOf("绝缘节类型"));
                indexMap.put("轨道区段名称", (list.get(0)).lastIndexOf("名称"));
                indexMap.put("编码制式", (list.get(0)).indexOf("编码制式"));
                indexMap.put("载频", (list.get(0)).indexOf("载频"));
                indexMap.put("实际长度", (list.get(0)).indexOf("实际长度"));
                indexMap.put("区段属性", (list.get(0)).indexOf("区段属性"));
            }
        }
        try {
            model3Map.put(sheet.getSheetName(), changeExcelPropertyIndex(indexMap, readModel, indexNameMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
        return model3Map;
    }

    /**
     * aop 方式      * ExcelProperty 注解
     * indexMap，model 模型，indexNameMap 属性对应的 filed元素
     *
     * @param indexMap     实际model 元素 对应的index  map
     * @param readModel    model
     * @param indexNameMap ExcelProperty 注解的model 元素和 @param indexMap 关联对应
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static ReadModel3 changeExcelPropertyIndex(Map indexMap, ReadModel3 readModel, Map<String, String> indexNameMap) throws NoSuchFieldException, IllegalAccessException {
        for (Object key : indexNameMap.keySet()) {
            Field field = readModel.getClass().getDeclaredField(indexNameMap.get(key));
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            InvocationHandler h = Proxy.getInvocationHandler(excelProperty);
            Field hfield = h.getClass().getDeclaredField("memberValues");
            hfield.setAccessible(true);
            Map memberValues = (Map) hfield.get(h);
            memberValues.put("index", indexMap.get(key));
        }
        return readModel;
    }


    public static List checkData(List<Model> list) {
        List<Model> list1 = new ArrayList<>();
        for (Model a : list) {
            Map  map = new HashMap();
            if (Pattern.matches("[0-9]{1,5}", a.getNumber())) {
                if (!(Pattern.matches("[A-Z0-9]{0,}", a.getSignalPointName()) || (a.getSignalPointName().equals("null")))) {
                   map.put("信号点名称","格式错误");
                }

                if (!Pattern.matches("^[K]{1}[0-9]{1,3}[+]{1}[0-9]{3}", a.getActualMileage())) {
                    if(a.getActualMileage().equals("null")){
                        map.put("实际里程为空","跳过");
                    }
                    if (!Pattern.matches("^[0-9]{1,3}[0-9]{3}", a.getActualMileage())) {
                        map.put("实际里程","格式错误");
                    }else {
                        String temp = a.getActualMileage();
                        String actualMileage = "K" + temp.substring(0, temp.length() - 1 - 3) + "+" + temp.substring(temp.length() - 1 - 3, temp.length() - 1);
                        a.setActualMileage(actualMileage);
                      map.put("实际里程格式不标准","格式不标准");
                    }
                }

                if (!(Pattern.matches("[A-Z|/|0-9|-]{0,}", a.getTrackSectionName()) ||  a.getTrackSectionName().equals("null"))) {
                    //没有判断为null，因为实际里程为空，一般就包括了
                    map.put("轨道区段名称","格式错误");
                }

                if (!(Pattern.matches("[0-9]{0,}[-][0-9]{0,}", a.getTrackSectionFrequency()) || a.getTrackSectionFrequency().equals("0"))) {
                    map.put("轨道区段载频","格式错误");
                }
            } else if (Pattern.matches(".*编制者.*", a.getNumber())) {
                //todo 取编制者
                System.out.println("跳过 .*编制者 ");
                break;
            }
            a.setProblem(map.toString());
            map.clear();
            list1.add(a);
        }
        return list1;
    }

    // todo 想的时将下面一个方法中的遍历匹配用forech写
    public Object signalModel(List<Object> list1, int row1, String[] keys, String[] pattern, String[] error) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        int j = 0;
        for (Object c : list1) {
            if (j < row1) {
                j++;
                continue;
            }
            JSONObject object = (JSONObject) JSONObject.parse(c.toString());
            if (c.toString().equals("")) {
                System.out.println("该行为空，跳过不处理");
                object.put("sysRemarks", "该行为空，跳过不处理");
                continue;
            }
            for (int k = 0; k < keys.length; k++) {
                //满足某列字段的条件，写到object
                if (Pattern.matches(pattern[k], object.getString(keys[k]))) {
                    object.put(keys[k], object.getString(keys[k]));
                } else {
                    object.put("sysRemarks", "该行字段" + keys[k] + "数据格式不正确");
                    continue;
                }
            }

        }
        return null;
    }

    //上行正向信号模型
    public static Map<String, List> upModel(List<Object> list, int row) {
        List<String> listError = new ArrayList<>();
        List<Object> listOk = new ArrayList<>();
        int i = 0;
        for (Object a : list) {
            if (i < row) {
                i++;
                // 跳过表头行数
                continue;
            }
            String b = a.toString();
            JSONObject object = (JSONObject) JSONObject.parse(b);
            if (b.equals("")) {
                System.out.println("该行为空，跳过不处理");
                continue;
            }
            if (Pattern.matches("[0-9]{1,5}", object.getString("number"))) {
                //车站名匹配
//                if (!Pattern.matches("[\u4e00-\u9fa5|[0-9]]{0,}", object.getString("stationName"))) {
//                    System.out.println("车站名包含非汉字字符和数字，错误");
//                    System.out.println("b= =====" + b);
//                }
                if (!(Pattern.matches("[A-Z0-9]{0,}", object.getString("signalPointName")) || (object.getString("signalPointName").equals("null")))) {
                    System.out.println("信号点名称包含非英文字母（大）和数字格式错误: " + object.get("signalPointName").toString());
//                    b.concat("信号点名称包含非英文字母（大）和数字格式错误: " + object.get("name").toString());
                    object.put("error", "signalPointName");
                    listError.add(object.toJSONString());
                }

                if (!Pattern.matches("^[K]{1}[0-9]{1,3}[+]{1}[0-9]{3}", object.get("actualMileage").toString())) {
                    if (object.get("actualMileage").toString().equals("null")) {
                        System.out.println("信号点里程为空，跳过该条: " + object.get("number").toString() + object.get("actualMileage").toString());
                        continue;
                    }
                    if (!Pattern.matches("^[0-9]{1,3}[0-9]{3}", object.get("actualMileage").toString())) {
                        System.out.println("信号点里程命名格式错误: " + object.get("actualMileage").toString());
//                        b.concat("信号点里程命名格式错误: " + object.get("actualMileage").toString());
                        object.put("error", "actualMileage");
                        listError.add(object.toJSONString());
                    } else {
//                        System.out.println("信号点里程命名格为数字需转换");
                        String temp = object.get("actualMileage").toString();
                        String actualMileage = "K" + temp.substring(0, temp.length() - 1 - 3) + "+" + temp.substring(temp.length() - 1 - 3, temp.length() - 1);
//                        System.out.println("KKKKKK=  " + actualMileage);
                        object.put("actualMileage", actualMileage);
                        a = object.toString();
                    }

                }
                //todo 简化相同判断
                if (!(Pattern.matches("[A-Z|/|0-9|-]{0,}", object.getString("trackSectionName")))) {
                    if (object.get("trackSectionName").toString().equals("null")) {
                        System.out.println("轨道区段名称为空，跳过该条: " + object.get("trackSectionName").toString());
                        //todo 后续处理
                        continue;
                    }
                    System.out.println("轨道区段包含非英文字母（大）和数字格式错误: " + object.get("trackSectionName").toString());
//                    b.concat("轨道区段包含非英文字母（大）和数字格式错误: " + object.get("trackSectionName").toString());
                    object.put("error", "trackSectionName");
                    listError.add(object.toJSONString());
                }
                if (!(Pattern.matches("[0-9]{0,}[-][0-9]{0,}", object.getString("trackSectionFrequency")))) {
                    if (object.get("trackSectionFrequency").toString().equals("null")) {
                        System.out.println("轨道区段载频为空，跳过该条: " + object.get("trackSectionFrequency").toString());
                        //todo 后续处理
                        continue;
                    }
                    System.out.println("轨道区段载频数字格式错误: " + object.get("number").toString() + object.get("trackSectionFrequency").toString());
//                    b.concat("轨道区段载频数字格式错误: " + object.get("trackSectionFrequency").toString());
                    object.put("error", "trackSectionFrequency");
                    listError.add(object.toJSONString());
                }


            } else if (Pattern.matches(".*编制者.*", object.get("number").toString())) {
                //todo 取编制者
                System.out.println("跳过");
                break;
            }
            listOk.add(a);
        }
        if (listError.size() == 0) {
            Map<String, List> map = new HashMap<>();
            map.put("Ok", listOk);
            return map;
        } else {
            Map<String, List> map = new HashMap<>();
            map.put("Error", listError);
            return map;
        }
    }

    //todo 表格式检查，list为某表的数据list每行，R为表格式站的行数
    public static Map<String, String> checkHead(List<Object> list, int R) {
        Map<String, String> map = new HashMap();
        String[] keys = {"number", "stationName", "signalPointName", "actualMileage", "signalPointType", "insulationSectionType", "trackSectionName", "codingSystem", "trackSectionFrequency", "actualLength", "sectionProperties", "remarks"};
        for (int i = 0; i < R; i++) {
            if (list.get(i).toString().equals("")) {
                continue;
            }
            JSONObject object = (JSONObject) JSONObject.parse(list.get(i).toString());
//            System.out.println("object=: " + object);
            //表格式定义和model 用的模型有关
            for (String a : keys) {
                if (map.get(a) == null) {
                    map.put(a, object.getString(a) + '+');
                } else {
                    map.put(a, map.get(a).concat(object.getString(a)) + '+');
                }
            }
        }
        Map<String, String> listError = new HashMap<>();
        Map<String,String> errorMap = new HashMap<>();
        //匹配model中定义的 表格式
        for (String a : keys) {
//            System.out.println("读取的"+a+"列： " + map.get(a));
//            System.out.println("模型的"+a+"列 " + ReadModel3.sheetHead().get(a));
            if (map.get(a).contains(ReadModel3.sheetHead().get(a))) {
                continue;
//                System.out.println(a + "对应的列表格式正确: " + ReadModel3.sheetHead().get(a));
            } else {
                System.out.println(a + " 对应的列表格式不正确: " + map.get(a)+"->>"+ReadModel3.sheetHead().get(a));
                errorMap.put(a,"error");
            }
//            errorMap.clear();
        }
        listError.put("error", errorMap.toString());
        return listError;
    }
}
