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
import me.fetonxu.netty.util.ResponseUtil;
import me.fetonxu.runtime.CommandLine;
import me.fetonxu.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PlayerBootstrapHandler implements HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBootstrapHandler.class);

    @Override public void get(ChannelHandlerContext ctx, HttpRequest request,
        Map<String, List<String>> queryStringMap) throws Exception {
        ctx.writeAndFlush(
            ResponseUtil.simpleResponse(HttpResponseStatus.BAD_GATEWAY, "0;method not support"));
    }

    @Override public void post(ChannelHandlerContext ctx, HttpRequest request,
        Map<String, List<String>> queryStringMap, ByteBuf requestBody) throws Exception {

        String responseString = "0;default";
        try {

            int port = Integer.parseInt(queryStringMap.get("port").get(0));
            long userId = Long.parseLong(queryStringMap.get("userId").get(0));
            long timestamp = Long.parseLong(queryStringMap.get("timestamp").get(0));
            logger.info(String.format("bootstrap player, userId: %d, port: %d", userId, port));

            String startDest =
                Config.getString("repository.path") + "/" + userId + "-" + timestamp + "/start.sh";

            CommandLine.copyFile("shell/start.sh", startDest);

            if (CommandLine.existPort(port)) {
                responseString = "0;port occupied";
            } else {
                daemonRun(startDest, port);
                Thread.sleep(3000);
                if (CommandLine.existPort(port)) {
                    responseString = "1;success";
                } else {
                    responseString = "0;operation fail";
                }
            }

        } catch (Exception e) {
            logger.error(String.format("error: %s", e));
            responseString = "0;error";
        }

        ctx.writeAndFlush(ResponseUtil.simpleResponse(HttpResponseStatus.OK, responseString));
    }

    private static void daemonRun(String path, int port) {

        ExecutorService threadpool = Executors.newSingleThreadExecutor();
        threadpool.submit(new Runnable() {
            @Override public void run() {
                try {
                    CommandLine.startPlayer(path, port);
                } catch (Exception e) {
                    logger.error(String.format("start player error: %s", e));
                }
            }
        });

    }
}
