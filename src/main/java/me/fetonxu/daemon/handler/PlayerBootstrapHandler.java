package me.fetonxu.daemon.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
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
        context.writeAndFlush(HttpServerUrlHandler.RESPONSE_502);
    }

    @Override
    public void post(ChannelHandlerContext context, HttpRequest request, Map<String, List<String>> queryStringMap, ByteBuf requestBody) {
        System.out.println("Bootstrap a player");

        byte[] originBytes = ByteBufUtil.getBytes(requestBody);

        try {
            Map map = JSON.parseObject(new String(originBytes, "UTF-8"), Map.class);

            int port = Integer.parseInt((String)map.get("port"));
            long userId = Long.parseLong((String)map.get("userId"));



        }catch (Exception e){
            e.printStackTrace();
        }

        context.writeAndFlush(HttpServerUrlHandler.RESPONSE_200);
    }
}
