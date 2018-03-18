package me.fetonxu.netty.handler;

import io.netty.channel.Channel;

public interface ChannelHandlerAppender {
    void handle(Channel channel);
}
