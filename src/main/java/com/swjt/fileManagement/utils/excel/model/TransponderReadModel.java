package com.swjt.fileManagement.utils.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class TransponderReadModel extends BaseRowModel {
    @Getter
    @Setter
    @ExcelProperty(value = {"序号"})
    private String number;

    @Getter
    @Setter
    @ExcelProperty(value = {"应答器名称"})
    private String transponderName;


    @Getter
    @Setter
    @ExcelProperty(value = {"应答器编号"})
    private String transponderNumber;

    @Getter
    @Setter
    @ExcelProperty(value = {"里程"})
    private String mileage;

    @Getter
    @Setter
    @ExcelProperty(value = {"设备类型"})
    private String type;

    @Getter
    @Setter
    @ExcelProperty(value = {"用途"})
    private String purpose;

    @Getter
    @Setter
    @ExcelProperty(value = {"车站名称"})
    private String stationName;

    @Getter
    @Setter
    @ExcelProperty(value = {"备注"})
    private String remark;


    //表头格式定义
    public static Map<String, String> sheetHead() {
        Map<String, String> map = new HashMap<>();
        map.put("number", "序号");
        map.put("transponderName", "应答器名称");
        map.put("transponderNumber", "应答器编号");
        map.put("mileage", "里程");
        map.put("type", "设备类型");
        map.put("purpose", "用途");
        map.put("stationName", "车站");
        map.put("remark", "备注");
        return map;
    }

    @Override
    public String toString() {
        Map<String,String> oneList = new HashMap<>();
        oneList.put("\"number\"",'\"'+number+'\"');
        oneList.put("\"transponderName\"",'\"'+transponderName+'\"');
        oneList.put("\"transponderNumber\"",'\"'+transponderNumber+'\"');
        oneList.put("\"mileage\"",'\"'+mileage+'\"');
        oneList.put("\"type\"",'\"'+type+'\"');
        oneList.put("\"purpose\"",'\"'+purpose+'\"');
        oneList.put("\"stationName\"",'\"'+stationName+'\"');
        oneList.put("\"remark\"",'\"'+remark+'\"');
        return oneList.toString();
    }
}
