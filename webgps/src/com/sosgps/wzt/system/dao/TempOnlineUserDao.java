/**
 * 
 */
package com.sosgps.wzt.system.dao;

import java.util.List;

import com.sosgps.wzt.orm.TempOnlineUser;

/**
 * @author xiaojun.luan
 *
 */
public interface TempOnlineUserDao {
	public void deleteAll();
	public List findByExample(TempOnlineUser onlineUser);
	public void save(TempOnlineUser onlineUser);
	public void delete(TempOnlineUser onlineUser);

}
