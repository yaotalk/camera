package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by yao on 2017/3/12.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long Id) {
            userRepository.delete(Id);
    }

    @Override
    public User loginIn(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }
}
