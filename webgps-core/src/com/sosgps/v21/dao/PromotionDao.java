package com.sosgps.v21.dao;

import com.sosgps.v21.model.Promotion;
import com.sosgps.wzt.manage.util.Page;

public interface PromotionDao {
	
	// 明细查询
	public Page<Promotion> listPromotionDetails(String entCode, int pageNo,
			int pageSize, Long startDate, Long endDate, int approved,
			String poiName, String deviceIds);
	
	// 审核
	public boolean promotionApproved(Long[] ids, String entCode);

}
