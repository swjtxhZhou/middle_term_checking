package com.swjt.fileManagement.services.services.common.errors;



import com.swjt.fileManagement.services.services.common.HttpResponse;
import com.swjt.fileManagement.services.services.common.ResultUtil;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

public class GlobalExceptionHandler {
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public HttpResponse<Object> myExceptionHandler(HttpServletRequest req, MyException e) {
        return ResultUtil.error(req.getRequestURI(), e.getMessage(), e.getCode());
    }

    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public HttpResponse<Object> shiroExceptionHandler(HttpServletRequest req, ShiroException e) {
        return ResultUtil.error(req.getRequestURI(), e.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public HttpResponse<Object> unauthorizedExceptionHandler(HttpServletRequest req, UnauthorizedException e) {
        return ResultUtil.error(req.getRequestURI(), e.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public HttpResponse<Object> authenticationExceptionHandler(HttpServletRequest req, AuthenticationException e) {
        return ResultUtil.error(req.getRequestURI(), e.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }
}
