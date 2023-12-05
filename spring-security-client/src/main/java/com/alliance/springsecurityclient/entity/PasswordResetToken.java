package com.alliance.springsecurityclient.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {
  private static final int EXPIRATION_TIME = 10;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  private String token;
  private Date expirationTime;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(
    name = "user_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN")
  )
  private User user;

  public PasswordResetToken(User user, String token) {
    super();
    this.user = user;
    this.token = token;
    this.expirationTime = calculateExpirationTime();
  }

  public PasswordResetToken(String token) {
    super();
    this.token = token;
    this.expirationTime = calculateExpirationTime();
  }

  private Date calculateExpirationTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(new Date().getTime());
    cal.add(Calendar.MINUTE, EXPIRATION_TIME);
    return new Date(cal.getTime().getTime());
  }
}
