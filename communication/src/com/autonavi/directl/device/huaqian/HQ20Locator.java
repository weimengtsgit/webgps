package com.autonavi.directl.device.huaqian;

import com.autonavi.directl.idirectl.*;

public class HQ20Locator extends LocatorAdaptor {
  /**
   * 构造函数
   */
  public HQ20Locator() {
  }

  /**
   * 构造函数
   * @param tparam TerminalParam
   */
  public HQ20Locator(com.autonavi.directl.idirectl.TerminalParam tparam) {
    this.terminalParam = tparam;
  }

  /**
   * B.5 不带范围的点名信息
   * 单次定位.先发送定位指令，然后到数据库中抓起
   * @return
   */
  public String singleLocator() {
    String tmp = "BE";
    String cmd = HQ20Util.makeCommandStr(tmp, false, true);
    return this.sent(cmd);
  }

  /**
   * B.9 设置定时回传时间间隔和次数
   * 设置连续定位
   * @param DDDD:发送时间间隔
   * @param NNNN：发送次数
   * @return
   */
  public String multiLocator(String DDDD, String NNNN) {
    //十进制到十六进制转换
    int frequence = Integer.parseInt(DDDD);
    int times = Integer.parseInt(NNNN);
    String hexFrequence = Integer.toHexString(frequence);
    hexFrequence = HQ20Util.extentString(hexFrequence, 4);
    String hexTimes = null;
    if (times != -1) {
      hexTimes = Integer.toHexString(times);
      hexTimes = HQ20Util.extentString(hexTimes, 4);
    } else { //连续发送，无时间限制
      hexTimes = "FFFF";
    }
    String tmp = "BI" + hexFrequence + hexTimes;
    String cmd = HQ20Util.makeCommandStr(tmp, false, true);
    return  cmd ;
    
  }

  /**
   * 设置连续定位开关
   * @param state String:0表示关闭，1表示开启
   * @return String
   */
  public String setMultiLocatorState(String state) {
    if (state.equals("1")) {
      //终端不支持开启功能，开启功能需要通过multiLocator接口指定具体的时间间隔。
      return "";
    } else if (state.equals("0")) {
      //用长时间间隔模拟关闭
      return this.multiLocator("65535", "0");
    } else {
      return "";
    }
  }

  public static void main(String[] args) {
    HQ20Locator HQ20Locator = new HQ20Locator();
    TerminalParam tp = new TerminalParam();
    tp.setGPRSModal(true);
    tp.setSimCard("13911238237");
    HQ20Locator.setTerminalParam(tp);

    //测试不带范围的点名信息
    String cmd = HQ20Locator.singleLocator();
    System.out.println(cmd);

    //测试重车时设定定时回传时间间隔和次数
    cmd = HQ20Locator.multiLocator("23", "34");
    System.out.println("multiLocator:" + cmd);
  }
}
