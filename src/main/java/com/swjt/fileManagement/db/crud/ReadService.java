package com.swjt.fileManagement.db.crud;

import java.util.List;

/**
 * @author AgilePhotonics
 */
public interface ReadService<T> {
    /**
     * 根据id获取对象
     *
     * @param id 要获取的对象的id
     * @return 获取到的对象
     */
    T get(int id);

    /**
     * 获取所有对象
     *
     * @return 所有对象的列表
     */
    List<T> getAll();
}
