package com.autonavi.directl.idirectl;

/**
 *
 * <p>Title: </p>
 * <p>Description:终端查询抽象类</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public abstract class TerminalQuery extends BaseDictate {
  public TerminalQuery() {
  }

  /**
   *查询电瓶电压信息
   */
  public abstract String getTensionInfo(String num);

  /**
   *得到行使里程
   */
  public abstract String getOdograph(String v, String z);

  /**
   *查询终端所有参数上传命令
   */
  public abstract String getStateParam(String seq);

  /**
   * 查询车台参数信息
   */
  public abstract String getCarParamInfor(String stateType);

  /**
   *查询版本信息
   */
  public abstract String getVersionInfo();

  /**
   *得到当前位置描述信息.
   */
  public abstract String getLocationInfo(String v);

}
