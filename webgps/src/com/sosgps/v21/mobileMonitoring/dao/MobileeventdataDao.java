package com.sosgps.v21.mobileMonitoring.dao;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;

public interface MobileeventdataDao {
	//查询手机操作状态
	Page<Object []> queryMobile(String keyWords, Date startTime,Date entTime,String type,String deviceId,int pageNo, int pageSize);
	//查询统计手机开关机操作次数
	Page<Object []> countMobileeventdata(String entName, String entCode, String termName, Date startTime,Date entTime,int pageNo, int pageSize);
}
