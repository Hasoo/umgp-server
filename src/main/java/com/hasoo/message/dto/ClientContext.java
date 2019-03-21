package com.hasoo.message.dto;

import com.hasoo.message.umgp.Umgp;
import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class ClientContext {

  private Channel channel;

  @Setter(AccessLevel.NONE)
  private Umgp umgp = new Umgp();

  private String username;
  private boolean reportline = false;
  private boolean authenticated = false;
  private Umgp.HType headerType;

  public ClientContext(Channel channel) {
    this.channel = channel;
  }
}
