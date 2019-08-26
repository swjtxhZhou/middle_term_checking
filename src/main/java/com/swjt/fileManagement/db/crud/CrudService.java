package com.swjt.fileManagement.db.crud;

/**
 * @author AgilePhotonics
 */
public interface CrudService<T>
        extends CreateService<T>, ReadService<T>, UpdateService<T>, DeleteService<T> {
}
