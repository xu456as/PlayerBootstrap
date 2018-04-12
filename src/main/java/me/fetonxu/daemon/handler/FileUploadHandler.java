package me.fetonxu.daemon.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import me.fetonxu.netty.handler.HttpRequestHandler;
import me.fetonxu.netty.util.ResponseUtil;
import me.fetonxu.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUploadHandler implements HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadHandler.class);

    private static final int ZIP_READ_COUNT = 4 * 1024 * 1024;

    @Override public void get(ChannelHandlerContext ctx, HttpRequest request,
        Map<String, List<String>> queryStringMap) throws Exception {

        System.out.println("fileUploadHandler get");

        ctx.writeAndFlush(ResponseUtil.simpleResponse(HttpResponseStatus.BAD_GATEWAY, "0;method not support"));

    }

    @Override public void post(ChannelHandlerContext ctx, HttpRequest request,
        Map<String, List<String>> queryStringMap, ByteBuf requestBody) {

        long userId = -1;
        String encoding = null;
        try {
            userId = Long.parseLong(queryStringMap.get("userId").get(0));
            encoding = queryStringMap.get("encoding").get(0);

            logger.info(String.format("fileUploadHandler post, userId:%d.", userId));

            String basePath = Config.getString("repository.path") + "/" + userId;
            if(encoding.equals("zip")) {
                upLoadZipFiles(basePath, requestBody);
                ctx.writeAndFlush(ResponseUtil.simpleResponse(HttpResponseStatus.OK, "1;success"));
            }
            else{
                ctx.writeAndFlush(ResponseUtil.simpleResponse(HttpResponseStatus.OK, "0;encoding not support"));
            }

        } catch (Exception e) {
            logger.error(String.format("error: %s", e));
        }

    }

    private static void upLoadZipFiles(String destBasePath, ByteBuf content) throws Exception{
        File baseDir = new File(destBasePath);
        baseDir.mkdirs();

        try (ZipInputStream zis = new ZipInputStream(
            new ByteArrayInputStream(ByteBufUtil.getBytes(content)));
        ) {
            ZipEntry entry;
            byte[] data = new byte[ZIP_READ_COUNT];
            while ((entry = zis.getNextEntry()) != null) {
                File file = new File(baseDir, entry.getName());
                if (entry.getName().endsWith("/")) {
                    file.mkdir();
                } else {
                    file.createNewFile();
                    try (BufferedOutputStream outputStream = new BufferedOutputStream(
                        new FileOutputStream(file));
                    ) {
                        int len = -1;
                        while ((len = zis.read(data, 0, ZIP_READ_COUNT)) != -1) {
                            outputStream.write(data, 0, len);
                        }
                        outputStream.flush();
                    }
                }
                zis.closeEntry();
            }
        }
    }
}
