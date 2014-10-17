/**
 * 
 */
package com.sosgps.wzt.locate.dao;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.orm.TTerminal;

/**
 * <p>Title:LocateDAO.java</p>
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2008</p>
 * <p>Company:图盟科技</p>
 * @author shiguang.zhou
 * @date: 2008-12-16
 * @time: 下午02:09:18	
 * 
 */
public interface LocateDAO {

	public List findLocByDeviceId(String deviceId);
	// sos通过deviceid和time查最后一条
	public TLocrecord queryLocByTime(String deviceId, Date time);
	// sos轨迹回放
	public List queryLocsByTime(String deviceId, Date startTime, Date endTime);
	// sos轨迹回放 add by liuhongxiao 2011-12-02
	public List queryLocsByTime(TTerminal terminal, String startDate, String endDate, String startTime, String endTime);
	// sos轨迹回放车辆信息分页
	public Page<TLocrecord> listQueryLocsByTime(String deviceId, Date startTime, Date endTime, int pageNo, int pageSize);
	// 查询没有位置描述及坐标未偏转数据
	public List queryNoEncryptionAndNoLocDesc();
    public List queryLocsByTime2(TTerminal terminal, String startDate, String endDate, String startTime, String endTime);
    public Page<Object[]> lastLocrecordList(String entCode, String deviceIds, String name, String locateType, int gpstime, int inputtime, int startint, int limitint);
    
}
