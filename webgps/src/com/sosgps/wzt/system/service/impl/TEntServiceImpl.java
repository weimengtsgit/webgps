/**
 * 
 */
package com.sosgps.wzt.system.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import cn.net.sosgps.memcache.Memcache;
import cn.net.sosgps.memcache.bean.Ent;

import com.sosgps.v21.dao.EmailDao;
import com.sosgps.v21.dao.TargetDao;
import com.sosgps.v21.util.Constants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.poi.dao.LayerAndPoiDao;
import com.sosgps.wzt.system.dao.TEntDao;
import com.sosgps.wzt.system.dao.TRoleDao;
import com.sosgps.wzt.system.dao.TUserDao;
import com.sosgps.wzt.system.service.TEntService;
import com.sosgps.wzt.terminal.dao.TerminalDAO;

/**
 * @author xiaojun.luan
 * 
 */
public class TEntServiceImpl implements TEntService {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(TEntServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.wzt.system.service.EntService#findByEntCode(java.lang.String)
	 */
	public TEnt findByEntCode(String entCode) {
		// TODO Auto-generated method stub
		return tEntDao.findByEntCode(entCode);
	}

	public void save(TEnt tEnt) {
		// TODO Auto-generated method stub
		tEntDao.save(tEnt);
	}

	public void update(TEnt tEnt) {
		// TODO Auto-generated method stub
		tEntDao.update(tEnt);

		/** ����ҵ��Ϣ���浽memcache��* */
		Ent entCached = new Ent();
		BeanUtils.copyProperties(tEnt, entCached);
		Constants.ENT_CACHE.set(tEnt.getEntCode(), entCached,
				10 * Memcache.EXPIRE_DAY);// ��ҵʱ�䵽��,oracle_job���޸���ҵ��ʶΪ�����ڡ�,��˻���10��
	}

	private TEntDao tEntDao;

	private LayerAndPoiDao layerAndPoiDao;

	private TUserDao userDao;

	private TerminalDAO terminalDAO;

	private TRoleDao roleDao;

	private TargetDao targetDao;
	private EmailDao emailDao;

	public EmailDao getEmailDao() {
		return emailDao;
	}

	public void setEmailDao(EmailDao emailDao) {
		this.emailDao = emailDao;
	}

	public TargetDao getTargetDao() {
		return targetDao;
	}

	public void setTargetDao(TargetDao targetDao) {
		this.targetDao = targetDao;
	}

	public TEntDao getTEntDao() {
		return tEntDao;
	}

	public void setTEntDao(TEntDao tEntDao) {
		this.tEntDao = tEntDao;
	}

	public LayerAndPoiDao getLayerAndPoiDao() {
		return layerAndPoiDao;
	}

	public void setLayerAndPoiDao(LayerAndPoiDao layerAndPoiDao) {
		this.layerAndPoiDao = layerAndPoiDao;
	}

	public TUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(TUserDao userDao) {
		this.userDao = userDao;
	}

	public TerminalDAO getTerminalDAO() {
		return terminalDAO;
	}

	public void setTerminalDAO(TerminalDAO terminalDAO) {
		this.terminalDAO = terminalDAO;
	}

	public TRoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(TRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public List findAll() {
		// TODO Auto-generated method stub
		return tEntDao.findAll();
	}

	public Page<TEnt> listEnt(int pageNo, int pageSize, String searchValue) {
		try {
			return tEntDao.listEnt(pageNo, pageSize, searchValue);
		} catch (Exception e) {
			logger.error("��ѯ��ҵ�б����", e);
			return null;
		}
	}

	public Page<TEnt> listEnt2(int pageNo, int pageSize, String searchValue) {
		try {
			return tEntDao.listEnt2(pageNo, pageSize, searchValue);
		} catch (Exception e) {
			logger.error("��ѯ��ҵ�б����", e);
			return null;
		}
	}

	public Page<TEnt> listEnt3(int pageNo, int pageSize, String searchValue) {
		try {
			return tEntDao.listEnt3(pageNo, pageSize, searchValue);
		} catch (Exception e) {
			logger.error("��ѯ��ҵ�б����", e);
			return null;
		}
	}

	public void deleteEnts(String[] entCodes) {
		try {
			for (String entCode : entCodes) {
				TEnt ent = tEntDao.findByEntCode(entCode);
				if (ent != null) {
					// ɾ��ͼ��
					layerAndPoiDao.deleteLayersByEntCode(entCode);
					// ɾ���û�
					List<TUser> users = userDao.findByEntId(entCode);
					for (TUser user : users) {
						userDao.delete(user);
						/** ���û���Ϣ��memcache��ɾ��* */
						Constants.USER_CACHE.delete(String
								.valueOf(user.getId()));
					}
					// ɾ����ɫ
					List<TRole> roles = roleDao.findByEmpCode(entCode);
					for (TRole role : roles) {
						roleDao.delete(role);
					}
					// ɾ���ն�
					List<TTerminal> terminals = terminalDAO
							.findByEntCode(entCode);
					for (TTerminal terminal : terminals) {
						terminalDAO.delete(terminal);

						/** ���ն���Ϣ��memcache��ɾ��* */
						Constants.TERMINAL_CACHE.delete(terminal.getDeviceId());
					}
					// �߼�ɾ��ģ��
					targetDao.deleteKpi(entCode);
					// ɾ����������
					emailDao.removeEmails(entCode);
					// ɾ����ҵ
					tEntDao.delete(ent);

					/** ����ҵ��Ϣ��memcache��ɾ��* */
					Constants.ENT_CACHE.delete(entCode);
				}
			}
		} catch (Exception e) {
			logger.error("ɾ����ҵ����", e);
		}
	}
}
