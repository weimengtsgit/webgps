package com.autonavi.directl.idirectl;

/**
 *
 * <p>Title: </p>
 * <p>Description: 定位功能抽象类</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public abstract class Locator extends BaseDictate {
  public Locator() {
  }

  /**
   * 单次定位
   * @return
   */
  public abstract String singleLocator(String seq);

  /**
   *连续定位<br>
   * 每隔t时间,发送一次位置,一共发送 count次
   * @param t:间隔时间
   * @param count:连续发送次数
   */
  public abstract String multiLocator(String seq,String t, String count);

  /**
   * 连续定位，每隔t时间,发送一次位置,一共发送 count次，发送完毕自动休眠
   * @param t String
   * @param count String
   * @return String
   */
  public abstract String autoSleepMultiLocator(String seq,String t, String count);

  /**
   * 设置连续定位开关
   * @param state String:0表示关闭，1表示开启
   * @return String
   */
  public abstract String setMultiLocatorState(String seq,String state);

  /**
   * 设置GPRS开关
   * @param state String:0表示关闭，1表示开启
   * @return String
   */
  public abstract String setGPRSState(String seq,String state);

  /**
   * 设置速度为零时是否需要上传定位数据
   * @param state String
   * @return String
   */
  public abstract String setLocatorBySpeedIsZero(String seq,String state);
  
  /**
   * 设置按距离定位
   * @param distance
   * @return
   */
  public abstract String setLocatorByDistance(String seq,String distance);
  /**
   * 设置空重车及任务车上传间隔
   * @param type
   * @param emptime
   * @param ztime
   * @param tasktime
   * @return
   */
  public abstract String setCarInterval(String seq,String type,String emptime, String ztime, String tasktime);
  /**
   * 黑匣子采集间隔
   * @param type
   * @param interval
   * @return
   */
  public abstract String setBlackInterval(String seq,String type,String interval);
  
  //黑匣子数据上传请求
  public abstract String setBlackQuest(String seq);


}
