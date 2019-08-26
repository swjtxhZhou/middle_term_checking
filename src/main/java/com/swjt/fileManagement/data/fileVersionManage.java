package com.swjt.fileManagement.data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "file_version_manage")
public class fileVersionManage {
    /**
     * 主键id 版本管理
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 数据类型，基础类、工程类、图
     */
    private String type;

    /**
     * 项目Id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 线路Id
     */
    @Column(name = "Line_id")
    private Integer lineId;

    /**
     * 项目（基础类、工程类、图）的版本
     */
    @Column(name = "project_version")
    private String projectVersion;

    /**
     * 文件（excel）的版本
     */
    @Column(name = "file_version")
    private String fileVersion;

    /**
     * 文件名
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 文件存放的位置，想的是根据项目和项目的数据类型的不同版本创建不同目录，存放
     */
    @Column(name = "file_path")
    private String filePath;

    /**
     * 该文件由谁上传
     */
    @Column(name = "belong_man")
    private String belongMan;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 文件类型：XX表，
     */
    @Column(name = "file_type")
    private String fileType;

    /**
     * 1文件被占用，0文件没有被占用
     */
    private Boolean status;

    /**
     * 上传时该文件的说明/备注
     */
    private String remarks;


    /**
     * 获取主键id 版本管理
     *
     * @return id - 主键id 版本管理
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id 版本管理
     *
     * @param id 主键id 版本管理
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取数据类型，基础类、工程类、图
     *
     * @return type - 数据类型，基础类、工程类、图
     */
    public String getType() {
        return type;
    }

    /**
     * 设置数据类型，基础类、工程类、图
     *
     * @param type 数据类型，基础类、工程类、图
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取项目Id
     *
     * @return project_id - 项目Id
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置项目Id
     *
     * @param projectId 项目Id
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取线路Id
     *
     * @return Line_id - 线路Id
     */
    public Integer getLineId() {
        return lineId;
    }

    /**
     * 设置线路Id
     *
     * @param lineId 线路Id
     */
    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    /**
     * 获取项目（基础类、工程类、图）的版本
     *
     * @return project_version - 项目（基础类、工程类、图）的版本
     */
    public String getProjectVersion() {
        return projectVersion;
    }

    /**
     * 设置项目（基础类、工程类、图）的版本
     *
     * @param projectVersion 项目（基础类、工程类、图）的版本
     */
    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    /**
     * 获取文件（excel）的版本
     *
     * @return file_version - 文件（excel）的版本
     */
    public String getFileVersion() {
        return fileVersion;
    }

    /**
     * 设置文件（excel）的版本
     *
     * @param fileVersion 文件（excel）的版本
     */
    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    /**
     * 获取文件名
     *
     * @return file_name - 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名
     *
     * @param fileName 文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取文件存放的位置，想的是根据项目和项目的数据类型的不同版本创建不同目录，存放
     *
     * @return file_path - 文件存放的位置，想的是根据项目和项目的数据类型的不同版本创建不同目录，存放
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置文件存放的位置，想的是根据项目和项目的数据类型的不同版本创建不同目录，存放
     *
     * @param filePath 文件存放的位置，想的是根据项目和项目的数据类型的不同版本创建不同目录，存放
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 获取该文件由谁上传
     *
     * @return belong_man - 该文件由谁上传
     */
    public String getBelongMan() {
        return belongMan;
    }

    /**
     * 设置该文件由谁上传
     *
     * @param belongMan 该文件由谁上传
     */
    public void setBelongMan(String belongMan) {
        this.belongMan = belongMan;
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
     * 获取文件类型：XX表，
     *
     * @return file_type - 文件类型：XX表，
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * 设置文件类型：XX表，
     *
     * @param fileType 文件类型：XX表，
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * 获取1文件被占用，0文件没有被占用
     *
     * @return status - 1文件被占用，0文件没有被占用
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置1文件被占用，0文件没有被占用
     *
     * @param status 1文件被占用，0文件没有被占用
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取上传时该文件的说明/备注
     *
     * @return remarks - 上传时该文件的说明/备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置上传时该文件的说明/备注
     *
     * @param remarks 上传时该文件的说明/备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}