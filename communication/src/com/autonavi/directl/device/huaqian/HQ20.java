package com.autonavi.directl.device.huaqian;

import com.autonavi.directl.idirectl.*;
import com.autonavi.directl.idirectl.Locator;
import com.autonavi.directl.idirectl.TerminalControl;
import com.autonavi.directl.idirectl.TerminalQuery;
import com.autonavi.directl.idirectl.TerminalSetting;

/**
 *
 * <p>Title: </p>
 * <p>Description: ʵ�����ڻ�ǿ20GPS�ն�</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: www.mapabc.com</p>
 * @author not attributable
 * @version 1.0
 */
public class HQ20
    extends Terminal {
  public HQ20() {
  }

  public static void main(String[] args) {
    HQ20 HQ201 = new HQ20();
  }
  /**
   * ��������������
   * @return
   */
  public Alarm createAlarm() {
    return new HQ20Alarm(this.terminalParam);
  }
  /**
   * ������λ������
   * @return
   */
  public Locator createLocator() {
    return new HQ20Locator(this.terminalParam);
  }
  /**
   * �����ն˿��ƹ�����
   * @return
   */

  public TerminalControl createTerminalControl() {
    return new HQ20TerminalControl(this.terminalParam);
  }
  /**
   * �����ն˲�ѯ������
   * @return
   */
  public TerminalQuery createTerminalQuery() {
    return new HQ20TerminalQuery(this.terminalParam);
 }
  /**
   * �����ն����ù�����
   * @return
   */

  public TerminalSetting createTerminalSetting() {
    return new HQ20TerminalSetting(this.terminalParam);
   }
  /**
   * �Ƿ�֧��ĳ����
   * @param functionName
   * @return
   */
  public boolean isSupport(String functionName) {
    String supported = "sentMsg,singleLocator,setMultiLocator,setRangeAlarm,setSpeedAlarm,setWarpAlarm,";
    supported += "setCenterNum,setPassWord,setAutoSaveIntervalTime,setFuntionState,setIPPort,setSPLogo,";
    supported += "setMedium,setFuntionState,setNeedRevertItem,setOnlineRevertTime,setCallLimitMode,setOther,";
    supported += "setCompressParam,reset,setOilState,setWorkState,sentPubMsg,replyNum,gointoGPRS,rangeCalling,";
    supported += "callBack,sentAffixMsg,quickRevertShortMessage,quickRevertCalling,shortMessageAttemper,telphoneAttemper";
    if (supported.indexOf(functionName) > -1) {
      return true;
    }
    return false;
  }
  /**
   * ��ʾ�ն˹�����Ϣ
   */
  public void showTerminalInfo() {
    String info = "֧�ֵĹ��ܺ���:\n";
    info += "\t1.���Ͷ���Ϣ sentMsg()\n";
    System.out.println(info);
    info = "һ����λ������\n";
    info += "\t1.���ζ�λ singleLocator()\n";
    info += "\t2.������λ setMultiLocator()\n";
    System.out.println(info);
    info = "��������������\n";
    info += "\t1.���򱨾� setRangeAlarm()\n";
    info += "\t2.�ٶȱ��� setSpeedAlarm()\n";
    info += "\t3.ƫ��� setWarpAlarm()\n";
    System.out.println(info);
    info = "�������ù�����\n";
    info += "\t1.�������ĺ��� setCenterNum()\n";
    info += "\t2.���õ�½���� setPassWord()\n";
    info += "\t3.����ʡ��ģʽ setAutoSaveIntervalTime()\n";
    info += "\t4.���ù���״̬ setFuntionState()\n";
    info += "\t5.���ö˿ں� setIPPort()\n";
    info += "\t6.����ӦӪ��LOGO setSPLogo()\n";
    info += "\t7.����ͨѶ��ʽ setMedium()\n";
    info += "\t8.��������GPRS���ʱ�� setFuntionState()\n";
    info += "\t9.������Ҫ���Ļظ�����Ŀ setNeedRevertItem()\n";
    info += "\t10.����λ�ûش���ʱ���� setOnlineRevertTime()\n";
    info += "\t11.���ú���ģʽ setCallLimitMode()\n";
    info += "\t12.�������� setOther()\n";
    info += "\t13.����ѹ������ setCompressParam()\n";
    System.out.println(info);
    info = "�ġ����ƹ�����\n";
    info += "\t1.�������� reset()\n";
    info += "\t2.�������� setOilState()\n";
    info += "\t3.�����ն˹���״̬ setWorkState()\n";
    info += "\t4.����������Ϣ sentPubMsg()\n";
    info += "\t5.���ֻش� replyNum()\n";
    info += "\t6.����GPRSģʽ gointoGPRS()\n";
    info += "\t7.ĳ��Χ���� rangeCalling()\n";
    info += "\t8.�ն˻ز��绰 callBack()\n";
    info += "\t9.���ն˸���������Ϣ sentAffixMsg()\n";
    info += "\t10.�������� quickRevertShortMessage()\n";
    info += "\t11.�绰���� quickRevertCalling()\n";
    info += "\t12.���ŵ��� shortMessageAttemper()\n";
    info += "\t13.�绰���� telphoneAttemper()\n";
    System.out.println(info);
    info = "�塢��ѯ������\n";
    info += "\t1.��ѯ��ƿ��ѹ��Ϣ getTensionInfo()\n";
    info += "\t2.��ѯ�����Ϣ getOdograph()\n";
    info += "\t3.�ն����в����ϴ����� getStateParam()\n";
    System.out.println(info);
  }

  public String sentMsg(String content) {
    if(this.terminalParam.getGPRSModal()){
    return "false";
 }else{
	 return null;//SMS
  // return this.sentSortMessage(content);
 }

  }
}
