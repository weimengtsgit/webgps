package com.sosgps.v21.dao;
 
import java.math.BigDecimal;
import java.util.List;
import com.sosgps.v21.model.SignBill;
import com.sosgps.wzt.manage.util.Page;

public interface SignBillDao {
	public List<BigDecimal> getSignBillCount(Long startTime, Long endTime,
			String entCode, int isApproved, int isNotApproved, String poiName, String deviceIds);

	public List<Object[]> getSignBillsByTime(Long startTime, Long endTime,
			String entCode, String poiName, String deviceIds);
	
	public Page<SignBill> listSignBillDetails(String entCode,
			int pageNo, int pageSize, Long startDate,
			Long endDate, int approved, String poiName, String deviceIds);
	
	public boolean approved(Long[] ids, String entCode);
}
