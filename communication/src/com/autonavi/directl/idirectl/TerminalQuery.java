package com.autonavi.directl.idirectl;

/**
 *
 * <p>Title: </p>
 * <p>Description:�ն˲�ѯ������</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public abstract class TerminalQuery extends BaseDictate {
  public TerminalQuery() {
  }

  /**
   *��ѯ��ƿ��ѹ��Ϣ
   */
  public abstract String getTensionInfo(String num);

  /**
   *�õ���ʹ���
   */
  public abstract String getOdograph(String v, String z);

  /**
   *��ѯ�ն����в����ϴ�����
   */
  public abstract String getStateParam(String seq);

  /**
   * ��ѯ��̨������Ϣ
   */
  public abstract String getCarParamInfor(String stateType);

  /**
   *��ѯ�汾��Ϣ
   */
  public abstract String getVersionInfo();

  /**
   *�õ���ǰλ��������Ϣ.
   */
  public abstract String getLocationInfo(String v);

}
