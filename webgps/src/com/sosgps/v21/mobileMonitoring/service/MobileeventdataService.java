package com.sosgps.v21.mobileMonitoring.service;

import java.util.Date;

public interface MobileeventdataService {
	String queryMobile(String keyWords,Date startTime,Date entTime,String type,String deviceId,int PageNo, int pageSize);
	String countMobileeventdata(String entName, String entCode, String termName, Date startTime,Date entTime,int pageNo, int pageSize);//查询统计手机开关机操作次数
}
