package com.sosgps.wzt.directl.idirectl;

/**
 * <p>Title: GPS网关</p>
 *
 * <p>Description:抽象类TerminalQuery的适配器类 </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: www.sosgps.com</p>
 *
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class TerminalQueryAdaptor
    extends TerminalQuery {
  public TerminalQueryAdaptor() {
  }

  /**
   * 得到当前位置描述信息.
   *
   * @param v String
   * @return String
   * @todo Implement this com.sosgps.directl.idirectl.TerminalQuery method
   */
  public String getLocationInfo(String v) {
    return "";
  }

  /**
   *
   * @param v String
   * @param z String
   * @return String
   * @todo Implement this com.sosgps.directl.idirectl.TerminalQuery method
   */
  public String getOdograph(String seq, String v, String z) {
    return "";
  }

  /**
   *
   * @return String
   * @todo Implement this com.sosgps.directl.idirectl.TerminalQuery method
   */
  public String getStateParam(String seq) {
    return "";
  }

  /**
   * 查询车台参数信息
   * @param stateType String --- 查询类型
   * @return String
   */
  public String getCarParamInfor(String stateType) {
    return "";
  }


  /**
   *
   * @param num String
   * @return String
   * @todo Implement this com.sosgps.directl.idirectl.TerminalQuery method
   */
  public String getTensionInfo(String num) {
    return "";
  }

  /**
   *
   * @return String
   * @todo Implement this com.sosgps.directl.idirectl.TerminalQuery method
   */
  public String getVersionInfo() {
    return "";
  }
}
