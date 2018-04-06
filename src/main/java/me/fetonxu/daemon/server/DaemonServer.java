package me.fetonxu.daemon.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import me.fetonxu.daemon.handler.HandlerInitializier;
import me.fetonxu.netty.server.CommonHttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaemonServer extends CommonHttpServer {

    private static final Logger logger = LoggerFactory.getLogger(DaemonServer.class);

    public DaemonServer(int port, int workerThreadCount){
        super(port, workerThreadCount, new HandlerInitializier());
    }

    @Override
    public void start(){
        logger.info("Daemon Server start");
        super.start();
    }

}
