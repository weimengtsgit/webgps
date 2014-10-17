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
			//兼容华强手持GPS
			HQ20Util.setProtocolHead("*HQ23");
			
		}
	}

	/**
	 * 设置中心号码<br>
	 * centerID可以为:<br>
	 * 1:设置第一服务中心号码<br>
	 * 2:设置第二服务中心号码<br>
	 * 3:预留<br>
	 * 4:设置求助电话号码<br>
	 * 5:设置DTMF报警电话号码<br>
	 * 6:设置整机重起电话号码<br>
	 * 7:设置短信息服务中心号码<br>
	 * 8:设置终端SIM卡号码<br>
	 * 
	 * @param centerID:代号
	 * @param newNum：中心号码
	 * @return
	 */
	public String setCenterNum(String newNum) {
		String tmp = "AA(" + "1" + newNum + ")";
		String cmd = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmd);

	}

	/**
	 * 设置登陆密码 UserKey为： 1：设置第一司机登陆密码 2：设置第二司机登陆密码
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
	 * 设置ACC进入省电模式的时间，单位为秒
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
	 * 设置功能状态 functionNum:<br>
	 * '1':登入信息上传 '2':脱网信息上传 '3':DTMF报警，终端默认为关闭 '4':传感器信息上传
	 * '5':设置终端的通信方式（state=0表示为GSM，state=1表示为GPRS，）
	 * '6':当终端的通讯方式为GPRS时，（state=0表示为允许利用短信上传信息，state=1state=0表示为禁止利用短信上传信息）；
	 * '7':是否需要进行开关门检测。 '8':是否需要ACC开关状态上传 '9':终端省电后是否需要关闭GPSR/CDMA通讯模块
	 * '1':登入信息上传
	 * 
	 * @param state:'0'表示关闭该功能；'1'表示打开该功能;
	 * @param functionNum:功能编码:
	 * @return
	 */
	public String setFuntionState(String state, String functionNum) {
		String tmp = "AI(" + functionNum + state + ")";
		String cmdStr = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmdStr);

	}

	/**
	 * 设置端口号
	 * 
	 * @param serverIP:服务器IP，格式为***.***.***.***
	 * @param serverPort:服务器端口
	 * @param localPort:本地端口
	 * @return
	 */
	public String setIPPort(String serverIP, String serverPort, String localPort) {
		StringBuffer buf = new StringBuffer();
		buf.append("DA");

		// 增加端口号
		StringTokenizer tokenizer = new StringTokenizer(serverIP, ".");
		if (tokenizer.countTokens() != 4) {
			return "";
		}
		for (int i = 0; i < 4; i++) {
			String hexIPPart = Integer.toHexString(Integer.parseInt(tokenizer
					.nextToken()));
			buf.append(HQ20Util.extentString(hexIPPart, 2));
		}

		// 增加本地端口
		String hexLP = Integer.toHexString(Integer.parseInt(localPort));
		buf.append(HQ20Util.extentString(hexLP, 2));

		// 增加服务器端口
		String hexSP = Integer.toHexString(Integer.parseInt(serverPort));
		buf.append(HQ20Util.extentString(hexSP, 2));

		String cmdStr = HQ20Util.makeCommandStr(buf.toString(), false, true);
		return this.sent(cmdStr);
	}

	/**
	 * 设置应营商LOGO
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
	 * 设置通讯方式
	 * 
	 * @param p：'0'GSM方式，'1'GPRS方式
	 * @return
	 */
	public String setMedium(String p) {
		String tmp = "AI(5" + p + ")";
		String cmdStr = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmdStr);
	}

	/**
	 * 设置连接GPRS最大时间
	 * 
	 * @param DDDD:最大连接时间
	 *            时间单位为秒，不能小于10，大于3600。
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
	 * 设置终端上传后需要中心回复的项目 itemKey='1':登陆 itemKey='2':脱网 itemKey='3':劫警
	 * itemKey='4':盗警
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
	 * 设置在线待命时位置回传的时间间隔
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
	 * 设置呼叫模式
	 * 
	 * @param v1:'0'表示拨打无限制,'1'表示可拨打固定电话,'2'禁止拨打
	 * @param v2:'0'表示接听无限制,'1'表示可接听固定电话,'2'禁止接听
	 * @param num:表示电话号码存在SIM卡中的位置
	 * @param tel:电话号码
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
	 * 设置其他
	 * 
	 * @param v1:'3'表示存储历史数据时间间隔，十六进制，单位为分。'4'表示设置短信标识
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
	 * 设置压缩参数
	 * 
	 * @param ee:表示压缩定位信息的时间间隔
	 * @param tt:表示压缩定位信息的次数
	 * @param nnnn:表示需要发送压缩定位信息的次数；'FFFF':表示连续不停发送；'0000'表示停止发送;
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
	 * 设置定时定位的间隔时间和次数。
	 * 
	 * @param intervalTime
	 *            String --- 间隔时间
	 * @param degree
	 *            String --- 次数
	 * @return String
	 */
	public String setLocatorIntervalTimeAndDegree(String intervalTime,
			String degree) {
		// 十进制到十六进制转换
		int frequence = Integer.parseInt(intervalTime);
		int times = Integer.parseInt(degree);
		String hexFrequence = Integer.toHexString(frequence);
		hexFrequence = HQ20Util.extentString(hexFrequence, 4);
		String hexTimes = null;
		if (times != -1) {
			hexTimes = Integer.toHexString(times);
			hexTimes = HQ20Util.extentString(hexTimes, 4);
		} else { // 连续发送，无时间限制
			hexTimes = "FFFF";
		}
		String tmp = "BI" + hexFrequence + hexTimes;
		String cmd = HQ20Util.makeCommandStr(tmp, true, true);
		return this.sent(cmd);
	}

	/**
	 * 设置终端ID。
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
	 * 设置保存历史数据时间间隔。
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
	 * TerminalSetting.java: 发送间隔时间设置
	 * 
	 * @param seq
	 * @param intervalType
	 * @param t:String
	 *            间隔时间
	 */
	public String setSendIntervalTime(String seq, String intervalType, String t) {
		//十进制到十六进制转换
	    int frequence = Integer.parseInt(t);
	   // int times = Integer.parseInt(NNNN);
	    String hexFrequence = Integer.toHexString(frequence);
	    hexFrequence = HQ20Util.extentString(hexFrequence, 4).toUpperCase();
	    	    
	    String tmp = "BI" + hexFrequence + "FFFF";
	    String cmd = HQ20Util.makeCommandStr(tmp, true, true);
	    
//	    HQ20TerminalQuery query = new HQ20TerminalQuery();
//	    String requestLC = query.getOdograph(seq, "1", "0");//要求回传指令中附带里程信息
	    
	    return  cmd ;//+ requestLC;
	}
	
	public static void main(String[] agra){
		HQ20TerminalSetting set = new HQ20TerminalSetting();
		set.setSendIntervalTime(null, null, "30");
	}

}
