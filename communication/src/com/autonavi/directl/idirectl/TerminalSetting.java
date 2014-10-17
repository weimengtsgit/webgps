package com.autonavi.directl.idirectl;
/**
 *
 * <p>Title: </p>
 * <p>Description:终端设置抽象类</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public abstract class TerminalSetting extends BaseDictate{
  public TerminalSetting() {
  }
  /**
   **设置中心号码
   *@param centerID:中心代号
   *@param newNum:中心号码
   */
  public abstract String setCenterNum(String seq,String newNum);

  /**
   *设置用户密码
   *@param UserKey：用户代号
   *@param pwd：用户密码
   */
  public abstract String setPassWord(String seq,String UserKey, String pwd);

  /**
   *设置是否进入省电模式状态
   */
  public abstract String startSaveMode(String seq,String v);

  /**
   *设置存储历史数据时间间隔
   */
  public abstract String setAutoSaveIntervalTime(String seq,String time);

  /**
   *设置通话限制功能
   */
  public abstract String setCallLimitMode(String seq,String[] nums);

  /**
   *设置功能开关
   */
  public abstract String setFuntionState(String seq,String state,String functionNum);

  /**
   *更改IP，端口
   */
  public abstract String setIPPort(String seq,String serverIP, String serverPort,
                                   String localPort);


  /**
   * 修改终端密码
   * @param newPwd1
   * @param newPwd2
   * @return
   */
  public abstract String changeTerminalPwd(String seq,String newPwd1, String newPwd2);

  /**
   * 更改终端模式
   * @param pMode:0:短信模式 1:GPRS模式
   * @return
   */
  public abstract String changeMode(String seq,String pMode);

  /**
   *初始化参数为默认参数
   */
  public abstract String initDefaultParam(String seq,String v);

  /**
   *设置时差
   */
  public abstract String setTimeDistance(String seq,String time);
  /**
   *设置网络运营尚LOGO
   */
  public abstract String setSPLogo(String seq,String v);

  /**
   *设置回传压缩参数
   * @param e:时间间隔
   * @param t:压缩定位信息的次数
   * @param nnnn:需要发送压缩定位信息的次数
   */
  public abstract String setCompressParam(String seq,String ee, String tt, String nnnn);
  /**
   * 设置回传压缩参数
   * @param t:时间，多久压缩一次数据然后回传
   * @return
   */
  public abstract String setCompressParam(String seq,String t);
  /**
   *设置回报媒介
   * @param p:媒介l类型,短信、GSM modem
   */
  public abstract String setMedium(String seq,String p);

  /**
   *设置拨号
   */
  public abstract String setDialParam(String seq,String dialUser, String dialNum,
                                      String dialPass);

  /**
   *设置拨号APN
   */
  public abstract String setAPN(String seq,String v);

  /**
   *设置重新建立GPRS连结的最大时间间隔
   */
  public abstract String setMaxReLinkTime(String seq,String t);

  /**
   *设置终端上传后需要中心回复的项目
   * @param itemKey:项目代码
   */
  public abstract String setNeedRevertItem(String seq,String itemKey);

  /**
   *设定特定短信息菜单
   */
  public abstract String setSMMenu(String seq,String v);

  /**
   *设置在线待命时位置回传的时间间隔
   */
  public abstract String setOnlineRevertTime(String seq,String tCount);
  /**
   * 设置车台ID
   * @param carId
   * @return
   */
  public abstract String setCarSIM(String seq,String sim);

  /**
  *其他设置
  */
 public abstract String setOther(String seq,String v1, String v2);

 /**
  *设置命令有效期 interval：分钟
  */
 public abstract String setCMDValidTime(String seq,String interval);

 /**
  *轨迹存储设置 type：模式 0 禁止 1 允许 interval：时间间隔秒
  */
 public abstract String setSaveContrail(String seq,String type, String interval);

 /**
  *设置预制文字信息 no：信息编号 msg：信息内容
  */
 public abstract String setPrefabricateMsg(String seq,String no, String msg);

 /**
  *设置预制预拨电话 type：预制类型 0 预制电话 1 预制电话长度 ；phones：电话或电话长度
  */
 public abstract String setPrefabricatePhone(String seq,String type,
                                             String phonesOrLength);
 /**
   TerminalSetting.java:
   * 发送间隔时间设置
   * @param intervalType String 间隔类型
   * @param intervalTime String 间隔时间
   */
  public abstract String setSendIntervalTime(String seq,String intervalType, String intervalTime);

  /**
   * 限拨电话信息
   * @param telNums String --- 电话间用“，”分开。
   * @return String
   */
  public abstract String setRestrictCallTel(String seq,String telNums);

  /**
   * 限接电话信息
   * @param telNums String --- 电话间用“，”分开。
   * @return String
   */
  public abstract String setRestrictPickUpTel(String seq,String telNums);

  /**
   * 开启、关闭电话限制功能
   * @param data String
   * @return String
   */
  public abstract String openOrCloseTelRestrict(String seq,String data);

  /**
   * 服务器回复包格式
   * @param dataArray byte[]
   * @return String
   */
  public abstract String restorePackFormatByServer(byte[] dataArray);
  
  /**
   * 设置自动监控命令
   * @param status
   * @return
   */
  public abstract String setAutoInspect(String seq,String status);
  /**
   * 设置超时时间间隔
   * @param type
   * @param t
   * @return
   */
  public abstract 	String setExtendTime(String seq,String type, String t);
  /**
   * 设置温度回传间隔
   * @param time
   * @return
   */
  public abstract String setTemptertureInterval(String seq,String time);
  /**
   * 油量异常变化阈值：单位为升。
   * 当油量在短时间内减少超过该阈值时报警。
   * 默认总容积的 15%。该值为0则不检测油量报警。持续时间：单位为s。
   * 在该时间段油量发生异常变化则报警。
   * @param type
   * @param value：油阀值
   * @param time：持续时间
   * @return
   */
  public abstract String setOilDoorValue(String seq,String type,String value, String time);
  
  public abstract String setTakePhoto(String seq,String number, String count, String interval,String isUpload);

}
