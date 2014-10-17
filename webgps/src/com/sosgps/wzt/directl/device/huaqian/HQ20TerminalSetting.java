package com.sosgps.wzt.directl.device.huaqian;

import java.util.StringTokenizer;

import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalSettingAdaptor;

public class HQ20TerminalSetting extends TerminalSettingAdaptor {
	public HQ20TerminalSetting() {
	}

	public HQ20TerminalSetting(TerminalParam tparam) {
		this.terminalParam = tparam;
		String typeCode = tparam.getTypeCode();
		if (typeCode != null && typeCode.equalsIgnoreCase("GP-HQHAND-GPRS")){
			//���ݻ�ǿ�ֳ�GPS
			HQ20Util.setProtocolHead("*HQ23");
			
		}
	}

	/**
	 * �������ĺ���<br>
	 * centerID����Ϊ:<br>
	 * 1:���õ�һ�������ĺ���<br>
	 * 2:���õڶ��������ĺ���<br>
	 * 3:Ԥ��<br>
	 * 4:���������绰����<br>
	 * 5:����DTMF�����绰����<br>
	 * 6:������������绰����<br>
	 * 7:���ö���Ϣ�������ĺ���<br>
	 * 8:�����ն�SIM������<br>
	 * 
	 * @param centerID:����
	 * @param newNum�����ĺ���
	 * @return
	 */
	public String setCenterNum(String newNum) {
		String tmp = "AA(" + "1" + newNum + ")";
		String cmd = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmd);

	}

	/**
	 * ���õ�½���� UserKeyΪ�� 1�����õ�һ˾����½���� 2�����õڶ�˾����½����
	 * 
	 * @param UserKey
	 * @param pwd
	 * @return
	 */
	public String setPassWord(String UserKey, String pwd) {
		String tmp = "AB(" + UserKey + pwd + ")";
		String cmd = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmd);
	}

	/**
	 * ����ACC����ʡ��ģʽ��ʱ�䣬��λΪ��
	 * 
	 * @param time
	 * @return
	 */
	public String setAutoSaveIntervalTime(String time) {
		StringBuffer buf = new StringBuffer();
		buf.append("AH(2");
		String hexTime = Integer.toHexString(Integer.parseInt(time));
		buf.append(HQ20Util.extentString(hexTime, 4));
		buf.append(")");
		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), true, true);
		return this.sent(cmdStr);
	}

	/**
	 * ���ù���״̬ functionNum:<br>
	 * '1':������Ϣ�ϴ� '2':������Ϣ�ϴ� '3':DTMF�������ն�Ĭ��Ϊ�ر� '4':��������Ϣ�ϴ�
	 * '5':�����ն˵�ͨ�ŷ�ʽ��state=0��ʾΪGSM��state=1��ʾΪGPRS����
	 * '6':���ն˵�ͨѶ��ʽΪGPRSʱ����state=0��ʾΪ�������ö����ϴ���Ϣ��state=1state=0��ʾΪ��ֹ���ö����ϴ���Ϣ����
	 * '7':�Ƿ���Ҫ���п����ż�⡣ '8':�Ƿ���ҪACC����״̬�ϴ� '9':�ն�ʡ����Ƿ���Ҫ�ر�GPSR/CDMAͨѶģ��
	 * '1':������Ϣ�ϴ�
	 * 
	 * @param state:'0'��ʾ�رոù��ܣ�'1'��ʾ�򿪸ù���;
	 * @param functionNum:���ܱ���:
	 * @return
	 */
	public String setFuntionState(String state, String functionNum) {
		String tmp = "AI(" + functionNum + state + ")";
		String cmdStr = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmdStr);

	}

	/**
	 * ���ö˿ں�
	 * 
	 * @param serverIP:������IP����ʽΪ***.***.***.***
	 * @param serverPort:�������˿�
	 * @param localPort:���ض˿�
	 * @return
	 */
	public String setIPPort(String serverIP, String serverPort, String localPort) {
		StringBuffer buf = new StringBuffer();
		buf.append("DA");

		// ���Ӷ˿ں�
		StringTokenizer tokenizer = new StringTokenizer(serverIP, ".");
		if (tokenizer.countTokens() != 4) {
			return "";
		}
		for (int i = 0; i < 4; i++) {
			String hexIPPart = Integer.toHexString(Integer.parseInt(tokenizer
					.nextToken()));
			buf.append(HQ20Util.extentString(hexIPPart, 2));
		}

		// ���ӱ��ض˿�
		String hexLP = Integer.toHexString(Integer.parseInt(localPort));
		buf.append(HQ20Util.extentString(hexLP, 2));

		// ���ӷ������˿�
		String hexSP = Integer.toHexString(Integer.parseInt(serverPort));
		buf.append(HQ20Util.extentString(hexSP, 2));

		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), false, true);
		return this.sent(cmdStr);
	}

	/**
	 * ����ӦӪ��LOGO
	 * 
	 * @param v
	 * @return
	 */
	public String setSPLogo(String v) {
		StringBuffer buf = new StringBuffer();
		buf.append("AF");
		buf.append(HQ20Util.to64Code(v));
		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), false, true);
		return this.sent(cmdStr);
	}

	/**
	 * ����ͨѶ��ʽ
	 * 
	 * @param p��'0'GSM��ʽ��'1'GPRS��ʽ
	 * @return
	 */
	public String setMedium(String p) {
		String tmp = "AI(5" + p + ")";
		String cmdStr = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmdStr);
	}

	/**
	 * ��������GPRS���ʱ��
	 * 
	 * @param DDDD:�������ʱ��
	 *            ʱ�䵥λΪ�룬����С��10������3600��
	 * @return
	 */
	public String setMaxReLinkTime(String DDDD) {
		StringBuffer buf = new StringBuffer();
		buf.append("DC");
		String hexTime = Integer.toHexString(Integer.parseInt(DDDD));
		buf.append(HQ20Util.extentString(hexTime, 4));
		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), true, true);
		return this.sent(cmdStr);

	}

	/**
	 * �����ն��ϴ�����Ҫ���Ļظ�����Ŀ itemKey='1':��½ itemKey='2':���� itemKey='3':�پ�
	 * itemKey='4':����
	 * 
	 * @param itemKey
	 * @return
	 */
	public String setNeedRevertItem(String itemKey) {
		String tmp = "AC(" + itemKey + ")";
		String cmdStr = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmdStr);
	}

	/**
	 * �������ߴ���ʱλ�ûش���ʱ����
	 * 
	 * @param TTTT
	 * @return
	 */
	public String setOnlineRevertTime(String TTTT) {
		StringBuffer buf = new StringBuffer();
		buf.append("BK");
		String hexTime = Integer.toHexString(Integer.parseInt(TTTT));
		buf.append(HQ20Util.extentString(hexTime, 4));

		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), true, true);
		return this.sent(cmdStr);
	}

	/**
	 * ���ú���ģʽ
	 * 
	 * @param v1:'0'��ʾ����������,'1'��ʾ�ɲ���̶��绰,'2'��ֹ����
	 * @param v2:'0'��ʾ����������,'1'��ʾ�ɽ����̶��绰,'2'��ֹ����
	 * @param num:��ʾ�绰�������SIM���е�λ��
	 * @param tel:�绰����
	 * @return
	 */
	public String setCallLimitMode(String v1, String v2, String num, String tel) {
		StringBuffer buf = new StringBuffer();
		buf.append("AG");
		buf.append(v1);
		buf.append(v2);
		buf.append("(");
		buf.append(HQ20Util.extentString(num, 2));
		buf.append(tel);
		buf.append(")");
		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), true, true);
		return this.sent(cmdStr);
	}

	/**
	 * ��������
	 * 
	 * @param v1:'3'��ʾ�洢��ʷ����ʱ������ʮ�����ƣ���λΪ�֡�'4'��ʾ���ö��ű�ʶ
	 * @param v2
	 * @return
	 */
	public String setOther(String v1, String v2) {
		// String tmp = "AH"+"(" + v1+v2 + ")";
		// String cmdStr = makeCommandStr(tmp);
		// return this.sent(cmdStr);
		return "";

	}

	/**
	 * ����ѹ������
	 * 
	 * @param ee:��ʾѹ����λ��Ϣ��ʱ����
	 * @param tt:��ʾѹ����λ��Ϣ�Ĵ���
	 * @param nnnn:��ʾ��Ҫ����ѹ����λ��Ϣ�Ĵ�����'FFFF':��ʾ������ͣ���ͣ�'0000'��ʾֹͣ����;
	 * @return
	 */
	public String setCompressParam(String ee, String tt, String nnnn) {
		StringBuffer buf = new StringBuffer();
		buf.append("BJ");
		buf.append(HQ20Util.extentString(ee, 2));
		buf.append(HQ20Util.extentString(tt, 2));
		buf.append(HQ20Util.extentString(nnnn, 4));

		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), true, true);
		return this.sent(cmdStr);
	}

	/**
	 * ���ö�ʱ��λ�ļ��ʱ��ʹ�����
	 * 
	 * @param intervalTime
	 *            String --- ���ʱ��
	 * @param degree
	 *            String --- ����
	 * @return String
	 */
	public String setLocatorIntervalTimeAndDegree(String intervalTime,
			String degree) {
		// ʮ���Ƶ�ʮ������ת��
		int frequence = Integer.parseInt(intervalTime);
		int times = Integer.parseInt(degree);
		String hexFrequence = Integer.toHexString(frequence);
		hexFrequence = HQ20Util.extentString(hexFrequence, 4);
		String hexTimes = null;
		if (times != -1) {
			hexTimes = Integer.toHexString(times);
			hexTimes = HQ20Util.extentString(hexTimes, 4);
		} else { // �������ͣ���ʱ������
			hexTimes = "FFFF";
		}
		String tmp = "BI" + hexFrequence + hexTimes;
		String cmd = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmd);
	}

	/**
	 * �����ն�ID��
	 * 
	 * @param sim
	 *            String
	 * @return String
	 */
	public String setCarSIM(String sim) {
		StringBuffer buf = new StringBuffer();
		buf.append("AM");
		buf.append(sim);
		buf.append("10");
		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), false, true);
		return this.sent(cmdStr);
	}

	/**
	 * ���ñ�����ʷ����ʱ������
	 * 
	 * @param intervalTime
	 *            String
	 * @return String
	 */
	public String setStorageHisDatasIntervalTime(String intervalTime) {
		StringBuffer buf = new StringBuffer();
		int d = Integer.parseInt(intervalTime) / 10;
		String time = Integer.toHexString(d);
		buf.append("EG");
		buf.append(time);
		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), false, true);
		return this.sent(cmdStr);
	}

	/**
	 * TerminalSetting.java: ���ͼ��ʱ������
	 * 
	 * @param seq
	 * @param intervalType
	 * @param t:String
	 *            ���ʱ��
	 */
	public String setSendIntervalTime(String seq, String intervalType, String t) {
		//ʮ���Ƶ�ʮ������ת��
	    int frequence = Integer.parseInt(t);
	   // int times = Integer.parseInt(NNNN);
	    String hexFrequence = Integer.toHexString(frequence);
	    hexFrequence = HQ20Util.extentString(hexFrequence, 4).toUpperCase();
	    	    
	    String tmp = "BI" + hexFrequence + "FFFF";
	    String cmd = HQ20Util.makeCommandStr(tmp, true, true);
	    
//	    HQ20TerminalQuery query = new HQ20TerminalQuery();
//	    String requestLC = query.getOdograph(seq, "1", "0");//Ҫ��ش�ָ���и��������Ϣ
	    
	    return  cmd ;//+ requestLC;
	}
	
	public static void main(String[] agra){
		HQ20TerminalSetting set = new HQ20TerminalSetting();
		set.setSendIntervalTime(null, null, "30");
	}

}
