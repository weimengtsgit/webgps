package com.sosgps.wzt.directl.device.sjhx;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalControlAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

public class SjhxControl extends TerminalControlAdaptor {
	String deviceid = "";

	public SjhxControl(TerminalParam tparam) {
		this.terminalParam = tparam;

		this.deviceid = tparam.getSeriesNo();
	}

	public SjhxControl() {
	}

	public String setListening(String seq, String state, String tel) {
		String dataHex = Tools.bytesToHexString(tel.getBytes());

		String ret = "";
		return ret;
	}

	public String setEletricityState(String seq, String state) {
		String ret = "";

		return ret;
	}

	public String setDoorState(String seq, String state) {
		String dataHex = "";
		if (state.equals("1"))
			dataHex = "01";
		else {
			dataHex = "02";
		}

		String ret = "";
		return ret;
	}

	public String setFlameout(String seq, String type, String state,
			String speed) {
		String dataHex = "";
		String deviceId = this.terminalParam.getSeriesNo();
		deviceId = parseDeviceId(deviceId);
		if (type.equals("1")) {
			dataHex = "@SJHX," + deviceId + "7E7E00007E7E03020200";
		} else if (type.equals("2")) {
			dataHex = "@SJHX," + deviceId + "7E7E00007E7E030212";
		} else {
			dataHex = "@SJHX," + deviceId + "7E7E00007E7E03020201";
		}
		return dataHex;
	}

	public static void main(String[] args) {
		SjhxControl sp = new SjhxControl();
		sp.SendMessage("1", "lingtu");
	}

	public String SendMessage(String seq, String msg) {
		return null;
	}

	public String takePictures(String seq, String size, String action, String no) {
		return null;
	}

	public String getPicture(String seq, String trackNo, String picId) {
		return null;
	}

	public static String bytesToHexString(byte[] bs) {
		String s = "";
		for (int i = 0; i < bs.length; ++i) {
			String tmp = Integer.toHexString(bs[i] & 0xFF);
			if (tmp.length() < 2) {
				tmp = "0" + tmp;
			}
			s = s + tmp;
		}
		return s;
	}

	public String camera(String seq) {
		return null;
	}

	public String parseDeviceId(String deviceId) {
		int count = deviceId.length() / 2;
		StringBuffer sb = new StringBuffer();
		for (int i = count; i > 0; --i) {
			int j = i * 2;
			String a1 = deviceId.substring(j - 2, j);
			sb.append(a1);
		}
		return sb.toString();
	}
}