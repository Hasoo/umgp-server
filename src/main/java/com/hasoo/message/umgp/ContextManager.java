package com.hasoo.message.umgp;

import com.hasoo.message.dto.ClientContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ContextManager {

  private HashMap<ChannelId, ClientContext> clientContexts = new HashMap<>();

  public synchronized void put(Channel channel) {
    clientContexts.put(channel.id(), new ClientContext(channel));
  }

  public synchronized void update(Channel channel, String username) {
    this.clientContexts.get(channel.id()).setUsername(username);
  }

  public synchronized ClientContext get(Channel channel) {
    return clientContexts.get(channel.id());
  }

  public synchronized void remove(Channel channel) {
    clientContexts.remove(channel.id());
  }

  public synchronized ArrayList<ClientContext> getReportLine() {
    ArrayList<ClientContext> ccs = new ArrayList<>();
    for (Map.Entry<ChannelId, ClientContext> elem : clientContexts.entrySet()) {
      ClientContext cc = elem.getValue();
      if (cc.isReportline()) {
        ccs.add(cc);
      }
    }
    return ccs;
  }
}
