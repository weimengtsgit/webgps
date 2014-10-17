package com.sosgps.wzt.system.dao;

import com.sosgps.wzt.orm.Configure;

public interface ConfigDao {


	public Configure findById(java.lang.Long id);

	public Configure findByKey(String key);
}
