package com.sosgps.wzt.stat.dao;

import java.util.Date;
import java.util.List;
/**
 * 详细报表文件dao接口
 * @author magiejue
 * @add by zhumingzhe 2010-09-15
 */
public interface TReportFilepathDao {
	//获得文件路径集合
	public List getReportFilepath(String deviceIds,String endCode,Date startDate,Date endDate);
}
