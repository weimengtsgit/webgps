package com.sosgps.wzt.stat.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:�ݷ�ͳ��dao�ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 ����08:20:58
 */
public interface VisitStatDao {
	// sos�鿴�ݷ�ͳ��
	Page<Object[]> listCustomVisitCountTjByCustom(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, Long poiId,String deviceId, int duration);

	// sos�鿴�ͻ��ݷ�ͳ��
	Page<Object[]> listVisitCountTjByDeviceId(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String deviceId, int duration);
	// sosҵ��Ա���ڱ���
	Page<Object[]> listAttendanceReport(String deviceIds, String entCode, Long userId,
			int pageNo, int pageSize, String startDate, String endDate,
			String searchValue);
	// sosҵ��Ա���ڱ�����ϸ
	Page<Object[]> listAttendanceReportDetail(String deviceId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue);
	// sos�ݷô���ͳ��
	Page<Object[]> listVisitCountTj(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue,
			String deviceIds, int duration);
	// sos �ͻ��ݷô���ͳ��
	Page<Object[]> listCustomVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration);
	// sos �ͻ��ݷô���ͳ�� add by renxianliang
	Page<Object[]> listCustomVisitCountLing(int pageNo, int pageSize, String startDate, String endDate,
			String searchValue, String deviceIds, int duration);
	Page<Object[]> listCustomVisitTj(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue,
			String deviceIds, int duration);
	//�ݷÿͻ���
	Page<Object[]> listVisitCustomerCountTj(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue,
			String deviceIds, int duration);
	//�ݷõص���
	Page<Object[]> listVisitPlaceCountTj(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue,
			String deviceIds, int duration);
	//�ݷ��ն���
	Page<Object[]> listVisitTerminal(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds);
	//ҵ��Ա�ݷô�������ͳ��ͼ��
	Page<Object[]> visitCountChart(int pageNo, int pageSize, Date startDate, Date endDate,String deviceId, int duration);
	//�ݷõص�����ͼ��ʾ����
	Page<Object[]> visitStatPlaceMap(int pageNo, int pageSize, String startDate, String endDate,String deviceId, int duration);
	//ҵ��Ա����ͳ�Ʊ���(sql)
	Page<Object[]> listVisitCountTjSql(String entCode, Long userId,int pageNo, int pageSize, String startDate, String endDate,String searchValue, String deviceIds, int duration);
	//ҵ��Ա�ݷô���������Աͳ��ͼ��(sql)
	Page<Object[]> visitCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration);
	// ҵ��Ա�����ݷô���ͼ��(sql)
	Page<Object[]> visitCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration);
	// ҵ��Ա�����ݷÿͻ���ͼ��(sql)
	Page<Object[]> visitCusCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration);
	// ҵ��Ա�����ݷõص���ͼ��(sql)
	Page<Object[]> visitPlaceCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration);
	//ҵ��Ա�ݷÿͻ���������Աͳ��ͼ��(sql)
	Page<Object[]> visitCusCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration);
	//ҵ��Ա�ݷõص���������Աͳ��ͼ��(sql)
	Page<Object[]> visitPlaceCountChartAll(int pageNo, int pageSize, String startDate, String endDate,String searchValue, String deviceIds, int duration);
	//������λ��Ϣ��ѯ
	Page<Object[]> listVehicleGPS(String deviceIds, int pageNo, int pageSize, String startDate, String endDate);
	//������λ��Ϣ��ѯ(�ؼ���)add by zhaofeng 
	Page<Object[]> listVehicleGPS(String searchValue,String deviceIds, int pageNo, int pageSize, String startDate, String endDate);
	//ҵ��Ա����ͳ�Ʊ���(sql)
	Page<Object[]> listVisitCountTjSql2(String entCode, Long userId,int pageNo, int pageSize, String startDate, String endDate,String searchValue, String deviceIds, int duration);
	
	Page<Object[]> listVisitCountTjByDeviceId2(String entCode, Long userId,
			int startint, int limitint, String startDate, String endDate,
			String deviceId, int duration);
	//ҵ��Ա�ݷÿͻ���������Աͳ��ͼ��(sql)��
	Page<Object[]> visitCusCountChartAll2(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration);
	//�ݷõص�����ͼ��ʾ����(��)
	Page<Object[]> visitStatPlaceMap2(int pageNo, int pageSize, String startDate, String endDate,String deviceId, int duration);
	
}
