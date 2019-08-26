package com.swjt.fileManagement.services.services.base.impl;


import com.swjt.fileManagement.data.base.Department;
import com.swjt.fileManagement.db.mappers.base.DepartmentMapper;
import com.swjt.fileManagement.services.services.base.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Department create(Department obj) {
        return null;
    }

    @Override
    public Department delete(int id) {
        return null;
    }

    @Override
    public List<Department> bulkDelete(List<Integer> idList) {
        return null;
    }

    @Override
    public Department get(int id) {
        return null;
    }

    @Override
    public List<Department> getAll() {
        return null;
    }

    @Override
    public Department update(Department obj) {
        return null;
    }

    @Override
    public Department getByName(String name) {
        Example example= new Example(Department.class);
        example.createCriteria().andEqualTo("name",name);
        return departmentMapper.selectOneByExample(example);
    }
}
