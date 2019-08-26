package com.swjt.fileManagement.services.services.base.impl;


import com.swjt.fileManagement.data.base.User;
import com.swjt.fileManagement.db.mappers.base.UserMapper;
import com.swjt.fileManagement.services.services.base.UserService;
import com.swjt.fileManagement.services.services.common.jwt.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User create(User obj) {
//        obj.setCreatetime(new Date());
        userMapper.insert(obj);
        return obj;
    }

    @Override
    public User delete(int id) {
        User user = userMapper.selectByPrimaryKey(id);
        userMapper.deleteByPrimaryKey(id);
        return user;
    }

    @Override
    public List<User> bulkDelete(List<Integer> idList) {
        return null;
    }

    @Override
    public User get(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> getAll() {
        return userMapper.selectAll();
    }

    @Override
    public User update(User obj) {
        userMapper.updateByPrimaryKeySelective(obj);
        return userMapper.selectByPrimaryKey(obj.getId());
    }

    @Override
    public User selectByUsername(String userName) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username",userName);
        return userMapper.selectOneByExample(example);
    }

    /**
     * 检查token有效性
     * @param token
     * @return
     */
    public static String checkToken(String token) {
        String userName = JwtUtil.getUsername(token);
        System.out.println("token==== "+token);
        UserServiceImpl userService = new UserServiceImpl();
        String hashPassword = userService.selectByUsername(userName).getPassword();
        try{
            JwtUtil.verify(token,userName,hashPassword);
        }
        catch(Exception e){
            return "false";
        }
        return "success";
    }
}
