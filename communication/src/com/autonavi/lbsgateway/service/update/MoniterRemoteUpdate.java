/**
 * 
 */
package com.autonavi.lbsgateway.service.update;

import java.util.Date;
import java.util.Iterator;

import com.autonavi.directl.Log;


/**
 * @author shiguang.zhou
 * 
 */
public class MoniterRemoteUpdate extends Thread {
	private boolean isRunning;
    public MoniterRemoteUpdate(){
  
    }

	public synchronized void run() {
		 
		while (!isRunning) {
			try {
 				
				 RemoteUpdateList.getInstance().checkPackState();
				 this.sleep(1 * 1000);
				 
			} catch (Exception e) {
				e.printStackTrace();
				 
				isRunning = false;
			}
		}

	}
}
