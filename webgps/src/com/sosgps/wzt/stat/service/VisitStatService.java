package com.sosgps.wzt.stat.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:拜访统计业务接口
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午08:21:55
 */
public interface VisitStatService {
	// sos查看拜访统计
	public Page<Object[]> listCustomVisitCountTjByCustom(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, Long poiId, String deviceIds, int duration);
	// sos查看客户拜访统计
	public Page<Object[]> listVisitCountTjByDeviceId(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String deviceIds, int duration);
	// sos业务员考勤报表
	public Page<Object[]> listAttendanceReport(String deviceIds, String entCode, Long userId, int pageNo,
			int pageSize, String startDate, String endDate, String searchValue);
	// sos业务员考勤报表明细
	public Page<Object[]> listAttendanceReportDetail(String deviceId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue);
	// sos拜访次数统计
	public Page<Object[]> listVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration);
	// sos 客户拜访次数统计
	public Page<Object[]> listCustomVisitCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration);
	// sos 客户拜访次数统计 add by renxianliang
		Page<Object[]> listCustomVisitCountLing(int pageNo, int pageSize, String startDate, String endDate,
				String searchValue, String deviceIds, int duration);
	// sos 拜访统计excel
	public Page<Object[]> listCustomVisitTj(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue, String deviceIds, int duration);
	//拜访客户数
	public Page<Object[]> listVisitCustomerCountTj(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceIds, int duration);
	//拜访地点数
	public Page<Object[]> listVisitPlaceCountTj(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceIds, int duration);
	//拜访终端名
	public Page<Object[]> listVisitTerminal(String entCode, Long userId, int pageNo, int pageSize, String searchValue, String deviceIds);
	// 拜访次数图表
	public Page<Object[]> visitCountChart(int pageNo, int pageSize, Date startDate, Date endDate, String deviceId, int duration);
	// 拜访地点数地图显示数据
	public Page<Object[]> visitStatPlaceMap(int pageNo, int pageSize, String startDate, String endDate, String deviceId, int duration);
	//业务员出访统计报表(sql)
	public Page<Object[]> listVisitCountTjSql(String entCode, Long userId,int pageNo, int pageSize, String startDate, String endDate,String searchValue, String deviceIds, int duration);
	//业务员拜访次数所有人员统计图表(sql)
	public Page<Object[]> visitCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration);
	//业务员单个拜访次数图表(sql)
	public Page<Object[]> visitCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration);
	//业务员单个拜访客户数图表(sql)
	public Page<Object[]> visitCusCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration);
	// 业务员单个拜访地点数图表(sql)
	public Page<Object[]> visitPlaceCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration);
	//业务员拜访客户数所有人员统计图表(sql)
	public Page<Object[]> visitCusCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration);
	//业务员拜访地点数所有人员统计图表(sql)
	public Page<Object[]> visitPlaceCountChartAll(int pageNo, int pageSize, String startDate, String endDate,String searchValue, String deviceIds, int duration);
	//车辆定位信息
	public Page<Object[]> listVehicleGPS(String deviceIds, int pageNo, int pageSize, String startDate, String endDate);
	//车辆定位信息(加关键字查询)add by zhaofeng 
	public Page<Object[]> listVehicleGPS(String searchValue,String deviceIds, int pageNo, int pageSize, String startDate, String endDate);
	// sos查看客户拜访统计
	public Page<Object[]> listVisitCountTjByDeviceId2(String entCode, Long userId, int pageNo,
			int pageSize, String startDate, String endDate, String deviceId, int duration);
	
}
