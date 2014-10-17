package com.autonavi.directl.idirectl;
/**
 *
 * <p>Title: </p>
 * <p>Description:�ն����ó�����</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public abstract class TerminalSetting extends BaseDictate{
  public TerminalSetting() {
  }
  /**
   **�������ĺ���
   *@param centerID:���Ĵ���
   *@param newNum:���ĺ���
   */
  public abstract String setCenterNum(String seq,String newNum);

  /**
   *�����û�����
   *@param UserKey���û�����
   *@param pwd���û�����
   */
  public abstract String setPassWord(String seq,String UserKey, String pwd);

  /**
   *�����Ƿ����ʡ��ģʽ״̬
   */
  public abstract String startSaveMode(String seq,String v);

  /**
   *���ô洢��ʷ����ʱ����
   */
  public abstract String setAutoSaveIntervalTime(String seq,String time);

  /**
   *����ͨ�����ƹ���
   */
  public abstract String setCallLimitMode(String seq,String[] nums);

  /**
   *���ù��ܿ���
   */
  public abstract String setFuntionState(String seq,String state,String functionNum);

  /**
   *����IP���˿�
   */
  public abstract String setIPPort(String seq,String serverIP, String serverPort,
                                   String localPort);


  /**
   * �޸��ն�����
   * @param newPwd1
   * @param newPwd2
   * @return
   */
  public abstract String changeTerminalPwd(String seq,String newPwd1, String newPwd2);

  /**
   * �����ն�ģʽ
   * @param pMode:0:����ģʽ 1:GPRSģʽ
   * @return
   */
  public abstract String changeMode(String seq,String pMode);

  /**
   *��ʼ������ΪĬ�ϲ���
   */
  public abstract String initDefaultParam(String seq,String v);

  /**
   *����ʱ��
   */
  public abstract String setTimeDistance(String seq,String time);
  /**
   *����������Ӫ��LOGO
   */
  public abstract String setSPLogo(String seq,String v);

  /**
   *���ûش�ѹ������
   * @param e:ʱ����
   * @param t:ѹ����λ��Ϣ�Ĵ���
   * @param nnnn:��Ҫ����ѹ����λ��Ϣ�Ĵ���
   */
  public abstract String setCompressParam(String seq,String ee, String tt, String nnnn);
  /**
   * ���ûش�ѹ������
   * @param t:ʱ�䣬���ѹ��һ������Ȼ��ش�
   * @return
   */
  public abstract String setCompressParam(String seq,String t);
  /**
   *���ûر�ý��
   * @param p:ý��l����,���š�GSM modem
   */
  public abstract String setMedium(String seq,String p);

  /**
   *���ò���
   */
  public abstract String setDialParam(String seq,String dialUser, String dialNum,
                                      String dialPass);

  /**
   *���ò���APN
   */
  public abstract String setAPN(String seq,String v);

  /**
   *�������½���GPRS��������ʱ����
   */
  public abstract String setMaxReLinkTime(String seq,String t);

  /**
   *�����ն��ϴ�����Ҫ���Ļظ�����Ŀ
   * @param itemKey:��Ŀ����
   */
  public abstract String setNeedRevertItem(String seq,String itemKey);

  /**
   *�趨�ض�����Ϣ�˵�
   */
  public abstract String setSMMenu(String seq,String v);

  /**
   *�������ߴ���ʱλ�ûش���ʱ����
   */
  public abstract String setOnlineRevertTime(String seq,String tCount);
  /**
   * ���ó�̨ID
   * @param carId
   * @return
   */
  public abstract String setCarSIM(String seq,String sim);

  /**
  *��������
  */
 public abstract String setOther(String seq,String v1, String v2);

 /**
  *����������Ч�� interval������
  */
 public abstract String setCMDValidTime(String seq,String interval);

 /**
  *�켣�洢���� type��ģʽ 0 ��ֹ 1 ���� interval��ʱ������
  */
 public abstract String setSaveContrail(String seq,String type, String interval);

 /**
  *����Ԥ��������Ϣ no����Ϣ��� msg����Ϣ����
  */
 public abstract String setPrefabricateMsg(String seq,String no, String msg);

 /**
  *����Ԥ��Ԥ���绰 type��Ԥ������ 0 Ԥ�Ƶ绰 1 Ԥ�Ƶ绰���� ��phones���绰��绰����
  */
 public abstract String setPrefabricatePhone(String seq,String type,
                                             String phonesOrLength);
 /**
   TerminalSetting.java:
   * ���ͼ��ʱ������
   * @param intervalType String �������
   * @param intervalTime String ���ʱ��
   */
  public abstract String setSendIntervalTime(String seq,String intervalType, String intervalTime);

  /**
   * �޲��绰��Ϣ
   * @param telNums String --- �绰���á������ֿ���
   * @return String
   */
  public abstract String setRestrictCallTel(String seq,String telNums);

  /**
   * �޽ӵ绰��Ϣ
   * @param telNums String --- �绰���á������ֿ���
   * @return String
   */
  public abstract String setRestrictPickUpTel(String seq,String telNums);

  /**
   * �������رյ绰���ƹ���
   * @param data String
   * @return String
   */
  public abstract String openOrCloseTelRestrict(String seq,String data);

  /**
   * �������ظ�����ʽ
   * @param dataArray byte[]
   * @return String
   */
  public abstract String restorePackFormatByServer(byte[] dataArray);
  
  /**
   * �����Զ��������
   * @param status
   * @return
   */
  public abstract String setAutoInspect(String seq,String status);
  /**
   * ���ó�ʱʱ����
   * @param type
   * @param t
   * @return
   */
  public abstract 	String setExtendTime(String seq,String type, String t);
  /**
   * �����¶Ȼش����
   * @param time
   * @return
   */
  public abstract String setTemptertureInterval(String seq,String time);
  /**
   * �����쳣�仯��ֵ����λΪ����
   * �������ڶ�ʱ���ڼ��ٳ�������ֵʱ������
   * Ĭ�����ݻ��� 15%����ֵΪ0�򲻼����������������ʱ�䣺��λΪs��
   * �ڸ�ʱ������������쳣�仯�򱨾���
   * @param type
   * @param value���ͷ�ֵ
   * @param time������ʱ��
   * @return
   */
  public abstract String setOilDoorValue(String seq,String type,String value, String time);
  
  public abstract String setTakePhoto(String seq,String number, String count, String interval,String isUpload);

}
