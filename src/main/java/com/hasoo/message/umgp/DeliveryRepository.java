package com.hasoo.message.umgp;

import com.hasoo.message.dto.ReportQue;

public interface DeliveryRepository {
  public ReportQue pop(String username);

  public void push(String username, ReportQue reportQue);
}
