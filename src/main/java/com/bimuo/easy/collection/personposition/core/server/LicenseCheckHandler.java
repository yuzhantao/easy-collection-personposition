package com.bimuo.easy.collection.personposition.core.server;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;
import com.bimuo.easy.license.client.LicenseVerify;
import com.bimuo.easy.license.core.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;

public class LicenseCheckHandler extends AbstractRemoteAddressFilter<InetSocketAddress> {
	private CollectionServer server;
	protected final static Logger logger = LogManager.getLogger(LicenseCheckHandler.class);
	
	public LicenseCheckHandler(CollectionServer server) {
		this.server = server;
	}

	@NotProguard
	@Override
	protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception {
//		String ip = remoteAddress.getAddress().getHostAddress();
//		System.out.println("连接�?:" + this.server.getConnectNum().get());

		if (this.server.getConnectNum().get() < 3) {
			logger.info("*****************当前连接�?:" + this.server.getConnectNum().get()+"   无需验证a");
			return true;
		}

		// 校验证书是否有效
		LicenseCheckModel serverInfos = ServerInfosUtil.getServerInfos();
		serverInfos.setLinkDeviceCount(this.server.getConnectNum().get());
		LicenseVerify licenseVerify = new LicenseVerify();
		boolean verifyResult = licenseVerify.verify(serverInfos);
		if(verifyResult) {
			logger.info("*****************当前连接�?:" + this.server.getConnectNum().get()+"   验证成功b");
		}else {
			logger.info("*****************当前连接�?:" + this.server.getConnectNum().get()+"   验证失败c");
		}

		// 这里获验�?
		return verifyResult;
	}
}
