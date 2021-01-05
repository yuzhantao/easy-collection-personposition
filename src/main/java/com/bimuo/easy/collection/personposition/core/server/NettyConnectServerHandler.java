package com.bimuo.easy.collection.personposition.core.server;

import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyConnectServerHandler extends ChannelInboundHandlerAdapter {
    private CollectionServer service;

    public NettyConnectServerHandler(CollectionServer service) {
        this.service=service;
    }
    @NotProguard
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.service.getConnectNum().incrementAndGet();
        super.channelRegistered(ctx);
    }
    @NotProguard
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        this.service.getConnectNum().decrementAndGet();
        super.channelUnregistered(ctx);
    }
}
