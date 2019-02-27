package com.hasoo.message;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UmgpServerApplication implements CommandLineRunner {

  public static void main(String[] args) {
    // begin
    SpringApplication.run(UmgpServerApplication.class, args);
    // end
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("run()");
  }
}
