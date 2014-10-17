package com.sosgps.wzt.system.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.net.sosgps.memcache.Memcache;
import cn.net.sosgps.memcache.bean.User;

import com.sosgps.v21.util.Constants;
import com.sosgps.wzt.exception.WZTException;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefUserRole;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.system.dao.RefUserRoleDao;
import com.sosgps.wzt.system.dao.TUserDao;
import com.sosgps.wzt.system.service.UserService;

public class UserServiceImpl implements UserService {

    private TUserDao tUserDao;

    private RefUserRoleDao refUserRoleDao;

    /**
     * 按照用户帐号查询用户
     */
    public List hasAccount(String userAccount) {
        List list = null;
        try {
            list = this.tUserDao.findByUserAccount(userAccount);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: 用户管理-查找用户失败", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: 用户管理-查找用户失败", ex);
        }
        return list;

    }

    /**
     * 按照用户帐号、企业代码查询用户
     */
    public List hasAccount(String userAccount, String empCode) {
        List list = null;
        try {
            list = this.tUserDao.findByUserAccount(userAccount, empCode);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: 用户管理-查找用户失败", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: 用户管理-查找用户失败", ex);
        }
        return list;
    }

    /**
     * 按照用户名、企业代码查询用户
     */
    public List hasUserName(String userName, String empCode) {
        List list = null;
        try {
            list = this.tUserDao.findByUserName(userName, empCode);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: 用户管理-查找用户失败", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: 用户管理-查找用户失败", ex);
        }
        return list;
    }

    /**
     * 根据企业代码查询企业信息
     * 
     * @param empCode
     * @return
     */
    public List queryEntInfo(String empCode) {
        return this.tUserDao.queryEntInfoByCode(empCode);
    }

    public List queryEntInfo2(String empCode) {
        return this.tUserDao.queryEntInfoByCode2(empCode);
    }

    /**
     * 按照用户手机号查询用户
     */
    public List hasAccountByPhoneNum(String phoneNum, String empCode) {
        List list = null;
        try {
            list = this.tUserDao.findByUserContact(phoneNum, empCode);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: 用户管理-查找用户失败", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: 用户管理-查找用户失败", ex);
        }
        return list;

    }

    /**
     * 保存用户信息
     */
    public void saveUser(TUser tUser, TRole tRole) {

        try {
            // 保存用户信息
            this.tUserDao.save(tUser);

            // 保存用户角色关系
            RefUserRole userRole = new RefUserRole();
            userRole.setTRole(tRole);
            userRole.setTUser(tUser);
            this.refUserRoleDao.save(userRole);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: 用户管理-增加用户失败,错误原因：" + re.getMessage(), re);
            System.out.println(re);
            throw re;
        } catch (WZTException ex) {
            SysLogger.sysLogger.error("E063: 用户管理-增加用户失败,错误原因：" + ex.getMessage(), ex);
        }
    }

    public void updateUser(TUser tUser) {
        // 保存用户信息
        try {
            // 保存用户信息
            this.tUserDao.update(tUser);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: 用户管理-增加用户失败,错误原因：" + ex.getMessage(), ex);
        }
    }

    /**
     * 更新用户信息
     */
    public void updateUser(TUser tUser, TRole tRole, RefUserRole refUserRole) {
        try {
            // 保存用户信息
            this.tUserDao.update(tUser);

            /** 将用户信息保存到memcache中* */
            User userCached = new User();
            BeanUtils.copyProperties(tUser, userCached);
            Constants.USER_CACHE.set(String.valueOf(tUser.getId()), userCached,
                    10 * Memcache.EXPIRE_DAY);// 缓存10天

            // 保存用户角色关系
            // RefUserRole userRole = new RefUserRole();
            if (tRole != null && refUserRole != null) {
                refUserRole.setTRole(tRole);
                refUserRole.setTUser(tUser);

                this.refUserRoleDao.update(refUserRole);
            }
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: 用户管理-更新用户失败,错误原因：" + re.getMessage(), re);
            throw re;
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: 用户管理-更新用户失败,错误原因：" + ex.getMessage(), ex);

        }
        this.tUserDao.update(tUser);
    }

    public boolean deleteAll(Long[] ids) {
        return this.tUserDao.deleteAll(ids);

    }

    /**
     * 依据ID查找指定用户
     */
    public TUser retrieveUser(Long id) {
        TUser tUser = null;
        try {
            tUser = this.tUserDao.findUserAndRoleById(id);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: 用户管理-删除用户失败", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: 用户管理-删除用户失败", ex);
        }
        return tUser;
    }

    public Page<TUser> listUser(String entCode, int startint, int limitint, String searchValue) {
        try {
            return this.tUserDao.listUser(entCode, startint, limitint, searchValue);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: 用户管理-查询用户失败", ex);
        }
        return null;
    }

    // 任献良
    public Page<TUser> listUserAdmin(String entCode, int startint, int limitint, String searchValue) {
        try {
            return this.tUserDao.listUserAdmin(entCode, startint, limitint, searchValue);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: 用户管理-查询用户失败", ex);
        }
        return null;
    }

    public void deleteUsers(Long[] ids) {
        try {
            for (int i = 0; i < ids.length; i++) {
                Long id = ids[i];
                TUser user = tUserDao.findById(id);
                tUserDao.delete(user);

                /** 将用户信息从memcache中删除* */
                Constants.USER_CACHE.delete(String.valueOf(user.getId()));
            }
        } catch (Exception e) {
            SysLogger.sysLogger.error("E063: 用户管理-删除用户失败", e);
        }
    }

    public TUserDao getTUserDao() {
        return tUserDao;
    }

    public void setTUserDao(TUserDao userDao) {
        tUserDao = userDao;
    }

    public RefUserRoleDao getRefUserRoleDao() {
        return refUserRoleDao;
    }

    public void setRefUserRoleDao(RefUserRoleDao refUserRoleDao) {
        this.refUserRoleDao = refUserRoleDao;
    }

    public List queryUserRolesById(String userId) {
        try {
            Long id = Long.parseLong(userId);
            return tUserDao.queryUserRolesById(id);
        } catch (Exception e) {
            SysLogger.sysLogger.error("E063: 用户管理-查询用户角色信息失败", e);
        }
        return null;
    }

    public List queryUserTgroupsById(String userId) {
        try {
            Long id = Long.parseLong(userId);
            return tUserDao.queryUserTgroupsById(id);
        } catch (Exception e) {
            SysLogger.sysLogger.error("E063: 用户管理-查询用户可见组信息失败", e);
        }
        return null;
    }

    public TUser findUserByLoginParam(final String empCode, final String userAccount,
            final String userPassword) {
        try {
            return tUserDao.findUserByLoginParam(empCode, userAccount, userPassword);
        } catch (Exception e) {
            SysLogger.sysLogger.error("E063: 用户管理-查询用户信息失败", e);
        }
        return null;
    }

}
