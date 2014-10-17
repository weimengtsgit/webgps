package com.autonavi.directl.device.huaqian;

import com.autonavi.directl.Log;
import com.autonavi.directl.idirectl.*;
 
 
import java.util.StringTokenizer;

public class HQ20TerminalControl extends TerminalControlAdaptor {
  public HQ20TerminalControl() {
  }

  public HQ20TerminalControl(com.autonavi.directl.idirectl.TerminalParam tparam) {
    this.terminalParam = tparam;
  }

  /**
   * 重新起动
   * @return
   */
  public String reset() {
    String tmp = "BA0";
    String cmd = HQ20Util.makeCommandStr(tmp, false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   * 断开油路
   * @param v：'1'表示锁定车辆油路；'0'表示恢复车辆油路；
   * @return
   */
  public String setOilState(String v) {
    String tmp = "BB" + v;
    String cmd = HQ20Util.makeCommandStr(tmp, false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   * 控制终端工作状态
   * @param v:'0'表示在ACC开的情况下，使终端重新启动；'1'表示重设置为出厂前设置并重新启动；'2'表示在ACC关的情况下，使终端进入省电模式;'3'表示唤醒GPS定位模块.
   * @return
   */
  public String setWorkState(String v) {
    String tmp = "BA" + v;
//    String cmd =makeCommandStr(tmp);
//   return this.sent(cmd);
    return null;

  }

  /**
   * 发布公共信息
   * @param msg
   * @return
   */
  public String sentPubMsg(String msg) {
    String codeMsg = HQ20Util.to64Code(msg);
    String tmp = "CA" + codeMsg;
    String cmd = HQ20Util.makeCommandStr(tmp, true, true);
    return cmd;//this.sent(cmd);

  }

  /**
   * 数字回传
   * @param ptype：是否是范围 数字回传,'1'非范围 数字回传;'2'范围 数字回传
   * @param xy
   * @param XY
   * @param msg
   * @return
   */
  public String replyNum(String ptype, EarthCoord xy, EarthCoord XY,
                         String msg) {
//    String tmp="CD";
//    if(ptype.trim().equalsIgnoreCase("1")){
//      tmp=tmp+ptype+"("+msg+")";
//    }else if(ptype.trim().equalsIgnoreCase("2")){
//      tmp=tmp+ptype+"("+xy.x+xy.y+XY.x+XY.y+")"+"("+msg+")";
//    }else{
//      return null;
//    }
//    String cmd =makeCommandStr(tmp);
//
//    return this.sent(cmd);
    return null;

  }

  /**
   * 要求进入GPRS模式
   * @param v：'0'表示退出GPRS工作状态；'1'表示进入GPRS工作状态；
   * @param pTime：'FFFF'表示终端持续GPRS状态；否则表示进入GPRS的持续时间；
   * @return
   */
  public String gointoGPRS(String v, String pTime) {
//    String tmp="BD"+v+pTime;
//    String cmd =makeCommandStr(tmp);
//  return  this.sent(cmd);
    return null;

  }

  /**
   * 某范围点名
   * @param xy
   * @param XY
   * @return
   */
  public String rangeCalling(EarthCoord xy, EarthCoord XY) {
    StringBuffer buf = new StringBuffer();
    buf.append("BM(");
    double maxLongitude = Double.parseDouble(xy.x);
    double maxLatitude = Double.parseDouble(xy.y);
    double minLongitude = Double.parseDouble(XY.x);
    double minLatitude = Double.parseDouble(XY.y);

    buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
    buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
    buf.append(HQ20Util.getLatLongString(minLatitude, 1));
    buf.append(HQ20Util.getLatLongString(minLongitude, 0));
    buf.append(")");
    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * 终端回拨电话
   * @param tel
   * @return
   */
  public String callBack(String tel) {
//    String tmp="BF"+tel;
//    String cmd =makeCommandStr(tmp);
// return   this.sent(cmd);
    return null;

  }

  /**
   给终端附件发送信息
   * @param affix:终端附件代码,如：显示平代码为04
   * @param protocol：协议
   * @param msg：发送的信息
   * @return
   */
  public String sentAffixMsg(String affix, String protocol, String msg) {
//    String tmp="BZ"+affix+protocol+msg;
//    String cmd =makeCommandStr(tmp);
//  return  this.sent(cmd);
    return null;

  }

  /**
   * 短信抢答
   * @param ptype String --- 是否是范围抢答,"0"代表否，"1"代表是
   * @param x String --- X1点经度
   * @param y String --- X1点纬度
   * @param X String --- X2点经度
   * @param Y String --- X2点纬度
   * @param msg String --- 抢答信息，多个抢答信息之间用","分隔
   * @return String
   */
  public String revertShortMessage(String ptype, String x, String y, String X, String Y, String msg) {
    EarthCoord xy = new EarthCoord(x, "", y, "");
    EarthCoord XY = new EarthCoord(X, "", Y, "");
    StringBuffer buf = new StringBuffer();
    buf.append("CB");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("1");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("2(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //增加抢答信息
    buf.append("(");
    StringTokenizer tokenizer = new StringTokenizer(msg, ",");
    int num = tokenizer.countTokens() > 4 ? 4 : tokenizer.countTokens(); //最多有四条信息
    StringBuffer msgBuf = new StringBuffer(); //用于存放格式化后的抢答信息
    for (int i = 1; i <= num; i++) {
      if (i > 1) {
        msgBuf.append("&");
      }
      String numStr = HQ20Util.extentString(Integer.toString(i), 2);
      msgBuf.append(numStr);
      msgBuf.append(tokenizer.nextToken());
    }
    buf.append(HQ20Util.to64Code(msgBuf.toString()));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, false);
    return cmd;//this.sent(cmd);

  }

  /**
   * 短信抢答
   *
   * @param v：是否是范围抢答,"0"代表否，"1"代表是
   * @param xy X1点经纬度
   * @param XY X2点经纬度
   * @param msg 抢答信息，多个抢答信息之间用","分隔
   * @return 控制指令
   */
  public String quickRevertShortMessage(String ptype, EarthCoord xy,
                                        EarthCoord XY, String msg) {
    StringBuffer buf = new StringBuffer();
    buf.append("CB");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("1");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("2(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //增加抢答信息
    buf.append("(");
    StringTokenizer tokenizer = new StringTokenizer(msg, ",");
    int num = tokenizer.countTokens() > 4 ? 4 : tokenizer.countTokens(); //最多有四条信息
    StringBuffer msgBuf = new StringBuffer(); //用于存放格式化后的抢答信息
    for (int i = 1; i <= num; i++) {
      if (i > 1) {
        msgBuf.append("&");
      }
      String numStr = HQ20Util.extentString(Integer.toString(i), 2);
      msgBuf.append(numStr);
      msgBuf.append(tokenizer.nextToken());
    }
    buf.append(HQ20Util.to64Code(msgBuf.toString()));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * 电话抢答
   * @param ptype String --- 是否是范围抢答,'3'非范围电话抢答;'4'范围电话抢答
   * @param x String --- X1点经度
   * @param y String --- X1点纬度
   * @param X String --- X2点经度
   * @param Y String --- X2点纬度
   * @param tel String --- 电话
   * @param msg String --- 调度信息
   * @return String
   */
  public String revertCalling(String ptype, String x, String y, String X, String Y,
                                   String tel, String msg) {
    EarthCoord xy = new EarthCoord(x, "", y, "");
    EarthCoord XY = new EarthCoord(X, "", Y, "");
    StringBuffer buf = new StringBuffer();
    buf.append("CB");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("3");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("4(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //增加抢答信息
    buf.append("(");
    buf.append(tel);
    buf.append(",");
    buf.append(HQ20Util.to64Code("p1" + msg));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   *电话抢答
   * @param ptype：是否是范围抢答,'0'非范围抢答;'1'范围抢答
   * @param xy X1点经纬度
   * @param XY X2点经纬度
   * @param tel 电话号码
   * @param msg 调度信息
   * @return 控制指令
   */
  public String quickRevertCalling(String ptype, EarthCoord xy, EarthCoord XY,
                                   String tel, String msg) {
    StringBuffer buf = new StringBuffer();
    buf.append("CB");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("3");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("4(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //增加抢答信息
    buf.append("(");
    buf.append(tel);
    buf.append(",");
    buf.append(HQ20Util.to64Code("p1" + msg));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * 短信息调度
   * @param ptype String
   * @param x String
   * @param y String
   * @param X String
   * @param Y String
   * @param tel String
   * @param msg String
   * @return String
   */
  public String shortMessageInforAttemper(String ptype, String x, String y, String X, String Y,
                                          String msg) {
    EarthCoord xy = new EarthCoord(x, "", y, "");
    EarthCoord XY = new EarthCoord(X, "", Y, "");
    StringBuffer buf = new StringBuffer();
    buf.append("CC");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("1");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("2(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }
    //增加抢答信息
    buf.append("(");
    buf.append(HQ20Util.to64Code("01" + msg));
    buf.append(")");
    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   * 短信调度
   * @param ptype：是否是范围调度,'0'非范围调度;'1'范围调度
   * @param xy
   * @param XY
   * @param msg
   * @return
   */
  public String shortMessageAttemper(String ptype, EarthCoord xy,
                                     EarthCoord XY, String msg) {
    StringBuffer buf = new StringBuffer();
    buf.append("CC");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("1");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("2(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }
    //增加抢答信息
    buf.append("(");
    buf.append(HQ20Util.to64Code("01" + msg));
    buf.append(")");
    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * 电话调度
   * @param ptype String：是否是范围调度,'3'非范围调度;'4'范围调度
   * @param x String: 左下坐标的经度
   * @param y String: 左下坐标的维度
   * @param X String: 右上坐标的经度
   * @param Y String: 右上坐标的维度
   * @param tel String: 电话
   * @param msg String：信息
   * @return String
   */
  public String telphoneInforAttemper(String ptype, String x, String y, String X, String Y,
                                      String tel, String msg) {
    EarthCoord xy = new EarthCoord(x, "", y, "");
    EarthCoord XY = new EarthCoord(X, "", Y, "");
    StringBuffer buf = new StringBuffer();
    buf.append("CC");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("3");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("4(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //增加抢答信息
    buf.append("(");
    buf.append(tel);
    buf.append(",");
    buf.append(HQ20Util.to64Code("01" + msg));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);

  }

  /**
   *电话调度
   * @param ptype：是否是范围调度,'3'非范围调度;'4'范围调度
   * @param xy
   * @param XY
   * @param tel
   * @param msg
   * @return
   */
  public String telphoneAttemper(String ptype, EarthCoord xy, EarthCoord XY,
                                 String tel, String msg) {
    StringBuffer buf = new StringBuffer();
    buf.append("CC");
    if (ptype.trim().equalsIgnoreCase("0")) {
      buf.append("3");
    } else if (ptype.trim().equalsIgnoreCase("1")) {
      buf.append("4(");
      double maxLongitude = Double.parseDouble(xy.x);
      double maxLatitude = Double.parseDouble(xy.y);
      double minLongitude = Double.parseDouble(XY.x);
      double minLatitude = Double.parseDouble(XY.y);

      buf.append(HQ20Util.getLatLongString(maxLatitude, 1));
      buf.append(HQ20Util.getLatLongString(maxLongitude, 0));
      buf.append(HQ20Util.getLatLongString(minLatitude, 1));
      buf.append(HQ20Util.getLatLongString(minLongitude, 0));
      buf.append(")");
    }

    //增加抢答信息
    buf.append("(");
    buf.append(tel);
    buf.append(",");
    buf.append(HQ20Util.to64Code("01" + msg));
    buf.append(")");

    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * 设置监听
   * @param state String 本处无用
   * @param tel String 电话号码
   * @return String 控制指令
   */
  public String setListening(String state, String tel) {
    StringBuffer buf = new StringBuffer();
    buf.append("BF");
    buf.append(tel);
    String cmd = HQ20Util.makeCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * 上传一张图片
   * added 2007-9-20 for HQ20
   * @return String
   */
  public String takePicture() {
    StringBuffer buf = new StringBuffer();
    buf.append("HA");
    buf.append( (char) 0x00);
    buf.append( (char) 0x00);
    String cmd = HQ20Util.makespCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * 按照指定的时间上传指定次数的图片
   * added 2007-9-20 for HQ20
   * @param frequence String 图象回传的频率，单位为秒
   * @param times String 图象回传的次数。 0代表停止，FFFF代表连续抓拍
   * @return String
   */
  public String takePictures(String frequence, String times) {
    StringBuffer buf = new StringBuffer();
    buf.append("HB");
    buf.append( (char) 0x00);
    buf.append( (char) 0x08);
    String hexFreq = Integer.toHexString(Integer.parseInt(frequence));
    buf.append(HQ20Util.extentString(hexFreq, 4));
    if (!times.equals("FFFF")) {
      String hexTimes = Integer.toHexString(Integer.parseInt(times));
      buf.append(HQ20Util.extentString(hexTimes, 4));
    } else {
      buf.append(times);
    }
    String cmd = HQ20Util.makespCommandStr(buf.toString(), false, true);
    return cmd;//this.sent(cmd);
  }

  /**
   * 中心回复。
   * @param simcard String
   * @return String
   */
  public String restoreInfor(String funNumAndkey) {
    StringBuffer buf = new StringBuffer();
    buf.append("*HQ20");
    buf.append("Y");
    buf.append(funNumAndkey);
    buf.append("#");
    String cmd = buf.toString();//HQ20Util.makeCommandStr(buf.toString(), false, false);
    Log.getInstance().outLog("普通中心回应华强终端指令："+cmd);
     return cmd;//this.sent(cmd);
  }
  
  /**
   * 中心特殊协议回复。
   * @param simcard String
   * @return String
   */
  public String restoreSPInfor(String funNumAndkey) {
    StringBuffer buf = new StringBuffer();
    buf.append("[HQ20");
    buf.append("Y");
    buf.append(funNumAndkey);
    buf.append("]");
    String cmd = buf.toString();
    Log.getInstance().outLog("特殊中心回应华强终端指令："+cmd);
    
     return cmd;//this.sent(cmd);
  }

  public static void main(String[] args) {
    HQ20TerminalControl tc = new HQ20TerminalControl();
    TerminalParam tp = new TerminalParam();
    tp.setGPRSModal(true);
    tp.setSimCard("13911238237");
    //tc.setTerminalParam(tp);
    EarthCoord xy = new EarthCoord("128.23404578", "E", "67.234923", "N");
    EarthCoord XY = new EarthCoord("56.957203", "E", "9.299395", "N");

    //测试普通短信
    String msg = tc.sentPubMsg(
        "深圳非典疫情控制的很好，至今无一例医务人员受到感染");
    System.out.println("普通短信指令：" + msg);

    //测试重启指令
    msg = tc.reset();
    System.out.println("重启指令：" + msg);

    //断开油路指令
    msg = tc.setOilState("0");
    System.out.println("恢复车辆油路指令：" + msg);
    msg = tc.setOilState("1");
    System.out.println("锁定车辆油路指令：" + msg);

    //监听
    msg = tc.setListening("", "13911238237");
    System.out.println("监听指令：" + msg);

    //短信抢答
    String msg111 = "来华强北拉客,来皇岗口岸卸货";
    msg = tc.quickRevertShortMessage("0", xy, XY, msg111);
    System.out.println("非范围短信抢答指令：" + msg);
    msg = tc.quickRevertShortMessage("1", xy, XY, msg111);
    System.out.println("范围短信抢答指令：" + msg);

    //范围点名
    msg = tc.rangeCalling(xy, XY);
    System.out.println("范围点名指令：" + msg);

    //电话抢答
    String tel = "13911238237";
    msg111 = "来华强北拉客";
    msg = tc.quickRevertCalling("0", xy, XY, tel, msg111);
    System.out.println("非范围电话抢答指令：" + msg);
    msg = tc.quickRevertCalling("1", xy, XY, tel, msg111);
    System.out.println("范围电话抢答指令：" + msg);

    //shortMessageAttemper
    //短信调度
    msg111 = "来华强北女人世界门口拉客";
    msg = tc.shortMessageAttemper("0", xy, XY, msg111);
    System.out.println("非范围短信调度指令：" + msg);
    msg = tc.shortMessageAttemper("1", xy, XY, msg111);
    System.out.println("范围短信调度指令：" + msg);

    //telphoneAttemper
    //电话调度
    msg = tc.telphoneAttemper("0", xy, XY, tel, msg111);
    System.out.println("非范围电话调度指令：" + msg);
    msg = tc.telphoneAttemper("1", xy, XY, tel, msg111);
    System.out.println("范围电话调度指令：" + msg);
  }

}
