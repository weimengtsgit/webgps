package com.sosgps.v21.dao;
 
import java.math.BigDecimal;
import java.util.List;
import com.sosgps.v21.model.Cost;
import com.sosgps.wzt.manage.util.Page;

public interface CostDao {
	public List<Object[]> getCostsByTime(Long startTime, Long endTime,
			String entCode);
	public Page<Cost> listCostDetails(String entCode,
			int pageNo, int pageSize, Long startDate,
			Long endDate, int approved, String deviceIds);

	public boolean approved(Long[] ids, String entCode);
	public List<Object[]> getCostsByTime(Long startTime, Long endTime,
			String entCode, String deviceIds);
	
	public List<BigDecimal> getCostCount(Long startTime, Long endTime,
			String entCode, int isApproved, int isNotApproved,String deviceIds);
	
}
