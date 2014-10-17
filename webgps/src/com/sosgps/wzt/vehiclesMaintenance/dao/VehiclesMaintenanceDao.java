package com.sosgps.wzt.vehiclesMaintenance.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface VehiclesMaintenanceDao {
	public Page<Object[]> listVehiclesMaintenance(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	
}
