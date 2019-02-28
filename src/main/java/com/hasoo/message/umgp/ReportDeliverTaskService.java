package com.hasoo.message.umgp;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ReportDeliverTaskService {

  @Autowired
  private UmgpWorker umgpWorker;

  @Autowired
  private TaskExecutor taskExecutor;

  private boolean isShutdown = false;

  public void shutdown() {
    isShutdown = true;
  }

  public void delivery() {
    taskExecutor.execute(new Runnable() {
      @Override
      public void run() {
        while (true != isShutdown) {
          if (true != umgpWorker.deliver()) {
            try {
              TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
          }
        }
      }
    });
  }
}
