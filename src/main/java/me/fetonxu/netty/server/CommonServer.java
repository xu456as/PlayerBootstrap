package me.fetonxu.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetAddress;

public class CommonServer {


    private int port;

    private final ServerBootstrap bootstrap = new ServerBootstrap();

    private final EventLoopGroup workerGroup;

    private final EventLoopGroup bossGroup;

    public CommonServer(int port, int workerThreadCount, ChannelInitializer channelInitializer){
        this.port = port;
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(workerThreadCount);

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 64)
                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .childHandler(channelInitializer)
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

}
