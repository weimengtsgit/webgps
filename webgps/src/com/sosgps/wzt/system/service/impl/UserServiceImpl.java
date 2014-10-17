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
     * �����û��ʺŲ�ѯ�û�
     */
    public List hasAccount(String userAccount) {
        List list = null;
        try {
            list = this.tUserDao.findByUserAccount(userAccount);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��", ex);
        }
        return list;

    }

    /**
     * �����û��ʺš���ҵ�����ѯ�û�
     */
    public List hasAccount(String userAccount, String empCode) {
        List list = null;
        try {
            list = this.tUserDao.findByUserAccount(userAccount, empCode);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��", ex);
        }
        return list;
    }

    /**
     * �����û�������ҵ�����ѯ�û�
     */
    public List hasUserName(String userName, String empCode) {
        List list = null;
        try {
            list = this.tUserDao.findByUserName(userName, empCode);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��", ex);
        }
        return list;
    }

    /**
     * ������ҵ�����ѯ��ҵ��Ϣ
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
     * �����û��ֻ��Ų�ѯ�û�
     */
    public List hasAccountByPhoneNum(String phoneNum, String empCode) {
        List list = null;
        try {
            list = this.tUserDao.findByUserContact(phoneNum, empCode);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��", ex);
        }
        return list;

    }

    /**
     * �����û���Ϣ
     */
    public void saveUser(TUser tUser, TRole tRole) {

        try {
            // �����û���Ϣ
            this.tUserDao.save(tUser);

            // �����û���ɫ��ϵ
            RefUserRole userRole = new RefUserRole();
            userRole.setTRole(tRole);
            userRole.setTUser(tUser);
            this.refUserRoleDao.save(userRole);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��,����ԭ��" + re.getMessage(), re);
            System.out.println(re);
            throw re;
        } catch (WZTException ex) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��,����ԭ��" + ex.getMessage(), ex);
        }
    }

    public void updateUser(TUser tUser) {
        // �����û���Ϣ
        try {
            // �����û���Ϣ
            this.tUserDao.update(tUser);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��,����ԭ��" + ex.getMessage(), ex);
        }
    }

    /**
     * �����û���Ϣ
     */
    public void updateUser(TUser tUser, TRole tRole, RefUserRole refUserRole) {
        try {
            // �����û���Ϣ
            this.tUserDao.update(tUser);

            /** ���û���Ϣ���浽memcache��* */
            User userCached = new User();
            BeanUtils.copyProperties(tUser, userCached);
            Constants.USER_CACHE.set(String.valueOf(tUser.getId()), userCached,
                    10 * Memcache.EXPIRE_DAY);// ����10��

            // �����û���ɫ��ϵ
            // RefUserRole userRole = new RefUserRole();
            if (tRole != null && refUserRole != null) {
                refUserRole.setTRole(tRole);
                refUserRole.setTUser(tUser);

                this.refUserRoleDao.update(refUserRole);
            }
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��,����ԭ��" + re.getMessage(), re);
            throw re;
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: �û�����-�����û�ʧ��,����ԭ��" + ex.getMessage(), ex);

        }
        this.tUserDao.update(tUser);
    }

    public boolean deleteAll(Long[] ids) {
        return this.tUserDao.deleteAll(ids);

    }

    /**
     * ����ID����ָ���û�
     */
    public TUser retrieveUser(Long id) {
        TUser tUser = null;
        try {
            tUser = this.tUserDao.findUserAndRoleById(id);
        } catch (RuntimeException re) {
            SysLogger.sysLogger.error("E063: �û�����-ɾ���û�ʧ��", re);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: �û�����-ɾ���û�ʧ��", ex);
        }
        return tUser;
    }

    public Page<TUser> listUser(String entCode, int startint, int limitint, String searchValue) {
        try {
            return this.tUserDao.listUser(entCode, startint, limitint, searchValue);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: �û�����-��ѯ�û�ʧ��", ex);
        }
        return null;
    }

    // ������
    public Page<TUser> listUserAdmin(String entCode, int startint, int limitint, String searchValue) {
        try {
            return this.tUserDao.listUserAdmin(entCode, startint, limitint, searchValue);
        } catch (Exception ex) {
            SysLogger.sysLogger.error("E063: �û�����-��ѯ�û�ʧ��", ex);
        }
        return null;
    }

    public void deleteUsers(Long[] ids) {
        try {
            for (int i = 0; i < ids.length; i++) {
                Long id = ids[i];
                TUser user = tUserDao.findById(id);
                tUserDao.delete(user);

                /** ���û���Ϣ��memcache��ɾ��* */
                Constants.USER_CACHE.delete(String.valueOf(user.getId()));
            }
        } catch (Exception e) {
            SysLogger.sysLogger.error("E063: �û�����-ɾ���û�ʧ��", e);
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
            SysLogger.sysLogger.error("E063: �û�����-��ѯ�û���ɫ��Ϣʧ��", e);
        }
        return null;
    }

    public List queryUserTgroupsById(String userId) {
        try {
            Long id = Long.parseLong(userId);
            return tUserDao.queryUserTgroupsById(id);
        } catch (Exception e) {
            SysLogger.sysLogger.error("E063: �û�����-��ѯ�û��ɼ�����Ϣʧ��", e);
        }
        return null;
    }

    public TUser findUserByLoginParam(final String empCode, final String userAccount,
            final String userPassword) {
        try {
            return tUserDao.findUserByLoginParam(empCode, userAccount, userPassword);
        } catch (Exception e) {
            SysLogger.sysLogger.error("E063: �û�����-��ѯ�û���Ϣʧ��", e);
        }
        return null;
    }

}
