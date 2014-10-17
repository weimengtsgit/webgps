package com.sosgps.wzt.directl.device.tianqin;

import com.sosgps.wzt.directl.idirectl.Terminal;
import com.sosgps.wzt.directl.idirectl.Alarm;
import com.sosgps.wzt.directl.idirectl.Locator;
import com.sosgps.wzt.directl.idirectl.TerminalControl;
import com.sosgps.wzt.directl.idirectl.TerminalQuery;
import com.sosgps.wzt.directl.idirectl.TerminalSetting;

/**
 * <p>Title: GPSÍø¹Ø</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.sosgps.com</p>
*HQor musicjiang@sohu.com
 * @version 1.0
 */
public class TianQinGPRS extends Terminal {
  public TianQinGPRS() {
  }

  public static void main(String[] args) {
    TianQinGPRS swyjsm = new TianQinGPRS();
  }

  public String sentMsg(String content) {
    return "";
  }

  public Alarm createAlarm() {
   return new TianQinGPRSAlarm(this.terminalParam);
  }

  public Locator createLocator() {
	  return new TianQinGPRSLocator(this.terminalParam);
  }

  public TerminalControl createTerminalControl() {
   return new TianQinGPRSTerminalControl(this.terminalParam);
  }

  public TerminalQuery createTerminalQuery() {
    return null;
  }

  public TerminalSetting createTerminalSetting() {
    return new TianQinGPRSTerminalSetting(this.terminalParam);
  }

  public boolean isSupport(String functionName) {
    return false;
  }

  public void showTerminalInfo() {
  }
}
