package me.fetonxu.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

public class ResponseUtil {

    public final static FullHttpResponse simpleResponse(HttpResponseStatus status, String content)
        throws Exception {
        byte[] bytes = content.getBytes("utf-8");
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
            Unpooled.wrappedBuffer(bytes));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);

        return response;
    }

}
