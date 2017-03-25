package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.User;

import java.util.List;

/**
 * Created by yao on 2017/3/12.
 */
public interface UserService {

    List<User> findAll();

    User update(User analyser);

    User create(User analyser);

    void delete(Long Id);

    User loginIn(String username, String password);
}
