package com.sosgps.wzt.directl.idirectl;

/**
 * <p>
 * Title: GPS����
 * </p>
 * 
 * <p>
 * Description:������TerminalControl����������
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
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String SelfCheck(String seq, String type) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String SendMessage(String seq, String msg) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String batchSentMsgToLED(String seq, String content) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String callBack(String seq, String tel) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String callCarInfor(String seq, String content) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String clearLEDContent() {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String commonCmdFormat(String seq, String content) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String deleteInfor(String seq, String inforNums) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String gointoGPRS(String seq, String v, String pTime) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String pwdResume(String seq, String centerNum) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String quickRevertCalling(String seq, String ptype, EarthCoord xy, EarthCoord XY, String tel, String msg) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String quickRevertShortMessage(String seq, String ptype, EarthCoord xy, EarthCoord XY, String msg) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String rangeCalling(String seq, EarthCoord xy, EarthCoord XY) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String replyNum(String seq, String ptype, EarthCoord xy, EarthCoord XY, String msg) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String reset(String seq) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String restoreInfor(String seq, String simcard) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String sendDest(String seq, String point) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String sendExternalDate(String seq, String content) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String sendMessageByType(String seq, String type, String msg) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String sendRoute(String seq, String points) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String sentAffixMsg(String seq, String affix, String protocol, String msg) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String sentMsgAloneToLED(String seq, String content) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String sentPubMsg(String seq, String msg) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setCallSetting(String seq, String type, String state) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setCameraControl(String seq,String type, String action, String times, String interval, String zhiliang, String liangdu, String duibdu, String baohd, String huidu) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setDoorState(String seq, String state) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setEletricityState(String seq, String state) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setFlameout(String seq, String type, String state, String speed) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setListening(String seq, String state, String tel) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setOilAndLock(String seq, String state, String param) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setOilState(String seq, String state) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setOpenWithPwd(String seq, String pwd) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setWorkState(String seq, String v) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String shortMessageAttemper(String seq, String ptype, EarthCoord xy, EarthCoord XY, String msg) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String startSaveModeByTime(String seq, String time) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String takePictures(String seq, String size, String action, String no) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String telphoneAttemper(String seq, String ptype, EarthCoord xy, EarthCoord XY, String tel, String msg) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String testConnectSwitch(String seq, String testState) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String turnOff(String seq, String v) {
	// TODO �Զ����ɷ������
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
