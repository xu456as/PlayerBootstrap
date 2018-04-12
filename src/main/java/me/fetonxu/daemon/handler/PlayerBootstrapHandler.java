package me.fetonxu.daemon.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.spdy.SpdyHeaders;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import me.fetonxu.netty.handler.HttpRequestHandler;
import me.fetonxu.netty.handler.HttpServerUrlHandler;
import me.fetonxu.runtime.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PlayerBootstrapHandler implements HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBootstrapHandler.class);

    @Override
    public void get(ChannelHandlerContext ctx, HttpRequest request, Map<String, List<String>> queryStringMap) {
        System.out.println("Bootstrap a player");

        try {


            ExecutorService executor = Executors.newSingleThreadExecutor();

            String text = "success";
            if(CommandLine.existPort(9123)){
                text = "port exists";
            }
            else{
                executor.submit(new Runnable() {
                    @Override public void run() {
                        try {
                            CommandLine.startPlayer(
                                "/Users/fetonxu/Works/workspace/TankBackEnd/STank/start.sh", 9123, 0);
                            logger.info("run_player success");
                        }catch (Exception e){
                            logger.error(String.format("run_player error, %s", e));
                        }
                    }
                });
            }


            byte[] bytes = text.getBytes("UTF-8");

            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
            response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, "chunked");
            HttpContent httpContent = new DefaultHttpContent(Unpooled.wrappedBuffer(bytes));
            LastHttpContent lastHttpContent = new DefaultLastHttpContent(Unpooled.wrappedBuffer(bytes));
            ctx.writeAndFlush(response);
            ctx.writeAndFlush(httpContent);
            ctx.writeAndFlush(lastHttpContent);

//            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
//                    Unpooled.wrappedBuffer(bytes));
//            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
//            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
//            ctx.writeAndFlush(fullHttpResponse);
        }catch (Exception e){
//            ctx.writeAndFlush(HttpServerUrlHandler.RESPONSE_502);
            logger.error(String.format("error: %s", e));
        }

    }

    @Override
    public void post(ChannelHandlerContext ctx, HttpRequest request, Map<String, List<String>> queryStringMap, ByteBuf requestBody) {
        System.out.println("Bootstrap a player");

        byte[] originBytes = ByteBufUtil.getBytes(requestBody);

        try {
            Map map = JSON.parseObject(new String(originBytes, "UTF-8"), Map.class);

            int port = Integer.parseInt((String)map.get("port"));
            long userId = Long.parseLong((String)map.get("userId"));


        }catch (Exception e){
            logger.error(String.format("error: %s", e));
        }

        ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
    }
}
