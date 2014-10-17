package com.sosgps.wzt.dispatchmoney.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDispatchMoney;

public interface DispatchMoneyService {
	public Page<Object[]> listdispatchCondition(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(TDispatchMoney transientInstance);
	public void update(TDispatchMoney transientInstance);
	public void deleteAll(final String ids);
	
}
