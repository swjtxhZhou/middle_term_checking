package com.swjt.fileManagement.data.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "base_line")
public class Line {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 线路名称
     */
    private String name;

    /**
     * 分属站段
     */
    private String segment;

    /**
     * 线路类型：既有线、新建线
     */
    private String type;

    /**
     * 线路等级：CTCS2、CTCS3
     */
    private String level;

    /**
     * 线路描述
     */
    private String detail;

    /**
     * 是否为主线：1.是 0.不是
     */
    @Column(name = "is_main_line")
    private Boolean isMainLine;

    /**
     * 所属项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

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
     * 获取线路名称
     *
     * @return name - 线路名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置线路名称
     *
     * @param name 线路名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取分属站段
     *
     * @return segment - 分属站段
     */
    public String getSegment() {
        return segment;
    }

    /**
     * 设置分属站段
     *
     * @param segment 分属站段
     */
    public void setSegment(String segment) {
        this.segment = segment;
    }

    /**
     * 获取线路类型：既有线、新建线
     *
     * @return type - 线路类型：既有线、新建线
     */
    public String getType() {
        return type;
    }

    /**
     * 设置线路类型：既有线、新建线
     *
     * @param type 线路类型：既有线、新建线
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取线路等级：CTCS2、CTCS3
     *
     * @return level - 线路等级：CTCS2、CTCS3
     */
    public String getLevel() {
        return level;
    }

    /**
     * 设置线路等级：CTCS2、CTCS3
     *
     * @param level 线路等级：CTCS2、CTCS3
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * 获取线路描述
     *
     * @return detail - 线路描述
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置线路描述
     *
     * @param detail 线路描述
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * 获取是否为主线：1.是 0.不是
     *
     * @return is_main_line - 是否为主线：1.是 0.不是
     */
    public Boolean getIsMainLine() {
        return isMainLine;
    }

    /**
     * 设置是否为主线：1.是 0.不是
     *
     * @param isMainLine 是否为主线：1.是 0.不是
     */
    public void setIsMainLine(Boolean isMainLine) {
        this.isMainLine = isMainLine;
    }

    /**
     * 获取所属项目id
     *
     * @return project_id - 所属项目id
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置所属项目id
     *
     * @param projectId 所属项目id
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}