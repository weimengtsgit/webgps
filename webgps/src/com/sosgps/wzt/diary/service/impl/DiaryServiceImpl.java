package com.sosgps.wzt.diary.service.impl;

import java.util.Date;
import org.apache.log4j.Logger;
import com.sosgps.wzt.diary.dao.DiaryDao;
import com.sosgps.wzt.diary.service.DiaryService;
import com.sosgps.wzt.diarymark.dao.DiaryMarkDao;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiary;
import com.sosgps.wzt.orm.TDiaryMark;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;

public class DiaryServiceImpl implements DiaryService {
	private DiaryDao diaryDao;
	private DiaryMarkDao diaryMarkDao;
	public DiaryMarkDao getDiaryMarkDao() {
		return diaryMarkDao;
	}
	public void setDiaryMarkDao(DiaryMarkDao diaryMarkDao) {
		this.diaryMarkDao = diaryMarkDao;
	}
	
	private Logger logger = Logger.getLogger(DiaryServiceImpl.class);
	
	public DiaryDao getDiaryDao() {
		return diaryDao;
	}
	public void setDiaryDao(DiaryDao diaryDao) {
		this.diaryDao = diaryDao;
	}
	public Page<Object[]> listDiaryByDeviceId(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceId){
		try {
			return diaryDao.listDiaryByDeviceId(entCode, userId, pageNo, pageSize,startDate, endDate, searchValue, deviceId);
		} catch (Exception e) {
			logger.error("listDiaryByUser error", e);
			return null;
		}
	}
	public Page<Object[]> listDiaryByDeviceIds(String entCode, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String termNames){
		try {
			return diaryDao.listDiaryByDeviceIds(entCode, pageNo, pageSize,startDate, endDate, searchValue, termNames);
		} catch (Exception e) {
			logger.error("listDiaryByUsers error", e);
			return null;
		}
	}
	public TDiary findById(Long diaryId){
		try {
			return diaryDao.findById(diaryId);
		} catch (Exception e) {
			logger.error("findById error", e);
			return null;
		}
	}
	public void save(TDiary tDiary){
		try {
			diaryDao.save(tDiary);
		} catch (Exception e) {
			logger.error("listDiaryByUser error", e);
		}
	}
	public boolean update(TDiary tDiary){
		try {
			return diaryDao.update(tDiary);
		} catch (Exception e) {
			logger.error("listDiaryByUser error", e);
			return false;
		}
	}
	
	public boolean saveTDiary(UserInfo userInfo, String[] lngs, String[] lats, Date startTime, 
			String deviceId, String entCode, Long userId, 
			String termName, String title, String content){
		try {
			long seq = diaryDao.getCurrentSequence();
			// 保存
			TDiary tDiary = new TDiary();
			tDiary.setId(seq);
			tDiary.setTermName(termName);
			tDiary.setTitle(title);
			tDiary.setContent(content);
			tDiary.setCreateDate(new Date());
			tDiary.setModifyDate(new Date());
			tDiary.setEntCode(entCode);
			tDiary.setUserId(userId);
			tDiary.setDiaryDate(startTime);
			tDiary.setDeviceId(deviceId);
			// save
			diaryDao.save(tDiary);
			// 日志实例
			TOptLog tOptLog = new TOptLog();
			// 日志记录
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.DIARY_SET);
			tOptLog.setFunCType(LogConstants.DIARY_SET_ADD_USER);
			tOptLog.setResult(1L);
			tOptLog.setOptDesc("添加业务员日志成功! ");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			
			if(lngs.length == lats.length && lngs.length >= 1 && lngs[0] != null && lngs[0].length() > 0){
				for(int i = 0;i < lngs.length;i++){
					TDiaryMark tDiaryMark = new TDiaryMark();
					tDiaryMark.setDiaryId(seq);
					tDiaryMark.setLongitude(Double.parseDouble(lngs[i]));
					tDiaryMark.setLatitude(Double.parseDouble(lats[i]));
					tDiaryMark.setCreateDate(new Date());
					tDiaryMark.setChangeDate(new Date());
					tDiaryMark.setDiaryDate(startTime);
					tDiaryMark.setDeviceId(deviceId);
					tDiaryMark.setEntCode(entCode);
					tDiaryMark.setUserId(userId);
					tDiaryMark.setIsArrive((long)0);
					tDiaryMark.setDistance((long)200);
					diaryMarkDao.save(tDiaryMark);
				}
			}
			
			return true;
		} catch (Exception e) {
			logger.error("saveTDiary error", e);
			return false;
		}
	}
	
	public boolean updateTDiary(String id, UserInfo userInfo, String[] lngs, String[] lats,
			String deviceId, String entCode, Long userId, 
			String termName, String title, String content){
		try {
			TDiary tDiary = diaryDao.findById(Long.parseLong(id));
			//修改
			tDiary.setTitle(title);
			tDiary.setContent(content);
			tDiary.setModifyDate(new Date());
			diaryDao.update(tDiary);
			// 日志实例
			TOptLog tOptLog = new TOptLog();
			// 日志记录
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.DIARY_SET);
			tOptLog.setFunCType(LogConstants.DIARY_SET_UPDATE_USER);
			tOptLog.setResult(1L);
			tOptLog.setOptDesc("修改业务员日志成功! ");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			diaryMarkDao.deleteByDeviceId(id);

			if(lngs.length == lats.length && lngs.length >= 1 && lngs[0] != null && lngs[0].length() > 0){
				for(int i = 0;i < lngs.length;i++){
					TDiaryMark tDiaryMark = new TDiaryMark();
					tDiaryMark.setDiaryId(Long.parseLong(id));
					tDiaryMark.setLongitude(Double.parseDouble(lngs[i]));
					tDiaryMark.setLatitude(Double.parseDouble(lats[i]));
					tDiaryMark.setCreateDate(new Date());
					tDiaryMark.setChangeDate(new Date());
					tDiaryMark.setDiaryDate(tDiary.getDiaryDate());
					tDiaryMark.setDeviceId(deviceId);
					tDiaryMark.setEntCode(entCode);
					tDiaryMark.setUserId(userId);
					tDiaryMark.setIsArrive((long)0);
					tDiaryMark.setDistance((long)200);
					diaryMarkDao.save(tDiaryMark);
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("updateTDiary error", e);
			return false;
		}
	}
}
