package com.swjt.fileManagement.services.services.base;



import com.swjt.fileManagement.data.base.Line;
import com.swjt.fileManagement.db.crud.CrudService;

import java.util.List;

public interface LineService extends CrudService<Line> {
    List<Line> getByPId(int PId);
}
