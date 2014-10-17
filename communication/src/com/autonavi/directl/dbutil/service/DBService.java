package com.autonavi.directl.dbutil.service;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.autonavi.directl.pic.Picture;
import com.autonavi.directl.parse.ParseBase;

public interface DBService {
	
	//insert into shandong_sms_wait(wait_id,longid,desmobile,content)
    //values(shandong_seq_sms_id.nextval,'106511111111','134'26457580,'test')
	//�����������Ϣ
	/**
	 * longid:�����ط�����
	 * userName:�û���½�˻�
	 * desmobile:���շ�����
	 * cont:����
	 */
	public boolean insertAlarmNotify(Connection conn,String longid, String userName,String desmobile, String cont );
	//ͨ���ն�ID��ȡ���û��ֻ�����
	
	public String[] getUserSimBinded(Connection con, String deviceId);
	
	public  void updateInstructionsState(String deviceId, String state, String cmdId) ;
	
	//���汨����Ϣ 
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
