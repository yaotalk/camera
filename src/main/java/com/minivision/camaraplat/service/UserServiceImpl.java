package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.repository.UserRepository;
import com.minivision.camaraplat.rest.result.system.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        User olduser = null;
        if(user.getUsername() == null || user.getPassword() == null){
              olduser = userRepository.findOne(user.getId());
              olduser.setAutologin(user.isAutologin());
        }
        if(olduser != null){
             return userRepository.save(olduser);
        }
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

    public UserResult validateUser(String username,String password){
        if(null == username || "".equals(username.trim())){
            return  new UserResult(400,"请填写用户名");
        }
        if(null == password || "".equals(password.trim())){
            return  new UserResult(400,"请填写密码");
        }
       User user = userRepository.findByUsernameAndPassword(username,password);
       if(null == user){
          return  new UserResult(401,"用户名或密码错误");
        }
       else return new UserResult(200,"登录成功");
    }

    @Override public User findOne(long id) {
        return userRepository.findOne(id);
    }
}
