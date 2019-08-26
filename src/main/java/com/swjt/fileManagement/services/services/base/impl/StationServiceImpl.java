package com.swjt.fileManagement.services.services.base.impl;


import com.swjt.fileManagement.data.base.Station;
import com.swjt.fileManagement.db.mappers.base.StationMapper;
import com.swjt.fileManagement.services.services.base.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper baseSheetsMapper;

    @Override
    public Station create(Station obj) {
//        obj.setCreatetime(new Date());
        baseSheetsMapper.insert(obj);
        return obj;
    }

    @Override
    public Station delete(int id) {
        return null;
    }

    @Override
    public List<Station> bulkDelete(List<Integer> idList) {
        return null;
    }

    @Override
    public Station get(int id) {
        return baseSheetsMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Station> getAll() {
        return baseSheetsMapper.selectAll();
    }

    @Override
    public Station update(Station obj) {
        baseSheetsMapper.updateByPrimaryKeySelective(obj);
        return baseSheetsMapper.selectByPrimaryKey(obj.getId());
    }
}
