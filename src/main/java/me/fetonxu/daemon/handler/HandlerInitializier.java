package me.fetonxu.daemon.handler;

import io.netty.channel.Channel;
import me.fetonxu.netty.handler.ChannelHandlerAppender;
import me.fetonxu.netty.handler.HttpServerUrlHandler;

public class HandlerInitializier implements ChannelHandlerAppender {

    @Override public void handle(Channel channel) {
        HttpServerUrlHandler mainHandler = new HttpServerUrlHandler();

        mainHandler.register("/run_player", new PlayerBootstrapHandler())
            .register("/upload_player", new PlayerUploadHandler())
            .register("/port_operation", new PortOperationHandler())
            .register("/compile_player", new PlayerCompileHandler());

        channel.pipeline().addLast(mainHandler);
    }
}
