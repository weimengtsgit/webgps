package com.sosgps.wzt.system.dao;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TUser;

public interface TUserDao {
	public List findByUserAccount(Object userAccount);
	//public List findByUserAccount(Object userAccount,String empCode);
	public List findByUserAccount(String userAccount,String empCode);
	public List findByUserName(String userName,String empCode);
	public List findByUserContact(Object userContact,String empCode);
	public void save(TUser transientInstance);
	public void update(TUser transientInstance);
	public void delete(TUser transientInstance);
	public boolean deleteAll(Long[] ids);
	public TUser findById( java.lang.Long id);
	public TUser findUserAndRoleById( java.lang.Long id);
	public List queryEntInfoByCode(String empCode);//add by yanglei
	public List queryEntInfoByCode2(String empCode);
	public List findByEntId(Object empCode);
	public Page<TUser> listUser(String entCode, int startint, int limitint,
			String searchValue);
	public Page<TUser>listUserAdmin(String entCode, int startint, int limitint,
			String searchValue);
	public List queryUserRolesById(Long id);
	public List queryUserTgroupsById(Long id);
	public TUser findUserByLoginParam(final String empCode, final String userAccount, final String userPassword);	
}
