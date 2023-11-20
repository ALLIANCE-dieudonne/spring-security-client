package com.alliance.springsecurityclient.service;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.model.UserModel;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  User createUser(UserModel userModel);
}
