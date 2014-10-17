package com.autonavi.directl.device.huaqian;

import com.autonavi.directl.idirectl.*;
import com.autonavi.directl.idirectl.Locator;
import com.autonavi.directl.idirectl.TerminalControl;
import com.autonavi.directl.idirectl.TerminalQuery;
import com.autonavi.directl.idirectl.TerminalSetting;

/**
 *
 * <p>Title: </p>
 * <p>Description: 实现深圳华强20GPS终端</p>
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
   * 创建报警功能类
   * @return
   */
  public Alarm createAlarm() {
    return new HQ20Alarm(this.terminalParam);
  }
  /**
   * 创建定位功能类
   * @return
   */
  public Locator createLocator() {
    return new HQ20Locator(this.terminalParam);
  }
  /**
   * 创建终端控制功能类
   * @return
   */

  public TerminalControl createTerminalControl() {
    return new HQ20TerminalControl(this.terminalParam);
  }
  /**
   * 创建终端查询功能类
   * @return
   */
  public TerminalQuery createTerminalQuery() {
    return new HQ20TerminalQuery(this.terminalParam);
 }
  /**
   * 创建终端设置功能类
   * @return
   */

  public TerminalSetting createTerminalSetting() {
    return new HQ20TerminalSetting(this.terminalParam);
   }
  /**
   * 是否支持某功能
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
   * 显示终端功能信息
   */
  public void showTerminalInfo() {
    String info = "支持的功能函数:\n";
    info += "\t1.发送短消息 sentMsg()\n";
    System.out.println(info);
    info = "一、定位功能类\n";
    info += "\t1.单次定位 singleLocator()\n";
    info += "\t2.连续定位 setMultiLocator()\n";
    System.out.println(info);
    info = "二、报警功能类\n";
    info += "\t1.区域报警 setRangeAlarm()\n";
    info += "\t2.速度报警 setSpeedAlarm()\n";
    info += "\t3.偏差报警 setWarpAlarm()\n";
    System.out.println(info);
    info = "三、设置功能类\n";
    info += "\t1.设置中心号码 setCenterNum()\n";
    info += "\t2.设置登陆密码 setPassWord()\n";
    info += "\t3.进入省电模式 setAutoSaveIntervalTime()\n";
    info += "\t4.设置功能状态 setFuntionState()\n";
    info += "\t5.设置端口号 setIPPort()\n";
    info += "\t6.设置应营商LOGO setSPLogo()\n";
    info += "\t7.设置通讯方式 setMedium()\n";
    info += "\t8.设置连接GPRS最大时间 setFuntionState()\n";
    info += "\t9.设置需要中心回复的项目 setNeedRevertItem()\n";
    info += "\t10.设置位置回传的时间间隔 setOnlineRevertTime()\n";
    info += "\t11.设置呼叫模式 setCallLimitMode()\n";
    info += "\t12.其他设置 setOther()\n";
    info += "\t13.设置压缩参数 setCompressParam()\n";
    System.out.println(info);
    info = "四、控制功能类\n";
    info += "\t1.重新启动 reset()\n";
    info += "\t2.开关油门 setOilState()\n";
    info += "\t3.控制终端工作状态 setWorkState()\n";
    info += "\t4.发布公共信息 sentPubMsg()\n";
    info += "\t5.数字回传 replyNum()\n";
    info += "\t6.进入GPRS模式 gointoGPRS()\n";
    info += "\t7.某范围点名 rangeCalling()\n";
    info += "\t8.终端回拨电话 callBack()\n";
    info += "\t9.给终端附件发送信息 sentAffixMsg()\n";
    info += "\t10.短信抢答 quickRevertShortMessage()\n";
    info += "\t11.电话抢答 quickRevertCalling()\n";
    info += "\t12.短信调度 shortMessageAttemper()\n";
    info += "\t13.电话调度 telphoneAttemper()\n";
    System.out.println(info);
    info = "五、查询功能类\n";
    info += "\t1.查询电瓶电压信息 getTensionInfo()\n";
    info += "\t2.查询里程信息 getOdograph()\n";
    info += "\t3.终端所有参数上传命令 getStateParam()\n";
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
