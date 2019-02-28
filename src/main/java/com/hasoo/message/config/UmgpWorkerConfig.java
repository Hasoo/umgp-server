package com.hasoo.message.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hasoo.message.umgp.ContextManager;
import com.hasoo.message.umgp.DeliveryRepository;
import com.hasoo.message.umgp.ReportLineHandler;
import com.hasoo.message.umgp.SendLineHandler;
import com.hasoo.message.umgp.UmgpWorker;

@Configuration
public class UmgpWorkerConfig {

  @Autowired
  private ContextManager contextManager;

  @Autowired
  private DeliveryRepository memDeliveryRepository;

  @Bean
  public UmgpWorker umgpWorker() {
    // @formatter:off
    return UmgpWorker.builder()
        .contextManager(contextManager)
        .deliveryRepository(memDeliveryRepository)
        .sendLineHandler(new SendLineHandler(memDeliveryRepository))
        .reportLineHandler(new ReportLineHandler())
        .build();
    // @formatter:on
  }

}
