package com.autonavi.directl.idirectl;


public abstract class BaseDictate {
  protected TerminalParam terminalParam =new TerminalParam();
  private String cmd;
  private byte[] byteArrayCmd;
  public BaseDictate() {
  }
  public void setTerminalParam(TerminalParam tparam) {
    this.terminalParam=tparam;
 }

  public void setByteArrayCmd(byte[] byteArrayCmd) {
    this.byteArrayCmd = byteArrayCmd;
  }

  public void setCmd(String cmd) {
    this.cmd = cmd;
  }

  protected String sent(String cmd)  {
//    if (cmd == null || this.terminalParam.getSimCard() == null || cmd.trim().length() == 0 ||
//        this.terminalParam.getSimCard().trim().length() == 0) {
//      return null;
//    }
//    if (!this.terminalParam.getGPRSModal()) {
//      String[] simCard= {this.terminalParam.getSimCard()};
//      mapabc.qxt.api.ResponseType rt = null;
//      try{
//        rt = mapabc.qxt.api.QXTSend.sendASCII(this.
//            terminalParam.getSmsUser(), this.terminalParam.getSmsPassword(), null, null, null,
//            simCard, cmd);
//        Log.getInstance().outLog("短信发送指令:企业：" + this.terminalParam.getSmsUser() +
//                                 " 密码:" + this.terminalParam.getSmsPassword() +
//                                 "," + "内容:" + cmd + " 发送状态:" + rt.getCode());
//      }catch(Exception e){
//        Log.getInstance().outLog("企信通异常：" + e.getMessage());
//        Log.getInstance().outLog("短信发送指令失败:企业：" + this.terminalParam.getSmsUser() +
//                                 " 密码:" + this.terminalParam.getSmsPassword() +
//                                 "," + "内容:" + cmd);
//      }
//       return (rt!=null&&rt.getCode()==0)?cmd:null;
//    }else {
//      return cmd;
//    }
	  
	  return null;
  }
  protected boolean sentShortMessage(String cmd){
//    String[] simCard= {this.terminalParam.getSimCard()};
//    mapabc.qxt.api.ResponseType rt=null;
//    try{
//      rt = mapabc.qxt.api.QXTSend.sendASCII(this.terminalParam.getSmsUser(),this.terminalParam.getSmsPassword(),null,null,null,simCard,cmd);
//    Log.getInstance().outLog("短信发送指令:企业："+this.terminalParam.getSmsUser()+" 密码:"+ this.terminalParam.getSmsPassword()+","+"内容:"+cmd+" 发送状态:"+rt.getCode());
//    }catch(Exception e){
//      Log.getInstance().outLog("企信通异常2：" + e.getMessage());
//      Log.getInstance().outLog("短信发送指令失败:企业：" + this.terminalParam.getSmsUser() +
//                                 " 密码:" + this.terminalParam.getSmsPassword() +
//                                 "," + "内容:" + cmd);
//
//    }
//    return (rt!=null&&rt.getCode()==0)?true:false;
	  return false;
  }

  public byte[] getByteArrayCmd() {
    return byteArrayCmd;
  }

  public String getCmd() {
    return cmd;
  }
public TerminalParam getTerminalParam() {
	return this.terminalParam;
}

}
