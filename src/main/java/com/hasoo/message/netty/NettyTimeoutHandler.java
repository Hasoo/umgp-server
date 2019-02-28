package com.hasoo.message.netty;

import com.hasoo.message.umgp.UmgpWorker;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyTimeoutHandler extends ChannelDuplexHandler {
  UmgpWorker umgpWorker;

  public NettyTimeoutHandler(UmgpWorker umgpWorker) {
    this.umgpWorker = umgpWorker;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (cause instanceof ReadTimeoutException) {
      log.info("timeout {}", umgpWorker.who(ctx.channel()));
    } else {
      super.exceptionCaught(ctx, cause);
    }
  }
}
