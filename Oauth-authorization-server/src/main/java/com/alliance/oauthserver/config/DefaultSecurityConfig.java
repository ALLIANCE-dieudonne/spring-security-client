package com.alliance.oauthserver.config;

import com.alliance.oauthserver.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class DefaultSecurityConfig {

  @Autowired
  private CustomAuthenticationProvider customAuthenticationProvider;

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
      .formLogin(Customizer.withDefaults());
    return http.build();
  }

  public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder){
    authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);

  }
}
