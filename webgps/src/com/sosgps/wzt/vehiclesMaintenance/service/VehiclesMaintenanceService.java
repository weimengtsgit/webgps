package com.sosgps.wzt.vehiclesMaintenance.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TVehiclesMaintenance;

public interface VehiclesMaintenanceService {
	public Page<Object[]> listVehiclesMaintenance(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(TVehiclesMaintenance transientInstance);
	public void update(TVehiclesMaintenance transientInstance);
	public void deleteAll(final String ids);
	
}
