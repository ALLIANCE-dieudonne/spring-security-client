package com.alliance.springsecurityclient.service;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.entity.VerificationToken;
import com.alliance.springsecurityclient.model.UserModel;
import com.alliance.springsecurityclient.repository.UserRepository;
import com.alliance.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private VerificationTokenRepository verificationTokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @Override
  public User createUser(UserModel userModel) {
    //existing user
    User existingUser = userRepository.findByEmail(userModel.getEmail());
    if (existingUser != null) {
      return existingUser;
    }

    //create new user
    User user = new User();
    user.setFirstName(userModel.getFirstName());
    user.setLastName(userModel.getLastName());
    user.setEmail(userModel.getEmail());
    user.setRole("USER");
    user.setPassword(passwordEncoder.encode(userModel.getPassword()));
    userRepository.save(user);
    return user;

  }

  //sending verification
  @Override
  public void saveVerificationTokenForUser(String token, User user) {
    VerificationToken verificationToken =
      new VerificationToken(user, token);

    verificationTokenRepository.save(verificationToken);
  }

  //verifying registration token
  @Override
  public String verifyRegistrationToken(String token) {
    VerificationToken verificationToken =
      verificationTokenRepository.findByToken(token);


    if (verificationToken == null) {
      return "Invalid verification token";
    }

    User user = verificationToken.getUser();
    Calendar cal = Calendar.getInstance();

    if ((verificationToken.getExpirationTime().getTime() - cal.getTime().getTime()) <= 0) {
      verificationTokenRepository.delete(verificationToken);
      return "Token expired!!";
    }
    user.setEnabled(true);
    userRepository.save(user);
    return "Valid";
  }

  @Override
  public VerificationToken generateNewVerificationToken(String oldToken) {
    VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
    verificationToken.setToken(UUID.randomUUID().toString());
    verificationTokenRepository.save(verificationToken);
    return verificationToken;
  }
}
