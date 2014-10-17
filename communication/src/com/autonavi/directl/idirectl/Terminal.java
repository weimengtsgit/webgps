package com.autonavi.directl.idirectl;
 
/**
 *
 * <p>Title: </p>
 * <p>Description: 终端抽象类</p>
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

 //发送消息接口,根据终端选择的通信方式，可以通过短信发送，也可以通过GPRS发送
public abstract String sentMsg(String content);

//protected String sentSortMessage(String cmd){
//  String[] simCard= {this.terminalParam.getSimCard()};
//  //mapabc.qxt.api.ResponseType rt = mapabc.qxt.api.QXTSend.sendGBK()
//  mapabc.qxt.api.ResponseType rt=mapabc.qxt.api.QXTSend.sendGBK(this.terminalParam.getSmsUser(),this.terminalParam.getSmsPassword(),null,null,null,simCard,cmd);
//  Log.getInstance().outLog("短信发送消息:企业："+this.terminalParam.getSmsUser()+" 密码:"+ this.terminalParam.getSmsPassword()+","+"内容:"+cmd+" 发送状态:"+rt.getCode());
//  return (rt.getCode()==0)?"true":"false";
//}
//protected String sentSortMessageAscii(String cmd){
//  String[] simCard= {this.terminalParam.getSimCard()};
//  mapabc.qxt.api.ResponseType rt=mapabc.qxt.api.QXTSend.sendASCII(this.terminalParam.getSmsUser(),this.terminalParam.getSmsPassword(),null,null,null,simCard,cmd);
//  Log.getInstance().outLog("短信发送消息:企业："+this.terminalParam.getSmsUser()+" 密码:"+ this.terminalParam.getSmsPassword()+","+"内容:"+cmd+" 发送状态:"+rt.getCode());
//   return (rt.getCode()==0)?"true":"false";
//}
protected String sentGPRSMessage(String cmd){
  return cmd;
}


  public abstract Alarm createAlarm(); //报警功能类

  public abstract Locator createLocator(); //定位功能类

  public abstract TerminalControl createTerminalControl(); //终端控制功能类

  public abstract TerminalQuery createTerminalQuery(); //终端查询功能类

  public abstract TerminalSetting createTerminalSetting(); //终端设置功能类

  public abstract boolean isSupport(String functionName); //是否支持某功能

  public abstract void showTerminalInfo(); //设备初始参数以及支持的功能信息
}
