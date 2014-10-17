package com.sosgps.wzt.directl.device.sjhx;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalSettingAdaptor;

public class SjhxSetting extends TerminalSettingAdaptor {
	public SjhxSetting(TerminalParam tparam) {
		this.terminalParam = tparam;
		this.terminalParam.setGPRSModal(true);
	}

	public String setSendIntervalTime(String seq, String deviceId,
			String intervalTime) {
		String frqHex = Tools.convertToHex(intervalTime, 4);
		frqHex = frqHex.substring(2) + frqHex.substring(0, 2);

		deviceId = parseDeviceId(deviceId);
		String dataHex = "@SJHX," + deviceId + "7E7E00007E7E030103" + frqHex;

		return dataHex;
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