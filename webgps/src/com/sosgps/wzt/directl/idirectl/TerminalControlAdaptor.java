package com.sosgps.wzt.directl.idirectl;

/**
 * <p>
 * Title: GPS网关
 * </p>
 * 
 * <p>
 * Description:抽象类TerminalControl的适配器类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: www.sosgps.com
 * </p>
 * 
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class TerminalControlAdaptor extends TerminalControl {
  public TerminalControlAdaptor() {
  }

@Override
public String CancelHelp(String seq, String yl, String jt, String dh) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String SelfCheck(String seq, String type) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String SendMessage(String seq, String msg) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String batchSentMsgToLED(String seq, String content) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String callBack(String seq, String tel) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String callCarInfor(String seq, String content) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String clearLEDContent() {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String commonCmdFormat(String seq, String content) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String deleteInfor(String seq, String inforNums) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String gointoGPRS(String seq, String v, String pTime) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String pwdResume(String seq, String centerNum) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String quickRevertCalling(String seq, String ptype, EarthCoord xy, EarthCoord XY, String tel, String msg) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String quickRevertShortMessage(String seq, String ptype, EarthCoord xy, EarthCoord XY, String msg) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String rangeCalling(String seq, EarthCoord xy, EarthCoord XY) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String replyNum(String seq, String ptype, EarthCoord xy, EarthCoord XY, String msg) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String reset(String seq) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String restoreInfor(String seq, String simcard) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String sendDest(String seq, String point) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String sendExternalDate(String seq, String content) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String sendMessageByType(String seq, String type, String msg) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String sendRoute(String seq, String points) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String sentAffixMsg(String seq, String affix, String protocol, String msg) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String sentMsgAloneToLED(String seq, String content) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String sentPubMsg(String seq, String msg) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setCallSetting(String seq, String type, String state) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setCameraControl(String seq,String type, String action, String times, String interval, String zhiliang, String liangdu, String duibdu, String baohd, String huidu) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setDoorState(String seq, String state) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setEletricityState(String seq, String state) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setFlameout(String seq, String type, String state, String speed) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setListening(String seq, String state, String tel) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setOilAndLock(String seq, String state, String param) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setOilState(String seq, String state) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setOpenWithPwd(String seq, String pwd) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String setWorkState(String seq, String v) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String shortMessageAttemper(String seq, String ptype, EarthCoord xy, EarthCoord XY, String msg) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String startSaveModeByTime(String seq, String time) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String takePictures(String seq, String size, String action, String no) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String telphoneAttemper(String seq, String ptype, EarthCoord xy, EarthCoord XY, String tel, String msg) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String testConnectSwitch(String seq, String testState) {
	// TODO 自动生成方法存根
	return null;
}

@Override
public String turnOff(String seq, String v) {
	// TODO 自动生成方法存根
	return null;
}

/* (non-Javadoc)
 * @see com.sosgps.wzt.directl.idirectl.TerminalControl#camera()
 */
@Override
public String camera(String seq) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see com.sosgps.wzt.directl.idirectl.TerminalControl#getPicture(java.lang.String, java.lang.String, java.lang.String)
 */
@Override
public String getPicture(String seq, String trackNo, String picId) {
	// TODO Auto-generated method stub
	return null;
}

  
}
