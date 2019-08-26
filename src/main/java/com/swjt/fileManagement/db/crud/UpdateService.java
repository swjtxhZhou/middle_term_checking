package com.swjt.fileManagement.db.crud;

/**
 * @author AgilePhotonics
 */
public interface UpdateService<T> {
    /**
     * 更新对象并返回更新后的对象
     *
     * @param obj 要更新的对象
     * @return obj
     */
    T update(T obj);
}
