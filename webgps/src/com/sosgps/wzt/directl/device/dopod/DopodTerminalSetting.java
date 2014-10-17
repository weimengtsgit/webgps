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
 * @author �ն˲�Ʒ�� ��ɽ��Ŀ��
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

    //SDGPS,�豸��,2,����IP,���Ķ˿�
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
	 * TerminalSetting.java: ���ͼ��ʱ������
	 * 
	 * @param intervalType
	 * 
	 * @param t
	 *            String ���ʱ��
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
		logger.info("����Ƶ��ָ�"+buffer.toString());
	
		return buffer.toString();
	}
	 

    /**
     * ��������
     * SDGPS,�豸��,7,ʱ����,����
     * @param number String����ͷ���
     * @param count String����������FF-ѭ���ģ�00-ֹͣ
     * @param interval String����������ʱ����,��λ5 ��
     * @param isUpload String��
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

    //�����ֻ��ɼ�ʱ���
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
