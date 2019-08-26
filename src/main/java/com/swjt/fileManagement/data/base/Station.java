package com.swjt.fileManagement.data.base;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "base_station")
public class Station {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 车站名
     */
    private String name;

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
     * 获取车站名
     *
     * @return name - 车站名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置车站名
     *
     * @param name 车站名
     */
    public void setName(String name) {
        this.name = name;
    }
}