package com.bimuo.easy.collection.personposition.core.server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;
import com.bimuo.easy.collection.personposition.core.message.IMessageHandleFactory;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.GlobalEventExecutor;

public class CollectionServer extends AbstractExecutionThreadService {
	public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private static Logger logger = LogManager.getLogger(CollectionServer.class.getName());
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	/**
	 * 閺堝秴濮熼崳銊ユ倳锟�??
	 */
	private String serverName;
	/**
	 * 閺堝秴濮熼崳銊ь伂閸欙絽锟�?
	 */
	private int serverPort;
	/**
	 * 閺堝秴濮熼崳鈺痮cket鐠囨槒绉撮弮鍓佹畱缁夋帗锟�?
	 */
	private long readTimeout = 600;
	/**
	 * 閺堝秴濮熼崳鈺痮cket閸愭瑨绉撮弮鍓佹畱缁夋帗锟�?
	 */
	private long writeTimeout = Long.MAX_VALUE;
	/**
	 * 閺堝秴濮熼崳銊ヮ槱閻炲棗浼愰崢?
	 */
	private IMessageHandleFactory messageHandleFactory;

	private AtomicInteger connectNum = new AtomicInteger();

	public AtomicInteger getConnectNum() {
		return connectNum;
	}

	public void setConnectNum(AtomicInteger connectNum) {
		this.connectNum = connectNum;
	}

	@NotProguard
	@Override
	protected void run() throws Exception {
		try {
			CollectionServer.this.bind(this.getMessageHandleFactory(), this.getServerName(), this.getServerPort(),
					this.getWriteTimeout(), this.getReadTimeout());
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@NotProguard
	@Override
	protected void triggerShutdown() {
		this.destory();
	}

	private void bind(IMessageHandleFactory factory, String serverName, int port, long writeTimetou, long readTime)
			throws Exception {

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup);
			serverBootstrap.channel(NioServerSocketChannel.class);
			serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			serverBootstrap.handler(new LoggingHandler());
			serverBootstrap.childHandler(new NettyChannelHandler(factory, writeTimetou, readTime));
			serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			// 治理内存泄露
			ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
			
			ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
			logger.info("{}(port={}) 服务已开启", this.getServerName(), this.getServerPort());
			channelFuture.channel().closeFuture().sync();
		} finally {
			this.destory();
		}
	}

	// 濞戝牊浼呮径鍕倞锟�??
	private class NettyChannelHandler extends ChannelInitializer<SocketChannel> {
		IMessageHandleFactory factory;
		long writeTimeout, readTimeout;

		public NettyChannelHandler(IMessageHandleFactory factory, long writeTimeout, long readTimeout) {
			super();
			this.factory = factory;
			this.readTimeout = readTimeout;
			this.writeTimeout = writeTimeout;
		}

		@NotProguard
		@Override
		protected void initChannel(SocketChannel socketChannel) throws Exception {
			socketChannel.pipeline().addLast(new NettyConnectServerHandler(CollectionServer.this));
			socketChannel.pipeline().addLast(new LicenseCheckHandler(CollectionServer.this));
			socketChannel.pipeline().addLast(new IdleStateHandler(this.readTimeout, this.writeTimeout,
					Math.max(this.readTimeout, this.writeTimeout), TimeUnit.SECONDS));
			// Netty管道注入超时提醒类
			socketChannel.pipeline().addLast(new TimeoutHandler());
			// Netty管道注入硬件指令解码类
			socketChannel.pipeline().addLast(factory.createMessageDecoder());
			// Netty管道注入解码消息处理类
			socketChannel.pipeline().addLast(factory.createMessageHandle());
		}
	}

	public void destory() {
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
			logger.info("{}(port={}) 鏈嶅姟宸插叧锟�?", this.getServerName(), this.getServerPort());
		}
		bossGroup = null;
		workerGroup = null;
	}

	@NotProguard
	@Override
	protected String serviceName() {
		return this.getServerName();
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public IMessageHandleFactory getMessageHandleFactory() {
		return messageHandleFactory;
	}

	public void setMessageHandleFactory(IMessageHandleFactory messageHandleFactory) {
		this.messageHandleFactory = messageHandleFactory;
	}

	public long getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

	public long getWriteTimeout() {
		return writeTimeout;
	}

	public void setWriteTimeout(long writeTimeout) {
		this.writeTimeout = writeTimeout;
	}
}
