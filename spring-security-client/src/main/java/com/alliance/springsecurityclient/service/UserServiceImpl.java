package com.alliance.springsecurityclient.service;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.model.UserModel;
import com.alliance.springsecurityclient.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public User createUser(UserModel userModel) {
    User user = new User();
    user.setFirstName(userModel.getFirstName());
    user.setLastName(userModel.getLastName());
    user.setEmail(userModel.getEmail());
    user.setRole("USER");
    user.setPassword(passwordEncoder.encode(userModel.getPassword()));
    userRepository.save(user);
    return null;

  }
}
