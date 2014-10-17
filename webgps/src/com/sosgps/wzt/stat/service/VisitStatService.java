package com.sosgps.wzt.stat.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:�ݷ�ͳ��ҵ��ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 ����08:21:55
 */
public interface VisitStatService {
	// sos�鿴�ݷ�ͳ��
	public Page<Object[]> listCustomVisitCountTjByCustom(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, Long poiId, String deviceIds, int duration);
	// sos�鿴�ͻ��ݷ�ͳ��
	public Page<Object[]> listVisitCountTjByDeviceId(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String deviceIds, int duration);
	// sosҵ��Ա���ڱ���
	public Page<Object[]> listAttendanceReport(String deviceIds, String entCode, Long userId, int pageNo,
			int pageSize, String startDate, String endDate, String searchValue);
	// sosҵ��Ա���ڱ�����ϸ
	public Page<Object[]> listAttendanceReportDetail(String deviceId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue);
	// sos�ݷô���ͳ��
	public Page<Object[]> listVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration);
	// sos �ͻ��ݷô���ͳ��
	public Page<Object[]> listCustomVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration);
	// sos �ͻ��ݷô���ͳ�� add by renxianliang
		Page<Object[]> listCustomVisitCountLing(int pageNo, int pageSize, String startDate, String endDate,
				String searchValue, String deviceIds, int duration);
	// sos �ݷ�ͳ��excel
	public Page<Object[]> listCustomVisitTj(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue, String deviceIds, int duration);
	//�ݷÿͻ���
	public Page<Object[]> listVisitCustomerCountTj(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceIds, int duration);
	//�ݷõص���
	public Page<Object[]> listVisitPlaceCountTj(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceIds, int duration);
	//�ݷ��ն���
	public Page<Object[]> listVisitTerminal(String entCode, Long userId, int pageNo, int pageSize, String searchValue, String deviceIds);
	// �ݷô���ͼ��
	public Page<Object[]> visitCountChart(int pageNo, int pageSize, Date startDate, Date endDate, String deviceId, int duration);
	// �ݷõص�����ͼ��ʾ����
	public Page<Object[]> visitStatPlaceMap(int pageNo, int pageSize, String startDate, String endDate, String deviceId, int duration);
	//ҵ��Ա����ͳ�Ʊ���(sql)
	public Page<Object[]> listVisitCountTjSql(String entCode, Long userId,int pageNo, int pageSize, String startDate, String endDate,String searchValue, String deviceIds, int duration);
	//ҵ��Ա�ݷô���������Աͳ��ͼ��(sql)
	public Page<Object[]> visitCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration);
	//ҵ��Ա�����ݷô���ͼ��(sql)
	public Page<Object[]> visitCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration);
	//ҵ��Ա�����ݷÿͻ���ͼ��(sql)
	public Page<Object[]> visitCusCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration);
	// ҵ��Ա�����ݷõص���ͼ��(sql)
	public Page<Object[]> visitPlaceCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration);
	//ҵ��Ա�ݷÿͻ���������Աͳ��ͼ��(sql)
	public Page<Object[]> visitCusCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration);
	//ҵ��Ա�ݷõص���������Աͳ��ͼ��(sql)
	public Page<Object[]> visitPlaceCountChartAll(int pageNo, int pageSize, String startDate, String endDate,String searchValue, String deviceIds, int duration);
	//������λ��Ϣ
	public Page<Object[]> listVehicleGPS(String deviceIds, int pageNo, int pageSize, String startDate, String endDate);
	//������λ��Ϣ(�ӹؼ��ֲ�ѯ)add by zhaofeng 
	public Page<Object[]> listVehicleGPS(String searchValue,String deviceIds, int pageNo, int pageSize, String startDate, String endDate);
	// sos�鿴�ͻ��ݷ�ͳ��
	public Page<Object[]> listVisitCountTjByDeviceId2(String entCode, Long userId, int pageNo,
			int pageSize, String startDate, String endDate, String deviceId, int duration);
	
}
