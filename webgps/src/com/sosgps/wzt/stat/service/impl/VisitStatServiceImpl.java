package com.sosgps.wzt.stat.service.impl;

import org.apache.log4j.Logger;
import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.stat.dao.VisitStatDao;
import com.sosgps.wzt.stat.service.VisitStatService;

/**
 * @Title:�ݷ�ͳ��ҵ��ӿھ���ʵ����
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 ����08:22:16
 */
public class VisitStatServiceImpl implements VisitStatService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(VisitStatServiceImpl.class);
	private VisitStatDao visitStatDao;

	public VisitStatDao getVisitStatDao() {
		return visitStatDao;
	}

	public void setVisitStatDao(VisitStatDao visitStatDao) {
		this.visitStatDao = visitStatDao;
	}

	public Page<Object[]> listCustomVisitCountTjByCustom(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, Long poiId, String deviceIds, int duration) {
		try {
			return visitStatDao.listCustomVisitCountTjByCustom(entCode, userId, pageNo, pageSize,
					startDate, endDate, poiId, deviceIds, duration);
		} catch (Exception e) {
			logger.error("�鿴�ݷ�ͳ�ƴ���", e);
			return null;
		}
	}

	public Page<Object[]> listVisitCountTjByDeviceId(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String deviceId, int duration) {
		try {
			return visitStatDao.listVisitCountTjByDeviceId(entCode, userId, pageNo, pageSize,
					startDate, endDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("�鿴�ͻ��ݷ�ͳ�ƴ���", e);
			return null;
		}
	}

	public Page<Object[]> listAttendanceReport(String deviceIds, String entCode, Long userId,
			int pageNo, int pageSize, String startDate, String endDate, String searchValue) {
		try {
			
//			String[] deviceIdss = CharTools.split(deviceIds, ",");
			return visitStatDao.listAttendanceReport(deviceIds, entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue);
		} catch (Exception e) {
			logger.error("ҵ��Ա���ڱ������", e);
			return null;
		}
	}

	public Page<Object[]> listAttendanceReportDetail(String deviceId,
			int pageNo, int pageSize, Date startDate, Date endDate, String searchValue) {
		try {
			
			return visitStatDao.listAttendanceReportDetail(deviceId, pageNo,
					pageSize, startDate, endDate, searchValue);
		} catch (Exception e) {
			logger.error("ҵ��Ա���ڱ�����ϸ����", e);
			return null;
		}
	}

	public Page<Object[]> listVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		try {
			return visitStatDao.listVisitCountTj(entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("�ݷô���ͳ�ƴ���", e);
			return null;
		}
	}
	// add by renxianliang
	public Page<Object[]> listCustomVisitCountLing(int pageNo, int pageSize,
			String startDate, String endDate, String searchValue, String deviceIds,
			int duration) {
		try {
			return visitStatDao.listCustomVisitCountLing(pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("�ݷô���ͳ�ƴ���", e);
			e.printStackTrace();
			return null;
		}
	}
	public Page<Object[]> listCustomVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		try {
			return visitStatDao.listCustomVisitCountTj(entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("�ͻ��ݷô���ͳ�ƴ���", e);
			return null;
		}
	}

	public Page<Object[]> listCustomVisitTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		try {
			return visitStatDao.listCustomVisitTj(entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("�ͻ��ݷô���ͳ�ƴ���", e);
			return null;
		}
	}
	//�ݷÿͻ���
	public Page<Object[]> listVisitCustomerCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		try {
			return visitStatDao.listVisitCustomerCountTj(entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("�ݷÿͻ���ͳ�ƴ���", e);
			return null;
		}
	}
	//�ݷõص���
	public Page<Object[]> listVisitPlaceCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		try {
			return visitStatDao.listVisitPlaceCountTj(entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("�ݷõص���ͳ�ƴ���", e);
			return null;
		}
	}
	//�ݷ��ն���
	public Page<Object[]> listVisitTerminal(String entCode, Long userId, int pageNo, int pageSize, String searchValue, String deviceIds) {
		try {
			return visitStatDao.listVisitTerminal(entCode, userId, pageNo, pageSize, searchValue, deviceIds);
		} catch (Exception e) {
			logger.error("�ݷ��ն�������", e);
			return null;
		}
	}
	//�ݷô���ͼ��
	public Page<Object[]> visitCountChart(int pageNo, int pageSize, Date startDate, Date endDate, String deviceId, int duration) {
		try {
			return visitStatDao.visitCountChart(pageNo, pageSize, startDate, endDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("�ݷô���ͼ�����", e);
			return null;
		}
	}
	//�ݷõص�����ͼ��ʾ����
	public Page<Object[]> visitStatPlaceMap(int pageNo, int pageSize, String startDate, String endDate, String deviceId, int duration) {
		try {
			return visitStatDao.visitStatPlaceMap2(pageNo, pageSize, startDate, endDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("�ݷõص�����ͼ��ʾ���ݴ���", e);
			return null;
		}
	}
	//ҵ��Ա����ͳ��(sql)
	public Page<Object[]> listVisitCountTjSql(String entCode, Long userId,int pageNo, int pageSize, 
			String startDate, String endDate,String searchValue, String deviceIds, int duration) {
		try {
			return visitStatDao.listVisitCountTjSql2(entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("ҵ��Ա����ͳ��(sql)����", e);
			return null;
		}
	}
	//ҵ��Ա�ݷô���������Աͳ��ͼ��(sql)
	public Page<Object[]> visitCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration){
		try {
			return visitStatDao.visitCountChartAll(pageNo, pageSize,startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("ҵ��Ա�ݷô���������Աͳ��ͼ��(sql)����", e);
			return null;
		}
	}
	//ҵ��Ա�����ݷô���ͼ��(sql)
	public Page<Object[]> visitCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration){
		try {
			return visitStatDao.visitCountChartSql(pageNo, pageSize,startDate, endDate, utcStartDate, utcEndDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("ҵ��Ա�����ݷô���ͼ��(sql)����", e);
			return null;
		}
	}
	//ҵ��Ա�����ݷÿͻ���ͼ��(sql)
	public Page<Object[]> visitCusCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration){
		try {
			return visitStatDao.visitCusCountChartSql(pageNo, pageSize,startDate, endDate, utcStartDate, utcEndDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("ҵ��Ա�����ݷÿͻ���ͼ��(sql)����", e);
			return null;
		}
	}
	// ҵ��Ա�����ݷõص���ͼ��(sql)
	public Page<Object[]> visitPlaceCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration){
		try {
			return visitStatDao.visitPlaceCountChartSql(pageNo, pageSize,startDate, endDate, utcStartDate, utcEndDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("ҵ��Ա�����ݷõص���ͼ��(sql)����", e);
			return null;
		}
	}
	//ҵ��Ա�ݷÿͻ���������Աͳ��ͼ��(sql)
	public Page<Object[]> visitCusCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration){
		try {
			return visitStatDao.visitCusCountChartAll2(pageNo, pageSize,startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("ҵ��Ա�ݷÿͻ���������Աͳ��ͼ��(sql)����", e);
			return null;
		}
	}
	//ҵ��Ա�ݷõص���������Աͳ��ͼ��(sql)
	public Page<Object[]> visitPlaceCountChartAll(int pageNo, int pageSize, String startDate, String endDate,String searchValue, String deviceIds, int duration){
		try {
			return visitStatDao.visitPlaceCountChartAll(pageNo, pageSize,startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("ҵ��Ա�ݷõص���������Աͳ��ͼ��(sql)����", e);
			return null;
		}
	}
	//������λ��Ϣ
	public Page<Object[]> listVehicleGPS(String deviceIds, int pageNo, int pageSize, String startDate, String endDate){
		try {
			return visitStatDao.listVehicleGPS(deviceIds, pageNo, pageSize,startDate, endDate);
		} catch (Exception e) {
			logger.error("ҵ��Ա�ݷõص���������Աͳ��ͼ��(sql)����", e);
			return null;
		}
	}

	public Page<Object[]> listVisitCountTjByDeviceId2(String entCode, Long userId, int pageNo,
			int pageSize, String startDate, String endDate, String deviceId, int duration) {
		try {
			return visitStatDao.listVisitCountTjByDeviceId2(entCode, userId, pageNo, pageSize,
					startDate, endDate, deviceId, duration);
			
		} catch (Exception e) {
			logger.error("�鿴�ͻ��ݷ�ͳ�ƴ���", e);
			return null;
		}
	}
//�ؼ��ֲ�ѯ add by zhaofeng 2011-10-19
	public Page<Object[]> listVehicleGPS(String searchValue, String deviceIds,
			int pageNo, int pageSize, String startDate, String endDate) {
		try {
			return visitStatDao.listVehicleGPS(searchValue,deviceIds, pageNo, pageSize,startDate, endDate);
		} catch (Exception e) {
			logger.error("��λ��Ϣ��ѯ����", e);
			return null;
		}
	}

	
}
