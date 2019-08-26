package com.swjt.fileManagement.services.controllers.upAndDownLoad;


import com.swjt.fileManagement.services.services.tc.ei.EiTransponderService;
import com.swjt.fileManagement.services.services.tc.ei.impl.TransponderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Distribute  {

    @Autowired
    private TransponderServiceImpl transponderServiceImpl;

    @Autowired
    private EiTransponderService eiTransponderService;

    /**
     * 不同excel文件的不同表头检查分发
     *
     * @param filePath
     * @param fileType
     * @return
     * @throws IOException
     */
    public Object checkExcelHead(String filePath, int fileType) throws IOException {
        switch (fileType) {

            case 3:
                //工程应答器位置表
                return TransponderServiceImpl.checkHead(filePath);

            default:
                return "no";
        }
    }

    /**
     * 不同表的线程写入分发
     *
     * @param filePath
     * @param fileid
     * @param fileType
     * @return
     * @throws IOException
     */
    public Object writeDB(String filePath, int fileid, int fileType) {
        switch (fileType) {

            case 3:
                //工程应答器数据表数据写入线程
                return transponderServiceImpl.writeDB(filePath, fileid,eiTransponderService);

            default:
                return null;
        }
    }
}
