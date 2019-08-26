package com.swjt.fileManagement.services.services.base;


import com.swjt.fileManagement.data.base.User;
import com.swjt.fileManagement.db.crud.CrudService;

public interface UserService extends CrudService<User> {
    User selectByUsername(String userName);
//    String checkToken(String token);
}
