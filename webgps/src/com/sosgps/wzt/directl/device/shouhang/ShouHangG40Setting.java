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
	 * ���ͼ��ʱ������
	 * 
	 * Э�飺#730#6#1#0000##
	 * 
	 * @param intervalType
	 * 
	 * @param t
	 *            String ���ʱ��
	 */
	@Override
	public String setSendIntervalTime(String seq, String intervalType, String intervalTime) {
		 String frqHex =Tools.convertToHex(intervalTime, 4);
		 String dataHex = "01"+frqHex+frqHex;
		 String ret = ShouHangUtil.crtCmdByte(this.terminalParam.getSeriesNo(), "01", "05", dataHex);
		 
		return  ret;
	}
	
	/**
	 * ����IP�˿�ָ�
	 * 
	 * Э�飺#803#�̶�IP��ַ#�˿ں�#����4λ##
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
	 * ���ý����ָ��
	 * 
	 * Э�飺#802#APN��ĸ������4-20λ#��¼�û�����ĸ������4-20λ#��¼������ĸ������4-20λ#�ն�����4λ##
	 * @param v
	 * @return
	 */
	@Override
	public String setAPN(String seq,String v){
		StringBuffer sb = new StringBuffer();
		
	 
		
		return sb.toString();
	}
	
	/**
	 * �����û�����
	 * 
	 * Э��:#801#��ĸ������4-20λ#����4λ##
	 * 
	 * @param UserKey���û�����
	 * @param pwd���û�����
	 */
	@Override
	public String setPassWord(String seq, String userKey, String pwd) {
		StringBuffer sb = new StringBuffer();
		
		 
		
		return sb.toString();
	}
	
	  /**
	   **�������ĺ���
	   *
	   *Э��:#ָ����(710)#���ĺ���4-20λ#����4λ##
	   *
	   *@param centerID:���Ĵ���
	   *@param newNum:���ĺ���
	   */
	  @Override
	  public String setCenterNum(String seq,String newNum){
		  StringBuffer sb = new StringBuffer();
		  
		   
		  
		  return sb.toString();
	  }
}
