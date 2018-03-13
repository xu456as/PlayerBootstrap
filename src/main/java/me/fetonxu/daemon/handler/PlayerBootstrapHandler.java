package me.fetonxu.daemon.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import me.fetonxu.netty.handler.HttpRequestHandler;
import me.fetonxu.netty.handler.HttpServerUrlHandler;

import java.util.List;
import java.util.Map;

public class PlayerBootstrapHandler implements HttpRequestHandler {
    @Override
    public void get(ChannelHandlerContext context, HttpRequest request, Map<String, List<String>> queryStringMap) {
        System.out.println("Bootstap a player");
        context.writeAndFlush(HttpServerUrlHandler.RESPONSE_200);
    }

    @Override
    public void post(ChannelHandlerContext context, HttpRequest request, Map<String, List<String>> queryStringMap, ByteBuf requestBody) {
        System.out.println("Bootstrap a player");
        context.writeAndFlush(HttpServerUrlHandler.RESPONSE_200);
    }
}
