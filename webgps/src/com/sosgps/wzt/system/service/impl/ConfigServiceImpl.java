package com.sosgps.wzt.system.service.impl;

import com.sosgps.wzt.orm.Configure;
import com.sosgps.wzt.system.dao.ConfigDao;
import com.sosgps.wzt.system.service.ConfigService;

public class ConfigServiceImpl implements  ConfigService{
	
	public Configure getById(Long id){
		return this.configDao.findById(id); 
	}	
	
	public Configure getByKey(String key){
		return configDao.findByKey(key);
	}

	
	private ConfigDao configDao ;


	public ConfigDao getConfigDao() {
		return configDao;
	}

	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}
}
