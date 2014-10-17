package com.sosgps.wzt.diary.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiary;
import com.sosgps.wzt.system.common.UserInfo;

public interface DiaryService {
	public Page<Object[]> listDiaryByDeviceId(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceId);
	public Page<Object[]> listDiaryByDeviceIds(String entCode, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceIds);
	public void save(TDiary tDiary);
	public TDiary findById(Long diaryId);
	public boolean update(TDiary tDiary);
	public boolean saveTDiary(UserInfo userInfo, String[] lngs, String[] lats,
			Date startTime, String deviceId, String entCode, Long userId,
			String termName, String title, String content);
	public boolean updateTDiary(String id, UserInfo userInfo, String[] lngs, String[] lats,
			String deviceId, String entCode, Long userId, 
			String termName, String title, String content);
}
