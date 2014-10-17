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
		Log.getInstance().outLog("������ʱ����udp�豸�ϴ�ͼƬ��");
	}

	public void run() {
		if (!isRunning) {// ��ֹ����һ�������ص�
			isRunning = true;
			long btime = System.currentTimeMillis();
			// ������Ƭhashtable,hashtable��һ��entity�洢��һ����Ƭ��Ϣ,һ����Ƭ��Ϣ����һ��ͼƬ֡�������
			Log.getInstance().outLog("������udp�ϴ�ͼƬ��: " + updPhotoHt.size());
			for (Iterator<String> it = updPhotoHt.keySet().iterator(); it.hasNext();) {
				try {
					String key = (String) it.next();
					ArrayList<UdpPhoto> value = updPhotoHt.get(key);
					// ������Ƭlist,�ж���Ƭudp����֡�Ƿ��������,����ж�֡�򲹷����ݣ�������������򱣴浽����
					UdpPhoto udpPhoto0 = value.get(0);
					int photoPackets = udpPhoto0.getPhotoPackets();// ��Ƭ��֡��
					boolean receiveAllPhotoPacketFlag = false;// ��Ƭ֡�������ձ�־λ
					// ���������Ƭ֡��������Ƭ��֡��,˵����Ƭ�ѽ�������,ֱ�ӱ���
					if (value.size() == photoPackets) {
						receiveAllPhotoPacketFlag = true;
					}
					StringBuffer photoData = new StringBuffer();
					for (int i = 0; i < value.size(); i++) {
						UdpPhoto udpPhoto = value.get(i);
						String dataStr = Tools.bytesToHexString(udpPhoto.getData());
//						Log.getInstance().outLog("=======ͼƬ����=======: "+dataStr);
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
							// ����ͼƬ�ɹ�,��hashtable���Ƴ�
							it.remove();
							updPhotoHt.remove(key);
							Log.getInstance().outLog("ͼƬ����ɹ�,��hashtable���Ƴ�. key = " +key);
						}
					}
				} catch (Exception e) {
					Log.getInstance().errorLog("ͼƬ�������ʱ���쳣", e);
					isRunning = false;
				}
			}
			long etime = System.currentTimeMillis();
			Log.getInstance().outLog("ͼƬ������� ִ�л���ʱ�䣺" + (etime - btime) / 1000 + "��");
			isRunning = false;

		}
	}

	public boolean saveToImgFile(String src, String filePath, String fileName) {
		if (src == null || src.length() == 0) {
			return false;
		}
		try {
			Log.getInstance().outLog("ͼƬ����·��: " + filePath+File.separator+fileName+fileExtName);
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
			Log.getInstance().outLog("ͼƬ����ɹ�");
			return true;
		} catch (Exception e) {
			Log.getInstance().errorLog("ͼƬ�����쳣", e);
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
