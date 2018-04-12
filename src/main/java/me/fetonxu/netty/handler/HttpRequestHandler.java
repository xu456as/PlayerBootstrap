package me.fetonxu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import java.util.List;
import java.util.Map;

public interface HttpRequestHandler {

    void get(ChannelHandlerContext context, HttpRequest request,
        Map<String, List<String>> queryStringMap) throws Exception;

    void post(ChannelHandlerContext context, HttpRequest request,
        Map<String, List<String>> queryStringMap, ByteBuf requestBody) throws Exception;
}
