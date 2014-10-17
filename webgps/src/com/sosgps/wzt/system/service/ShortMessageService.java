/**
 * 
 */
package com.sosgps.wzt.system.service;

import com.sosgps.wzt.orm.TempShortMessage;

/**
 * @author xiaojun.luan
 *
 */
public interface ShortMessageService {
	public boolean sendDynamicPassword(String empCode,String phoneNumber,int passwordLength,int type);
	public boolean deleteTempDynamicPassword(String empCode,String phoneNumber,int type);
	public TempShortMessage findByPhone(String empCode,String phoneNumber,int type);
	
	public static final int LOGIN_TYPE=0;
	public static final int MODIFY_PASSWORD_TYPE=1;
	public static final int DYNAMIC_MESSAGE_LENGTH=10;
	public static final int MODIFY_PASSWORD_LENGTH=6;
}
