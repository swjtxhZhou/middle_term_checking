package com.swjt.fileManagement.db.crud;

import java.util.List;

/**
 * @author AgilePhotonics
 */
public interface DeleteService<T> {
    /**
     * 通过对象id删除对象并返回被删除的对象
     *
     * @param id 要被删除对象的id
     * @return 被删除的对象
     */
    T delete(int id);

    /**
     * 删除所有{@code id}在{@code idList}中的对象
     *
     * @param idList 待删除对象的id列表
     * @return 被删除的对象列表
     */
    List<T> bulkDelete(List<Integer> idList);
}
