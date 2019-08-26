package com.swjt.fileManagement.utils.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.HashMap;
import java.util.Map;

public class ReadModel3 extends BaseRowModel {
    @ExcelProperty(value = {"序号"})
    private String number;

    @ExcelProperty(value = {"车站名"})
    private String stationName;

    @ExcelProperty(value = {"信号点名称"})
    private String signalPointName;

    @ExcelProperty(value = {"实际里程"})
    private String actualMileage;

    @ExcelProperty(value = {"信号点类型"})
    private String signalPointType;

    @ExcelProperty(value = {"绝缘节类型"})
    private String insulationSectionType;

    @ExcelProperty(value = {"轨道区段名称"})
    private String trackSectionName;

    @ExcelProperty(value = {"编码制式"})
    private String codingSystem;

    @ExcelProperty(value = { "载频"})
    private String trackSectionFrequency;

    @ExcelProperty(value = {"实际长度"})
    private String actualLength;

    @ExcelProperty(value = {"区段属性"})
    private String sectionProperties;

    @ExcelProperty(value = {"备注"})
    private String remarks;

    //表头格式定义
    public static Map<String, String> sheetHead() {
        Map<String, String> map = new HashMap<>();
        map.put("number","序号");
        map.put("stationName","车站名");
        map.put("signalPointName","信号点+名称+");
        map.put("actualMileage","null+实际里程+");
        map.put("signalPointType","null+信号点类型+");
        map.put("insulationSectionType","null+绝缘节类型+");
        map.put("trackSectionName","轨道区段+名称+");
        map.put("codingSystem","null+编码制式+");
        map.put("trackSectionFrequency","null+载频+");
        map.put("actualLength","null+实际长度+");
        map.put("sectionProperties","null+区段属性+");
        map.put("remarks","备注+null+");
        return map;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getSignalPointName() {
        return signalPointName;
    }

    public void setSignalPointName(String signalPointName) {
        this.signalPointName = signalPointName;
    }

    public String getActualMileage() {
        return actualMileage;
    }

    public void setActualMileage(String actualMileage) {
        this.actualMileage = actualMileage;
    }

    public String getSignalPointType() {
        return signalPointType;
    }

    public void setSignalPointType(String signalPointType) {
        this.signalPointType = signalPointType;
    }

    public String getInsulationSectionType() {
        return insulationSectionType;
    }

    public void setInsulationSectionType(String insulationSectionType) {
        this.insulationSectionType = insulationSectionType;
    }

    public String getTrackSectionName() {
        return trackSectionName;
    }

    public void setTrackSectionName(String trackSectionName) {
        this.trackSectionName = trackSectionName;
    }

    public String getCodingSystem() {
        return codingSystem;
    }

    public void setCodingSystem(String codingSystem) {
        this.codingSystem = codingSystem;
    }

    public String getTrackSectionFrequency() {
        return trackSectionFrequency;
    }

    public void setTrackSectionFrequency(String trackSectionFrequency) {
        this.trackSectionFrequency = trackSectionFrequency;
    }

    public String getActualLength() {
        return actualLength;
    }

    public void setActualLength(String actualLength) {
        this.actualLength = actualLength;
    }

    public String getSectionProperties() {
        return sectionProperties;
    }

    public void setSectionProperties(String sectionProperties) {
        this.sectionProperties = sectionProperties;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @Override
    public String toString() {
        return "JavaModel{" +
                "\"number\":" + '\"' + number + '\"' +
                ", \"stationName\":" + '\"' + stationName + '\"' +
                ", \"signalPointName\":" + '\"' + signalPointName + '\"' +
                ", \"actualMileage\":" + '\"' + actualMileage + '\"' +
                ", \"signalPointType\":" + '\"' + signalPointType + '\"' +
                ", \"insulationSectionType\":" + '\"' + insulationSectionType + '\"' +
                ", \"trackSectionName\":" + '\"' + trackSectionName + '\"' +
                ", \"codingSystem\":" + '\"' + codingSystem + '\"' +
                ", \"trackSectionFrequency\":" + '\"' + trackSectionFrequency + '\"' +
                ", \"actualLength\":" + '\"' + actualLength + '\"' +
                ", \"sectionProperties\":" + '\"' + sectionProperties + '\"' +
                ", \"remarks\":" + '\"' + remarks + '\"' +
                '}';
    }
}

