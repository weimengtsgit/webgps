package com.sosgps.wzt.directl.device.huaqian;

import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalQueryAdaptor;

 


public class HQ20TerminalQuery extends TerminalQueryAdaptor {
//  protected String splProtocolHead = "[HQ20";
//	protected String splProtocolEnd = "]";
//  protected String protocolHead = "*HQ20";
//  protected String protocolEnd = "#";
//  protected String isNeedSave = "1";
//  protected String isNeedReply = "1";
  public HQ20TerminalQuery() {
  }

  public HQ20TerminalQuery( TerminalParam tparam) {
    this.terminalParam = tparam;
    String typeCode = tparam.getTypeCode();
	if (typeCode != null && typeCode.equalsIgnoreCase("GP-HQHAND-GPRS")){
		//���ݻ�ǿ�ֳ�GPS
		HQ20Util.setProtocolHead("*HQ23");
		
	}
  }

//  private String makeCommandStr(String cmdKey) {
//    String cmdStr = this.protocolHead +this.isNeedSave+this.isNeedReply + cmdKey +this.protocolEnd;
//    return cmdStr.replaceAll(" ","");
//  }
//  private String makeSplCommandStr(String cmdKey) {
//    String cmdStr = this.splProtocolHead +this.isNeedSave+this.isNeedReply  + cmdKey +this.splProtocolEnd;
//    return cmdStr.replaceAll(" ","");
//  }
  /**
   * ��ѯ��ƿ��ѹ��Ϣ --- B.G ��ѯ������ƿ��ѹ��Ϣ
   * @param num:���ϲ���
   * @return
   */
  public String getTensionInfo(String num) {
    String tmp = "BG";
    String cmdStr = HQ20Util.makeCommandStr(tmp, false, true);
    return this.sent(cmdStr);
  }

  /**
   *��ѯ�����Ϣ
   * @param v:'0'��ʾ�ϴ�����Ϣ�в�����������ݣ�'1'��ʾ�ϴ�����Ϣ��Ҫ����������ݣ�
   * @param z:'0'��ʾ�ն������Ϣ���㣻'1'��ʾ�ն�����Ų����㣻
   * @return
   */
  public String getOdograph(String seq, String v, String z) {
    String tmp = "BH" + v + z;
    String cmdStr = HQ20Util.makeCommandStr(tmp, true, true);
    return cmdStr;
  }

  /**
   * �ն����в����ϴ�����
   * @return
   */
  public String getStateParam() {
    StringBuffer buf = new StringBuffer();
    buf.append("AJ(");
    buf.append( (char) 0x00);
    buf.append( (char) 0x00);
    buf.append(")");
    String cmdStr = HQ20Util.makespCommandStr(buf.toString(), false, true);
    return this.sent(cmdStr);
  }

  public static void main(String[] args) {
    HQ20TerminalQuery tq = new HQ20TerminalQuery();
    TerminalParam tp = new TerminalParam();
    tp.setGPRSModal(true);
    tp.setSimCard("13911238237");
    tq.setTerminalParam(tp);

    //��̲�ѯ
   
 
  }

}
