package com.autonavi.lbsgateway.service.udpphoto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TimerTask;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.bean.UdpPhoto;

public class UdpPhotoSave extends TimerTask {
	private static boolean isRunning = false;
	public static Hashtable<String, ArrayList<UdpPhoto>> updPhotoHt = new Hashtable<String, ArrayList<UdpPhoto>>();
	public static final String URL_SEPARATOR = "/";
	public static final String HTKEY_SEPARATOR = "__";
	private String filesRootDir = "../webapps/filemanage";
	private String parentDir = "udpPhoto";
	private String fileExtName = ".jpg";

	public UdpPhotoSave() {
		Log.getInstance().outLog("启动定时保存udp设备上传图片！");
	}

	public void run() {
		if (!isRunning) {// 防止与下一次任务重叠
			isRunning = true;
			long btime = System.currentTimeMillis();
			// 遍历照片hashtable,hashtable的一个entity存储了一个照片信息,一个照片信息是由一组图片帧数据组成
			Log.getInstance().outLog("待保存udp上传图片数: " + updPhotoHt.size());
			for (Iterator<String> it = updPhotoHt.keySet().iterator(); it.hasNext();) {
				try {
					String key = (String) it.next();
					ArrayList<UdpPhoto> value = updPhotoHt.get(key);
					// 遍历照片list,判断照片udp数据帧是否接收完整,如果有丢帧则补发数据，如果接收完整则保存到磁盘
					UdpPhoto udpPhoto0 = value.get(0);
					int photoPackets = udpPhoto0.getPhotoPackets();// 照片总帧数
					boolean receiveAllPhotoPacketFlag = false;// 照片帧完整接收标志位
					// 如果已收照片帧数等于照片总帧数,说明照片已接收完整,直接保存
					if (value.size() == photoPackets) {
						receiveAllPhotoPacketFlag = true;
					}
					StringBuffer photoData = new StringBuffer();
					for (int i = 0; i < value.size(); i++) {
						UdpPhoto udpPhoto = value.get(i);
						String dataStr = Tools.bytesToHexString(udpPhoto.getData());
//						Log.getInstance().outLog("=======图片数据=======: "+dataStr);
						photoData.append(dataStr);
					}
					// TODO
					if (receiveAllPhotoPacketFlag) {

					}
					String[] hashTableKey = key.split(HTKEY_SEPARATOR);
					if (hashTableKey.length == 2) {
						boolean saveFlag = saveToImgFile(
								photoData.toString().toUpperCase(),
								generateFilePath(hashTableKey[0]), hashTableKey[1]);
						if (saveFlag) {
							// 保存图片成功,从hashtable中移除
							it.remove();
							updPhotoHt.remove(key);
							Log.getInstance().outLog("图片保存成功,从hashtable中移除. key = " +key);
						}
					}
				} catch (Exception e) {
					Log.getInstance().errorLog("图片保存程序定时器异常", e);
					isRunning = false;
				}
			}
			long etime = System.currentTimeMillis();
			Log.getInstance().outLog("图片保存程序 执行花费时间：" + (etime - btime) / 1000 + "秒");
			isRunning = false;

		}
	}

	public boolean saveToImgFile(String src, String filePath, String fileName) {
		if (src == null || src.length() == 0) {
			return false;
		}
		try {
			Log.getInstance().outLog("图片保存路径: " + filePath+File.separator+fileName+fileExtName);
			File file = new File(filePath);
			if (file.exists() == false) {
				file.mkdirs();
			}
			File file1 = new File(filePath+File.separator+fileName+fileExtName);
			FileOutputStream out = new FileOutputStream(file1);
			byte[] bytes = src.getBytes();
			for (int i = 0; i < bytes.length; i += 2) {
				out.write(charToInt(bytes[i]) * 16 + charToInt(bytes[i + 1]));
			}
			out.close();
			Log.getInstance().outLog("图片保存成功");
			return true;
		} catch (Exception e) {
			Log.getInstance().errorLog("图片保存异常", e);
			return false;
		}
	}

	public int charToInt(byte ch) {
		int val = 0;
		if (ch >= 0x30 && ch <= 0x39) {
			val = ch - 0x30;
		} else if (ch >= 0x41 && ch <= 0x46) {
			val = ch - 0x41 + 10;
		}
		return val;
	}

	public String generateFilePath(String deviceId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String separator = File.separator;
		// String fileBaseName = FilenameUtils.getBaseName(fileName);
		// String fileExtName = FilenameUtils.getExtension(fileName);
		// fileExtName = fileExtName == null ? "" : "." + fileExtName;
		return new StringBuffer(filesRootDir).append(separator)
				.append(parentDir).append(separator)
				.append(calendar.get(Calendar.YEAR)).append(separator)
				.append(calendar.get(Calendar.MONTH) + 1).append(separator)
				.append(deviceId).toString();
	}

}
