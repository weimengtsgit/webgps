package com.sosgps.wzt.dispatchmoney.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface DispatchMoneyDao {
	public Page<Object[]> listdispatchCondition(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	
}
