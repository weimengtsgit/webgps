/**
 * 
 */
package com.autonavi.lbsgateway.service.update;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.lbsgateway.GprsTcpThread;
 
 
public class RemoteUpdateList extends Hashtable {

	static RemoteUpdateList instance = null;
	
 
	public synchronized static RemoteUpdateList getInstance() {
		if (instance == null) {

			instance = new RemoteUpdateList();

		}
		return instance;
	}

	public synchronized void addRemoteProgram(String deviceid,
			UpdateBean updateProgram) {

		if (deviceid != null) {

			this.put(deviceid, updateProgram);

		}
	}

	public synchronized UpdateBean getRemoteUpdateProgram(String deviceid) {
		UpdateBean rup = null;

		if (deviceid != null) {
			rup = (UpdateBean) this.get(deviceid);
		}
		return rup;
	}

 
	public synchronized void remove(String key) {
		UpdateBean gpsClient = (UpdateBean) instance.get(key);
		if (gpsClient != null) {
			instance.remove(key);
		}
	}
	
 
 
	public synchronized void checkPackState() {

		Object obj = null;
		boolean flag = false;
		Iterator it = instance.keySet().iterator();
		 

		while (it.hasNext()) {
			obj = instance.get(it.next());
			
			if (obj instanceof UpdateBean) {
				UpdateBean bean = (UpdateBean) obj;
				
				bean.checkIsResponse();
				//Log.getInstance().outLog("====="+bean.toString());
  			
		}
			
	}

}

}