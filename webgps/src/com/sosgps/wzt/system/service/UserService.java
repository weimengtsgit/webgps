package com.sosgps.wzt.system.service;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefUserRole;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.orm.TUser;

public interface UserService {
	public List hasAccount(String userAccount);
	public List hasAccount(String userAccount,String empCode);
	public List hasUserName(String userName, String empCode);
	public List hasAccountByPhoneNum(String phoneNum,String empCode);
	public void saveUser(TUser tUser,TRole tRole);
	public void updateUser(TUser tUser);//add by zhangwei
	public void updateUser(TUser tUser,TRole tRole,RefUserRole refUserRole);
	public TUser retrieveUser(Long id);
	public boolean deleteAll(Long[] ids);
	public List queryEntInfo(String empCode);//add by yanglei
	public List queryEntInfo2(String empCode);//add by yanglei
	public Page<TUser> listUser(String entCode, int startint, int limitint,
			String searchValue);
	public Page<TUser> listUserAdmin(String entCode, int startint, int limitint,
			String searchValue);
	public void deleteUsers(Long[] ids);
	public List queryUserRolesById(String userId);
	public List queryUserTgroupsById(String userId);
	public TUser findUserByLoginParam(final String empCode, final String userAccount, final String userPassword);
}
