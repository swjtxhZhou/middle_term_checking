package com.swjt.fileManagement.services.services.common.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author AgilePhotonics
 */
@AllArgsConstructor
public enum Errors {
    /**
     * 权限和数据问题
     */
    UNAUTHORIZED(401, "身份认证失败"),
    FORBIDDEN(403, "没有操作权限"),
    NOT_FOUND(404, "找不到该资源"),

    /**
     * 错误的请求
     */
    BAD_REQUEST(400, "请求有错误"),
    INTERNAL_SERVER_ERROR(500, "内部错误"),

    /**
     * token异常
     */
    TOKEN_EXPIRED(700, "token过期"),
    TOKEN_ERROR(700, "token验证失败");

    @Getter
    @Setter
    private Integer code;

    @Getter
    @Setter
    private String message;
}
