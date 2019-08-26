package com.swjt.fileManagement.services.services.base.impl;


import com.swjt.fileManagement.data.base.Line;
import com.swjt.fileManagement.db.mappers.base.LineMapper;
import com.swjt.fileManagement.services.services.base.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class LineServiceImpl implements LineService {

    @Autowired
    private LineMapper lineMapper;

    @Override
    public Line create(Line obj) {
//        obj.setCreatetime(new Date());
        lineMapper.insert(obj);
        return obj;
    }

    @Override
    public Line delete(int id) {
        return null;
    }

    @Override
    public List<Line> bulkDelete(List<Integer> idList) {
        return null;
    }

    @Override
    public Line get(int id) {
        return lineMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Line> getAll() {
        return lineMapper.selectAll();
    }

    @Override
    public Line update(Line obj) {
        lineMapper.updateByPrimaryKeySelective(obj);
        return lineMapper.selectByPrimaryKey(obj.getId());
    }

    @Override
    public List<Line> getByPId(int PId) {
        Example example = new Example(Line.class);
        example.createCriteria().andEqualTo("projectId",PId);
        return lineMapper.selectByExample(example);
    }
}
