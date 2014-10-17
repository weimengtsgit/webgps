package com.sosgps.wzt.diarymarktj.dao;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiaryMark;

public interface DiaryMarkTjDao {
	
	public Page<Object[]> listDiaryMarkTj(String deviceIds,
			String entCode, Long userId, int startint, int limitint,
			String startDate, String endDate, String searchValue);
	
	public Page<Object[]> listDiaryTjDetail(String deviceId, String entCode, String diaryDate,
			int pageNo, int pageSize);
}
