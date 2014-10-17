package com.sosgps.v21.mobileMonitoring.service.impl;

import java.util.Date;

import com.sosgps.v21.mobileMonitoring.dao.MobileeventdataDao;
import com.sosgps.v21.mobileMonitoring.service.MobileeventdataService;
import com.sosgps.v21.util.JsonUtil;
import com.sosgps.wzt.manage.util.Page;

public class MobileeventdataServiceImpl implements MobileeventdataService {
	private MobileeventdataDao mobileeventdataDao;

	public MobileeventdataDao getMobileeventdataDao() {
		return mobileeventdataDao;
	}

	public void setMobileeventdataDao(MobileeventdataDao mobileeventdataDao) {
		this.mobileeventdataDao = mobileeventdataDao;
	}

	public String queryMobile(String keyWords,
			Date startTime, Date entTime, String type, String deviceId,int PageNo, int pageSize) {
		Page<Object[]> page = mobileeventdataDao.queryMobile(keyWords,startTime, entTime, type, deviceId,PageNo, pageSize);
		
		int size = page == null ? 0 : page.getTotalCount();
		return JsonUtil.convertToJsonStr(new String[] {"id",
				"gpstime", "termName", "groupName", "simcard", "type",
				"state" }, size == 0 ? null : page.getResult(), size);
	}
	/**
	 * 查询统计手机开关机操作次数
	 */
	public String countMobileeventdata(String entName, String entCode,
			String termName, Date startTime, Date entTime, int pageNo,
			int pageSize) {
		Page<Object[]> page = mobileeventdataDao.countMobileeventdata(entName, entCode,
				termName, startTime, entTime, pageNo, pageSize);
		int size = page == null ? 0 : page.getTotalCount();
		return JsonUtil.convertToJsonStr(new String[] {"entName",
				"entCode", "termName",  "deviceId", "type","countDeviceId" }, size == 0 ? null : page.getResult(), size);
	}

}
