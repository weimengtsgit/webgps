package com.sosgps.wzt.diarymark.dao;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiary;
import com.sosgps.wzt.orm.TDiaryMark;

public interface DiaryMarkDao {
	public Page<Object[]> listDiaryByUser(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public Page<Object[]> listDiaryByUsers(String entCode, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String termNames);
	public void save(TDiaryMark tDiaryMark);
	public TDiaryMark findById( Long id);
	public boolean update(TDiaryMark persistentInstance);
	public Page<TDiaryMark> listDiaryMarkByDeviceId(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceId);
	public void deleteByDeviceId(final String diaryId);
	public Page<TDiaryMark> listDiaryMarkById(String entCode, Long userId,
			int pageNo, int pageSize, Long id);
}
