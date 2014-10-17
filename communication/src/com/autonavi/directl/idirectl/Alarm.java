package com.autonavi.directl.idirectl;

import com.mapabc.geom.DPoint;

/**
 *
 * <p>Title: </p>
 * <p>Description: �������ܳ�����</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public abstract class Alarm extends BaseDictate {
  /**
   *���÷�Χ����<br>
   * @param alarmType:0��ʾ�����򱨾���1��ʾ�����򱨾�
   * @param xy,XY:��γ��
   *@return String<br>
   */
  public abstract String setRangeAlarm(String seq,String alarmType, EarthCoord xy,
                                       EarthCoord XY);

  /**
   * * ���򱨾������趨�����������
   * @param alarmType String --- ��������
   * @param rangeId String --- �����
   * @param x String --- ���ϽǾ���
   * @param y String --- ���Ͻ�γ��
   * @param X String --- ���ϽǾ���
   * @param Y String --- ���Ͻ�γ��
   * @return String
   */
  public abstract String setRangeAlarmById(String seq,String alarmType, String rangeId, String x, String y,
                                           String X, String Y);
  
  /**
   * ��������򱨾�����
   * @param seq��ָ�����к�
   * @param alarmType������ 0�� 1�� 2ȡ��
   * @param rangeId��������
   * @param startDate:������Ч�Ŀ�ʼʱ��
   * @param endDate:������Ч�Ľ���ʱ��
   * @param points����������
   * @return
   */
  public abstract String setRangeAlarm(String seq,String alarmType, String rangeId, String startDate, String endDate,com.mapabc.geom.DPoint[] points);

  /**
   *���ط�Χ����
   * @param state:״̬0�رգ�״̬1��
   * @return
   */
  public abstract String setRangeAlarmState(String seq,String state);

  /**
   *�����ٶȱ���,�������ٱ����͵��ٱ���<br>
   * @param speedType��1����
   * @param speed:�ٶ�ֵ
   * @param time:����ʱ��
   * @return
   */
  public abstract String setSpeedAlarm(String seq,String speedType, String speed,String time);
  /**
   *�����ٶȱ���,�������ٱ����͵��ٱ���<br>
   * @param speedType��1������0����
   * @param speed:�ٶ�ֵ
   * @return
   */
  public abstract String setSpeedAlarm(String seq, String speedType, String speed);

  /**
   *�����ٶȱ���
   * @param state:״̬0�رգ�״̬1��
   * @return
   */
  public abstract String setSpeedAlarmState(String seq,String state);

  /**
   *�����ƶ�����
   */
  public abstract String setMovingAlarm(String seq,String v);
  
  /**
   *����ƫ������
   *@param type:1���� 0ȡ��
   *@param distance:ƫ��ľ���
   *@param points:·�ߵ�
   */
  public abstract String setLineAlarm(String seq,String type,String distance,com.mapabc.geom.DPoint[] points);

  /**
   *�����ڶ�����ش�
   *@param xy:γ��ƫ��ֵ
   */
  public abstract String setWarpAlarm(String seq,EarthCoord xy);

  /**
   *�����ڶ�����ش�
   *@param distance:����
   */
  public abstract String setWarpAlarm(String seq,String distance);

  /**
   * ����ϵͳ��[���ٳ���ʱ��]�ڰ�[���ʱ��]�������Ͷ�λ��Ϣ
   * @return String
   */
  public abstract String setIntervalTimeLastDate(String seq);

  /**
   * �������������򱨾����Ե�ǰλ��Ϊ���ģ���radioΪ�뾶��������
   * @param radius:�뾶
   * @return
   */
  public abstract String setSquareAlarm(String seq,String radius);

  /**
   *�������
   * @param v:������������� 0 :�ر����б��� 1�������б���
   */
  public abstract String stopAlarm(String seq,String v);

  /**
   * ��Ϣ��ӡ��޸�
   * ��ӡ��޸��ն˵ı�����Ϣ���ݣ����30��
   * @param contents String
   * @return String
   */
  public abstract String addOrModifyAlarmInfor(String seq,String contents);

  /**
   * ��Ϣɾ��
   * @param inforNums String
   * @return String
   */
  public abstract String deleteInforAlarmInfor(String seq,String inforNums);
  
  /**
   * �����Զ��屨����
   * @param params --- ���ڲ�ͬ�ն˲������͡�������ͬ������Ҫ����ƴ��һ�������ַ�����������������
   * ����*XX,YYYYYYYYYY,S19,HHMMSS,Ax,S,time,K # ���Ϻ���̫��
         Ax��������ı�����Ϣ��S������������ʽ��time��ʱ�䣻K���Զ��屨���������Ƿ񴥷�S17�Զ���أ�
         ���������params = Ax,S,time,K
   * @return
   */
  public abstract String setSelfDefineAlarm(String seq,String params);
  /**
   * �����¶ȷ�Χ����
   * @param up
   * @param down
   * @return
   */
  public abstract String setTemperatureAlarm(String seq,String up, String down);
  /**
   * ������������
   * @param seq����ˮ��
   * @param type������ ���򱨾� ���ٱ��� ƫ������
   * @param timelen �жϴﵽ��������������������ʱ��������λ��
   * @param interval ���α���֮��ļ������λ��
   * @param times �ڲ������������µı���������0������
   * @return
   */
  public abstract String setAlarmParam(String seq, String type, String timelen, String interval, String times);

  /**
   * ����Բ������
   * @param seq ���к�
   * @param type ����
   * @param x ����
   * @param y γ��
   * @param r �뾶
   * @return
   */
  public abstract String setCicleArea(String seq, String type, String x, String y, String r);
}
