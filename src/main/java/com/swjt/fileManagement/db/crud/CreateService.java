package com.swjt.fileManagement.db.crud;

/**
 * @author AgilePhotonics
 */
public interface CreateService<T> {
    /**
     * 创建对象并返回创建后的对象
     *
     * @param obj 要创建的对象
     * @return obj
     */
    T create(T obj);
}
