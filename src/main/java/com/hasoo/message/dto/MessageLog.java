package com.hasoo.message.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageLog {
  private Long msgKey;
  private String userKey;
  private String msgType;
  private String phone;
  private String callback;
  private String message;
  private Integer status;
  private LocalDateTime resDate;
  private LocalDateTime sentDate;
  private LocalDateTime doneDate;
  private LocalDateTime reportDate;
}
