package com.swjt.fileManagement.utils.excel.listen;//package com.swjtu.tsc.util.excel.listen;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class testListener extends AnalysisEventListener {
//
//    private List<Object> data = new ArrayList<Object>();
//    private Map<String, Object> map = new HashMap<>();
//
//    //每读一行数据回调一次
//    @Override
//    public void invoke(Object object, AnalysisContext context) {
//        data.add(object);
////        System.out.println("data=  " + object);
//        if (data.size() >= 100) {
////            System.out.println(context.getCurrentSheet());
//            doSomething(context);
//            data = new ArrayList<Object>();
//        }
////        doAfterAllAnalysed(context);
//    }
//
//    //每个sheet完后调用一次 ，data 要清空
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext context) {
//        doSomething(context);
//    }
//
//    public void doAddToMap(AnalysisContext context) {
//        String sheetName = context.getCurrentSheet().getSheetName();
////        System.out.println("sheetName" + sheetName);
////        System.out.println("mapm  " + map);
//        if (map.get(sheetName) == null) {
//            map.put(sheetName, context);
//        } else {
////            System.out.println(map.get(sheetName));
//            map.put(sheetName, map.get(sheetName).toString().concat(data.toString()));
//        }
//    }
//
//    //data 用完后要清空
//    public void doSomething(AnalysisContext context) {
//        doAddToMap(context);
//        data.clear();
//    }
//
//    public Map<String, Object> oneExcelDB() {
//        return map;
//    }
//}
