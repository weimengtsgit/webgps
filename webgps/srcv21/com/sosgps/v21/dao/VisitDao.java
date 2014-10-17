package com.sosgps.v21.dao;
 
import java.util.List;
import java.util.Map;
import com.sosgps.v21.model.Visit;
import com.sosgps.wzt.manage.util.Page;

public interface VisitDao {

	public List<Visit> queryVisitsByCondition(Map<String, Object> paramMap,
			String entCode);

	public List<Object[]> queryVisitRank(Map<String, Object> paramMap,
			String entCode);

	public List<Long> getVisitCount(Long startTime, Long endTime, String entCode);

	public List<Object[]> getVisitsByTime(Long startTime, Long endTime,
			String entCode);

	public List<Long> getCusVisitCount(Long startTime, Long endTime,
			String entCode);

	public List<Object[]> getCusVisitsByTime(Long startTime, Long endTime,
			String entCode);
	
	public Page<Visit> listVisitDetails(String entCode,
			int pageNo, int pageSize, Long startDate,
			Long endDate, String poiName, String deviceIds);
	
	public Page<Object[]> listCustomVisitCountTj(String entCode,
			int pageNo, int pageSize, Long startDate, Long endDate,
			String searchValue, String deviceIds, Long duration);
	
	public Page<Object[]> listCustomVisitCountTjByCustom(String entCode,
			int startint, int limitint, Long startDate, Long endDate,
			Long poiId, String deviceIds, Long duration);
	
	public Page<Object[]> listVisitCountTjSql(String entCode,
			int startint, int limitint, Long startDate, Long endDate,
			String searchValue, String deviceIds, Long duration);
	
	public Page<Object[]> listVisitCountTjByCustom(String entCode,
			int startint, int limitint, Long startDate, Long endDate,
			String deviceId, Long duration);
	
}
