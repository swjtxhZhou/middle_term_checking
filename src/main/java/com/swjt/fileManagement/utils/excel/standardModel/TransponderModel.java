package com.swjt.fileManagement.utils.excel.standardModel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;

import com.swjt.fileManagement.utils.excel.model.TransponderReadModel;
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

@Getter
@Setter
public class TransponderModel {

    private String number;
    private String transponderName;
    private String transponderNumber;
    private String mileage;
    private String type;
    private String purpose;
    private String stationName;
    private String remark;
    private String isUpLine;
    private String status;


    public static Map<String, TransponderReadModel> getTransponderReadModel(String filename, Sheet sheet, int Row, TransponderReadModel readModel) throws IOException {
        Map<String, Integer> indexMap = new HashMap<>();
        Map<String, TransponderReadModel> model3Map = new HashMap<>();
        //model 元素名和indexMap 关联
        Map<String, String> indexNameMap = new HashMap<>();
        indexNameMap.put("序号", "number");
        indexNameMap.put("应答器名称", "transponderName");
        indexNameMap.put("应答器编号", "transponderNumber");
        indexNameMap.put("里程", "mileage");
        indexNameMap.put("设备类型", "type");
        indexNameMap.put("用途", "purpose");
        indexNameMap.put("车站名称", "stationName");
        indexNameMap.put("备注", "remark");
        List<List<String>> list = new ArrayList<>();
//        for (Sheet sheet : sheets) {
        InputStream inputStreamS = new BufferedInputStream(new FileInputStream(filename));
        List<Object> data = EasyExcelFactory.read(inputStreamS, new Sheet(sheet.getSheetNo(), sheet.getHeadLineMun()));
        inputStreamS.close();
        indexMap.clear();
        //实际有效的表头
        for (int i = 0; i < Row; i++) {
            list.clear();
            list.add((List<String>) data.subList(i, i + 1).get(0));
//                System.out.println(sheet.getSheetName() + i + "list= " + list);
            if (i == 1) {
                indexMap.put("序号", (list.get(0)).indexOf("序号"));
                indexMap.put("应答器名称", (list.get(0)).indexOf("应答器名称"));
                indexMap.put("应答器编号", (list.get(0)).indexOf("应答器编号"));
                indexMap.put("里程", (list.get(0)).indexOf("里程"));
                indexMap.put("设备类型", (list.get(0)).indexOf("设备类型"));
                indexMap.put("用途", (list.get(0)).indexOf("用途"));
                indexMap.put("车站名称", (list.get(0)).indexOf("车站"));
                indexMap.put("备注", (list.get(0)).indexOf("备注"));
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

    private static TransponderReadModel changeExcelPropertyIndex(Map indexMap, TransponderReadModel readModel, Map<String, String> indexNameMap) throws NoSuchFieldException, IllegalAccessException {
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


    //todo 表格式检查，list为某表的数据list每行，R为表格式站的行数
    public static Map<String, String> TranspondercheckHead(List<Object> list, int R) {
//        System.out.println("lsit = "+ list);
        Map<String, String> map = new HashMap();
        String[] keys = {"number", "transponderName", "transponderNumber", "mileage", "type", "purpose", "stationName", "remark"};
        for (int i = 0; i < R; i++) {
            if (list.get(i).toString().equals("")) {
                continue;
            }
            JSONObject object = (JSONObject) JSONObject.parse(list.get(i).toString());
            //表格式定义和CADmodel 用的模型有关
            for (String a : keys) {
                if (map.get(a) == null) {
                    map.put(a, object.getString(a) + '+');
                } else {
                    map.put(a, map.get(a).concat(object.getString(a)) + '+');
                }
            }
        }
        Map<String, String> listError = new HashMap<>();
        Map<String, String> errorMap = new HashMap<>();
        //匹配model中定义的 表格式
        for (String a : keys) {
//            System.out.println("map.get(a)="+map.get(a));
//            System.out.println("TransponderReadModel.sheetHead().get(a)="+ TransponderReadModel.sheetHead().get(a));
            if (map.get(a).contains(TransponderReadModel.sheetHead().get(a))) {
                continue;
            } else {
                System.out.println(a + "=  对应的列表格式不正确: " + TransponderReadModel.sheetHead().get(a));
                errorMap.put(a, map.get(a) + "-->" + TransponderReadModel.sheetHead().get(a));
            }
            listError.put("error", errorMap.toString());
        }
        return listError;
    }

    /**
     * 数据的格式检查
     *
     * @param list
     * @param row
     * @return  返回map ; map{
     *                        "Ok",List，
     *                         "Error"，List，
     *                         ”StringWhy",List
     *                      }
     *  Ok：没有错误，返回所有正确的数据;   Error: 某行具体错误项 ;  StringWhy 具体错误原因
     */
    public static Map<String, List> checkData(List<Object> list, int row) {
        //错误的完整行数据
        List<String> listError = new ArrayList<>();
        //错误的每行具体列错误和原因
        List<StringBuffer> listErrorWhy = new ArrayList<>();
        // 完全正确的数据
        List<Object> listOk = new ArrayList<>();
        int i = 0, j = 0;
        for (Object a : list) {
            if (i < row) {
                i++;
                // 跳过表头行数
                continue;
            }
            j++;
            JSONObject object = (JSONObject) JSONObject.parse(a.toString());
            if (a.toString().equals("")) {
                System.out.println("该行为空，跳过不处理");
                continue;
            }
            StringBuffer ErrorWhy = new StringBuffer();
            if (Pattern.matches("[0-9]{1,6}", object.getString("number"))) {
                if (!(Pattern.matches("[B][A-Z|0-9]{0,9}[-]?[0-9]+", object.getString("transponderName")) || (object.getString("transponderName").equals("null")))) {
                    System.out.println("应答器名称数据格式: " + object.get("transponderName").toString());
                    object.put("error", "transponderName");
                    listError.add(object.toJSONString());
                    ErrorWhy.append(ErrorWhy.toString() + "第 " + object.getString("number") + "行, 数据[信号点名称] " + object.getString("signalPointName") + "  ->数据格式错误。");
                }
                // 应答器编号
                if (!Pattern.matches("[0-9]{3}[-][0-9][-][0-9]{2}[-][0-9]{3}[-]?[0-9]?", object.get("transponderNumber").toString())) {
                    System.out.println("应答器编号: " + object.get("transponderNumber").toString());
                    System.out.println(object);
                    object.put("error", "transponderNumber");
                    listError.add(object.toJSONString());
                    ErrorWhy.append(ErrorWhy.toString() + "第 " + object.getString("number") + "行, 数据[信号点里程] " + object.getString("transponderNumber") + "  ->数据格式错误。");
                }
                //应答器里程
                if (!Pattern.matches("[A-Z]{0,4}[K][0-9]{1,4}[+][0-9]{3}", object.getString("mileage"))) {
                    System.out.println("应答器里程: " + object.get("mileage").toString());
                    //todo 应答器里程 数据为空且为调车数据没有处理，DC/FDC
                    object.put("error", "mileage");
                    listError.add(object.toJSONString());
                    ErrorWhy.append(ErrorWhy.toString() + "第 " + object.getString("number") + "行, 应答器里程 " + object.getString("mileage") + "  ->数据格式错误。");
                }
            } else if (Pattern.matches(".*编制者.*", object.get("number").toString())) {
                //todo 取编制者
                System.out.println("跳过");
                break;
            }
            if (ErrorWhy.length() > 0) {
//                System.out.println("ErrorWhy" + ErrorWhy);
//                System.out.println("listErrorWhy.size()" + listErrorWhy.size());
                listErrorWhy.add(ErrorWhy);
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
            map.put("StringWhy", listErrorWhy);
            return map;
        }
    }


    public static List<TransponderModel> exchageData(List<Object> list, int R) {
        List<TransponderModel> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i >= R) {
                TransponderModel cadSignalModel = new TransponderModel();
                JSONObject object = (JSONObject) JSONObject.parse(list.get(i).toString());
                if (Pattern.matches(".*编制者.*", object.get("number").toString())) {
                    //todo 取编制者
                    System.out.println("编制者-> 跳过");
                    break;
                }
                cadSignalModel.setNumber(object.getString("number"));
                cadSignalModel.setTransponderName(object.getString("transponderName"));
                cadSignalModel.setTransponderNumber(object.getString("transponderNumber"));
                cadSignalModel.setMileage(object.getString("mileage"));
                cadSignalModel.setType(object.getString("type"));
                cadSignalModel.setPurpose(object.getString("purpose"));
                cadSignalModel.setStationName(object.getString("stationName"));
                cadSignalModel.setRemark(object.getString("remark"));
                list1.add(cadSignalModel);
            }
        }
        return list1;
    }
}
