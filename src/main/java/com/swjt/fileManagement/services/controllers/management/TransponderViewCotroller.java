package com.swjt.fileManagement.services.controllers.management;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.swjt.fileManagement.data.EiTransponder;
import com.swjt.fileManagement.services.services.FileVersionManageService;
import com.swjt.fileManagement.services.services.common.HttpResponse;
import com.swjt.fileManagement.services.services.common.ResultUtil;
import com.swjt.fileManagement.services.services.model.EiTransponderModel;
import com.swjt.fileManagement.services.services.tc.ei.EiTransponderService;

import com.swjt.fileManagement.utils.excel.listen.ExcelListener;
import com.swjt.fileManagement.utils.excel.model.TransponderReadModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


import static com.swjt.fileManagement.utils.excel.standardModel.TransponderModel.getTransponderReadModel;

@RestController("View")
@RequestMapping("/View")
public class TransponderViewCotroller {
    @Autowired
    private EiTransponderService eiTransponderService;

    @Autowired
    private FileVersionManageService fileVersionManageService;


    @PutMapping("/Operate/transponderTable")
    public HttpResponse<List> operate(@RequestBody List<JSONObject> lists){
        List<String> results = new LinkedList<>();
        for(JSONObject list:lists){
//            int operateId = list.getInteger("operateId");
            int operateType = list.getInteger("operateType");
//            JSONObject object = list.getJSONObject("object");
            String operator = "admin";
            String operateInfo = null;
            switch (operateType){
                case 1://删除
                    operateInfo = DBDeleteRow(list.getInteger("operateId"),operator);
                    break;
                case 2://修改
                    operateInfo = DBUpdateRow(list.getInteger("operateId"),list.getJSONObject("object"),operator);
                    break;
                case 3://增加
                    operateInfo = DBCreateRow2(list.getInteger("operateId"),operator,list.getJSONArray("objects"));
                    break;
                default://未定义
                    break;
            }
            results.add("该条操作状态"+operateInfo);
        }
        return ResultUtil.success(results,"/View/Operate/transponderTable","对应答器表格数据更新结果");

    }



    /*查询表格的最新版本，需要的参数，表格索引Id,上行还是下行isUpLine
     */
    @GetMapping("/Query/transponderTable")
    public HttpResponse<List> transponderTable(@RequestParam("id") Integer id, @RequestParam("isUpLine") Boolean isUpLine){


        return ResultUtil.success(DBQuery(id,isUpLine),"/View/Query/transponderTable","应答器位置表数据库查询");
    }


    /*
     * 通过id值先查询fileVerisonManage中的源文件的路径地址，
     * 在通过该地址解析excel表的内容，并以JSON格式返回
     * */

    @GetMapping("/transponderOriginFile")
    public HttpResponse<List> transponderOriginTable(@RequestParam("id") Integer id){
      /*  JSONObject transponderOriginTable = new JSONObject(new LinkedHashMap());//因为JsonObject内部是用Hashmap来存
        储的，所以输出是按key的排序来的，如果要让JsonObject按固定顺序（put的顺序）排列，可以修改JsonObject的定义HashMap改为
        LinkedHashMap。
        */
        List<JSONObject> lists= new LinkedList<>();


        //JSONObject jsonObject = JSON.parseObject(info);
        //int Id = jsonObject.getInteger("id");
        System.out.println(id);
        String filePath;
        filePath = fileVersionManageService.get(id).getFilePath();//文件路径
        System.out.println(filePath);

        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
            ExcelListener excelListener = new ExcelListener();
            ExcelReader excelReader = EasyExcelFactory.getReader(inputStream, excelListener);
            List<Sheet> sheets = excelReader.getSheets();
            int count = 0;
            for (Sheet sheet : sheets){

                //一张表一张表的读取
                TransponderReadModel readModel = new TransponderReadModel();
                Map<String, TransponderReadModel> transponderReadModelMap = new HashMap<>(getTransponderReadModel(filePath, sheet, 2, readModel));//读取应答器位置表
                sheet.setClazz(transponderReadModelMap.get(sheet.getSheetName()).getClass());
                excelReader.read(sheet);
                transponderReadModelMap.clear();
                Map a = excelListener.oneExcelDB();
                String one = a.get(sheet.getSheetName()).toString();
                String[] temp = one.substring(one.indexOf("[") + 1, one.length() - 1).replace("=", ":").replace(" ", "").replace("][", "Agile").replace("},", "}Agile").split("Agile");
                List<Object> list = Arrays.asList(temp);
                List<Object> list1 = new ArrayList<>(list);
                list1.remove(0);//去除split产生的第一个为空对象
                boolean up;
                if (sheet.getSheetName().contains("上")) {
                    up = true;
                } else {
                    up = false;
                }
                System.out.println(up);

                int len = list1.size();//数据的条数

                System.out.println(len);
                //  transponderOriginData = new JSONObject[len];
                for (Object c : list1){

                    JSONObject object = (JSONObject) JSONObject.parse(c.toString());
                    if (!Pattern.matches("[0-9]{1,5}", object.getString("number"))) {
                        System.out.println("该行序号不是数字跳过：  " + object);
                        //todo 表头和表尾没有处理，没有写进数据库
                        continue;
                    }
                    object.put("isUpLine",up);
                    lists.add(object);
                    // transponderOriginTable.put(count+"", object);
                    System.out.println(count);
                    count++;

                }
            }
            inputStream.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }

