/**
 * 
 */
package com.autonavi.lbsgateway.service.update;

import com.autonavi.directl.Config;

/**
 * @author shiguang.zhou
 *
 */
public class RemoteServer {
	
	public static void start(){
		String isUsed = Config.getInstance().getString("isUsedRemoteServer");
		if (isUsed == null || isUsed.equals("0")){
			return;
		}
		MoniterRemoteUpdate mru = new MoniterRemoteUpdate();
		mru.start();
	}

}
