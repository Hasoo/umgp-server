package com.hasoo.message.netty;

import com.hasoo.message.umgp.UmgpWorker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.LineSeparator;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.TimeUnit;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class NettyServer {

  @Builder.Default
  private EventLoopGroup serverGroup = new NioEventLoopGroup();
  @Builder.Default
  private EventLoopGroup childGroup = new NioEventLoopGroup();
  @Builder.Default
  private int port = 4000;

  private UmgpWorker umgpWorker;

  public void run() throws Exception {

    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap.group(serverGroup, childGroup).channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            /* @formatter:off */
            ch.pipeline()
                .addLast(new ReadTimeoutHandler(60, TimeUnit.SECONDS))
                .addLast(new LineEncoder(LineSeparator.WINDOWS))
                .addLast(new LineBasedFrameDecoder(4096))
                .addLast(new NettyTimeoutHandler(umgpWorker))
                .addLast(new NettyServerHandler(umgpWorker))
            ;
            /* @formatter:on */
          }
        }).childOption(ChannelOption.TCP_NODELAY, true);

    ChannelFuture f = serverBootstrap.bind(port).sync();
    f.channel().closeFuture().sync();
    log.debug("exited");
  }

  public void shutdown() {
    serverGroup.shutdownGracefully();
    childGroup.shutdownGracefully();
  }
}
