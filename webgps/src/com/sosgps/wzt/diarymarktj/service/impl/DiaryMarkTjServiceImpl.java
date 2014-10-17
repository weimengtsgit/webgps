package com.sosgps.wzt.diarymarktj.service.impl;

import org.apache.log4j.Logger;
import com.sosgps.wzt.diarymarktj.dao.DiaryMarkTjDao;
import com.sosgps.wzt.diarymarktj.service.DiaryMarkTjService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiaryMark;

public class DiaryMarkTjServiceImpl implements DiaryMarkTjService {
	private DiaryMarkTjDao diaryMarkTjDao;
	private Logger logger = Logger.getLogger(DiaryMarkTjServiceImpl.class);

	public DiaryMarkTjDao getDiaryMarkTjDao() {
		return diaryMarkTjDao;
	}

	public void setDiaryMarkTjDao(DiaryMarkTjDao diaryMarkTjDao) {
		this.diaryMarkTjDao = diaryMarkTjDao;
	}

	public Page<Object[]> listDiaryMarkTj(String deviceIds, String entCode, Long userId, int startint, int limitint, String startDate, String endDate, String searchValue){
		try {
			return diaryMarkTjDao.listDiaryMarkTj(deviceIds, entCode, userId, startint, limitint, startDate, endDate, searchValue);
		} catch (Exception e) {
			logger.error("listDiaryMarkTj error", e);
			return null;
		}
	}
	
	public Page<Object[]> listDiaryTjDetail(String deviceId, String entCode, String diaryDate,
			int pageNo, int pageSize){
		try {
			return diaryMarkTjDao. listDiaryTjDetail(deviceId, entCode, diaryDate, pageNo, pageSize);
		} catch (Exception e) {
			logger.error("listDiaryTjDetail error", e);
			return null;
		}
	}
}
