package com.minivision.cameraplat.service;

import com.minivision.cameraplat.domain.User;
import com.minivision.cameraplat.rest.result.system.UserResult;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User update(User user);

    User create(User user);

    void delete(User user);

    User findOne(long id);
    
    User findByUsername(String username);
}
