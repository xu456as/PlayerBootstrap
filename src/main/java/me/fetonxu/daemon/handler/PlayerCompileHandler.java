package me.fetonxu.daemon.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.fetonxu.netty.handler.HttpRequestHandler;
import me.fetonxu.netty.util.ResponseUtil;
import me.fetonxu.runtime.CommandLine;
import me.fetonxu.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class PlayerCompileHandler implements HttpRequestHandler {

    private final static Logger logger = LoggerFactory.getLogger(PlayerCompileHandler.class);

    @Override public void get(ChannelHandlerContext context, HttpRequest request,
        Map<String, List<String>> queryStringMap) throws Exception {
        context.writeAndFlush(
            ResponseUtil.simpleResponse(HttpResponseStatus.BAD_GATEWAY, "0;method not support"));
    }

    @Override public void post(ChannelHandlerContext context, HttpRequest request,
        Map<String, List<String>> queryStringMap, ByteBuf requestBody) throws Exception {

        String info = "0;default";

        try {

            long userId = Long.parseLong(queryStringMap.get("userId").get(0));
            logger.info(String.format("compile player, userId: %d", userId));

            String buildDest = Config.getString("repository.path") + "/" + userId + "/shell/build.xml";
            CommandLine.copyFile("shell/build.xml", buildDest);
            info = CommandLine.compilePlayer(buildDest);

        } catch (Exception e) {
            logger.error("error: %s", e);
        }
        context.writeAndFlush(
            ResponseUtil.simpleResponse(HttpResponseStatus.OK, info));

    }
}
