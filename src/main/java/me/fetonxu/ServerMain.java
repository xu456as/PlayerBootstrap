package me.fetonxu;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import me.fetonxu.daemon.handler.PlayerBootstrapHandler;
import me.fetonxu.daemon.server.DaemonServer;
import me.fetonxu.netty.handler.ChannelHandlerAppender;
import me.fetonxu.netty.handler.HttpServerUrlHandler;
import me.fetonxu.netty.server.CommonHttpServer;

public class ServerMain {
    public static void main(String[] args){

        DaemonServer daemonServer = new DaemonServer(9000, 4);
        daemonServer.start();
    }
}
