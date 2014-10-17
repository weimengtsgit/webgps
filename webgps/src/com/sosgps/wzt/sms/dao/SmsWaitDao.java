package com.sosgps.wzt.sms.dao;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ShandongSmsWait;

/**
 * @Title:待发短信入库dao层接口
 * @Description:
 * @Company: 
 * @author: 
 * @version 1.0
 * @date: 2009-3-24 下午03:03:18
 */
public interface SmsWaitDao {
	
	public void saveWaitSMS(String entCode, String phone, String message);

	// sos查询已发送短信
	public Page<Object[]> listSendSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);
	//add by magiejue 2010-12-13 查询所有发送短信未分页
	public List<Object> listSendSmsLog(String entCode, Long userId,Date startTime, Date endTime,String searchValue);
}
