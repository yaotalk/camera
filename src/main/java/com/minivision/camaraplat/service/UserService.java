package com.minivision.camaraplat.service;
import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.rest.result.system.UserResult;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User update(User user);

    User create(User user);

    void delete(Long Id);

    UserResult validateUser(String name,String password);

    User findOne(long id);
    
    User findByUsername(String username);
}
