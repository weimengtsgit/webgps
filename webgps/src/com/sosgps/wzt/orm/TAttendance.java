package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * @author WangChao
 * @description 签到签退实体类
 */
@SuppressWarnings("serial")
public class TAttendance extends AbstractTAttendance implements java.io.Serializable {

	public TAttendance(){
		
	}
	
	public TAttendance(TTerminal tTerminal,Date createTime,Integer attendanceDate,Integer deleteFlag,
			Date signInTime, Float signInLongitude, Float signInLatitude, String signInDesc, Date signOffTime, 
			Float signOffLongitude, Float signOffLatitude , String signOffDesc){
			super(tTerminal,createTime,attendanceDate,deleteFlag,signInTime,signInLongitude,signInLatitude,signInDesc,signOffTime,signOffLongitude,signOffLatitude,signInDesc);
		}
}
