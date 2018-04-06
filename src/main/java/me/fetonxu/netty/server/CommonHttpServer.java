package me.fetonxu.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import me.fetonxu.netty.handler.ChannelHandlerAppender;

public class CommonHttpServer {


    private int port;

    private final ServerBootstrap bootstrap = new ServerBootstrap();

    private final EventLoopGroup workerGroup;

    private final EventLoopGroup bossGroup;

    public CommonHttpServer(int port, int workerThreadCount, ChannelHandlerAppender appender){
        this.port = port;
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(workerThreadCount);

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 64)
                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .childHandler(new CommonHttpHandlerInitializer(appender))
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true);
    }

    public void start() {
        try {
            bootstrap.bind(port).sync();
        }catch (Exception e){
            System.err.println(String.format("Common Server start error, %s", e));
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            System.exit(-1);
        }
    }

    private static class CommonHttpHandlerInitializer extends ChannelInitializer{

        private ChannelHandlerAppender channelHandlerAppender;

        CommonHttpHandlerInitializer(ChannelHandlerAppender nextInitializer){
            this.channelHandlerAppender = nextInitializer;
        }

        @Override
        protected void initChannel(Channel channel) throws Exception {
            channel.pipeline().addLast(new HttpServerCodec());
            channel.pipeline().addLast(new HttpObjectAggregator(1024 * 1024 * 5));
            channelHandlerAppender.handle(channel);
        }
    }

}
