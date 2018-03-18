package me.fetonxu.daemon.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import me.fetonxu.daemon.handler.HandlerInitializier;
import me.fetonxu.netty.server.CommonHttpServer;

public class DaemonServer extends CommonHttpServer {
    public DaemonServer(int port, int workerThreadCount){
        super(port, workerThreadCount, new HandlerInitializier());
    }

    @Override
    public void start(){
        super.start();
    }

}
