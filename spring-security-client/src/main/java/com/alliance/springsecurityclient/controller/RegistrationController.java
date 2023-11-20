package com.alliance.springsecurityclient.controller;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.event.RegistrationCompleteEvent;
import com.alliance.springsecurityclient.model.UserModel;
import com.alliance.springsecurityclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

  @Autowired
  private UserService userService;

  @Autowired
  private ApplicationEventPublisher publisher;

  @PostMapping("/register")
  public String registerUser(@RequestBody UserModel userModel) {
    User user = userService.createUser(userModel);
    publisher.publishEvent(new RegistrationCompleteEvent(
      user, "url"
    ));
    return "Success!!!";
  }
}
