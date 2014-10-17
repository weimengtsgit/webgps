package com.sosgps.wzt.stat.service.impl;

import org.apache.log4j.Logger;
import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.stat.dao.VisitStatDao;
import com.sosgps.wzt.stat.service.VisitStatService;

/**
 * @Title:拜访统计业务接口具体实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午08:22:16
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
			logger.error("查看拜访统计错误", e);
			return null;
		}
	}

	public Page<Object[]> listVisitCountTjByDeviceId(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String deviceId, int duration) {
		try {
			return visitStatDao.listVisitCountTjByDeviceId(entCode, userId, pageNo, pageSize,
					startDate, endDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("查看客户拜访统计错误", e);
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
			logger.error("业务员考勤报表错误", e);
			return null;
		}
	}

	public Page<Object[]> listAttendanceReportDetail(String deviceId,
			int pageNo, int pageSize, Date startDate, Date endDate, String searchValue) {
		try {
			
			return visitStatDao.listAttendanceReportDetail(deviceId, pageNo,
					pageSize, startDate, endDate, searchValue);
		} catch (Exception e) {
			logger.error("业务员考勤报表明细错误", e);
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
			logger.error("拜访次数统计错误", e);
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
			logger.error("拜访次数统计错误", e);
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
			logger.error("客户拜访次数统计错误", e);
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
			logger.error("客户拜访次数统计错误", e);
			return null;
		}
	}
	//拜访客户数
	public Page<Object[]> listVisitCustomerCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		try {
			return visitStatDao.listVisitCustomerCountTj(entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("拜访客户数统计错误", e);
			return null;
		}
	}
	//拜访地点数
	public Page<Object[]> listVisitPlaceCountTj(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds, int duration) {
		try {
			return visitStatDao.listVisitPlaceCountTj(entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("拜访地点数统计错误", e);
			return null;
		}
	}
	//拜访终端名
	public Page<Object[]> listVisitTerminal(String entCode, Long userId, int pageNo, int pageSize, String searchValue, String deviceIds) {
		try {
			return visitStatDao.listVisitTerminal(entCode, userId, pageNo, pageSize, searchValue, deviceIds);
		} catch (Exception e) {
			logger.error("拜访终端名错误", e);
			return null;
		}
	}
	//拜访次数图表
	public Page<Object[]> visitCountChart(int pageNo, int pageSize, Date startDate, Date endDate, String deviceId, int duration) {
		try {
			return visitStatDao.visitCountChart(pageNo, pageSize, startDate, endDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("拜访次数图表错误", e);
			return null;
		}
	}
	//拜访地点数地图显示数据
	public Page<Object[]> visitStatPlaceMap(int pageNo, int pageSize, String startDate, String endDate, String deviceId, int duration) {
		try {
			return visitStatDao.visitStatPlaceMap2(pageNo, pageSize, startDate, endDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("拜访地点数地图显示数据错误", e);
			return null;
		}
	}
	//业务员出访统计(sql)
	public Page<Object[]> listVisitCountTjSql(String entCode, Long userId,int pageNo, int pageSize, 
			String startDate, String endDate,String searchValue, String deviceIds, int duration) {
		try {
			return visitStatDao.listVisitCountTjSql2(entCode, userId, pageNo, pageSize,
					startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("业务员出访统计(sql)错误", e);
			return null;
		}
	}
	//业务员拜访次数所有人员统计图表(sql)
	public Page<Object[]> visitCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration){
		try {
			return visitStatDao.visitCountChartAll(pageNo, pageSize,startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("业务员拜访次数所有人员统计图表(sql)错误", e);
			return null;
		}
	}
	//业务员单个拜访次数图表(sql)
	public Page<Object[]> visitCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration){
		try {
			return visitStatDao.visitCountChartSql(pageNo, pageSize,startDate, endDate, utcStartDate, utcEndDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("业务员单个拜访次数图表(sql)错误", e);
			return null;
		}
	}
	//业务员单个拜访客户数图表(sql)
	public Page<Object[]> visitCusCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration){
		try {
			return visitStatDao.visitCusCountChartSql(pageNo, pageSize,startDate, endDate, utcStartDate, utcEndDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("业务员单个拜访客户数图表(sql)错误", e);
			return null;
		}
	}
	// 业务员单个拜访地点数图表(sql)
	public Page<Object[]> visitPlaceCountChartSql(int pageNo, int pageSize, String startDate, String endDate, String utcStartDate, String utcEndDate, String deviceId, int duration){
		try {
			return visitStatDao.visitPlaceCountChartSql(pageNo, pageSize,startDate, endDate, utcStartDate, utcEndDate, deviceId, duration);
		} catch (Exception e) {
			logger.error("业务员单个拜访地点数图表(sql)错误", e);
			return null;
		}
	}
	//业务员拜访客户数所有人员统计图表(sql)
	public Page<Object[]> visitCusCountChartAll(int pageNo, int pageSize, String startDate, String endDate, String searchValue, String deviceIds, int duration){
		try {
			return visitStatDao.visitCusCountChartAll2(pageNo, pageSize,startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("业务员拜访客户数所有人员统计图表(sql)错误", e);
			return null;
		}
	}
	//业务员拜访地点数所有人员统计图表(sql)
	public Page<Object[]> visitPlaceCountChartAll(int pageNo, int pageSize, String startDate, String endDate,String searchValue, String deviceIds, int duration){
		try {
			return visitStatDao.visitPlaceCountChartAll(pageNo, pageSize,startDate, endDate, searchValue, deviceIds, duration);
		} catch (Exception e) {
			logger.error("业务员拜访地点数所有人员统计图表(sql)错误", e);
			return null;
		}
	}
	//车辆定位信息
	public Page<Object[]> listVehicleGPS(String deviceIds, int pageNo, int pageSize, String startDate, String endDate){
		try {
			return visitStatDao.listVehicleGPS(deviceIds, pageNo, pageSize,startDate, endDate);
		} catch (Exception e) {
			logger.error("业务员拜访地点数所有人员统计图表(sql)错误", e);
			return null;
		}
	}

	public Page<Object[]> listVisitCountTjByDeviceId2(String entCode, Long userId, int pageNo,
			int pageSize, String startDate, String endDate, String deviceId, int duration) {
		try {
			return visitStatDao.listVisitCountTjByDeviceId2(entCode, userId, pageNo, pageSize,
					startDate, endDate, deviceId, duration);
			
		} catch (Exception e) {
			logger.error("查看客户拜访统计错误", e);
			return null;
		}
	}
//关键字查询 add by zhaofeng 2011-10-19
	public Page<Object[]> listVehicleGPS(String searchValue, String deviceIds,
			int pageNo, int pageSize, String startDate, String endDate) {
		try {
			return visitStatDao.listVehicleGPS(searchValue,deviceIds, pageNo, pageSize,startDate, endDate);
		} catch (Exception e) {
			logger.error("定位信息查询错误", e);
			return null;
		}
	}

	
}
