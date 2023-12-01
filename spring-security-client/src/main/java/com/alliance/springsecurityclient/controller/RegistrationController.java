package com.alliance.springsecurityclient.controller;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.event.RegistrationCompleteEvent;
import com.alliance.springsecurityclient.model.UserModel;
import com.alliance.springsecurityclient.service.UserService;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

  @Autowired
  private UserService userService;

  @Autowired
  private ApplicationEventPublisher publisher;

  @PostMapping("/register")
  public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
    User user = userService.createUser(userModel);

    if (user != null) {
      publisher.publishEvent(new RegistrationCompleteEvent(
        user, applicationUrl(request)
      ));
      return "Success!!!";
    } else {
      return "Failed to create the user!!";
    }
  }

  @GetMapping("/verifyRegistration")
  public String verifyRegistration(@RequestParam("token") String token) {
    String result = userService.verifyRegistrationToken(token);
    if (result.equalsIgnoreCase("valid")) {
      return "User verified successfully!!";
    }
    return "Bad user!!";
  }

  private String applicationUrl(HttpServletRequest request) {
    return "http://" +
      request.getServerName() +
      ":" +
      request.getServerPort() +
      request.getContextPath();
  }
}
