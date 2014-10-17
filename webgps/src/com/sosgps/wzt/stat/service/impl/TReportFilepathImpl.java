package com.sosgps.wzt.stat.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sosgps.wzt.stat.dao.TReportFilepathDao;
import com.sosgps.wzt.stat.service.TReportFilepathService;
/**
 * ��ϸ��־�����ļ�ʵ����
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
	//����ļ�·������
	public List getFilepathes(String deviceIds,String entCode,Date startDate, Date endDate) {
		try {
			return tReportFilepathDao.getReportFilepath(deviceIds,entCode,startDate, endDate);
		} catch (Exception e) {
			logger.error("����ļ�·�����ϴ���", e);
			return null;
		}
	}

}
