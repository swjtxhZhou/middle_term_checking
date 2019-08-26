package com.swjt.fileManagement.services.services.model;


import com.swjt.fileManagement.data.EiTransponder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
public class EiTransponderModel {
    private int id;
    private String name;
    private int number;
    private String mileage;
    private String type;
    private String purpose;
    private String stationName;
    private String remark;
    private Boolean isUpLine;
    private Byte status;
    private int inputId;
    private String transponderNumber;
    private int lineId;
    private String operator;
    private Date operatingTime;

    public static List<EiTransponderModel> exchangeData(List<EiTransponder> lists){
        List<EiTransponderModel> exchangeList = new ArrayList<>();
        for(EiTransponder list:lists){
            EiTransponderModel eiTransponderModel = new EiTransponderModel();
            eiTransponderModel.setId(list.getId());
            eiTransponderModel.setInputId(list.getInputId());
            eiTransponderModel.setIsUpLine(list.getIsUpLine());
            eiTransponderModel.setLineId(list.getLineId());
            eiTransponderModel.setMileage(list.getMileage());
            eiTransponderModel.setName(list.getName());
            eiTransponderModel.setNumber(Integer.valueOf(list.getNumber()));
            eiTransponderModel.setOperatingTime(list.getOperatingtime());
            eiTransponderModel.setOperator(list.getOperator());
            eiTransponderModel.setPurpose(list.getPurpose());
            eiTransponderModel.setRemark(list.getRemark());
            eiTransponderModel.setStationName(list.getStationName());
            eiTransponderModel.setStatus(list.getStatus());
            eiTransponderModel.setTransponderNumber(list.getTranponderNumber());
            eiTransponderModel.setType(list.getType());
            exchangeList.add(eiTransponderModel);

        }
        return exchangeList;
    }
}
