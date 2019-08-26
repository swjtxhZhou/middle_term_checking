package com.swjt.fileManagement.services.services.tc.ei.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;

import com.swjt.fileManagement.data.EiTransponder;
import com.swjt.fileManagement.data.fileVersionManage;
import com.swjt.fileManagement.db.mappers.tc.ei.EiTransponderMapper;
import com.swjt.fileManagement.services.services.FileVersionManageService;
import com.swjt.fileManagement.services.services.tc.ei.EiTransponderService;
import com.swjt.fileManagement.utils.excel.listen.ExcelListener;
import com.swjt.fileManagement.utils.excel.model.TransponderReadModel;
import com.swjt.fileManagement.utils.excel.standardModel.SMFun;
import com.swjt.fileManagement.utils.excel.standardModel.TransponderModel;
import com.swjt.fileManagement.utils.thread.ExecutorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

import static com.swjt.fileManagement.utils.excel.standardModel.TransponderModel.getTransponderReadModel;

@Service
public class TransponderServiceImpl implements EiTransponderService {

    @Autowired
    private EiTransponderMapper eiTransponderMapper;
    @Autowired
    private FileVersionManageService fileVersionManageService;

    @Override
    public EiTransponder create(EiTransponder obj) {
        eiTransponderMapper.insert(obj);
        return obj;
    }
    @Override
    public EiTransponder update(EiTransponder obj)
    {
        eiTransponderMapper.updateByPrimaryKey(obj);
        return obj;
    }
    @Override
    public List<EiTransponder> getByStatus(byte status){
        Example example = new Example(EiTransponder.class);
        example.createCriteria().andEqualTo("status", status);
        return  eiTransponderMapper.selectByExample(example);
    }
    @Override
    public EiTransponder getById(int id){
        EiTransponder eiTransponder = new EiTransponder();
        eiTransponder = eiTransponderMapper.selectByPrimaryKey(id);
        return eiTransponder;
    }
    @Override
    public List<EiTransponder> getByInputId(int inputId,Boolean isUpLine){
        Example example = new Example(EiTransponder.class);
        example.createCriteria().andEqualTo("inputId", inputId).andEqualTo("isUpLine",isUpLine);
        //example.setOrderByClause("number asc");number是String格式不能直接排序，排序方法见TransponderViewController
        return eiTransponderMapper.selectByExample(example);
    }
    @Override
    public List<EiTransponder> getByNumber(String number){
        Example example = new Example(EiTransponder.class);
        example.createCriteria().andEqualTo("number", number);
        return eiTransponderMapper.selectByExample(example);
    }
    @Override
    public EiTransponder delete(int id) {
        return null;
    }

    @Override
    public List<EiTransponder> bulkDelete(List<Integer> idList) {
        return null;
    }

    @Override
    public EiTransponder get(int id) {
        return null;
    }

    @Override
    public List<EiTransponder> getAll() {
        return null;
    }



    //通过上下型线和线路编号来查询应答器位置表
    @Override
    public List<EiTransponder> getByUpAndLid(boolean up, int lineId) {
        Example example = new Example(EiTransponder.class);
        example.createCriteria().andEqualTo("isUpLine", up).andEqualTo("lineId", lineId);
        return eiTransponderMapper.selectByExample(example);
    }


