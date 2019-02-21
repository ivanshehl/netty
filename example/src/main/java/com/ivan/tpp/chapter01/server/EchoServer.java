package com.ivan.tpp.chapter01.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.ivan.tpp.utils.ThreadPoolUtils;

/**
 * Listing 2.2 EchoServer class
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EchoServer {
	private final int port;

	public EchoServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		
		int port = 8080;
		new EchoServer(port).start();
	}

	public void start() throws Exception {
		File file = new File("server_info.txt");
    	if(file.exists()){
    		file.delete();
    		file.createNewFile();
    	}else{
    		file.createNewFile();
    	}
		final EchoServerHandler serverHandler = new EchoServerHandler(file);
		
		EventLoopGroup boss = new NioEventLoopGroup(2, ThreadPoolUtils.newFixedThreadArrayPool(2, 5, 0, 1000));
		
		EventLoopGroup work = new NioEventLoopGroup(5, ThreadPoolUtils.newFixedThreadArrayPool(5, 8, 1000, 1000));
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss,work).channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.childOption(ChannelOption.SO_RCVBUF, 1024*10)
					.childOption(ChannelOption.SO_SNDBUF, 1024*10)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(new LineBasedFrameDecoder(1024*800));
							ch.pipeline().addLast(serverHandler);
						}
					});

			ChannelFuture f = b.bind().sync();
			System.out.println(EchoServer.class.getName()
					+ " started and listening for connections on "
					+ f.channel().localAddress());
			f.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully().sync();
			work.shutdownGracefully().sync();
		}
	}
}