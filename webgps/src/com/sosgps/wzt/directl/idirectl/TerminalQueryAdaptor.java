package com.sosgps.wzt.directl.idirectl;

/**
 * <p>Title: GPS����</p>
 *
 * <p>Description:������TerminalQuery���������� </p>
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
   * �õ���ǰλ��������Ϣ.
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
   * ��ѯ��̨������Ϣ
   * @param stateType String --- ��ѯ����
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
