package com.minivision.camaraplat.service;
import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.rest.result.system.UserResult;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User update(User analyser);

    User create(User analyser);

    void delete(Long Id);

    User loginIn(String username, String password);

    UserResult validateUser(String name,String password);

    User findOne(long id);
}
