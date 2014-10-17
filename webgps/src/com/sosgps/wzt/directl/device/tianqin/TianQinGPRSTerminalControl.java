package com.sosgps.wzt.directl.device.tianqin;

import com.sosgps.wzt.directl.Tools;
import com.sosgps.wzt.directl.idirectl.TerminalControlAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

 
 

/**
 * <p>Title: GPS����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
*HQor musicjiang@sohu.com
 * @version 1.0
 */
public class TianQinGPRSTerminalControl extends TerminalControlAdaptor {
	private String head = "*HQ,";
	private String end = "#";
	private String gpssn;
  public TianQinGPRSTerminalControl() {
  }
  
  public TianQinGPRSTerminalControl(TerminalParam param) {
 this.terminalParam = param;
 gpssn = terminalParam.getSeriesNo();
  }

  public static void main(String[] args) {
    TianQinGPRSTerminalControl swyjsmterminalcontrol = new TianQinGPRSTerminalControl();
  }
  
  public String setFlameout(String seq, String type, String state,
			String speed) {
		String ret = "";
		if (type.equals("1")) {// ���͵�
			ret = head + gpssn + ",S20," + Tools.getCurHMS()
					+ ",1,3,10,3,5,5,3,5,3,5,3,5" + end;
		} else {// �ָ�
			ret = head + gpssn + ",S20," + Tools.getCurHMS() + ",1,0" + end;
		}
		return ret;
	}
	
  /**
   * @param seq:���к�
   * @param no: ����ͨ����
   */
  public  String takePictures(String seq,String size, String action, String no){
  	String ret = "";
  	 
  	//*TH,000,S39,130305,3,2,10,3#  ֪ͨ����ģ��2��640x480�߷ֱ��ʼ�ʱ���㡢�ش�����ʱMap_no=0
  	ret = head + gpssn +",S39,"+Tools.getCurHMS()+",3,1,0,3"+end;
  	
		return ret;
  }
  
}
