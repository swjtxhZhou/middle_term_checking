package com.swjt.fileManagement.services.services.common.errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyException extends Exception {
    private Integer code;
    private String message;

    public MyException(Errors errors) {
        this.code = errors.getCode();
        this.message = errors.getMessage();
    }
}
