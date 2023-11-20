package com.alliance.springsecurityclient.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor

public class VerificationToken {

  //expiration duration
  private static final int EXPIRATION_TIME = 10;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;
  private Date expirationDate;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
  private User user;

  public VerificationToken(User user, String token) {
    super();
    this.token = token;
    this.user = user;
    this.expirationDate = calculateExpirationTime();
  }

  public VerificationToken(String token) {
    super();
    this.token = token;
    this.expirationDate = calculateExpirationTime();
  }

  private Date calculateExpirationTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(new Date().getTime());
    calendar.add(Calendar.MINUTE, EXPIRATION_TIME);

    return new Date(calendar.getTime().getTime());

  }
}
