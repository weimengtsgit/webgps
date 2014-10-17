package com.sosgps.wzt.sms.dao;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ShandongSmsRecv;

/**
 * @Title:短信接收dao层接口
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2009-3-25 上午10:58:07
 */
public interface SmsReceiveDao {
	// sos查询未读短信
	public Page<Object[]> listNotReadReceiveSms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);

	// sos查询所有接收短信
	public Page<Object[]> listReceiveSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);
	
	public ShandongSmsRecv findById( java.lang.String id);
	// sos短信设置为已读
	public void delete(ShandongSmsRecv persistentInstance);
	//add by magiejue查询所有接收短信为未页
	public List<Object> listReceiveSmsLog(String entCode, Long userId,Date startTime, Date endTime,String searchValue);
}
