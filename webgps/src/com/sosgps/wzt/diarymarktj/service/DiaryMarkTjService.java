package com.sosgps.wzt.diarymarktj.service;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiaryMark;

public interface DiaryMarkTjService {
	public Page<Object[]> listDiaryMarkTj(String deviceIds, String entCode, Long userId, int startint, int limitint, String startDate, String endDate, String searchValue);
	public Page<Object[]> listDiaryTjDetail(String deviceId, String entCode, String diaryDate,
			int pageNo, int pageSize);
	
}
