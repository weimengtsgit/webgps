package com.autonavi.directl.idirectl;
 
/**
 *
 * <p>Title: </p>
 * <p>Description: �ն˳�����</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public abstract class Terminal {
  protected TerminalParam terminalParam =new TerminalParam();

  public Terminal() {
  }

public void setTerminalParam(TerminalParam tparam) {
    this.terminalParam=tparam;
 }
 public TerminalParam getTerminalParam(){
   return this.terminalParam;
 }

 //������Ϣ�ӿ�,�����ն�ѡ���ͨ�ŷ�ʽ������ͨ�����ŷ��ͣ�Ҳ����ͨ��GPRS����
public abstract String sentMsg(String content);

//protected String sentSortMessage(String cmd){
//  String[] simCard= {this.terminalParam.getSimCard()};
//  //mapabc.qxt.api.ResponseType rt = mapabc.qxt.api.QXTSend.sendGBK()
//  mapabc.qxt.api.ResponseType rt=mapabc.qxt.api.QXTSend.sendGBK(this.terminalParam.getSmsUser(),this.terminalParam.getSmsPassword(),null,null,null,simCard,cmd);
//  Log.getInstance().outLog("���ŷ�����Ϣ:��ҵ��"+this.terminalParam.getSmsUser()+" ����:"+ this.terminalParam.getSmsPassword()+","+"����:"+cmd+" ����״̬:"+rt.getCode());
//  return (rt.getCode()==0)?"true":"false";
//}
//protected String sentSortMessageAscii(String cmd){
//  String[] simCard= {this.terminalParam.getSimCard()};
//  mapabc.qxt.api.ResponseType rt=mapabc.qxt.api.QXTSend.sendASCII(this.terminalParam.getSmsUser(),this.terminalParam.getSmsPassword(),null,null,null,simCard,cmd);
//  Log.getInstance().outLog("���ŷ�����Ϣ:��ҵ��"+this.terminalParam.getSmsUser()+" ����:"+ this.terminalParam.getSmsPassword()+","+"����:"+cmd+" ����״̬:"+rt.getCode());
//   return (rt.getCode()==0)?"true":"false";
//}
protected String sentGPRSMessage(String cmd){
  return cmd;
}


  public abstract Alarm createAlarm(); //����������

  public abstract Locator createLocator(); //��λ������

  public abstract TerminalControl createTerminalControl(); //�ն˿��ƹ�����

  public abstract TerminalQuery createTerminalQuery(); //�ն˲�ѯ������

  public abstract TerminalSetting createTerminalSetting(); //�ն����ù�����

  public abstract boolean isSupport(String functionName); //�Ƿ�֧��ĳ����

  public abstract void showTerminalInfo(); //�豸��ʼ�����Լ�֧�ֵĹ�����Ϣ
}
