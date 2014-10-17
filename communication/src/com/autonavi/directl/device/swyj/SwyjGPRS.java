package com.autonavi.directl.device.swyj;

import com.autonavi.directl.idirectl.Terminal;
import com.autonavi.directl.idirectl.Alarm;
import com.autonavi.directl.idirectl.Locator;
import com.autonavi.directl.idirectl.TerminalControl;
import com.autonavi.directl.idirectl.TerminalQuery;
import com.autonavi.directl.idirectl.TerminalSetting;

/**
 * <p>Title: GPSÍø¹Ø</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class SwyjGPRS extends Terminal {
  public SwyjGPRS() {
  }

  public static void main(String[] args) {
    SwyjGPRS swyjsm = new SwyjGPRS();
  }

  public String sentMsg(String content) {
    return "";
  }

  public Alarm createAlarm() {
    return null;
  }

  public Locator createLocator() {
    return null;
  }

  public TerminalControl createTerminalControl() {
    return null;
  }

  public TerminalQuery createTerminalQuery() {
    return null;
  }

  public TerminalSetting createTerminalSetting() {
    return null;
  }

  public boolean isSupport(String functionName) {
    return false;
  }

  public void showTerminalInfo() {
  }
}
