package com.hasoo.message.umgp;

import com.hasoo.message.dto.ClientContext;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportLineHandler implements LineHandler {

  @Override
  public void handle(Channel channel, ClientContext clientContext) {
    Umgp.HType headerType = clientContext.getHeaderType();
    Umgp umgp = clientContext.getUmgp();

    if (Umgp.HType.ACK == headerType) {
      log.debug("-> {} key:{}", Umgp.ACK, umgp.getKey());
    } else if (Umgp.HType.PING == headerType) {
      log.debug("-> {} key:{}", Umgp.PING, umgp.getKey());
      sendPong(channel, umgp.getKey());
    } else {
      throw new RuntimeException(String.format("invalid reportline packet -> %s", headerType));
    }
  }

  public void sendPong(Channel channel, String key) {
    log.debug("<- {} key:{}", Umgp.PONG, key);
    StringBuilder packet = new StringBuilder();
    packet.append(Umgp.headerPart(Umgp.PONG));
    packet.append(Umgp.dataPart(Umgp.KEY, key));
    packet.append(Umgp.end());
    channel.writeAndFlush(packet.toString());
  }
}
