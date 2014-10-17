package com.autonavi.directl.idirectl;

import com.mapabc.geom.DPoint;

/**
 *
 * <p>Title: </p>
 * <p>Description: 报警功能抽象类</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public abstract class Alarm extends BaseDictate {
  /**
   *设置范围报警<br>
   * @param alarmType:0表示出区域报警，1表示进区域报警
   * @param xy,XY:经纬度
   *@return String<br>
   */
  public abstract String setRangeAlarm(String seq,String alarmType, EarthCoord xy,
                                       EarthCoord XY);

  /**
   * * 区域报警（可设定多个报警区域）
   * @param alarmType String --- 报警类型
   * @param rangeId String --- 区域号
   * @param x String --- 左上角经度
   * @param y String --- 左上角纬度
   * @param X String --- 右上角经度
   * @param Y String --- 右上角纬度
   * @return String
   */
  public abstract String setRangeAlarmById(String seq,String alarmType, String rangeId, String x, String y,
                                           String X, String Y);
  
  /**
   * 多边形区域报警设置
   * @param seq：指令序列号
   * @param alarmType：类型 0出 1进 2取消
   * @param rangeId：区域编号
   * @param startDate:区域生效的开始时间
   * @param endDate:区域生效的结束时间
   * @param points：顶点数组
   * @return
   */
  public abstract String setRangeAlarm(String seq,String alarmType, String rangeId, String startDate, String endDate,com.mapabc.geom.DPoint[] points);

  /**
   *开关范围报警
   * @param state:状态0关闭，状态1打开
   * @return
   */
  public abstract String setRangeAlarmState(String seq,String state);

  /**
   *设置速度报警,包括超速报警和低速报警<br>
   * @param speedType：1超过
   * @param speed:速度值
   * @param time:持续时间
   * @return
   */
  public abstract String setSpeedAlarm(String seq,String speedType, String speed,String time);
  /**
   *设置速度报警,包括超速报警和低速报警<br>
   * @param speedType：1超过、0低于
   * @param speed:速度值
   * @return
   */
  public abstract String setSpeedAlarm(String seq, String speedType, String speed);

  /**
   *开关速度报警
   * @param state:状态0关闭，状态1打开
   * @return
   */
  public abstract String setSpeedAlarmState(String seq,String state);

  /**
   *设置移动报警
   */
  public abstract String setMovingAlarm(String seq,String v);
  
  /**
   *设置偏航报警
   *@param type:1设置 0取消
   *@param distance:偏离的距离
   *@param points:路线点
   */
  public abstract String setLineAlarm(String seq,String type,String distance,com.mapabc.geom.DPoint[] points);

  /**
   *设置在定距离回传
   *@param xy:纬度偏差值
   */
  public abstract String setWarpAlarm(String seq,EarthCoord xy);

  /**
   *设置在定距离回传
   *@param distance:距离
   */
  public abstract String setWarpAlarm(String seq,String distance);

  /**
   * 设置系统在[跟踪持续时间]内按[间隔时间]持续发送定位信息
   * @return String
   */
  public abstract String setIntervalTimeLastDate(String seq);

  /**
   * 设置正方形区域报警，以当前位置为中心，以radio为半径的正方形
   * @param radius:半径
   * @return
   */
  public abstract String setSquareAlarm(String seq,String radius);

  /**
   *解除报警
   * @param v:解除、开启报警 0 :关闭所有报警 1：打开所有报警
   */
  public abstract String stopAlarm(String seq,String v);

  /**
   * 信息添加、修改
   * 添加、修改终端的报警信息内容，最多30条
   * @param contents String
   * @return String
   */
  public abstract String addOrModifyAlarmInfor(String seq,String contents);

  /**
   * 信息删除
   * @param inforNums String
   * @return String
   */
  public abstract String deleteInforAlarmInfor(String seq,String inforNums);
  
  /**
   * 设置自定义报警。
   * @param params --- 由于不同终端参数类型、个数不同，把需要参数拼成一个参数字符串，再做解析处理。
   * 例：*XX,YYYYYYYYYY,S19,HHMMSS,Ax,S,time,K # （上海永太）
         Ax：被定义的报警信息；S：报警触发方式；time：时间；K：自定义报警触发后是否触发S17自动监控；
         输入参数：params = Ax,S,time,K
   * @return
   */
  public abstract String setSelfDefineAlarm(String seq,String params);
  /**
   * 设置温度范围报警
   * @param up
   * @param down
   * @return
   */
  public abstract String setTemperatureAlarm(String seq,String up, String down);
  /**
   * 报警参数设置
   * @param seq：流水号
   * @param type：类型 区域报警 超速报警 偏航报警
   * @param timelen 判断达到报警条件并产生报警的时间间隔，单位秒
   * @param interval 两次报警之间的间隔，单位秒
   * @param times 在不解除报警情况下的报警次数，0无意义
   * @return
   */
  public abstract String setAlarmParam(String seq, String type, String timelen, String interval, String times);

  /**
   * 绘制圆形区域
   * @param seq 序列号
   * @param type 类型
   * @param x 经度
   * @param y 纬度
   * @param r 半径
   * @return
   */
  public abstract String setCicleArea(String seq, String type, String x, String y, String r);
}
