package com.swjt.fileManagement.data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tc_ei_transponder")
public class EiTransponder {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 应答器名称
     */
    private String name;

    /**
     * 序号
     */
    private String number;

    /**
     * 里程
     */
    private String mileage;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 车站名称
     */
    @Column(name = "station_name")
    private String stationName;

    /**
     * 1：上行线   0：下行线
     */
    @Column(name = "is_up_line")
    private Boolean isUpLine;

    /**
     * 数据操作状态（0：原始，1：删除，2：修改，3：增加，4：修改删除，5：增加删除）
     */
    private Byte status;

    /**
     * 输入数据表 关联id
     */
    @Column(name = "input_id")
    private Integer inputId;

    /**
     * 应答器编号
     */
    @Column(name = "tranponder_number")
    private String tranponderNumber;

    @Column(name = "line_id")
    private Integer lineId;

    /**
     * 操作者
     */
    @Column(name ="operator")
    private String operator;

    /**
     * 操作时间
     */
    @Column(name = "operatingTime")
    private Date operatingtime;

    /**
     * 备注
     */
    private String remark;



    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取应答器名称
     *
     * @return name - 应答器名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置应答器名称
     *
     * @param name 应答器名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取应答器编号
     *
     * @return number - 应答器编号
     */
    public String getNumber() {
        return number;
    }

    /**
     * 设置应答器编号
     *
     * @param number 应答器编号
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 获取里程
     *
     * @return mileage - 里程
     */
    public String getMileage() {
        return mileage;
    }

    /**
     * 设置里程
     *
     * @param mileage 里程
     */
    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    /**
     * 获取设备类型
     *
     * @return type - 设备类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置设备类型
     *
     * @param type 设备类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取用途
     *
     * @return purpose - 用途
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * 设置用途
     *
     * @param purpose 用途
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * 获取车站名称
     *
     * @return station_name - 车站名称
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * 设置车站名称
     *
     * @param stationName 车站名称
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    /**
     * 获取1：上行线   0：下行线
     *
     * @return is_up_line - 1：上行线   0：下行线
     */
    public Boolean getIsUpLine() {
        return isUpLine;
    }

    /**
     * 设置1：上行线   0：下行线
     *
     * @param isUpLine 1：上行线   0：下行线
     */
    public void setIsUpLine(Boolean isUpLine) {
        this.isUpLine = isUpLine;
    }

    /**
     * 获取数据操作状态（0：原始，1：删除，2：修改，3：增加，4：修改删除，5：增加删除）
     *
     * @return status - 数据操作状态（0：原始，1：删除，2：修改，3：增加，4：修改删除，5：增加删除）
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置数据操作状态（0：原始，1：删除，2：修改，3：增加，4：修改删除，5：增加删除）
     *
     * @param status 数据操作状态（0：原始，1：删除，2：修改，3：增加，4：修改删除，5：增加删除）
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取输入数据表 关联id
     *
     * @return input_id - 输入数据表 关联id
     */
    public Integer getInputId() {
        return inputId;
    }

    /**
     * 设置输入数据表 关联id
     *
     * @param inputId 输入数据表 关联id
     */
    public void setInputId(Integer inputId) {
        this.inputId = inputId;
    }

    /**
     * @return tranponder_number
     */
    public String getTranponderNumber() {
        return tranponderNumber;
    }

    /**
     * @param tranponderNumber
     */
    public void setTranponderNumber(String tranponderNumber) {
        this.tranponderNumber = tranponderNumber;
    }

    /**
     * @return line_id
     */
    public Integer getLineId() {
        return lineId;
    }

    /**
     * @param lineId
     */
    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    /**
     * 获取操作者
     *
     * @return operator - 操作者
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 设置操作者
     *
     * @param operator 操作者
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 获取操作时间
     *
     * @return operatingTime - 操作时间
     */
    public Date getOperatingtime() {
        return operatingtime;
    }

    /**
     * 设置操作时间
     *
     * @param operatingtime 操作时间
     */
    public void setOperatingtime(Date operatingtime) {
        this.operatingtime = operatingtime;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}