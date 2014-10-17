package com.sosgps.wzt.stat.service;

import java.util.Date;
import java.util.List;
/**
 * 详细日志报表文件service
 * @author magiejue
 * @add by zhumingzhe 2010-09-15
 */
public interface TReportFilepathService {
	////获得文件路径集合
	public List getFilepathes(String deviceIds,String entCode,Date startDate,Date endDate);
}