        return ResultUtil.success(lists,"/View/transponderOriginFile","应答器位置表源文件预览");
    }

    public List<JSONObject> DBQuery(int id, Boolean isUpLine){
        //JSONObject QueryTable = new JSONObject(new LinkedHashMap<>());
        List<JSONObject> QueryTable = new ArrayList<>();
        //JSONObject QueryData[];
        List<EiTransponder> items = eiTransponderService.getByInputId(id,isUpLine);

        List<EiTransponderModel> targets = EiTransponderModel.exchangeData(items);
        //targets.stream().sorted(Comparator.comparingInt(EiTransponderModel::getNumber));
        Collections.sort(targets, (o1, o2) -> {
            if(o1.getNumber() > o2.getNumber()){
                return 1;
            }else if(o1.getNumber() == o2.getNumber()){
                return 0;
            }
            return -1;
        });
        int len = targets.size();
        // QueryData = new JSONObject[len];
        //  int count = 0;
        for(EiTransponderModel target:targets){
            if (len > 0){
                JSONObject QueryData = new JSONObject();
                //QueryData[count] = new JSONObject();
                QueryData.put("id",target.getId());
                QueryData.put("stationName",target.getStationName());//车站名
                QueryData.put("name",target.getName());//应答器名称
                QueryData.put("mileage",target.getMileage());//里程
                QueryData.put("type",target.getType());//应答器类型
                QueryData.put("purpose",target.getPurpose());//应答器用途
                QueryData.put("transponderNumber",target.getTransponderNumber());//应答器编号
                QueryData.put("lineId",target.getLineId());
                QueryData.put("isUpLine",target.getIsUpLine());
                QueryData.put("remark",target.getRemark());//备注
                QueryData.put("operator",target.getOperator());
                Date operatingTime = target.getOperatingTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                QueryData.put("operatingTime",sdf.format(operatingTime));
                QueryData.put("status",target.getStatus());
                QueryData.put("number",target.getNumber());
                QueryData.put("inputId",target.getInputId());

                QueryTable.add(QueryData);
                //count++;
            }
            else {
                System.out.println("空值");
            }

        }

        return QueryTable;
    }


    private String DBDeleteRow(int operateId,String operator){
        String result = null;
        try {
            /**
             * 判断是否当前操作的数据是否是增加的数据
             */
            if(eiTransponderService.getById(operateId).getStatus() == (byte) 3){
                EiTransponder item = eiTransponderService.getById(operateId);//获取目标行

                if (item != null) {
                    item.setStatus((byte) 5);//将该行状态标记为增加删除状态
                    item.setOperator(operator);
                    item.setOperatingtime(new Date());//删除了只会有一次操作时间
                    System.out.println(item.getId());
                    eiTransponderService.update(item);
                    result = "删除成功";
                } else {
                    result = "未找到目标行";
                }
            }
            else {

                EiTransponder item = eiTransponderService.getById(operateId);//获取目标行

                if (item != null) {
                    item.setStatus((byte) 1);//将该行状态标记为删除状态
                    item.setOperator(operator);
                    item.setOperatingtime(new Date());//删除了只会有一次操作时间
                    System.out.println(item.getId());
                    eiTransponderService.update(item);
                    result = "删除成功";
                } else {
                    result = "未找到目标行";
                }
            }
        }catch (Exception e){
            System.out.println(e);
            result = "删除失败  ";
        }
        return result;
    }

    private String DBUpdateRow(int id,JSONObject updateInfo,String operator){
        String result = null;
        try {
            if (eiTransponderService.getById(id).getStatus() == (byte) 3) {
                /**
                 * 增加了又修改的情况
                 */
                EiTransponder item = eiTransponderService.getById(id);//获取目标表格的所有行
                if (item != null) {
                    /**
                     * 增加又修改的情况原始增加的数据将状态致4（暂时）
                     */
                    item.setStatus((byte) 4);
                    eiTransponderService.update(item);
                    /**
                     * 新修改的状态致增加状态3
                     */

                    EiTransponder updateEiTransponder = new EiTransponder();

                    updateEiTransponder.setName(updateInfo.getString("name"));
                    updateEiTransponder.setNumber(item.getNumber());
                    updateEiTransponder.setMileage(updateInfo.getString("mileage"));
                    updateEiTransponder.setType(updateInfo.getString("type"));
                    updateEiTransponder.setPurpose(updateInfo.getString("purpose"));
                    updateEiTransponder.setStationName(updateInfo.getString("stationName"));
                    updateEiTransponder.setIsUpLine(item.getIsUpLine());
                    updateEiTransponder.setStatus((byte) 3);//将状  态致增加状态
                    updateEiTransponder.setInputId(item.getInputId());
                    updateEiTransponder.setTranponderNumber(updateInfo.getString("transponderNumber"));
                    updateEiTransponder.setLineId(item.getLineId());
                    updateEiTransponder.setRemark(updateInfo.getString("remark"));
                    // updateEiTransponder.setOrderId(item.getOrderId());//OrderId不变
                    updateEiTransponder.setOperatingtime(new Date());
                    updateEiTransponder.setOperator(operator);
                    eiTransponderService.create(updateEiTransponder);
                    result = "更新成功";
                } else {
                    result = "未找到目标行";
                }
            }
            else{

                EiTransponder item = eiTransponderService.getById(id);//获取目标表格的所有行
                if (item != null) {

                    item.setStatus((byte) 2);
                    eiTransponderService.update(item);
                    EiTransponder updateEiTransponder = new EiTransponder();

                    updateEiTransponder.setName(updateInfo.getString("name"));
                    updateEiTransponder.setNumber(item.getNumber());
                    updateEiTransponder.setMileage(updateInfo.getString("mileage"));
                    updateEiTransponder.setType(updateInfo.getString("type"));
                    updateEiTransponder.setPurpose(updateInfo.getString("purpose"));
                    updateEiTransponder.setStationName(updateInfo.getString("stationName"));
                    updateEiTransponder.setIsUpLine(item.getIsUpLine());
                    updateEiTransponder.setStatus((byte) 0);//将状  态致原始状态
                    updateEiTransponder.setInputId(item.getInputId());
                    updateEiTransponder.setTranponderNumber(updateInfo.getString("transponderNumber"));
                    updateEiTransponder.setLineId(item.getLineId());
                    updateEiTransponder.setRemark(updateInfo.getString("remark"));
                    // updateEiTransponder.setOrderId(item.getOrderId());//OrderId不变
                    updateEiTransponder.setOperatingtime(new Date());
                    updateEiTransponder.setOperator(operator);
                    eiTransponderService.create(updateEiTransponder);
                    result = "更新成功";
                } else {
                    result = "未找到目标行";
                }
            }
        }catch (Exception e){
            System.out.println(e);
            result= "更新失败";
        }
        return result;
    }

    private String DBCreateRow3(int afterId,String operator,JSONArray objects){
        String result = null;
        try{
            /**
             * 在表格末尾增加数据,间隔为固定的128
             */
            if(afterId == 0) {
                int interval = 128;
                int len = objects.size();
                JSONObject object = objects.getJSONObject(0);
                Boolean up = object.getBoolean("isUpLine");
                int inputId = object.getInteger("inputId");
                List<EiTransponder> items = eiTransponderService.getByInputId(inputId, up);
                List<EiTransponderModel> targets = EiTransponderModel.exchangeData(items);
                //targets.stream().sorted(Comparator.comparing(EiTransponderModel::getNumber));
                Collections.sort(targets, (o1, o2) -> {//对数据按number的大小排序
                    if(o1.getNumber() > o2.getNumber()){
                        return 1;
                    }else if(o1.getNumber() == o2.getNumber()){
                        return 0;
                    }
                    return -1;
                });
                int beforeTargetNumber = targets.get(targets.size()-1).getNumber();//目标表格末尾number
                for(int i=0; i<len;i++) {
                    EiTransponder eiTransponder = new EiTransponder();

                    eiTransponder.setNumber(String.valueOf(beforeTargetNumber + (i + 1) * interval));
                    eiTransponder.setName(objects.getJSONObject(i).getString("name"));
                    eiTransponder.setMileage(objects.getJSONObject(i).getString("mileage"));
                    eiTransponder.setType(objects.getJSONObject(i).getString("type"));
                    eiTransponder.setPurpose(objects.getJSONObject(i).getString("purpose"));
                    eiTransponder.setStationName(objects.getJSONObject(i).getString("stationName"));
                    eiTransponder.setTranponderNumber(objects.getJSONObject(i).getString("transponderNumber"));
                    eiTransponder.setRemark(objects.getJSONObject(i).getString("remark"));
                    eiTransponder.setOperatingtime(new Date());
                    eiTransponder.setOperator(operator);
                    eiTransponder.setInputId(objects.getJSONObject(i).getInteger("inputId"));
                    eiTransponder.setIsUpLine(objects.getJSONObject(i).getBoolean("isUpLine"));
                    eiTransponder.setStatus((byte) 3);
                    eiTransponder.setLineId(objects.getJSONObject(i).getInteger("lineId"));

                    eiTransponderService.create(eiTransponder);
                }
                result = "增加成功";
            }
            /**
             * 在表格中间插入数据
             */
            else{
                int count = 0;

                int flag=0;
                int len = objects.size();//即时增加数据的数目
                int afterNumber = Integer.valueOf(eiTransponderService.getById(afterId).getNumber());//插入数据的后一条数据number
                /**
                 * 找出前一个128倍数和后一个128倍数；
                 */
                JSONObject object = null;
                object = objects.getJSONObject(0);
                Boolean up = object.getBoolean("isUpLine");
                int inputId = object.getInteger("inputId");
                List<EiTransponder> items = eiTransponderService.getByInputId(inputId, up);
                List<EiTransponderModel> targets = EiTransponderModel.exchangeData(items);
                /**
                 * 对数据按number大小排序
                 */
                Collections.sort(targets, (o1, o2) -> {
                    if (o1.getNumber() > o2.getNumber()) {
                        return 1;
                    } else if (o1.getNumber() == o2.getNumber()) {
                        return 0;
                    }
                    return -1;
                });

                int beforeRowNumber = 0;

                for (EiTransponderModel target : targets) {
                    if (Integer.valueOf(target.getNumber()) == afterNumber) {
                        // System.out.println(beforeRowOrderId);
                        break;
                    } else {
                        beforeRowNumber = Integer.valueOf(target.getNumber());//插入数据的前一条数据number
                        //  System.out.println(beforeRowOrderId);
                    }
                }
                System.out.println("beforeRowNumber:" + beforeRowNumber);
                /**
                 * 如果单次增加的条数超过内增加的间隙大小，则增加失败
                 */
                if ((afterNumber - beforeRowNumber) < len) {
                    result = "超出单次增加条数上限";
                } else {
                    List<EiTransponderModel> beforeList = new LinkedList<>();
                    List<EiTransponderModel> afterList = new LinkedList<>();
                    for (EiTransponderModel target : targets) {
                        if (target.getNumber() < afterNumber) {
                            if (target.getNumber() % 128 == 0) {
                                beforeList.add(target);
                            }
                        }
                    }
                    for (EiTransponderModel target : targets) {
                        if (target.getNumber() >= afterNumber) {
                            if (target.getNumber() % 128 == 0) {
                                afterList.add(target);
                            }
                        }
                    }
                    int beforeTargetNumber = beforeList.get(beforeList.size() - 1).getNumber();//前一个128
                    int afterTargetNumber = afterList.get(0).getNumber();//后一个128

                    /**
                     * TODO 将前面的数据和后面的数据移动到靠近128倍数的位置
                     */
                    /**
                     * 将beforeTargetNumber和beforeRowNumber间的数据间隔变为1（包含beforeRpwNUmber）
                     */
                    List<EiTransponderModel> beforeInterval_1 = new ArrayList<EiTransponderModel>();
                    List<EiTransponderModel> afterInterval_1 = new ArrayList<EiTransponderModel>();
                    for(EiTransponderModel target:targets){
                        if(target.getNumber() > beforeTargetNumber && target.getNumber() < afterNumber){
                            /**
                             * 注意序号相同的数据改变后任然保持数据相同
                             */
                            beforeInterval_1.add(target);
                        }
                        if(target.getNumber() > beforeRowNumber &&  target.getNumber() <afterTargetNumber){
                            afterInterval_1.add(target);
                        }
                    }
                    /**
                     * 注意序号相同的数据改变后任然保持数据相同
                     */
                    int o1=0;
                    int o2=0;
                    for(int i=0;i<beforeInterval_1.size();i++){
                        if(i!=0){
                            o1 = beforeInterval_1.get(i).getNumber();
                            if(o1!=flag){//与上一个number不同
                                flag = beforeInterval_1.get(i).getNumber();
                                beforeInterval_1.get(i).setNumber(beforeInterval_1.get(i-1).getNumber()+1);
                                EiTransponder item = eiTransponderService.getById(beforeInterval_1.get(i).getId());
                                item.setNumber(String.valueOf(beforeInterval_1.get(i).getNumber()));
                                eiTransponderService.update(item);
                                beforeRowNumber = beforeInterval_1.get(i).getNumber();
                            }else{//与上一个number相同
                                flag = beforeInterval_1.get(i).getNumber();
                                beforeInterval_1.get(i).setNumber(beforeInterval_1.get(i-1).getNumber());
                                EiTransponder item = eiTransponderService.getById(beforeInterval_1.get(i).getId());
                                item.setNumber(String.valueOf(beforeInterval_1.get(i).getNumber()));
                                eiTransponderService.update(item);
                                beforeRowNumber = beforeInterval_1.get(i).getNumber();
                            }
                        }else{
                            flag = beforeInterval_1.get(0).getNumber();
                            beforeInterval_1.get(0).setNumber(beforeTargetNumber+1);
                            EiTransponder item = eiTransponderService.getById(beforeInterval_1.get(0).getId());
                            item.setNumber(String.valueOf(beforeInterval_1.get(0).getNumber()));
                            eiTransponderService.update(item);
                            beforeRowNumber = beforeInterval_1.get(0).getNumber();
                        }
                    }
                    for(int i=0;i<afterInterval_1.size();i++){
                        if(i!=0){
                            o1 = afterInterval_1.get(i).getNumber();
                            if(o1!=flag){//与上一个number不同
                                flag = afterInterval_1.get(i).getNumber();
                                afterInterval_1.get(i).setNumber(afterInterval_1.get(i-1).getNumber()+1);
                                EiTransponder item = eiTransponderService.getById(afterInterval_1.get(i).getId());
                                item.setNumber(String.valueOf(afterInterval_1.get(i).getNumber()));
                                eiTransponderService.update(item);
                            }else{//与上一个number相同
                                flag = afterInterval_1.get(i).getNumber();
                                afterInterval_1.get(i).setNumber(afterInterval_1.get(i-1).getNumber());
                                EiTransponder item = eiTransponderService.getById(afterInterval_1.get(i).getId());
                                item.setNumber(String.valueOf(afterInterval_1.get(i).getNumber()));
                                eiTransponderService.update(item);
                            }
                        }else{
                            flag = afterInterval_1.get(0).getNumber();
                            afterInterval_1.get(0).setNumber(afterTargetNumber-afterInterval_1.size());
                            EiTransponder item = eiTransponderService.getById(afterInterval_1.get(0).getId());
                            item.setNumber(String.valueOf(afterInterval_1.get(0).getNumber()));
                            eiTransponderService.update(item);
                        }

                    }
                    //按间隔为1把即时数据增加进去

                    for (int i = 0; i < len; i++) {
                        EiTransponder eiTransponder = new EiTransponder();
                        int temporaryNumber = beforeRowNumber + i + 1;
                        eiTransponder.setNumber(String.valueOf(temporaryNumber));
                        System.out.println("temporaryNumber:"+eiTransponder.getNumber());
                        eiTransponder.setName(objects.getJSONObject(i).getString("name"));
                        eiTransponder.setMileage(objects.getJSONObject(i).getString("mileage"));
                        eiTransponder.setType(objects.getJSONObject(i).getString("type"));
                        eiTransponder.setPurpose(objects.getJSONObject(i).getString("purpose"));
                        eiTransponder.setStationName(objects.getJSONObject(i).getString("stationName"));
                        eiTransponder.setTranponderNumber(objects.getJSONObject(i).getString("transponderNumber"));
                        eiTransponder.setRemark(objects.getJSONObject(i).getString("remark"));
                        eiTransponder.setOperatingtime(new Date());
                        eiTransponder.setOperator(operator);
                        eiTransponder.setInputId(objects.getJSONObject(i).getInteger("inputId"));
                        eiTransponder.setIsUpLine(objects.getJSONObject(i).getBoolean("isUpLine"));
                        eiTransponder.setStatus((byte) 3);
                        eiTransponder.setLineId(objects.getJSONObject(i).getInteger("lineId"));
                        eiTransponderService.create(eiTransponder);
                    }

                    //TODO 重新分配间隔，并且维护好numberId相同的情况
                    List<EiTransponderModel> addedTargets = new LinkedList<>();
                    /**
                     * 已经更新了数据库，要再全部找出来处理
                     */
                    List<EiTransponder> operatedItems = eiTransponderService.getByInputId(inputId, up);
                    List<EiTransponderModel> operatedTargets = EiTransponderModel.exchangeData(operatedItems);
                    /**
                     * 对数据按number大小排序
                     */
                    Collections.sort(operatedTargets, (o3, o4) -> {
                        if (o3.getNumber() > o4.getNumber()) {
                            return 1;
                        } else if (o3.getNumber() == o4.getNumber()) {
                            return 0;
                        }
                        return -1;
                    });
                    for (EiTransponderModel operatedTarget : operatedTargets) {
                        if (Integer.valueOf(operatedTarget.getNumber()) > beforeTargetNumber && Integer.valueOf(operatedTarget.getNumber()) < afterTargetNumber) {
                            addedTargets.add(operatedTarget);//两个原始数据之间所有的数据(包括增加，增加修改-修改删除)
                        }
                    }
                    System.out.println("beforeTargetNumber="+beforeTargetNumber+"afterTargetNumber="+afterTargetNumber);
                    System.out.println("size="+addedTargets.size());

                    //TODO 找numberId不同的数据的个数
                    for(int i = 0;i<addedTargets.size()-1;i++){
                        System.out.println("前："+addedTargets.get(i).getNumber()+"后："+addedTargets.get(i+1).getNumber());
                        o1=addedTargets.get(i).getNumber();
                        o2=addedTargets.get(i+1).getNumber();
                        if(o1 != o2){
                            count++;
                        }
                        System.out.println("temporaryCount:"+count);
                    }
                    count++;//中间数据numberId不同的个数

                    //TODO number以等间隔存入
                    System.out.println("count="+count);
                    int interval = (int) (128 / (count+1));//新的间隔
                    System.out.println("interval:"+interval);

                    //TODO 使numberId相同的数据修改间隔后任然相同
                    for(int i=0; i<addedTargets.size();i++){
                        if(i!=0) {
                            /**
                             * 除了第一条数据，剩余的每一条数据都与上一条对比numberId,若相同则新的numberId也相同
                             */
                            o1=addedTargets.get(i).getNumber();
                            if (o1 != flag) {
                                flag=addedTargets.get(i).getNumber();
                                addedTargets.get(i).setNumber(addedTargets.get(i - 1).getNumber() + interval);
                                System.out.println("i:"+i+"real number:" + addedTargets.get(i).getNumber());
                                EiTransponder item = eiTransponderService.getById(addedTargets.get(i).getId());
                                item.setNumber(String.valueOf(addedTargets.get(i).getNumber()));
                                eiTransponderService.update(item);
                            } else {
                                flag=addedTargets.get(i).getNumber();
                                addedTargets.get(i).setNumber(addedTargets.get(i - 1).getNumber());
                                System.out.println("i:"+i+"real number:" + addedTargets.get(i).getNumber());
                                EiTransponder item = eiTransponderService.getById(addedTargets.get(i).getId());
                                item.setNumber(String.valueOf(addedTargets.get(i).getNumber()));
                                eiTransponderService.update(item);
                            }

                        }else{
                            /**
                             * 第一条数据单独处理
                             */
                            flag=addedTargets.get(0).getNumber();
                            addedTargets.get(0).setNumber(beforeTargetNumber + interval);
                            System.out.println("real number:" + addedTargets.get(0).getNumber());
                            EiTransponder item = eiTransponderService.getById(addedTargets.get(0).getId());
                            item.setNumber(String.valueOf(addedTargets.get(0).getNumber()));
                            eiTransponderService.update(item);

                        }
                    }
                    result = "增加成功";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            result = "增加失败";
        }

        return result;
    }
    /**
     *动态增加（均匀间隔）
     */
    private String DBCreateRow2(int afterId,String operator,JSONArray objects){
        String result=null;
        try{
            /**
             * 在表格末尾增加数据,间隔为固定的128
             */
            if(afterId == 0) {
                int interval = 128;
                int len = objects.size();
                JSONObject object = objects.getJSONObject(0);
                Boolean up = object.getBoolean("isUpLine");
                int inputId = object.getInteger("inputId");
                List<EiTransponder> items = eiTransponderService.getByInputId(inputId, up);
                List<EiTransponderModel> targets = EiTransponderModel.exchangeData(items);
                //targets.stream().sorted(Comparator.comparing(EiTransponderModel::getNumber));
                Collections.sort(targets, (o1, o2) -> {//对数据按number的大小排序
                    if(o1.getNumber() > o2.getNumber()){
                        return 1;
                    }else if(o1.getNumber() == o2.getNumber()){
                        return 0;
                    }
                    return -1;
                });
                int beforeTargetNumber = targets.get(targets.size()-1).getNumber();//目标表格末尾number
                for(int i=0; i<len;i++) {
                    EiTransponder eiTransponder = new EiTransponder();

                    eiTransponder.setNumber(String.valueOf(beforeTargetNumber + (i + 1) * interval));
                    eiTransponder.setName(objects.getJSONObject(i).getString("name"));
                    eiTransponder.setMileage(objects.getJSONObject(i).getString("mileage"));
                    eiTransponder.setType(objects.getJSONObject(i).getString("type"));
                    eiTransponder.setPurpose(objects.getJSONObject(i).getString("purpose"));
                    eiTransponder.setStationName(objects.getJSONObject(i).getString("stationName"));
                    eiTransponder.setTranponderNumber(objects.getJSONObject(i).getString("transponderNumber"));
                    eiTransponder.setRemark(objects.getJSONObject(i).getString("remark"));
                    eiTransponder.setOperatingtime(new Date());
                    eiTransponder.setOperator(operator);
                    eiTransponder.setInputId(objects.getJSONObject(i).getInteger("inputId"));
                    eiTransponder.setIsUpLine(objects.getJSONObject(i).getBoolean("isUpLine"));
                    eiTransponder.setStatus((byte) 3);
                    eiTransponder.setLineId(objects.getJSONObject(i).getInteger("lineId"));

                    eiTransponderService.create(eiTransponder);
                }
                result = "增加成功";
            }
            /**
             * 在表格中间插入数据
             */
            else{
                int len = objects.size();//即时增加数据的数目
                int afterNumber = Integer.valueOf(eiTransponderService.getById(afterId).getNumber());
                /**
                 * 找出前一个128倍数和后一个128倍数；
                  */
                JSONObject object = null;
                object = objects.getJSONObject(0);
                Boolean up = object.getBoolean("isUpLine");
                int inputId = object.getInteger("inputId");
                List<EiTransponder> items = eiTransponderService.getByInputId(inputId, up);
                List<EiTransponderModel> targets = EiTransponderModel.exchangeData(items);
                /**
                 * 对数据按number大小排序
                 */
                Collections.sort(targets, (o1, o2) -> {
                    if (o1.getNumber() > o2.getNumber()) {
                        return 1;
                    } else if (o1.getNumber() == o2.getNumber()) {
                        return 0;
                    }
                    return -1;
                });

                int beforeRowNumber = 0;

                for (EiTransponderModel target : targets) {
                    if (Integer.valueOf(target.getNumber()) == afterNumber) {
                        // System.out.println(beforeRowOrderId);
                        break;
                    } else {
                        beforeRowNumber = Integer.valueOf(target.getNumber());
                        //  System.out.println(beforeRowOrderId);
                    }
                }
                System.out.println("beforeRowNumber:" + beforeRowNumber);
                /**
                 * 如果单次增加的条数超过内增加的间隙大小，则增加失败
                 */
                if ((afterNumber - beforeRowNumber) < len) {
                    result = "超出单次增加条数上限";
                } else {
                    List<EiTransponderModel> beforeList = new LinkedList<>();
                    List<EiTransponderModel> afterList = new LinkedList<>();
                    for (EiTransponderModel target : targets) {
                        if (target.getNumber() < afterNumber) {
                            if (target.getNumber() % 128 == 0) {
                                beforeList.add(target);
                            }
                        }
                    }
                    for (EiTransponderModel target : targets) {
                        if (target.getNumber() >= afterNumber) {
                            if (target.getNumber() % 128 == 0) {
                                afterList.add(target);
                            }
                        }
                    }
                    int beforeTargetNumber = beforeList.get(beforeList.size() - 1).getNumber();
                    int afterTargetNumber = afterList.get(0).getNumber();


                    //按间隔为1把即时数据增加进去
                    for (int i = 0; i < len; i++) {
                        EiTransponder eiTransponder = new EiTransponder();
                        int temporaryNumber = beforeRowNumber + i + 1;
                        eiTransponder.setNumber(String.valueOf(temporaryNumber));
                        System.out.println("temporaryNumber:"+eiTransponder.getNumber());
                        eiTransponder.setName(objects.getJSONObject(i).getString("name"));
                        eiTransponder.setMileage(objects.getJSONObject(i).getString("mileage"));
                        eiTransponder.setType(objects.getJSONObject(i).getString("type"));
                        eiTransponder.setPurpose(objects.getJSONObject(i).getString("purpose"));
                        eiTransponder.setStationName(objects.getJSONObject(i).getString("stationName"));
                        eiTransponder.setTranponderNumber(objects.getJSONObject(i).getString("transponderNumber"));
                        eiTransponder.setRemark(objects.getJSONObject(i).getString("remark"));
                        eiTransponder.setOperatingtime(new Date());
                        eiTransponder.setOperator(operator);
                        eiTransponder.setInputId(objects.getJSONObject(i).getInteger("inputId"));
                        eiTransponder.setIsUpLine(objects.getJSONObject(i).getBoolean("isUpLine"));
                        eiTransponder.setStatus((byte) 3);
                        eiTransponder.setLineId(objects.getJSONObject(i).getInteger("lineId"));

                        eiTransponderService.create(eiTransponder);
                    }
                    //TODO 重新分配间隔，并且维护好numberId相同的情况

                    List<EiTransponderModel> addedTargets = new LinkedList<>();
                    /**
                     * 已经更新了数据库，要再全部找出来处理
                     */
                    List<EiTransponder> operatedItems = eiTransponderService.getByInputId(inputId, up);
                    List<EiTransponderModel> operatedTargets = EiTransponderModel.exchangeData(operatedItems);
                    /**
                     * 对数据按number大小排序
                     */
                    Collections.sort(operatedTargets, (o1, o2) -> {
                        if (o1.getNumber() > o2.getNumber()) {
                            return 1;
                        } else if (o1.getNumber() == o2.getNumber()) {
                            return 0;
                        }
                        return -1;
                    });
                    for (EiTransponderModel operatedTarget : operatedTargets) {
                        if (Integer.valueOf(operatedTarget.getNumber()) > beforeTargetNumber && Integer.valueOf(operatedTarget.getNumber()) < afterTargetNumber) {
                            addedTargets.add(operatedTarget);//两个原始数据之间所有的数据(包括增加，增加修改-修改删除)
                        }
                    }
                    System.out.println("beforeTargetNumber="+beforeTargetNumber+"afterTargetNumber="+afterTargetNumber);
                    System.out.println("size="+addedTargets.size());
                    //TODO 找numberId不同的数据的个数

                    int count = 0;
                    int o1=0;
                    int o2=0;
                    for(int i = 0;i<addedTargets.size()-1;i++){
                        System.out.println("前："+addedTargets.get(i).getNumber()+"后："+addedTargets.get(i+1).getNumber());
                        o1=addedTargets.get(i).getNumber();
                        o2=addedTargets.get(i+1).getNumber();
                        if(o1 != o2){
                            count++;
                        }
                        System.out.println("temporaryCount:"+count);
                    }
                    count++;//中间数据numberId不同的个数
                    //TODO number以等间隔存入
                    System.out.println("count="+count);
                  //  int len1 = addedTargets.size();
                    int interval = (int) (128 / (count+1));//新的间隔
                    System.out.println("interval:"+interval);

                    //TODO 使numberId相同的数据修改间隔后任然相同
                    int flag=0;
                    for(int i=0; i<addedTargets.size();i++){
                        if(i!=0) {
                            /**
                             * 除了第一条数据，剩余的每一条数据都与上一条对比numberId,若相同则新的numberId也相同
                             */
                            o1=addedTargets.get(i).getNumber();
                            if (o1 != flag) {
                                flag=addedTargets.get(i).getNumber();
                                addedTargets.get(i).setNumber(addedTargets.get(i - 1).getNumber() + interval);
                                System.out.println("i:"+i+"real number:" + addedTargets.get(i).getNumber());
                                EiTransponder item = eiTransponderService.getById(addedTargets.get(i).getId());
                                item.setNumber(String.valueOf(addedTargets.get(i).getNumber()));
                                eiTransponderService.update(item);
                            } else {
                                flag=addedTargets.get(i).getNumber();
                                addedTargets.get(i).setNumber(addedTargets.get(i - 1).getNumber());
                                System.out.println("i:"+i+"real number:" + addedTargets.get(i).getNumber());
                                EiTransponder item = eiTransponderService.getById(addedTargets.get(i).getId());
                                item.setNumber(String.valueOf(addedTargets.get(i).getNumber()));
                                eiTransponderService.update(item);
                            }

                        }else{
                            /**
                             * 第一条数据单独处理
                             */
                            flag=addedTargets.get(0).getNumber();
                            addedTargets.get(0).setNumber(beforeTargetNumber + interval);
                            System.out.println("real number:" + addedTargets.get(0).getNumber());
                            EiTransponder item = eiTransponderService.getById(addedTargets.get(0).getId());
                            item.setNumber(String.valueOf(addedTargets.get(0).getNumber()));
                            eiTransponderService.update(item);

                        }
                    }
                    result = "增加成功";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            result = "增加失败";
        }
        return result;
    }

    /**
     * 非动态增加
     * @param afterId
     * @param operator
     * @param objects
     * @return
     */
    private String DBCreateRow(int afterId,String operator,JSONArray objects){
        String result=null;
        try{
            int len = objects.size();
            System.out.println("len:"+len);
            JSONObject object = null;
            object = objects.getJSONObject(0);
            Boolean up = object.getBoolean("isUpLine");
            int inputId = object.getInteger("inputId");
            System.out.println("inputId:"+inputId);
            System.out.println(up);
            int interval =0;
            List<EiTransponder> items = eiTransponderService.getByInputId(inputId, up);
            List<EiTransponderModel> targets = EiTransponderModel.exchangeData(items);
            //targets.stream().sorted(Comparator.comparing(EiTransponderModel::getNumber));
            Collections.sort(targets, (o1, o2) -> {//对数据按number的大小排序
                if(o1.getNumber() > o2.getNumber()){
                    return 1;
                }else if(o1.getNumber() == o2.getNumber()){
                    return 0;
                }
                return -1;
            });
            int beforeRowOrderId = 0;
            if(afterId !=0) {//确定间隔长度
                EiTransponder afterRow = eiTransponderService.getById(afterId);
                int afterRowOrderId = Integer.valueOf(afterRow.getNumber());


                for (EiTransponderModel target : targets) {
                    if (Integer.valueOf(target.getNumber()) == afterRowOrderId) {
                        // System.out.println(beforeRowOrderId);
                        interval =(int) ((afterRowOrderId - beforeRowOrderId) / len);
                        break;
                    } else {
                        beforeRowOrderId = Integer.valueOf(target.getNumber());
                        //  System.out.println(beforeRowOrderId);
                    }
                }
            }else{
                interval = 128;
                beforeRowOrderId= targets.get(targets.size()-1).getNumber();
            }
            System.out.println("interval:"+interval);
            for(int i=0; i<len;i++){
                EiTransponder eiTransponder = new EiTransponder();

                eiTransponder.setNumber(String.valueOf(beforeRowOrderId+(i+1)*interval));
                eiTransponder.setName(objects.getJSONObject(i).getString("name"));
                // eiTransponder.setNumber(object.getString("number"));
                //新增加的行不需要number参数
                eiTransponder.setMileage(objects.getJSONObject(i).getString("mileage"));
                eiTransponder.setType(objects.getJSONObject(i).getString("type"));
                eiTransponder.setPurpose(objects.getJSONObject(i).getString("purpose"));
                eiTransponder.setStationName(objects.getJSONObject(i).getString("stationName"));
                eiTransponder.setTranponderNumber(objects.getJSONObject(i).getString("transponderNumber"));
                eiTransponder.setRemark(objects.getJSONObject(i).getString("remark"));
                eiTransponder.setOperatingtime(new Date());
                eiTransponder.setOperator(operator);
                eiTransponder.setInputId(objects.getJSONObject(i).getInteger("inputId"));
                eiTransponder.setIsUpLine(objects.getJSONObject(i).getBoolean("isUpLine"));
                eiTransponder.setStatus((byte) 3);
                eiTransponder.setLineId(objects.getJSONObject(i).getInteger("lineId"));

                eiTransponderService.create(eiTransponder);
                result="增加成功";
            }
        }catch(Exception e){
            System.out.println(e);
            result="增加失败";
        }
        return result;
    }


}
