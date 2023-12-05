package com.alliance.springsecurityclient.controller;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.entity.VerificationToken;
import com.alliance.springsecurityclient.event.RegistrationCompleteEvent;
import com.alliance.springsecurityclient.model.PasswordModel;
import com.alliance.springsecurityclient.model.UserModel;
import com.alliance.springsecurityclient.service.UserService;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

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
    resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
    return "Verification Link sent!!";

  }

  //resend verification token
  private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {

    //send mail to user
    String url = applicationUrl
      + "/verifyRegistration?token="
      + verificationToken.getToken();

    log.info("Click the link to verify your account: {}", url);
  }

  //reset password
  @PostMapping("/resetPassword")
  public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request
  ) {
    User user = userService.findUserByEmail(passwordModel.getEmail());
    String url = "";
    if (user != null) {
      String token = UUID.randomUUID().toString();
      userService.createPasswordResetToken(token, user);
      url = passwordResetTokenMail(user, applicationUrl(request), token);
    }
    return url;
  }

  @PostMapping("/savePassword")
  public String savePassword(@RequestParam("token") String token
    , @RequestBody PasswordModel passwordModel){

    String result = userService.validateResetPasswordToken(token);
    if(!result.equalsIgnoreCase("Valid!!")){
      return "Invalid token!!";
    }
    Optional<User> user = userService.getUserByResetPasswordToken(token);
    if (user.isPresent()){
      userService.changePassword(user.get(), passwordModel.getNewPassword());
      return "Password reset successfully!!";
    }else {
      return "Invalid token!!!";
    }
  }

  //changing the password
  @PostMapping("/changePassword")
  private String changePassword(@RequestBody PasswordModel passwordModel){
    User user = userService.findUserByEmail(passwordModel.getEmail());
   if (!userService.checkIfOldPasswordIsValid(user,passwordModel.getOldPassword())){
     return "Invalid old password!!";
   }
   //saving new password
    userService.changePassword(user, passwordModel.getNewPassword());
    return "Password changed successfully!!";
  }

  //password reset mail
  private String passwordResetTokenMail(User user, String applicationUrl, String token) {
    //send mail to user
    String url = applicationUrl
      + "/savePassword?token="
      + token;

    log.info("Click the link to reset your password: {}", url);
    return url;
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
