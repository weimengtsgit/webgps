package com.sosgps.wzt.directl.device.dopod;

import org.apache.log4j.Logger;

import com.sosgps.wzt.directl.idirectl.LocatorAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

 

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
 * Company: Beijing Mapabc Co., Ltd.
 * </p>
 * 
 * @author 终端产品部 黄山项目组
 * @version 1.0
 */
public class DopodLocator extends LocatorAdaptor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DopodLocator.class);

	public DopodLocator( TerminalParam tparam) {
		this.terminalParam = tparam;
	}

	/**
	 * 单次定位 SDGPS,设备号,4
	 * 
	 * @return String
	 */
	public String singleLocator() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SDGPS,");
		buffer.append(this.terminalParam.getSeriesNo());
		buffer.append(",4");
		buffer.append("\r\n");

		return buffer.toString();
	}

	/**
	 * 按时间间隔回传指定条数的信息 SDGPS,设备号,5/9,时间,次数
	 * 
	 * @param t
	 *            String
	 * @param count
	 *            String
	 * @return String
	 */
	public String multiLocator(String t, String count) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SDGPS,");
		int cnt = Integer.parseInt(count);
		buffer.append(this.terminalParam.getSeriesNo());
		if (t != null && t.trim().length() > 0) {
			if (cnt > 1) {
				buffer.append(",5,");
				buffer.append(t);
				buffer.append(",");
				buffer.append(count);
			} else {
				buffer.append(",9,");
				buffer.append(t);
			}
		}
		buffer.append("\r\n");
		logger.info("设置频率指令："+buffer.toString());
	
		return buffer.toString();

	}
}
