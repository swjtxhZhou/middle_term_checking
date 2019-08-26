package com.swjt.fileManagement.services.services;


import com.swjt.fileManagement.data.fileVersionManage;
import com.swjt.fileManagement.db.mappers.fileVersionManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


import java.util.Date;
import java.util.List;

@Service
public class FileVersionManageServiceImpl implements FileVersionManageService {

    @Autowired
    com.swjt.fileManagement.db.mappers.fileVersionManageMapper fileVersionManageMapper;

    @Override
    public fileVersionManage create(fileVersionManage obj) {
        obj.setCreateTime(new Date());
        fileVersionManageMapper.insert(obj);
        return obj;
    }

    @Override
    public fileVersionManage delete(int id) {
        return null;
    }

    @Override
    public List<fileVersionManage> bulkDelete(List<Integer> idList) {
        return null;
    }

    @Override
    public fileVersionManage get(int id) {
        return fileVersionManageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<fileVersionManage> getAll() {
        return null;
    }

    @Override
    public fileVersionManage update(fileVersionManage obj) {
        return null;
    }


    @Override
    public List<fileVersionManage> getBypid(int pid) {
        Example example = new Example(fileVersionManage.class);
        example.createCriteria().andEqualTo("projectId",pid);
        return fileVersionManageMapper.selectByExample(example);
    }

    @Override
    public List<fileVersionManage> getByLid(int LId) {
        Example example = new Example(fileVersionManage.class);
        example.createCriteria().andEqualTo("lineId",LId);
        return fileVersionManageMapper.selectByExample(example);
    }

    @Override
    public List<fileVersionManage> getByLidAndType(int Lid, String type) {
        Example example = new Example(fileVersionManage.class);
        example.createCriteria().andEqualTo("lineId",Lid).andEqualTo("type",type);
        return fileVersionManageMapper.selectByExample(example);
    }

    @Override
    public List<fileVersionManage> getByPidAndType(int Pid, String type) {
        Example example = new Example(fileVersionManage.class);
        example.createCriteria().andEqualTo("projectId",Pid).andEqualTo("type",type);
        return fileVersionManageMapper.selectByExample(example);
    }

    @Override
    public List<fileVersionManage> getByPidAndTypeAndLineMin(int Pid, String type) {
        Example example = new Example(fileVersionManage.class);
        example.createCriteria().andEqualTo("projectId",Pid).andEqualTo("type",type).andEqualTo("lineId",0);
        return fileVersionManageMapper.selectByExample(example);
    }

    @Override
    public List<fileVersionManage> getByPidAndfileType(int pid, String fileType) {
        Example example = new Example(fileVersionManage.class);
        example.createCriteria().andEqualTo("projectId",pid).andEqualTo("fileType",fileType);
        return fileVersionManageMapper.selectByExample(example);
    }

    @Override
    public List<fileVersionManage> getByPidAndLidAndPVersionAndFVersionAndType(int Pid,int Lid,String PVersion,String FVersion,String Type){
        Example example = new Example(fileVersionManage.class);
        example.createCriteria().andEqualTo("projectId",Pid).andEqualTo("lineId",Lid).andEqualTo("projectVersion",PVersion).andEqualTo("fileVersion",FVersion).andEqualTo("type",Type);
        return fileVersionManageMapper.selectByExample(example);
    }
}
