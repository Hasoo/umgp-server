package com.hasoo.message.config;

import com.hasoo.message.netty.NettyServer;
import com.hasoo.message.umgp.UmgpWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyServerConfig {

  @Value("${umgp.server.port}")
  private int port;

  @Autowired
  UmgpWorker umgpWorker;

  @Bean
  NettyServer nettyServer() {
    return NettyServer.builder().umgpWorker(umgpWorker).port(port).build();
  }

}
