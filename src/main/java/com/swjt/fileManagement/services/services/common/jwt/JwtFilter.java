package com.swjt.fileManagement.services.services.common.jwt;

import com.alibaba.fastjson.JSON;

import com.swjt.fileManagement.services.services.common.HttpResponse;
import com.swjt.fileManagement.services.services.common.ResultUtil;
import com.swjt.fileManagement.services.services.common.errors.Errors;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * 代码的执行流程 preHandle -> isAccessAllowed -> isLoginAttempt -> executeLogin
 *
 * @author AgilePhotonics
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

        String s = httpServletRequest.getMethod();
        String a = RequestMethod.OPTIONS.name();
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }

    /**
     * 判断是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (tokenExists(request)) {
            try {
                executeLogin(request, response);
                return true;
            } catch (AuthenticationException e) {
                if (e.getMessage().equals(Errors.TOKEN_EXPIRED.getMessage())) {
                    tokenExpired(request, response);
                }
            }
        }
        return false;
    }

    private void tokenExpired(ServletRequest request, ServletResponse response) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpServletResponse.getOutputStream(),
                    StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            HttpResponse<String> res = ResultUtil.error(
                    httpServletRequest.getRequestURI(),
                    Errors.TOKEN_EXPIRED.getMessage(),
                    Errors.TOKEN_EXPIRED.getCode()
            );
            bufferedWriter.write(JSON.toJSON(res).toString());
            bufferedWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean tokenExists(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        getSubject(request, response).login(new JwtToken(authorization));
        return true;
    }
}
