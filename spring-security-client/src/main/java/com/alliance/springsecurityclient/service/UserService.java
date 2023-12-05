package com.alliance.springsecurityclient.service;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.entity.VerificationToken;
import com.alliance.springsecurityclient.model.UserModel;

import java.util.Optional;

public interface UserService {
  User createUser(UserModel userModel);

  void saveVerificationTokenForUser(String token, User user);

  String verifyRegistrationToken(String token);

  VerificationToken generateNewVerificationToken(String oldToken);

  User findUserByEmail(String email);

  void createPasswordResetToken(String token, User user);

  String validateResetPasswordToken(String token);

  Optional<User> getUserByResetPasswordToken(String token);

  void changePassword(User user, String newPassword);

  boolean checkIfOldPasswordIsValid(User user, String oldPassword);
}
