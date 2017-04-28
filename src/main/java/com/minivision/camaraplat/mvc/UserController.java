package com.minivision.camaraplat.mvc;

import com.minivision.camaraplat.domain.User;
import com.minivision.camaraplat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final String PATH = "sysmanage";

    @Autowired
    private UserService userService;

    @GetMapping("page")
    public ModelAndView userpage() {
        List<User> user = this.userService.findAll();
        return new ModelAndView(PATH + "/userlist");
    }

    @GetMapping
    public  List<User> userlist() {
        return   this.userService.findAll();
    }

    @PostMapping
    public String createUser(User user) {
        this.userService.create(user);
        return "success";
    }

    @PatchMapping
    public String updateUser(User user) {
        this.userService.update(user);
        return "success";
    }

    @DeleteMapping
    public String deleteUser(String id) {
        this.userService.delete(Long.valueOf(id));
        return "success";
    }
}
