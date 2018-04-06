package me.fetonxu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChannelHandler.Sharable
public class HttpServerUrlHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final HttpRequestHandler defaultHandler;

    public final static FullHttpResponse RESPONSE_502 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
            HttpResponseStatus.BAD_GATEWAY, ByteBufUtil.writeUtf8(ByteBufAllocator.DEFAULT, "Nothing to show"));
    public final static FullHttpResponse RESPONSE_200 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK, ByteBufUtil.writeUtf8(ByteBufAllocator.DEFAULT, "Connect successfully"));

    private Map<String, HttpRequestHandler> handlerMap = new HashMap<>();

    public HttpServerUrlHandler(HttpRequestHandler defaultHandler){
        this.defaultHandler = defaultHandler;
    }

    public HttpServerUrlHandler(){
        this.defaultHandler = null;
    }

    public HttpServerUrlHandler register(String uriPath, HttpRequestHandler handler){
        handlerMap.put(uriPath, handler);
        return this;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(fullHttpRequest.uri());
        ByteBuf requestBody = fullHttpRequest.content();
        Map<String, List<String>> queryString = queryStringDecoder.parameters();
        HttpRequestHandler handler = handlerMap.getOrDefault(queryStringDecoder.path(), defaultHandler);
        if(handler == null) {
            channelHandlerContext.writeAndFlush(RESPONSE_502.retain());
            return;
        }

        if(fullHttpRequest.method() == HttpMethod.GET){
            handler.get(channelHandlerContext, fullHttpRequest, queryString);
        }
        else if(fullHttpRequest.method() == HttpMethod.POST){
            handler.post(channelHandlerContext, fullHttpRequest, queryString, requestBody);
        }
        else{
            channelHandlerContext.writeAndFlush(RESPONSE_502.retain());
        }

    }
}
