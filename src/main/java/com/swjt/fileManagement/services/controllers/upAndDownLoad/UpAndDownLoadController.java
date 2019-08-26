package com.swjt.fileManagement.services.controllers.upAndDownLoad;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.swjt.fileManagement.data.fileVersionManage;
import com.swjt.fileManagement.services.services.FileVersionManageService;
import com.swjt.fileManagement.services.services.base.LineService;
import com.swjt.fileManagement.services.services.base.ProjectService;
import com.swjt.fileManagement.services.services.common.HttpResponse;
import com.swjt.fileManagement.services.services.common.ResultUtil;
import com.swjt.fileManagement.services.services.common.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class UpAndDownLoadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpAndDownLoadController.class);

    @Value("${file.dest.path}")
    private String fileDestPath;

    @Autowired
    private FileVersionManageService fileVersionManageService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private LineService lineService;

    @Autowired
    Distribute distribute;

    @PostMapping("/upload")
    @ResponseBody
    public HttpResponse<Object> upload(HttpServletRequest request, @RequestParam("info") String info) {
        String username = JwtUtil.getUsername(request.getHeader("Authorization"));
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        for (int i = 0; i < files.size(); i++) {
            System.out.println("filename = " + files.get(i).getOriginalFilename());
            file = files.get(0);
            if (file.isEmpty()) {
                return ResultUtil.error("upload","上传失败，请选择文件",404);
            }
            JSONObject jsonObject = JSON.parseObject(info);
            String fileParentPath = fileDestPath;
            int type = jsonObject.getInteger("type");
            int fileType = jsonObject.getInteger("fileType");
            /*
             * 图：0
             *     6-CAD图
             * 基础类：1
             *   工务
             *     0-公务类基础数据表（word表）
             *   电务
             *     1-电务正线信号数据表
             * 工程类数据：2
             *   输入表：
             *     2-正线信号数据表
             *     3- 应答器位置表
             *     4-进路信息表
             *     5-道岔信息表
             *
             */
            String filePath = projectService.get(jsonObject.getInteger("pid")).getName() + "/" + FileTypeMap.TYPE[type] + jsonObject.getString("projectVersion") + "/" + lineService.get(jsonObject.getInteger("Lid")).getName() + "/" + file.getOriginalFilename();

            File dest = new File(fileParentPath + filePath);
            System.out.println("dest = " + dest);
            File fileParent = dest.getParentFile();
            //文件目录不存在
            if (!fileParent.exists()) {
                fileParent.mkdirs();
                System.out.println("创建目录" + dest.getPath());
            }
            try {
                //会覆盖相同的原文件
                file.transferTo(dest);
//                Distribute distribute = new Distribute();
                if(type==0){
                    //cad图上传 并存储
                    if(fileType!=6){
                        return ResultUtil.error("upload","文件类型绑定错误",400);
                    }
                }else {
                    try {
                        String errorString = distribute.checkExcelHead(dest.getPath(), fileType).toString();
                        if (!("ok" == errorString)) {
                            //todo 文件没有通过检查，是否将上传的文件删除
                            return ResultUtil.error("upload",errorString,400);
                        }
                    } catch (Exception e) {
                        return ResultUtil.error("upload","表头错误",400);
                    }
                }
                fileVersionManage fileVersionManage = new fileVersionManage();
                fileVersionManage.setProjectId(jsonObject.getInteger("pid"));
                fileVersionManage.setType( FileTypeMap.TYPE[type]);
                fileVersionManage.setLineId(jsonObject.getInteger("Lid"));
                fileVersionManage.setProjectVersion(jsonObject.getString("projectVersion"));
                fileVersionManage.setFileVersion(jsonObject.getString("fileVersion"));
                fileVersionManage.setFilePath(dest.getPath());
                fileVersionManage.setRemarks(jsonObject.getString("remarks"));
                fileVersionManage.setFileName(file.getOriginalFilename());
                fileVersionManage.setFileType(FileTypeMap.FILE_TYPE[fileType]);
                fileVersionManage.setBelongMan(username);
                System.out.println("fileVersionManage" + file.getOriginalFilename());
                fileVersionManageService.create(fileVersionManage);
                int fileid = fileVersionManage.getId();
                if(type==0){
                    return ResultUtil.success(files.get(i).getOriginalFilename()+" 上传成功");
                }
                //todo 多线程处理异步耗时方法 分发
                distribute.writeDB(dest.getPath(), fileid, fileType);
                return ResultUtil.success("上传成功");
            } catch (IOException e) {
                LOGGER.error(e.toString(), e);
            }
        }
        return ResultUtil.error("upload","上传失败！",400);
//        return "上传失败！";
    }

    //实现Spring Boot 的文件下载功能，映射网址为/download/{Fid}
    @GetMapping("/download/{id}")
    public OutputStream downloadFile(@PathVariable int id, HttpServletResponse response) throws UnsupportedEncodingException {

        String fileName = fileVersionManageService.get(id).getFilePath();
        String message;
        //下载的文件名
        System.out.println("D:/dev/TSC/test_data/ ==" + fileName);
        // 如果文件名不为空，则进行下载
        if (fileName != null) {
            File file = new File(fileName);

            // 如果文件名存在，则进行下载
            if (file.exists()) {

                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("Download the song successfully!");
                    message="下载完成";
                    return os;
                } catch (Exception e) {
                    message="下载失败";
                    System.out.println("Download the song failed!");
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else {
                message="文件资源不存在了";
            }
        }
        else {
            message="文件名为空";
        }
        return null;
    }

    @PostMapping("test")
    public String test(@RequestBody String info) {
        System.out.println("info= " + info);
        JSONObject jsonObject = JSON.parseObject(info);
        String fileParentPath = fileDestPath;
        String filePath = projectService.get(jsonObject.getInteger("pid")).getName() + "/" + jsonObject.getString("type") + jsonObject.getString("projectVersion") + "/" + lineService.get(jsonObject.getInteger("Lid")).getName() + "/";

        File dest = new File(fileParentPath + filePath);
        System.out.println("dest = " + dest);
        return dest.getName();
    }
}


