package com.sosgps.wzt.system.dao.hibernate;

import java.util.List;

import com.sosgps.wzt.orm.Configure;
import com.sosgps.wzt.orm.ConfigureDAO;
import com.sosgps.wzt.system.dao.ConfigDao;

public class ConfigDaoImpl  extends ConfigureDAO implements ConfigDao{
	 public Configure findById( java.lang.Long id){
		 return this.findById(id);
	 }

	public Configure findByKey(String key) {
		List list=super.findByCKey(key);
		if(list!=null && list.size()>0){
			return (Configure)list.get(0);
		}
		return null;
	}
	 
}
