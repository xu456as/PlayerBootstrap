package me.fetonxu.daemon.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import me.fetonxu.netty.handler.ChannelHandlerAppender;
import me.fetonxu.netty.handler.HttpServerUrlHandler;

public class HandlerInitializier implements ChannelHandlerAppender {

    @Override
    public void handle(Channel channel) {
        HttpServerUrlHandler mainHandler = new HttpServerUrlHandler();

        mainHandler.register(HttpMethod.POST, "/run_player", new PlayerBootstrapHandler());

        channel.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(1024 * 5 * 1024))
                .addLast(mainHandler);
    }
}
