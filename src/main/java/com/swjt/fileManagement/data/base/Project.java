package com.swjt.fileManagement.data.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "base_project")
public class Project {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建单位（用户所属部门）
     */
    @Column(name = "creator_entity")
    private String creatorEntity;

    /**
     * 设计单位
     */
    @Column(name = "design_entity")
    private String designEntity;

    /**
     * 施工单位
     */
    @Column(name = "`construction entity`")
    private String constructionEntity;

    /**
     * 设备厂商
     */
    @Column(name = "equip_supplier")
    private String equipSupplier;

    /**
     * 项目状态（在建、已完成）
     */
    private String status;

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
     * 获取项目名称
     *
     * @return name - 项目名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置项目名称
     *
     * @param name 项目名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建单位（用户所属部门）
     *
     * @return creator_entity - 创建单位（用户所属部门）
     */
    public String getCreatorEntity() {
        return creatorEntity;
    }

    /**
     * 设置创建单位（用户所属部门）
     *
     * @param creatorEntity 创建单位（用户所属部门）
     */
    public void setCreatorEntity(String creatorEntity) {
        this.creatorEntity = creatorEntity;
    }

    /**
     * 获取设计单位
     *
     * @return design_entity - 设计单位
     */
    public String getDesignEntity() {
        return designEntity;
    }

    /**
     * 设置设计单位
     *
     * @param designEntity 设计单位
     */
    public void setDesignEntity(String designEntity) {
        this.designEntity = designEntity;
    }

    /**
     * 获取施工单位
     *
     * @return construction entity - 施工单位
     */
    public String getConstructionEntity() {
        return constructionEntity;
    }

    /**
     * 设置施工单位
     *
     * @param constructionEntity 施工单位
     */
    public void setConstructionEntity(String constructionEntity) {
        this.constructionEntity = constructionEntity;
    }

    /**
     * 获取设备厂商
     *
     * @return equip_supplier - 设备厂商
     */
    public String getEquipSupplier() {
        return equipSupplier;
    }

    /**
     * 设置设备厂商
     *
     * @param equipSupplier 设备厂商
     */
    public void setEquipSupplier(String equipSupplier) {
        this.equipSupplier = equipSupplier;
    }

    /**
     * 获取项目状态（在建、已完成）
     *
     * @return status - 项目状态（在建、已完成）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置项目状态（在建、已完成）
     *
     * @param status 项目状态（在建、已完成）
     */
    public void setStatus(String status) {
        this.status = status;
    }
}