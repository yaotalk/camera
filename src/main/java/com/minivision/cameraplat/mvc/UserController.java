package com.minivision.cameraplat.mvc;

import com.minivision.cameraplat.domain.User;
import com.minivision.cameraplat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView userpage() {
    return new ModelAndView("sysmanage/userlist");
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<User> userlist() {
    return this.userService.findAll();
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
  public String deleteUser(User user) {
    this.userService.delete(user);
    return "success";
  }
}
