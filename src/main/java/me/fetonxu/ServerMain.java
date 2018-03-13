package me.fetonxu;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import me.fetonxu.daemon.handler.PlayerBootstrapHandler;
import me.fetonxu.netty.handler.HttpRequestHandler;
import me.fetonxu.netty.handler.HttpServerUrlHandler;
import me.fetonxu.netty.server.CommonServer;

public class ServerMain {
    public static void main(String[] args){

        HttpServerUrlHandler urlHandler = new HttpServerUrlHandler(null);
        urlHandler.register(HttpMethod.GET, "/playerBootstrap", new PlayerBootstrapHandler());

        CommonServer server = new CommonServer(8766, 64, new ChannelInitializer<SocketChannel>(){
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(1024 * 80))
                .addLast(new HttpContentCompressor())
                .addLast(urlHandler) ;
            }
        });
        server.start();
    }
}
