package com.sosgps.wzt.diary.dao;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiary;

public interface DiaryDao {
	public Page<Object[]> listDiaryByDeviceId(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceId);
	public Page<Object[]> listDiaryByDeviceIds(String entCode, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String termNames);
	public void save(TDiary tDiary);
	public TDiary findById( Long id);
	public boolean update(TDiary persistentInstance);
	public long getCurrentSequence();
	
}
