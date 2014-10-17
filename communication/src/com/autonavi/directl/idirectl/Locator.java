package com.autonavi.directl.idirectl;

/**
 *
 * <p>Title: </p>
 * <p>Description: ��λ���ܳ�����</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public abstract class Locator extends BaseDictate {
  public Locator() {
  }

  /**
   * ���ζ�λ
   * @return
   */
  public abstract String singleLocator(String seq);

  /**
   *������λ<br>
   * ÿ��tʱ��,����һ��λ��,һ������ count��
   * @param t:���ʱ��
   * @param count:�������ʹ���
   */
  public abstract String multiLocator(String seq,String t, String count);

  /**
   * ������λ��ÿ��tʱ��,����һ��λ��,һ������ count�Σ���������Զ�����
   * @param t String
   * @param count String
   * @return String
   */
  public abstract String autoSleepMultiLocator(String seq,String t, String count);

  /**
   * ����������λ����
   * @param state String:0��ʾ�رգ�1��ʾ����
   * @return String
   */
  public abstract String setMultiLocatorState(String seq,String state);

  /**
   * ����GPRS����
   * @param state String:0��ʾ�رգ�1��ʾ����
   * @return String
   */
  public abstract String setGPRSState(String seq,String state);

  /**
   * �����ٶ�Ϊ��ʱ�Ƿ���Ҫ�ϴ���λ����
   * @param state String
   * @return String
   */
  public abstract String setLocatorBySpeedIsZero(String seq,String state);
  
  /**
   * ���ð����붨λ
   * @param distance
   * @return
   */
  public abstract String setLocatorByDistance(String seq,String distance);
  /**
   * ���ÿ��س��������ϴ����
   * @param type
   * @param emptime
   * @param ztime
   * @param tasktime
   * @return
   */
  public abstract String setCarInterval(String seq,String type,String emptime, String ztime, String tasktime);
  /**
   * ��ϻ�Ӳɼ����
   * @param type
   * @param interval
   * @return
   */
  public abstract String setBlackInterval(String seq,String type,String interval);
  
  //��ϻ�������ϴ�����
  public abstract String setBlackQuest(String seq);


}
