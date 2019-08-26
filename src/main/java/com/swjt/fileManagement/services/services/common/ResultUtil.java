package com.swjt.fileManagement.services.services.common;

/**
 *
 * @author YangXiong
 */
public class ResultUtil {
    /**
     *
     * @param data
     * @param url
     * @param msg
     * @param code
     * @param <T> 
     * @return
     */
    public static <T> HttpResponse<T> response(T data, String url, String msg, Integer code) {
        HttpResponse<T> response = new HttpResponse<>();
        response.setCode(code);
        response.setMessage(msg);
        response.setUrl(url);
        response.setData(data);
        return response;
    }

    public static <T> HttpResponse<T> success(T data, String url, String msg) {
        return response(data, url, msg, 200);
    }

    public static <T> HttpResponse<T> success(T data, String url) {
        return response(data, url, "", 200);
    }

    public static <T> HttpResponse<T> success(T data) {
        return response(data, "", "", 200);
    }

    public static <T> HttpResponse<T> error(String url, String msg, Integer code) {
        return response(null, url, msg, code);
    }
}
