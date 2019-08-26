package com.swjt.fileManagement.services.services.common;

import lombok.Data;

/**
 * 这个类是API统一返回响应对象
 *
 * @author AgilePhotonics
 */
@Data
public class HttpResponse<T> {
    /**
     * 返回状态响应代码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 请求的URL
     */
    private String url;

    /**
     * 返回的数据
     */
    private T data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setData(T data) {
        this.data = data;
    }
}