    public static Map<String, Object> checkData(fileVersionManage fileVersionManage) throws IOException {
//        String filename = fileVersionManage.getFilePath();
        String filename = "D:\\dev\\TSC\\tsc-utils\\src\\main\\resources\\怀衡线怀化南至衡阳东站应答器位置表-V1.0.9.xls";
        InputStream inputStream = new BufferedInputStream(new FileInputStream(filename));
        ExcelListener excelListener = new ExcelListener();
        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream, excelListener);
        List<Sheet> sheets = excelReader.getSheets();
        Map<String, Object> map = new HashMap<>();
        //每个sheet可以调用不同的model
        for (Sheet sheet : sheets) {
            TransponderReadModel readModel = new TransponderReadModel();
            Map<String, TransponderReadModel> model3Map = new HashMap<>(getTransponderReadModel(filename, sheet, 2, readModel));
            sheet.setClazz(model3Map.get(sheet.getSheetName()).getClass());
//            sheet.setHeadLineMun(0);
            excelReader.read(sheet);
            model3Map.clear();
            Map a = excelListener.oneExcelDB();
            String one = a.get(sheet.getSheetName()).toString();
            String[] temp = one.substring(one.indexOf("[") + 1, one.length() - 1).replace("=", ":").replace(" ", "").replace("][", "Agile").replace("},", "}Agile").split("Agile");

            List<Object> list = Arrays.asList(temp);
            List<Object> list1 = new ArrayList<>(list);
            System.out.println("SheetName=   " + sheet.getSheetName());
            Map map1 = TransponderModel.checkData(list1,1);
            System.out.println("数据格式错误=: "+map1.get("StringWhy"));
            map.put(sheet.getSheetName(),map1);
        }
        inputStream.close();
        return map;
    }




    /**
     *
     * @param filename 文件的实际存放路径名
     * @return
     * @throws IOException
     */
    public static String checkHead(String filename)  throws IOException {
        String fileName = filename;
        InputStream inputStream = new BufferedInputStream(new FileInputStream(fileName));
        ExcelListener excelListener = new ExcelListener();
        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream, excelListener);
        List<Sheet> sheets = excelReader.getSheets();
        for(Sheet sheet:sheets){
            TransponderReadModel readModel = new TransponderReadModel();
            Map<String, TransponderReadModel> transponderReadModelMap = new HashMap<>(getTransponderReadModel(fileName,sheet,2,readModel));
            sheet.setClazz(transponderReadModelMap.get(sheet.getSheetName()).getClass());
            excelReader.read(sheet);
            transponderReadModelMap.clear();
            Map a = excelListener.oneExcelDB();
            String one = a.get(sheet.getSheetName()).toString();
            String[] temp = one.substring(one.indexOf("[") + 1, one.length() - 1).replace("=", ":").replace(" ", "").replace("][", "Agile").replace("},", "}Agile").split("Agile");
            List<Object> list = Arrays.asList(temp);
            List<Object> list1 = new ArrayList<>(list);
//            list1.remove(0);//去除split产生的第一个为空对象
            if(!TransponderModel.TranspondercheckHead(list1, 1).isEmpty()){
                System.out.println("表头格式不符合的列为: " + TransponderModel.TranspondercheckHead(list1, 1).get("error"));
                inputStream.close();
                return "false";
            }
            System.out.println(sheet.getSheetName() + "表头格式检查通过");
        }
        inputStream.close();
        return "ok";
    }

    /**
     * 应答器位置表数据线程写入数据库
     *
     * @param filePath
     * @param fileId
     * @return
     * @throws IOException
     */
    public String writeDB(String filePath, int fileId, EiTransponderService eiTransponderService)  {
        ExecutorUtil.UP_FILE_EXECUTOR.submit(new Runnable() {
            public void run() {
                Long now = System.currentTimeMillis();
                try {
                    String fileName = filePath;
                    System.out.println("fileId=" + fileId);
                    InputStream inputStream = new BufferedInputStream(new FileInputStream(fileName));
                    ExcelListener excelListener = new ExcelListener();
                    ExcelReader excelReader = EasyExcelFactory.getReader(inputStream, excelListener);
                    List<Sheet> sheets = excelReader.getSheets();
                    //每个sheet可以调用不同的model
                    for (Sheet sheet : sheets) {
                        TransponderReadModel readModel = new TransponderReadModel();
                        Map<String, TransponderReadModel> transponderReadModelMap = new HashMap<>(getTransponderReadModel(fileName, sheet, 2, readModel));
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
                        System.out.println("up= " + up + '\n');
                        int lineId = fileVersionManageService.get(fileId).getLineId();
                        for (Object c : list1) {
                            JSONObject object = (JSONObject) JSONObject.parse(c.toString());
                            if (!Pattern.matches("[0-9]{1,5}", object.getString("number"))) {
                                System.out.println("该行序号不是数字跳过：  " + object);
                                //todo 表头和表尾没有处理，没有写进数据库
                                continue;
                            }
                            if (object.getString("mileage").equals("null")) {
                                // 将实际里程为空的数据跳过，变相等于将
                                System.out.println("该行实际里程为空跳过：  " + object);
                                continue;
                            }
                            EiTransponder eiTransponder = new EiTransponder();
                        //    eiTransponder.setNumber(object.getString("number"));//序号
                            eiTransponder.setName(object.getString("transponderName"));//应答器名称
                            eiTransponder.setTranponderNumber(object.getString("transponderNumber"));//应答器编号
                            eiTransponder.setMileage(object.getString("mileage"));//里程
                            eiTransponder.setType(object.getString("type"));//应答器类型
                            eiTransponder.setPurpose(object.getString("purpose"));//应答器用途
                            eiTransponder.setStationName(object.getString("stationName"));//车站名称
                            eiTransponder.setRemark(object.getString("remark"));
                            eiTransponder.setIsUpLine(up);
                            eiTransponder.setInputId(fileId);//fileVersionManage中唯一确定该表位置的id
                            eiTransponder.setStatus((byte) 0);
                            eiTransponder.setLineId(lineId);
                            eiTransponder.setOperatingtime(new Date());
                            try {
                                int number = Integer.parseInt(object.getString("number"));
                                eiTransponder.setNumber(String.valueOf(number*128));
                            }catch (NumberFormatException e){
                                System.out.println(e);
                                e.printStackTrace();
                            }
                            eiTransponderService.create(eiTransponder);
                        }

                    }
                    inputStream.close();
                } catch (
                        Exception e) {
                    System.out.println(e);
                  //  return "write false";
                }

                Long win = System.currentTimeMillis();
                Long a = win - now;
                System.out.println("应答器写数据用时：= " + a);


            }
        });
        return "write ok";
    }

    @Override
    public List<EiTransponder> getByInputId(int fid) {
        Example example = new Example(EiTransponder.class);
        example.createCriteria().andEqualTo("inputId",fid);
        return eiTransponderMapper.selectByExample(example);
    }

}
