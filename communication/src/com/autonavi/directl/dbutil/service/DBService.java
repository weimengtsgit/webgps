package com.autonavi.directl.dbutil.service;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.autonavi.directl.pic.Picture;
import com.autonavi.directl.parse.ParseBase;

public interface DBService {
	
	//insert into shandong_sms_wait(wait_id,longid,desmobile,content)
    //values(shandong_seq_sms_id.nextval,'106511111111','134'26457580,'test')
	//插入待发短消息
	/**
	 * longid:短信特服号码
	 * userName:用户登陆账户
	 * desmobile:接收方号码
	 * cont:内容
	 */
	public boolean insertAlarmNotify(Connection conn,String longid, String userName,String desmobile, String cont );
	//通过终端ID获取其用户手机号码
	
	public String[] getUserSimBinded(Connection con, String deviceId);
	
	public  void updateInstructionsState(String deviceId, String state, String cmdId) ;
	
	//保存报警信息 
	public boolean saveActiveAlarm(ParseBase pb,String type);
	
	public void saveMessage(String deviceid,String cont);
	
	public boolean insertPicInfo(Picture p) ;
	
	public void saveLineAlarm(ParseBase base) throws Exception;
	
	public void saveAreaAlarm(ParseBase base) throws Exception;
	
	public void saveSpeedAlarm(ParseBase base) throws Exception;
	
	public void saveActiveAlarm(ParseBase base) throws Exception;
	
	public void saveMoreAlarm(ArrayList<ParseBase> baseList) throws Exception;
	
	public void saveImagePack(Picture p);
	
	public void updateNoCmdIdInstructionsState(String deviceId, String state,
			String cmdId) ;
	
 

}
