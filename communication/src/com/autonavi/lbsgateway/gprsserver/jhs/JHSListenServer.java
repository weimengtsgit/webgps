package com.autonavi.lbsgateway.gprsserver.jhs;

import java.io.IOException;

import com.autonavi.directl.Config;

public class JHSListenServer extends Thread {
	public void run() {

		int lbsport = Integer.parseInt(com.autonavi.directl.Config
				.getInstance().getString("TCPPORT"));
		String lbsip = Config.getInstance().getString("IP");
		LbsClient lbsClient = new LbsClient("位置网关客户端", lbsip, lbsport);
		lbsClient.start();

		String jhsserver = Config.getInstance().getString("JHSSERVERIP");
		int jhsport = Integer.parseInt(Config.getInstance().getString(
				"JHSSERVERPORT"));
		JHSClient jhsClient = new JHSClient("交互式导航客户端", jhsserver, jhsport);
		jhsClient.setLbsClinet(lbsClient);
		jhsClient.start();

		try {

			int port2 = Integer.parseInt(Config.getInstance().getString(
					"JHSLISTENPORT"));
			JHSServer jhsServer = new JHSServer(port2, jhsClient);
			jhsServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
