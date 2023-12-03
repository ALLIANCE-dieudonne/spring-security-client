package com.alliance.springsecurityclient.controller;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.entity.VerificationToken;
import com.alliance.springsecurityclient.event.RegistrationCompleteEvent;
import com.alliance.springsecurityclient.model.UserModel;
import com.alliance.springsecurityclient.service.UserService;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RegistrationController {

  @Autowired
  private UserService userService;

  @Autowired
  private ApplicationEventPublisher publisher;

  //registering the user
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

  //verifying registration
  @GetMapping("/verifyRegistration")
  public String verifyRegistration(@RequestParam("token") String token) {
    String result = userService.verifyRegistrationToken(token);
    if (result.equalsIgnoreCase("valid")) {
      return "User verified successfully!!";
    }
    return "Bad user!!";
  }

  //resend verification token
  @GetMapping("/resendVerificationToken")
  public String resendVerificationToken(
    @RequestParam("token") String oldToken,
    HttpServletRequest request) {

    VerificationToken verificationToken =
      userService.generateNewVerificationToken(oldToken);

    User user = verificationToken.getUser();
    resendVerificationTokenMail(user, applicationUrl(request),verificationToken);
    return "Verification Link sent!!";

  }

  //resend verification token
  private void resendVerificationTokenMail(User user, String applicationUrl,VerificationToken verificationToken){

    //send mail to user
    String url =applicationUrl
      + "/verifyRegistration?token="
      + verificationToken.getToken();

    log.info("Click the link to verify your account: {}", url);
  }

//creating the application url
  private String applicationUrl(HttpServletRequest request) {
    return "http://" +
      request.getServerName() +
      ":" +
      request.getServerPort() +
      request.getContextPath();
  }
}
