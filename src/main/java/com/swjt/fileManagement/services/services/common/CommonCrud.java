package com.swjt.fileManagement.services.services.common;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swjt.fileManagement.db.crud.CrudService;
import com.swjt.fileManagement.services.services.common.errors.Errors;
import com.swjt.fileManagement.services.services.common.errors.MyException;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommonCrud {

    public static <T, S extends CrudService<T>> HttpResponse<T> create(S s, T t) {
        return ResultUtil.success(s.create(t));
    }

    public static <T, S extends CrudService<T>> HttpResponse<T> update(S s, T t) {
        return ResultUtil.success(s.update(t));
    }

    public static <T, S extends CrudService<T>> HttpResponse<T> delete(S s, String id) {
        return ResultUtil.success(s.delete(Integer.valueOf(id)));
    }

    public static <T, S extends CrudService<T>> HttpResponse<List<T>> bulkDelete(S s, List<String> idList) {
        List<Integer> list = idList.stream().map(Integer::valueOf).collect(Collectors.toList());
        return ResultUtil.success(s.bulkDelete(list));
    }

    public static <T, S extends CrudService<T>> HttpResponse<T> get(S s, String id) throws Exception {
        T t = s.get(Integer.valueOf(id));
        if (t == null) {
            throw new MyException(Errors.NOT_FOUND);
        }
        return ResultUtil.success(t);
    }

    public static <T, S extends CrudService<T>> HttpResponse<JSONObject> getList(S s, HttpServletRequest request) {
        String pageNum = request.getParameter("pageNum");
        JSONObject response = new JSONObject();
        if (pageNum != null) {
            int pageSize = Integer.parseInt(Optional.ofNullable(request.getParameter("pageSize")).orElse("10"));
            PageHelper.startPage(Integer.parseInt(pageNum), pageSize);
            PageInfo<T> pageInfo = new PageInfo<>(s.getAll());
            response.put("tableData", pageInfo.getList());
            response.put("pageNum", pageInfo.getPageNum());
            response.put("pageSize", pageInfo.getPageSize());
            response.put("pageCount", pageInfo.getPages());
            response.put("total", pageInfo.getTotal());
        } else {
            List<T> list = s.getAll();
            response.put("tableData", list);
            response.put("total", list.size());
        }

        return ResultUtil.success(response);
    }
}
