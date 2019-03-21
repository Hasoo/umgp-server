package com.hasoo.message;

import com.hasoo.message.netty.NettyServer;
import com.hasoo.message.umgp.ReportDeliverTaskService;
import com.hasoo.message.util.HUtil;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@Slf4j
@SpringBootApplication
@Profile("!test")
public class UmgpServerApplication implements CommandLineRunner {

  private final NettyServer nettyServer;

  private final ReportDeliverTaskService reportDeliverTaskService;

  private boolean isExit = false;

  @Autowired
  public UmgpServerApplication(NettyServer nettyServer,
      ReportDeliverTaskService reportDeliverTaskService) {
    this.nettyServer = nettyServer;
    this.reportDeliverTaskService = reportDeliverTaskService;
  }

  public static void main(String[] args) {
    SpringApplication.run(UmgpServerApplication.class, args);
  }

  @PreDestroy
  public void onExit() {
    isExit = true;
    reportDeliverTaskService.shutdown();
  }

  @Override
  public void run(String... args) throws Exception {
    try {
      reportDeliverTaskService.delivery();
      nettyServer.run();
      log.info("after run");
    } catch (Exception e) {
      log.error(HUtil.getStackTrace(e));
    } finally {
      nettyServer.shutdown();
    }
  }
}
