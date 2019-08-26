package com.swjt.fileManagement.services.services.base.impl;


import com.swjt.fileManagement.data.base.Project;
import com.swjt.fileManagement.db.mappers.base.ProjectMapper;
import com.swjt.fileManagement.services.services.base.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    public ProjectMapper projectMapper;

    @Override
    public Project create(Project obj) {
//        obj.setCreatetime(new Date());
        projectMapper.insert(obj);
        return obj;
    }

    @Override
    public Project delete(int id) {
        return null;
    }

    @Override
    public List<Project> bulkDelete(List<Integer> idList) {
        return null;
    }

    @Override
    public Project get(int id) {
        return projectMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Project> getAll() {
        return projectMapper.selectAll();
    }

    @Override
    public Project update(Project obj) {
        projectMapper.updateByPrimaryKeySelective(obj);
        return projectMapper.selectByPrimaryKey(obj.getId());
    }
}
