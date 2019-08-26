package com.swjt.fileManagement.utils.excel.standardModel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;

public interface SMFun<T> {
    /**
     * @param filename
     * @param sheet
     * @param notNeed      不要的前几行
     * @param Row          根据前几行创建(不包括row)
     * @param t            Read的modeel 类
     * @param indexNameMap {"序号":"number","备注":"remarks"}
     * @param RowMap       {1:[序号,车站名,备注],2:[]}
     * @return
     * @throws IOException
     */
    //todo sheet为空，会有错误，调用时应先判断。
    public default Map<String, T> createReadModel(String filename, Sheet sheet, int notNeed, int Row, T t, Map<String, String> indexNameMap, Map<Integer, List<String>> RowMap, Map<String, String> uniformityMap) throws IOException {
        Map<String, Integer> indexMap = new HashMap<>();
        Map<String, T> model3Map = new HashMap<>();
        List<List<String>> list = new ArrayList<>();
        InputStream inputStreamS = new BufferedInputStream(new FileInputStream(filename));
        List<Object> data = EasyExcelFactory.read(inputStreamS, new Sheet(sheet.getSheetNo(), sheet.getHeadLineMun()));
        inputStreamS.close();
        indexMap.clear();
        for (int i = 0; i < Row; i++) {
            if (i < notNeed) {
                continue;
            }
            list.clear();
            list.add((List<String>) data.subList(i, i + 1).get(0));
            //实际表头列前后空格去掉
            List list1 = new ArrayList();
            list.get(0).forEach(a -> {
                if (a != null) {
                    list1.add(a.trim());
                } else {
                    list1.add(a);
                }
            });
            list.set(0, list1);

            System.out.println("head->list= " + list.get(0));
            Set<String> set = new HashSet<>();
            if (i == 0) {
                for (String key : RowMap.get(0)) {
//                    System.out.println("key0= "+key);
                    if (set.add(key)) {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, ((list.get(0)).indexOf(key)));
                    } else {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).lastIndexOf(key));
                    }
                }
            } else if (i == 1) {
                set.clear();
                for (String key : RowMap.get(1)) {
//                    System.out.println("key1= "+key);
                    if (set.add(key)) {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).indexOf(key));
                    } else {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).lastIndexOf(key));
                    }
                }
            } else if (i == 2) {
                set.clear();
                for (String key : RowMap.get(2)) {
//                    System.out.println("key2= "+key);
                    if (set.add(key)) {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).indexOf(key));
                    } else {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).lastIndexOf(key));
                    }
                }
            } else if (i == 3) {
                set.clear();
                for (String key : RowMap.get(3)) {
//                    System.out.println("key3= "+key);
                    if (set.add(key)) {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).indexOf(key));
                    } else {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).lastIndexOf(key));
                    }
                }
            } else if (i == 4) {
                set.clear();
                for (String key : RowMap.get(4)) {
//                    System.out.println("key4= "+key);
                    if (set.add(key)) {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).indexOf(key));
                    } else {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).lastIndexOf(key));
                    }
                }
            } else if (i == 5) {
                set.clear();
                for (String key : RowMap.get(5)) {
//                    System.out.println("key5= "+key);
                    if (set.add(key)) {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).indexOf(key));
                    } else {
                        if ((list.get(0)).indexOf(key) == -1) {
                            continue;
                        }
                        indexMap.put(key, (list.get(0)).lastIndexOf(key));
                    }
                }
            }
        }
        for (Object key : uniformityMap.keySet()) {
            if (indexMap.get(key) != null) {
                int temp = indexMap.get(key);
                indexMap.remove(key);
                indexMap.putIfAbsent(uniformityMap.get(key), temp);
            }
        }
        System.out.println("indexMap=" + indexMap);
        System.out.println("indexNameMap=" + indexNameMap);
        // 将某一合并的列中后面几列没有取名的列，利用命名的特定格式确定没有命名的列是在哪列（index）
        //eg;  |方向          |
        //     |    |通过|    |  根据方向能和特殊命名 方向>>1 能拿到通过这列对应的excel的Index值，方向>>2 能拿到通过后依赖对应的excel的Index值
        for (String key : indexNameMap.keySet()) {
            if (indexMap.get(key) == null) {
                List lists = Arrays.asList(key.split(">>"));
                int index = indexMap.get(lists.get(0));
                int newIndex = index + (Integer.valueOf(lists.get(1).toString()));
                System.out.println(newIndex);
                indexMap.put(key, newIndex);
                System.out.println("key=" + indexMap.get(key));
            }
        }
        try {
            for (Object key : indexNameMap.keySet()) {
                try {
                    if (indexMap.get(key) == null) {
                        throw new Exception("表头有问题:" + key);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                Field field = t.getClass().getDeclaredField(indexNameMap.get(key));
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                InvocationHandler h = Proxy.getInvocationHandler(excelProperty);
                Field hfield = h.getClass().getDeclaredField("memberValues");
                hfield.setAccessible(true);
                Map memberValues = (Map) hfield.get(h);
                memberValues.put("index", indexMap.get(key));
            }
            model3Map.put(sheet.getSheetName(), t);
//            model3Map.put(sheet.getSheetName(), changeExcelPropertyIndex(indexMap, t, indexNameMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
        return model3Map;
    }

//    /**
//     * aop 方式      * ExcelProperty 注解
//     * indexMap，model 模型，indexNameMap 属性对应的 filed元素
//     *
//     * @param indexMap     实际model 元素 对应的index  map
//     * @param readModel    model
//     * @param indexNameMap ExcelProperty 注解的model 元素和 @param indexMap 关联对应
//     * @return
//     * @throws NoSuchFieldException
//     * @throws IllegalAccessException
//     */
//    public default T changeExcelPropertyIndex(Map indexMap, T readModel, Map<String, String> indexNameMap) throws NoSuchFieldException, IllegalAccessException {
//        for (Object key : indexNameMap.keySet()) {
//            Field field = readModel.getClass().getDeclaredField(indexNameMap.get(key));
//            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
//            InvocationHandler h = Proxy.getInvocationHandler(excelProperty);
//            Field hfield = h.getClass().getDeclaredField("memberValues");
//            hfield.setAccessible(true);
//            Map memberValues = (Map) hfield.get(h);
//            memberValues.put("index", indexMap.get(key));
//        }
//        return readModel;
//    }


    public static Map<String, String> checkHeadIteration(List<Object> list, String[] keys, int R) {
        Map<String, String> map = new HashMap();
        for (int i = 0; i < R; i++) {
            if (list.get(i).toString().equals("")) {
                continue;
            }
//            System.out.println(list.get(i));
            JSONObject object = (JSONObject) JSONObject.parse(list.get(i).toString().replace("\\", "\\\\"));
//            System.out.println(object);
//            System.out.println("checkHeadIteration=" + object);
            //表格式定义和CADmodel 用的模型有关
            for (String a : keys) {
                if (map.get(a) == null) {
                    map.put(a, object.getString(a) + '+');
                } else {
                    map.put(a, map.get(a).concat(object.getString(a)) + '+');
                }
            }
        }
        return map;
    }
//    /**
//     * aop 方式      * ExcelProperty 注解
//     * indexMap，model 模型，indexNameMap 属性对应的 filed元素
//     *
//     * @param indexMap     实际model 元素 对应的index  map
//     * @param readModel    model
//     * @param indexNameMap ExcelProperty 注解的model 元素和 @param indexMap 关联对应
//     * @return
//     * @throws NoSuchFieldException
//     * @throws IllegalAccessException
//     */
//    public static Object changeExcelPropertyIndex(Map indexMap, Object readModel, Map<String, String> indexNameMap) throws NoSuchFieldException, IllegalAccessException {
//        for (Object key : indexNameMap.keySet()) {
//            Field field = readModel.getClass().getDeclaredField(indexNameMap.get(key));
//            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
//            InvocationHandler h = Proxy.getInvocationHandler(excelProperty);
//            Field hfield = h.getClass().getDeclaredField("memberValues");
//            hfield.setAccessible(true);
//            Map memberValues = (Map) hfield.get(h);
//            memberValues.put("index", indexMap.get(key));
//        }
//        return readModel;
//    }
}
