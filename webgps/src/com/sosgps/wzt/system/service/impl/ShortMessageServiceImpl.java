/**
 * 
 */
package com.sosgps.wzt.system.service.impl;

import java.util.List;

import com.sosgps.wzt.orm.TempShortMessage;
import com.sosgps.wzt.system.dao.TempShortMessageDao;
import com.sosgps.wzt.system.service.ShortMessageService;
import com.sosgps.wzt.system.util.MessageTool;
import com.sosgps.wzt.util.RandomPassword;

/**
 * @author xiaojun.luan
 * 
 */
public class ShortMessageServiceImpl implements ShortMessageService {

	public boolean sendDynamicPassword(String empCode, String phoneNumber,
			int passwordLength, int type) {
		RandomPassword rp = new RandomPassword();
		String password = rp.generatePassword(passwordLength);
		com.sosgps.wzt.log.SysLogger.sysLogger.info("动态短信：手机号：" + phoneNumber
				+ "密码：" + password);
		if (!savePassword(empCode, password, phoneNumber, type))
			return false;
		return MessageTool.sendMessage(password, phoneNumber);
	}

	private boolean savePassword(String empCode, String dynamicPassword,
			String phoneNumber, int type) {
		List list = tempShortMessageDao.findByPhoneNumber(empCode, phoneNumber,
				type);
		boolean isExist = false;
		TempShortMessage tempShortMessage = null;
		if (list.size() > 0) {
			tempShortMessage = (TempShortMessage) list.get(0);
			isExist = true;
		} else {
			tempShortMessage = new TempShortMessage();
			isExist = false;
		}

		tempShortMessage.setCreateTime(System.currentTimeMillis());
		tempShortMessage.setDynamicPassword(dynamicPassword);
		tempShortMessage.setPhoneNumber(phoneNumber);
		tempShortMessage.setType(new Long(type));
		tempShortMessage.setEmpCode(empCode);
		// tempShortMessage.setUserId(userId);
		if (isExist) {
			tempShortMessageDao.update(tempShortMessage);
		} else {
			tempShortMessageDao.save(tempShortMessage);
		}
		return true;
	}

	public boolean deleteTempDynamicPassword(String empCode,
			String phoneNumber, int type) {
		tempShortMessageDao.deleteMessageByPhoneNum(empCode, phoneNumber, type);
		return true;
	}

	public TempShortMessage findByPhone(String empCode, String phoneNumber,
			int type) {
		// TODO Auto-generated method stub
		List list = tempShortMessageDao.findByPhoneNumber(empCode, phoneNumber,
				type);
		if (list == null || list.size() < 1) {
			return null;
		} else {
			return (TempShortMessage) list.get(0);
		}
	}

	private TempShortMessageDao tempShortMessageDao;

	public TempShortMessageDao getTempShortMessageDao() {
		return tempShortMessageDao;
	}

	public void setTempShortMessageDao(TempShortMessageDao tempShortMessageDao) {
		this.tempShortMessageDao = tempShortMessageDao;
	}

}
