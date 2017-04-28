package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.repository.UserRepository;
import com.minivision.camaraplat.rest.result.system.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
//    @Autowired
//    private StandardPasswordEncoder passwordEncoder;
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
     // user.setPassword(passwordEncoder.encode(user.getPassword()));
      return userRepository.save(user);
    }

    @Override
    public User create(User user) {
    //  user.setPassword(passwordEncoder.encode(user.getPassword()));
      return userRepository.save(user);
    }

    @Override
    public void delete(Long Id) {
        userRepository.delete(Id);
    }

    public UserResult validateUser(String username,String password){
        if(null == username || "".equals(username.trim())){
            return  new UserResult(400,"请填写用户名");
        }
        if(null == password || "".equals(password.trim())){
            return  new UserResult(400,"请填写密码");
        }
       User user = userRepository.findByUsernameAndPassword(username, password);
       if(null == user){
          return  new UserResult(401,"用户名或密码错误");
        }
       else return new UserResult(200,"登录成功");
    }

    @Override public User findOne(long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
      return userRepository.findByUsername(username);
    }
}
