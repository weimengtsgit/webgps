package com.sosgps.wzt.vehiclesMaintenance.service.impl;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TVehiclesMaintenance;
import com.sosgps.wzt.vehiclesMaintenance.dao.hibernate.VehiclesMaintenanceDaoImpl;
import com.sosgps.wzt.vehiclesMaintenance.service.VehiclesMaintenanceService;

public class VehiclesMaintenanceServiceImpl implements VehiclesMaintenanceService {

	public VehiclesMaintenanceDaoImpl getVehiclesMaintenanceDaoImpl() {
		return vehiclesMaintenanceDaoImpl;
	}

	public void setVehiclesMaintenanceDaoImpl(
			VehiclesMaintenanceDaoImpl vehiclesMaintenanceDaoImpl) {
		this.vehiclesMaintenanceDaoImpl = vehiclesMaintenanceDaoImpl;
	}

	private VehiclesMaintenanceDaoImpl vehiclesMaintenanceDaoImpl;

	public Page<Object[]> listVehiclesMaintenance(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue) {
		return vehiclesMaintenanceDaoImpl.listVehiclesMaintenance(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}
	
	public void save(TVehiclesMaintenance transientInstance) {
		vehiclesMaintenanceDaoImpl.save(transientInstance);
	}
	
	public void update(TVehiclesMaintenance transientInstance) {
		vehiclesMaintenanceDaoImpl.update(transientInstance);
	}
	
	public void deleteAll(final String ids) {
		vehiclesMaintenanceDaoImpl.deleteAll(ids);
	}
}
