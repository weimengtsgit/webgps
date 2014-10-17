/**
 * @author shiguang.zhou
 *
 */
package com.autonavi.lbsgateway.bean;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * @author shiguang.zhou
 *
 */
public class BaJuTask implements Serializable{
	
	private long id;
	
	private String deviceId;
	
	private String taskContet;
	
	private String state;
	
	private String type;
	
	private String reply;
	
	private Date crtdate;
	
	private String crtman;
	
	private byte[] buf;

	 

	public  Date getCrtdate() {
		return crtdate;
	}

	public  void setCrtdate(Date crtdate) {
		this.crtdate = crtdate;
	}

	public  String getCrtman() {
		return crtman;
	}

	public  void setCrtman(String crtman) {
		this.crtman = crtman;
	}

	public  String getDeviceId() {
		return deviceId;
	}

	public  void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public  long getId() {
		return id;
	}

	public  void setId(long id) {
		this.id = id;
	}

	public  String getReply() {
		return reply;
	}

	public  void setReply(String reply) {
		this.reply = reply;
	}

	public  String getState() {
		return state;
	}

	public  void setState(String state) {
		this.state = state;
	}

	public  String getTaskContet() {
		return taskContet;
	}

	public  void setTaskContet(String taskContet) {
		this.taskContet = taskContet;
	}

	public  String getType() {
		return type;
	}

	public  void setType(String type) {
		this.type = type;
	}

	public synchronized byte[] getBuf() {
		return buf;
	}

	public synchronized void setBuf(byte[] buf) {
		this.buf = buf;
	}
	

}
