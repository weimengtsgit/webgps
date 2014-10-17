package com.sosgps.wzt.directl.device.shouhang;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalSettingAdaptor;

public class ShouHangG40Setting extends TerminalSettingAdaptor {
	  
	
	public ShouHangG40Setting(TerminalParam tparam) {
		this.terminalParam = tparam;
        this.terminalParam.setGPRSModal(true);
	}
	
	/**
	 * 发送间隔时间设置
	 * 
	 * 协议：#730#6#1#0000##
	 * 
	 * @param intervalType
	 * 
	 * @param t
	 *            String 间隔时间
	 */
	@Override
	public String setSendIntervalTime(String seq, String intervalType, String intervalTime) {
		 String frqHex =Tools.convertToHex(intervalTime, 4);
		 String dataHex = "01"+frqHex+frqHex;
		 String ret = ShouHangUtil.crtCmdByte(this.terminalParam.getSeriesNo(), "01", "05", dataHex);
		 
		return  ret;
	}
	
	/**
	 * 设置IP端口指令：
	 * 
	 * 协议：#803#固定IP地址#端口号#密码4位##
	 * 
	 * @param serverIP
	 * @param serverPort
	 * @param reDial
	 * @return
	 */
	@Override
	public String setIPPort(String seq,String serverIP, String serverPort,
            String localPort) {
		StringBuffer sb = new StringBuffer();
		
		 
		
		return sb.toString();
	}
	
	/**
	 * 设置接入点指令
	 * 
	 * 协议：#802#APN字母或数字4-20位#登录用户名字母或数字4-20位#登录密码字母或数字4-20位#终端密码4位##
	 * @param v
	 * @return
	 */
	@Override
	public String setAPN(String seq,String v){
		StringBuffer sb = new StringBuffer();
		
	 
		
		return sb.toString();
	}
	
	/**
	 * 设置用户密码
	 * 
	 * 协议:#801#字母或数字4-20位#密码4位##
	 * 
	 * @param UserKey：用户代号
	 * @param pwd：用户密码
	 */
	@Override
	public String setPassWord(String seq, String userKey, String pwd) {
		StringBuffer sb = new StringBuffer();
		
		 
		
		return sb.toString();
	}
	
	  /**
	   **设置中心号码
	   *
	   *协议:#指令码(710)#中心号码4-20位#密码4位##
	   *
	   *@param centerID:中心代号
	   *@param newNum:中心号码
	   */
	  @Override
	  public String setCenterNum(String seq,String newNum){
		  StringBuffer sb = new StringBuffer();
		  
		   
		  
		  return sb.toString();
	  }
}
