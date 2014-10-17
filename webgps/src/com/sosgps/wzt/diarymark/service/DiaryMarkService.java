package com.sosgps.wzt.diarymark.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiaryMark;

public interface DiaryMarkService {
	public Page<Object[]> listDiaryByUser(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public Page<Object[]> listDiaryByUsers(String entCode, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String termNames);
	public void save(TDiaryMark tDiaryMark);
	public TDiaryMark findById(Long diaryId);
	public boolean update(TDiaryMark tDiaryMark);
	public Page<TDiaryMark> listDiaryMarkByDeviceId(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceId);
	public Page<TDiaryMark> listDiaryMarkById(String entCode, Long userId,
			int i, int j, Long id);
	
}
