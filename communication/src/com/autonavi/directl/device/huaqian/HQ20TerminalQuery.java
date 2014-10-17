package com.autonavi.directl.device.huaqian;

import com.autonavi.directl.idirectl.*;

public class HQ20TerminalQuery extends TerminalQueryAdaptor {
//  protected String splProtocolHead = "[HQ20";
//protected String splProtocolEnd = "]";
//  protected String protocolHead = "*HQ20";
//  protected String protocolEnd = "#";
//  protected String isNeedSave = "1";
//  protected String isNeedReply = "1";
  public HQ20TerminalQuery() {
  }

  public HQ20TerminalQuery(com.autonavi.directl.idirectl.TerminalParam tparam) {
    this.terminalParam = tparam;
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
   * 查询电瓶电压信息 --- B.G 查询车辆电瓶电压信息
   * @param num:作废不用
   * @return
   */
  public String getTensionInfo(String num) {
    String tmp = "BG";
    String cmdStr = HQ20Util.makeCommandStr(tmp, false, true);
    return this.sent(cmdStr);
  }

  /**
   *查询里程信息
   * @param v:'0'表示上传的信息中不附加里程数据；'1'表示上传的信息中要附加里程数据；
   * @param z:'0'表示终端里程信息清零；'1'表示终端里程信不清零；
   * @return
   */
  public String getOdograph(String v, String z) {
    String tmp = "BH" + v + z;
    String cmdStr = HQ20Util.makeCommandStr(tmp, false, false);
    return this.sent(cmdStr);
  }

  /**
   * 终端所有参数上传命令
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

    //里程查询
    String cmd = tq.getOdograph("0", "0");
    System.out.println("里程查询指令：" + cmd);

    //电压
    cmd = tq.getTensionInfo("0");
    System.out.println("电压查询指令：" + cmd);

    //查询参数getStateParam()
    cmd = tq.getStateParam();
    System.out.println("查询终端参数指令：" + cmd);

  }

}
