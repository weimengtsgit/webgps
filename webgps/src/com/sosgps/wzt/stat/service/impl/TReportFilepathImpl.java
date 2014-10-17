package com.sosgps.wzt.stat.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sosgps.wzt.stat.dao.TReportFilepathDao;
import com.sosgps.wzt.stat.service.TReportFilepathService;
/**
 * 详细日志报表文件实现类
 * @author magiejue
 * @add by zhumingzhe 2010-09-15
 */
public class TReportFilepathImpl implements TReportFilepathService {
	
	private static final Logger logger = Logger
	.getLogger(TReportFilepathImpl.class);
	
	private TReportFilepathDao tReportFilepathDao;
	
	public TReportFilepathDao gettReportFilepathDao() {
		return tReportFilepathDao;
	}
	
	public void settReportFilepathDao(TReportFilepathDao tReportFilepathDao) {
		this.tReportFilepathDao = tReportFilepathDao;
	}
	//获得文件路径集合
	public List getFilepathes(String deviceIds,String entCode,Date startDate, Date endDate) {
		try {
			return tReportFilepathDao.getReportFilepath(deviceIds,entCode,startDate, endDate);
		} catch (Exception e) {
			logger.error("获得文件路径集合错误", e);
			return null;
		}
	}

}
