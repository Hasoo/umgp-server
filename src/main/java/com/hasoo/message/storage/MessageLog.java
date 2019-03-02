package com.hasoo.message.storage;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageLog {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long MsgKey;
  @Column(length = 40, nullable = false)
  private String UserKey;
  @Column(length = 3, nullable = false)
  private String MsgType;
  @Column(length = 20, nullable = false)
  private String phone;
  @Column(length = 20, nullable = false)
  private String callback;
  @Column(length = 4000, nullable = false)
  private String message;
  @Column(nullable = false)
  private Integer status;
  @Column(nullable = false)
  private LocalDateTime resDate;
  private LocalDateTime sentDate;
  private LocalDateTime doneDate;
  private LocalDateTime reportDate;
}
