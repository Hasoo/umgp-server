package com.hasoo.message.umgp;

import com.hasoo.message.dto.ClientContext;

public interface LineHandler {

  public void handle(ClientContext clientContext);
}
