package com.swjt.fileManagement.services.controllers;

import com.alibaba.fastjson.JSONObject;

import com.swjt.fileManagement.data.base.User;
import com.swjt.fileManagement.services.services.base.UserService;
import com.swjt.fileManagement.services.services.common.HttpResponse;
import com.swjt.fileManagement.services.services.common.PBKDF2;
import com.swjt.fileManagement.services.services.common.ResultUtil;
import com.swjt.fileManagement.services.services.common.jwt.JwtUtil;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("login")
@RequestMapping("login")
public class Login {

    @Autowired
    UserService userService;

    @PostMapping("")
    public HttpResponse<JSONObject> login(@RequestBody JSONObject loginInfo) {
        String username = Optional.ofNullable(loginInfo.getString("username")).orElse("");
        String password = Optional.ofNullable(loginInfo.getString("password")).orElse("");
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        User user = userService.selectByUsername(username);
        PBKDF2 pbkdf2 = new PBKDF2();
        String newtoken = JwtUtil.sign(username, password);
        System.out.println(" new token :"+newtoken);
        System.out.println("hashPassword:"+pbkdf2.encode(password));

        if (user == null || !pbkdf2.checkPassword(password, user.getPassword())) {
            throw new UnauthorizedException("用户名或密码错误");
        }

        JSONObject obj = new JSONObject();
        obj.put("token", JwtUtil.sign(username, user.getPassword()));
        user.setPassword(" ");
        obj.put("user", user);
        return ResultUtil.success(obj);
    }

    @PostMapping("init")
    public HttpResponse<String> init(@RequestBody JSONObject authInfo) {
        String accessKey = Optional.ofNullable(authInfo.getString("access_key")).orElse("");

        return ResultUtil.success("");
    }
}
