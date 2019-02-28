package com.hasoo.message.umgp;

import com.hasoo.message.dto.ClientContext;
import io.netty.channel.Channel;

public interface LineHandler {
  public void handle(Channel channel, ClientContext clientContext);
}
