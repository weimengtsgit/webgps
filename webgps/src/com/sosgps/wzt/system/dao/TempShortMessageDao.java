package com.sosgps.wzt.system.dao;

import java.util.List;

import com.sosgps.wzt.orm.TempShortMessage;

public interface TempShortMessageDao {
	public void save(TempShortMessage message);
	public void update(TempShortMessage message);
	public void deleteMessageByPhoneNum(String empCode,String phoneNum,int type);
	public List findByPhoneNumber(String empCode,String phoneNum,int type);
	public boolean deleteAll();
}
