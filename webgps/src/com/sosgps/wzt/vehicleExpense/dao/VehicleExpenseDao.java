package com.sosgps.wzt.vehicleExpense.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface VehicleExpenseDao {
	public Page<Object[]> listVehicleExpense(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	public Page<Object[]> listVehicleMsg(String deviceIds,
			String entCode, Long userId, int startint, int limitint,
			String startDate, String endDate, String searchValue, String carTypeInfoId);
	
}
