package com.naruto.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by yue.yuan on 2017/4/13.
 */
public class TestServerHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bb = (ByteBuf)msg;
        while (bb.isReadable()) {
            System.out.print((char)bb.readByte());
            System.out.flush();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
