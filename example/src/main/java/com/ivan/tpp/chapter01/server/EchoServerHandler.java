package com.ivan.tpp.chapter01.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;

/**
 * Listing 2.1 EchoServerHandler
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	private File file;
	
	public EchoServerHandler(File file){
		this.file = file;
	}
	
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		
		ByteBuf message = (ByteBuf) msg;
        byte[] response = new byte[message.readableBytes()];
        message.readBytes(response);
        String line = atomicInteger.getAndIncrement()+"::::"+new String(response);
        try {
			FileUtils.write(file, line, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String sendContent = "hello client ,im server";
        ByteBuf seneMsg = Unpooled.buffer(sendContent.length());
        seneMsg.writeBytes(sendContent.getBytes());

        ctx.writeAndFlush(Unpooled.copiedBuffer(seneMsg));
        System.out.println("send info to client:" + sendContent);
	}

//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(
//				ChannelFutureListener.CLOSE);
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//		cause.printStackTrace();
//		System.out.println(cause.getMessage());
//		ctx.close();
//	}
	
	// 2.2
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("--- accepted client---");
        ctx.fireChannelActive();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}