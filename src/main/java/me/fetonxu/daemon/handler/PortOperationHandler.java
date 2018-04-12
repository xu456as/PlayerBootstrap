package me.fetonxu.daemon.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.fetonxu.netty.handler.HttpRequestHandler;
import me.fetonxu.netty.util.ResponseUtil;
import me.fetonxu.runtime.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class PortOperationHandler implements HttpRequestHandler {

    private final static Logger logger = LoggerFactory.getLogger(PortOperationHandler.class);

    @Override public void get(ChannelHandlerContext context, HttpRequest request,
        Map<String, List<String>> queryStringMap) throws Exception {
        String responseString = "0;default";
        try{
            int port = Integer.parseInt(queryStringMap.get("port").get(0));
            if(CommandLine.existPort(port)){
                responseString = "1;exist";
            }
            else{
                responseString = "0;not exist";
            }
        }catch (Exception e){
            logger.error(String.format("error: %s", e));
        }
        context.writeAndFlush(ResponseUtil.simpleResponse(HttpResponseStatus.OK, responseString));
    }

    @Override public void post(ChannelHandlerContext context, HttpRequest request,
        Map<String, List<String>> queryStringMap, ByteBuf requestBody) throws Exception {
        String responseString = "0;default";
        try{
            int port = Integer.parseInt(queryStringMap.get("port").get(0));
            responseString = CommandLine.reclaimPort(port);
        }catch (Exception e){
            logger.error(String.format("error: %s", e));
        }
        context.writeAndFlush(ResponseUtil.simpleResponse(HttpResponseStatus.OK, responseString));
    }
}
