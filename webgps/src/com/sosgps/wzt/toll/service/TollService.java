package com.sosgps.wzt.toll.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TToll;

public interface TollService {
	public Page<Object[]> listToll(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(TToll transientInstance);
	public void update(TToll transientInstance);
	public void deleteAll(final String ids);
	
}
