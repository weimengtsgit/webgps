package com.sosgps.wzt.diarymark.service.impl;

import java.util.Date;
import org.apache.log4j.Logger;
import com.sosgps.wzt.diarymark.dao.DiaryMarkDao;
import com.sosgps.wzt.diarymark.service.DiaryMarkService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiaryMark;

public class DiaryMarkServiceImpl implements DiaryMarkService {
	private DiaryMarkDao diaryMarkDao;
	private Logger logger = Logger.getLogger(DiaryMarkServiceImpl.class);
	public DiaryMarkDao getDiaryMarkDao() {
		return diaryMarkDao;
	}
	public void setDiaryMarkDao(DiaryMarkDao diaryMarkDao) {
		this.diaryMarkDao = diaryMarkDao;
	}
	public Page<TDiaryMark> listDiaryMarkByDeviceId(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceId){
		try {
			return diaryMarkDao.listDiaryMarkByDeviceId(entCode, userId, pageNo, pageSize,startDate, endDate, searchValue, deviceId);
		} catch (Exception e) {
			logger.error("listDiaryMarkByDeviceId error", e);
			return null;
		}
	}
	
	public Page<Object[]> listDiaryByUser(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue){
		try {
			return diaryMarkDao.listDiaryByUser(entCode, userId, pageNo, pageSize,startDate, endDate, searchValue);
		} catch (Exception e) {
			logger.error("listDiaryByUser error", e);
			return null;
		}
	}
	public Page<Object[]> listDiaryByUsers(String entCode, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String termNames){
		try {
			return diaryMarkDao.listDiaryByUsers(entCode, pageNo, pageSize,startDate, endDate, searchValue, termNames);
		} catch (Exception e) {
			logger.error("listDiaryByUsers error", e);
			return null;
		}
	}

	public void save(TDiaryMark tDiaryMark){
		try {
			diaryMarkDao.save(tDiaryMark);
		} catch (Exception e) {
			logger.error("save TDiaryMark error", e);
		}
	}
	
	public boolean update(TDiaryMark tDiaryMark){
		try {
			return diaryMarkDao.update(tDiaryMark);
		} catch (Exception e) {
			logger.error("update TDiaryMark error", e);
			return false;
		}
	}
	public TDiaryMark findById(Long diaryId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Page<TDiaryMark> listDiaryMarkById(String entCode, Long userId, int pageNo, int pageSize, Long id){
		try {
			return diaryMarkDao.listDiaryMarkById(entCode, userId, pageNo, pageSize, id);
		} catch (Exception e) {
			logger.error("listDiaryByUsers error", e);
			return null;
		}
	}
	
}
