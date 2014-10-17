package com.sosgps.wzt.directl.device.dopod;

import org.apache.log4j.Logger;

 
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalSettingAdaptor;
 
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Beijing Mapabc Co., Ltd.</p>
 *
 * @author 终端产品部 黄山项目组
 * @version 1.0
 */
public class DopodTerminalSetting extends TerminalSettingAdaptor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(DopodTerminalSetting.class);

    public DopodTerminalSetting( TerminalParam
                                tparam) {
        this.terminalParam = tparam;
    }

    //SDGPS,设备号,2,中心IP,中心端口
    public String setIPPort(String serverIP, String serverPort,
                            String localPort) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SDGPS,");

        buffer.append(this.terminalParam.getSeriesNo());
        buffer.append(",2,");
        buffer.append(serverIP);
        buffer.append(",");
        buffer.append(serverPort);
        buffer.append("\r\n");

        return buffer.toString();
    }
    
    /**
	 * TerminalSetting.java: 发送间隔时间设置
	 * 
	 * @param intervalType
	 * 
	 * @param t
	 *            String 间隔时间
	 */
	public String setSendIntervalTime(String seq, String intervalType, String t) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SDGPS,");
		 
		buffer.append(this.terminalParam.getSeriesNo());
		if (t != null && t.trim().length() > 0) {
			 
				buffer.append(",9,");
				buffer.append(t);
			 
		}
		buffer.append("\r\n");
		logger.info("设置频率指令："+buffer.toString());
	
		return buffer.toString();
	}
	 

    /**
     * 设置拍照
     * SDGPS,设备号,7,时间间隔,条数
     * @param number String：像头编号
     * @param count String：拍照数：FF-循环拍，00-停止
     * @param interval String：连续拍照时间间隔,单位5 秒
     * @param isUpload String：
     * @return String
     */
    public String setTakePhoto(String number, String count,
                               String interval,
                               String isUpload) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("SDGPS,");

        buffer.append(this.terminalParam.getSeriesNo());
        buffer.append(",7,");
        buffer.append(interval);
        buffer.append(",");
        buffer.append(count);
        buffer.append("\r\n");

        return buffer.toString();
    }

    //设置手机采集时间段
  public String setInterval(String devicesn, String beginTime,
                             String endTime) {
//      SimpleDateFormat simpleDate = new SimpleDateFormat(
//              "yyyy-MM-dd HH:mm:ss");
//      Calendar car = Calendar.getInstance();
//      Date newDate = new Date(car.getTimeInMillis());
      String date = Tools.getDBCurDate();
      String cmd = "SDGPS," + devicesn + ",8," + date + "," + beginTime + "," +
                   endTime+"\r\n";
      return cmd;
  }



}
