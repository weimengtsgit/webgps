package com.sosgps.wzt.system.service;

import com.sosgps.wzt.orm.Configure;

public interface ConfigService {
	public static final String PARA_LOGIN = "PARA_LOGIN";
	
	
	public Configure getById(Long id); 
	public Configure getByKey(String key);
}
