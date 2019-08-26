package com.swjt.fileManagement.services.services.tc.ei;



import com.swjt.fileManagement.data.EiTransponder;
import com.swjt.fileManagement.db.crud.CrudService;

import java.util.List;

public interface EiTransponderService extends CrudService<EiTransponder> {
    List<EiTransponder> getByInputId(int fid);
    List<EiTransponder> getByUpAndLid(boolean up, int lineId);
    List<EiTransponder> getByInputId(int inputId, Boolean isUpLine);

    List<EiTransponder> getByNumber(String number);
    EiTransponder getById(int id);
    List<EiTransponder> getByStatus(byte status);
}
