package com.swjt.fileManagement.services.services.base;


import com.swjt.fileManagement.data.base.Department;
import com.swjt.fileManagement.db.crud.CrudService;

/**
 * @author Administrator
 */
public interface DepartmentService extends CrudService<Department> {
    Department getByName(String name);
}
