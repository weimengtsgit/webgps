package com.sosgps.wzt.directl.idirectl;

import com.mapabc.geom.DPoint;

/**
 * <p>Title: GPS����</p>
 *
 * <p>Description:������Alarm���������� </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: www.sosgps.com</p>
 *
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class AlarmAdapter extends Alarm {
  public AlarmAdapter() {
  }

@Override
public String addOrModifyAlarmInfor(String seq, String contents) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String deleteInforAlarmInfor(String seq, String inforNums) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setIntervalTimeLastDate(String seq) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setMovingAlarm(String seq, String v) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setRangeAlarm(String seq, String alarmType, EarthCoord xy, EarthCoord XY) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setRangeAlarmById(String seq, String alarmType, String rangeId, String x, String y, String X, String Y) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setRangeAlarmState(String seq, String state) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setSelfDefineAlarm(String seq, String params) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setSpeedAlarm(String seq, String speedType, String speed,String time) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setSpeedAlarmState(String seq, String state) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setSquareAlarm(String seq, String radius) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setTemperatureAlarm(String seq, String up, String down) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setWarpAlarm(String seq, EarthCoord xy) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String setWarpAlarm(String seq, String distance) {
	// TODO �Զ����ɷ������
	return null;
}

@Override
public String stopAlarm(String seq, String v) {
	// TODO �Զ����ɷ������
	return null;
}

/* (non-Javadoc)
 * @see com.sosgps.wzt.directl.idirectl.Alarm#setRangeAlarm(java.lang.String, java.lang.String, java.lang.String, com.sosgps.geom.DPoint[])
 */
@Override
public String setRangeAlarm(String seq, String alarmType, String rangeId,String startDate, String endDate,
		DPoint[] points) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see com.sosgps.wzt.directl.idirectl.Alarm#setSpeedAlarm(java.lang.String, java.lang.String, java.lang.String)
 */
@Override
public String setSpeedAlarm(String seq, String speedType, String speed) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see com.sosgps.wzt.directl.idirectl.Alarm#setLineAlarm(java.lang.String, java.lang.String, com.sosgps.geom.DPoint[])
 */
@Override
public String setLineAlarm(String seq,  String type,String distance, DPoint[] points) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see com.sosgps.wzt.directl.idirectl.Alarm#setAlarmParam(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
 */
@Override
public String setAlarmParam(String seq, String type, String timelen,
		String interval, String times) {
	// TODO Auto-generated method stub
	return null;
}

/*
 * (non-Javadoc)
 * @see com.sosgps.wzt.directl.idirectl.Alarm#setCicleArea(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
 */
@Override
public String setCicleArea(String seq, String type, String x, String y, String r) {
	// TODO Auto-generated method stub
	return null;
}

   
}
