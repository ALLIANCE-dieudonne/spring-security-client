package com.alliance.springsecurityclient.event.listener;

import com.alliance.springsecurityclient.entity.User;
import com.alliance.springsecurityclient.event.RegistrationCompleteEvent;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

  @Override
  public void onApplicationEvent(RegistrationCompleteEvent event) {
    //create the verification token for the user
    User user = event.getUser();
    String token = UUID.randomUUID().toString();


    //send mail to user

  }
}
