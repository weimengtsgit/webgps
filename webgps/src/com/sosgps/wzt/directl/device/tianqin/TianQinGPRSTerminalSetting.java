package com.sosgps.wzt.directl.device.tianqin;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.*;

/**
 * <p>
 * Title: GPS网关
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.sosgps.com
 * </p>
 * 
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class TianQinGPRSTerminalSetting extends TerminalSettingAdaptor {
	public TianQinGPRSTerminalSetting() {
	}

	private String head = "*HQ,";
	private String end = "#";
	private String gpssn;

	public TianQinGPRSTerminalSetting(TerminalParam param) {
		this.terminalParam = param;
		gpssn = terminalParam.getSeriesNo();
	}

	public static void main(String[] args) {
		TianQinGPRSTerminalSetting swyjsmterminalsetting = new TianQinGPRSTerminalSetting();
	}

	/**
	 * TerminalSetting.java: 发送间隔时间设置
	 * 
	 * @param intervalType
	 * 
	 * @param t String 间隔时间
	 */
	public String setSendIntervalTime(String seq, String intervalType, String t) {
		String ret = "";
		String hms = Tools.getCurHMS();
		//ret = head + gpssn + ",S17," + hms + "," + t + "#";
		//*TH,000,D1,130305,5,4#
		ret = head + gpssn + ",D1,"+hms+","+t+",1#";
		return ret;

	}
}
