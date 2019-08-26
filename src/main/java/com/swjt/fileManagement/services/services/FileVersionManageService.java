package com.swjt.fileManagement.services.services;



import com.swjt.fileManagement.data.fileVersionManage;
import com.swjt.fileManagement.db.crud.CrudService;

import java.util.List;

public interface FileVersionManageService extends CrudService<fileVersionManage> {


    List<fileVersionManage> getBypid(int pid);
    List<fileVersionManage> getByLid(int LId);



    List<fileVersionManage> getByLidAndType(int Lid, String type);
    List<fileVersionManage> getByPidAndType(int Pid, String type);
    List<fileVersionManage> getByPidAndTypeAndLineMin(int Pid, String type);
    List<fileVersionManage> getByPidAndfileType(int pid, String fileType);
    List<fileVersionManage> getByPidAndLidAndPVersionAndFVersionAndType(int Pid, int Lid, String PVersion, String Fversion, String Type);

}
