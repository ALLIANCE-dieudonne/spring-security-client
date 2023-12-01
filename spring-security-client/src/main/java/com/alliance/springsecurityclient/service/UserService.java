package com.alliance.springsecurityclient.service;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.model.UserModel;
import org.springframework.stereotype.Service;

public interface UserService {
  User createUser(UserModel userModel);

  void saveVerificationTokenForUser(String token, User user);

  String verifyRegistrationToken(String token);
}
