package com.autonavi.directl.pic;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: www.mapabc.com
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class Picture extends Hashtable {
	private int num; // 图象编号
	private int pakcNo; // 包号
	private int packcounts; // 本图象包总数
	private byte[] imgcontent; // 图象内容
	private String imgStrCont; // 图象内容
	private String localFileName; // 存储图象路径
	private String type; // 拍照触发条件
	private Timestamp timeStamp; // 照片上传时间
	private String deviceId; // 拍照的终端号码
	private float x;
	private float y;
	private byte[] imgBytes;
	private boolean isReq; //是否存在图片上传请求
 

	public synchronized void addImgCont(String packNo, ByteCont bc) {
		this.put(packNo, bc);

	}

	public synchronized void addImgContHex(String packNo, String bc) {
		if (packNo != null)
			this.put(packNo, bc);

	}

	/**
	 * @return the localFileName
	 */
	public String getLocalFileName() {
		return this.localFileName;
	}

	/**
	 * @return the timeStamp
	 */
	public Timestamp getTimeStamp() {
		return this.timeStamp;
	}

	/**
	 * @param localFileName
	 *            the localFileName to set
	 */
	public synchronized void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public synchronized void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return this.deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public synchronized void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Picture() {
	}

	public static void main(String[] args) {
		Picture picture = new Picture();
	}

	public InputStream getImgcontent() {

		ByteArrayOutputStream baos = null;
		ByteArrayInputStream bais = null;

		try {
			baos = new ByteArrayOutputStream();

			for (int i = 0; i < this.size(); i++) {
				// ByteCont bc = (ByteCont) instance.get((i + 1) + "");
				String bcs = (String) this.get((i + 1) + "");
				if (bcs != null) {
					byte[] bb = Tools.fromHexString(bcs);
					baos.write(bb);
				}
			}

			bais = new ByteArrayInputStream(baos.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().errorLog("出现异常", e);
		} finally {

			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}

			} catch (IOException ex1) {
				Log.getInstance().errorLog(ex1.getMessage(), ex1);
			}

		}

		return bais;
	}

	public int getNum() {
		return num;
	}

	public int getPackcounts() {
		return packcounts;
	}

	public String getImgStrCont() {
		return imgStrCont;
	}

	public int getPakcNo() {
		return pakcNo;
	}

	public String getType() {
		return type;
	}

	public synchronized void setImgcontent(byte[] imgcontent) {
		this.imgcontent = imgcontent;
	}

	public synchronized void setNum(int num) {
		this.num = num;
	}

	public synchronized void setPackcounts(int packcounts) {
		this.packcounts = packcounts;
	}

	public synchronized void setImgStrCont(String imgStrCont) {
		this.imgStrCont = imgStrCont;
	}

	public synchronized void setPakcNo(int pakcNo) {
		this.pakcNo = pakcNo;
	}

	public synchronized void setType(String type) {
		this.type = type;
	}

	// 数据包是否传输完毕
	public synchronized boolean isTraverOver() {
		boolean ret = false;
		if (this.packcounts == this.pakcNo) {
			ret = true;
		}
		return ret;
	}

	// 重置图片各参数
	public synchronized void reset() {
		this.clear();
		this.packcounts = 0;
		this.pakcNo = -1;
		this.type = null;
		this.x = 0f;
		this.y = 0f;
		this.timeStamp = null;
		this.localFileName = null;
		this.isReq=false;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return this.y;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public synchronized void setX(float x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public synchronized void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the imgBytes
	 */
	public byte[] getImgBytes() {
		return this.imgBytes;
	}

	/**
	 * @param imgBytes the imgBytes to set
	 */
	public void setImgBytes(byte[] imgBytes) {
		this.imgBytes = imgBytes;
	}


	/**
	 * @return the isReq
	 */
	public boolean isReq() {
		return this.isReq;
	}


	/**
	 * @param isReq the isReq to set
	 */
	public void setReq(boolean isReq) {
		this.isReq = isReq;
	}
}
