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
//        Log.getInstance().outLog("���ŷ���ָ��:��ҵ��" + this.terminalParam.getSmsUser() +
//                                 " ����:" + this.terminalParam.getSmsPassword() +
//                                 "," + "����:" + cmd + " ����״̬:" + rt.getCode());
//      }catch(Exception e){
//        Log.getInstance().outLog("����ͨ�쳣��" + e.getMessage());
//        Log.getInstance().outLog("���ŷ���ָ��ʧ��:��ҵ��" + this.terminalParam.getSmsUser() +
//                                 " ����:" + this.terminalParam.getSmsPassword() +
//                                 "," + "����:" + cmd);
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
//    Log.getInstance().outLog("���ŷ���ָ��:��ҵ��"+this.terminalParam.getSmsUser()+" ����:"+ this.terminalParam.getSmsPassword()+","+"����:"+cmd+" ����״̬:"+rt.getCode());
//    }catch(Exception e){
//      Log.getInstance().outLog("����ͨ�쳣2��" + e.getMessage());
//      Log.getInstance().outLog("���ŷ���ָ��ʧ��:��ҵ��" + this.terminalParam.getSmsUser() +
//                                 " ����:" + this.terminalParam.getSmsPassword() +
//                                 "," + "����:" + cmd);
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
