package com.sosgps.wzt.directl.device.tianqin;

import com.sosgps.wzt.directl.idirectl.*;

/**
 * <p>Title: GPSÍø¹Ø</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.sosgps.com</p>
*HQor musicjiang@sohu.com
 * @version 1.0
 */
public class TianQinGPRSLocator extends LocatorAdaptor {
  public TianQinGPRSLocator() {
  }
  
  public TianQinGPRSLocator(TerminalParam param) {
this.terminalParam = param;
  }

  public static void main(String[] args) {
    TianQinGPRSLocator swyjsmlocator = new TianQinGPRSLocator();
  }
}
