package com.autonavi.directl.pic;

import java.nio.ByteBuffer;
import java.sql.*;
import java.util.*;

import com.autonavi.directl.Log;

public class PicCache extends Hashtable {
	static PicCache instance = null;

	public static synchronized PicCache getInstance() {
		if (instance == null) {
			instance = new PicCache();
		}
		return instance;
	}

	public synchronized void addPicture(String deviceId, Picture picture) {

		if (null != deviceId) {
			if (instance.getPicture(deviceId) != null) {
				instance.remove(deviceId);
			}
			instance.put(deviceId, picture);
		}

	}

	public Picture getPicture(String deviceId) {
		Picture pic = null;
		if (deviceId != null)
			pic = (Picture) instance.get(deviceId);

		return pic;
	}
	//是否存在第一个请求包
	public  boolean isExistFirstReq(String deviceId){
 		Picture pic = instance.getPicture(deviceId);
		if (pic !=null && pic.isReq()){
			return true;
		}else{
			return false;
		}
	}

	public void removePicture(String deviceId) {

		if (deviceId != null)
			instance.remove(deviceId);
	}

}
