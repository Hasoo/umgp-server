package com.hasoo.message;

import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import com.hasoo.message.netty.NettyServer;
import com.hasoo.message.umgp.ReportDeliverTaskService;
import com.hasoo.message.util.HUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAsync
@SpringBootApplication
public class UmgpServerApplication implements CommandLineRunner {
  @Autowired
  private NettyServer nettyServer;

  @Autowired
  private ReportDeliverTaskService reportDeliverTaskService;

  public static void main(String[] args) {
    SpringApplication.run(UmgpServerApplication.class, args);
  }

  @PreDestroy
  public void onExit() {
    reportDeliverTaskService.shutdown();
  }

  @Override
  public void run(String... args) throws Exception {
    try {
      reportDeliverTaskService.delivery();
      nettyServer.run();
    } catch (Exception e) {
      log.error(HUtil.getStackTrace(e));
    } finally {
      nettyServer.shutdown();
    }
  }
}
