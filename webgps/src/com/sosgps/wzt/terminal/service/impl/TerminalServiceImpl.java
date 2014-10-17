package com.sosgps.wzt.terminal.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import cn.net.sosgps.memcache.Memcache;
import cn.net.sosgps.memcache.bean.Terminal;

import com.sosgps.v21.util.Constants;
import com.sosgps.wzt.directl.TerminalFactory;
import com.sosgps.wzt.group.dao.GroupDAO;
import com.sosgps.wzt.manage.terminal.dao.TerminalManageDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ModuleParamConfig;
import com.sosgps.wzt.orm.RefTermGroup;
import com.sosgps.wzt.orm.RefTermGroupDAO;
import com.sosgps.wzt.orm.RefTermGroupId;
import com.sosgps.wzt.orm.TEntDAO;
import com.sosgps.wzt.orm.TEntTermtype;
import com.sosgps.wzt.orm.TEntTermtypeDAO;
import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TSpeedCase;
import com.sosgps.wzt.orm.TStructions;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TVehicleMsg;
import com.sosgps.wzt.orm.TermParamConfig;
import com.sosgps.wzt.struction.dao.SpeedCaseDao;
import com.sosgps.wzt.struction.dao.StructionDAO;
import com.sosgps.wzt.terminal.bean.TTerminalBean;
import com.sosgps.wzt.terminal.dao.TerminalDAO;
import com.sosgps.wzt.terminal.dao.VehicleMsgDAO;
import com.sosgps.wzt.terminal.form.TerminalForm;
import com.sosgps.wzt.terminal.service.TerminalService;
import com.sosgps.wzt.util.CharTools;

/**
 * 2009-06-18 �����ն�ͬʱд�봴��ʱ�� //modfiy 2009-06-18 Ĭ��Ϊ����״̬
 */
public class TerminalServiceImpl implements TerminalService {

	private GroupDAO tTargetGroupDao;

	private TerminalDAO tTargetObjectDao;

	private TEntTermtypeDAO tTerminalTypeDao;

	private RefTermGroupDAO RTermGroupDAO;

	private TEntDAO tEntDAO;

	private StructionDAO structionDAO;

	private SpeedCaseDao speedCaseDao;

	private TerminalManageDao terminalManageDao;
	private VehicleMsgDAO vehicleMsgDAO;

	public VehicleMsgDAO getVehicleMsgDAO() {
		return this.vehicleMsgDAO;
	}

	public void setVehicleMsgDAO(VehicleMsgDAO vehicleMsgDAO) {
		this.vehicleMsgDAO = vehicleMsgDAO;
	}

	public TerminalManageDao getTerminalManageDao() {
		return terminalManageDao;
	}

	public void setTerminalManageDao(TerminalManageDao terminalManageDao) {
		this.terminalManageDao = terminalManageDao;
	}

	public SpeedCaseDao getSpeedCaseDao() {
		return speedCaseDao;
	}

	public void setSpeedCaseDao(SpeedCaseDao speedCaseDao) {
		this.speedCaseDao = speedCaseDao;
	}

	public StructionDAO getStructionDAO() {
		return structionDAO;
	}

	public void setStructionDAO(StructionDAO structionDAO) {
		this.structionDAO = structionDAO;
	}

	/**
	 * @param entDAO
	 *            the tEntDAO to set
	 */
	public void setTEntDAO(TEntDAO entDAO) {
		tEntDAO = entDAO;
	}

	public RefTermGroupDAO getRTermGroupDAO() {
		return this.RTermGroupDAO;
	}

	public void setRTermGroupDAO(RefTermGroupDAO termGroupDAO) {
		this.RTermGroupDAO = termGroupDAO;
	}

	public TEntTermtypeDAO getTTerminalTypeDao() {
		return tTerminalTypeDao;
	}

	public void setTTerminalTypeDao(TEntTermtypeDAO terminalTypeDao) {
		tTerminalTypeDao = terminalTypeDao;
	}

	public GroupDAO getTTargetGroupDao() {
		return tTargetGroupDao;
	}

	public void setTTargetGroupDao(GroupDAO targetGroupDao) {
		tTargetGroupDao = targetGroupDao;
	}

	public TerminalDAO getTTargetObjectDao() {
		return tTargetObjectDao;
	}

	public void setTTargetObjectDao(TerminalDAO targetObjectDao) {
		tTargetObjectDao = targetObjectDao;
	}

	/**
	 * ����groupId�õ�TTargetGroup
	 * 
	 * @param groupId
	 * @return
	 */
	public TTermGroup getTTargetGroupByGroupId(Long groupId) {
		return tTargetGroupDao.findById(groupId);
	}

	/**
	 * �����/���Žṹ
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getTTargetGroup(String empCode) {
		return tTargetGroupDao.getTTargetGroup(empCode);
	}

	/**
	 * ����groupId�����������TTargetObject�б����
	 */
	@SuppressWarnings("rawtypes")
	public List getGroupTTargetObject(Long groupId) {
		return tTargetObjectDao.findByGroupId(groupId);
	}

	/**
	 * ����groupId����������TTargetObject�б����
	 */
	@SuppressWarnings("rawtypes")
	public List getUnGroupTTargetObject(Long groupId) {
		return tTargetObjectDao.findNoGroupId(groupId);
	}

	/**
	 * ����TTargetObject
	 * 
	 * @param tTargetObject
	 */
	public void saveTTargetObject(TTerminal tTargetObject) {
		tTargetObjectDao.save(tTargetObject);
	}

	/**
	 * add by yanglei 2009-03-25 14:14:00 ����Bossͬ���ն�����
	 * 
	 * @param entCode
	 * @param deviceId
	 * @param locateType
	 *            - ��λ���ͣ�0:LBS 1:GPS';
	 * @return
	 */
	public String insertTerminal(String entCode, String deviceId,
			String locateType, String areaName) {
		TTerminal transientInstance = new TTerminal();
		transientInstance.setEntCode(entCode);
		transientInstance.setTermName(deviceId);
		transientInstance.setDeviceId(deviceId);
		transientInstance.setSimcard(deviceId);
		transientInstance.setSubCompany(areaName);
		// modfiy zhangwei 2009-06-18 Ĭ��Ϊ����״̬
		// transientInstance.setUsageFlag(new Long(1));
		transientInstance.setUsageFlag(0L);
		transientInstance.setLocateType(locateType);
		transientInstance.setIsAllocate("0");
		// add zhangwei 2009-06-18 ����
		transientInstance.setCrtdate(new Date());
		transientInstance.setUsageFlag(0L);

		TTerminal term = this.tTargetObjectDao.findById(deviceId);
		if (term != null && !entCode.equals(term.getEntCode())) {
			return "2007";
		}
		if (term != null && entCode.equals(term.getEntCode())) {
			return "2006";
		}
		try {
			tTargetObjectDao.save(transientInstance);
		} catch (Exception ex) {
			return "2005";
		}
		return "0000";
	}

	/**
	 * add by yanglei ɾ��Bossͬ���ն�
	 * 
	 * @param entCode
	 * @param phoneNumber
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String deleteTerinal(String entCode, String phoneNumber) {
		TTerminal transientInstance = new TTerminal();
		// System.out.println("entCode===:" + entCode + ";phoneNumber===:" +
		// phoneNumber);
		transientInstance.setEntCode(entCode);
		transientInstance.setDeviceId(phoneNumber);
		List termList = tTargetObjectDao.findTermByTerminal(transientInstance);
		if (termList == null || termList.size() == 0) {
			return "2009";// ���ų�Ա�ڸü����в����ڣ�����ʧ��
		}
		List entList = tEntDAO.findByProperty("entCode", entCode);
		// System.out.println("EntList is ===: " + entList);
		if (entList == null || entList.size() == 0) {
			return "2004";// �ü��Ų����ڣ�����ʧ��
		}
		try {
			String[] ids = { phoneNumber };
			this.delTTargetObjectByIds(ids);
		} catch (Exception ex) {
			return "2005";// ���ſ������������ʹ���
		}
		return "0000";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean save(TerminalForm form) {
		try {
			Long groupId = form.getGroupId();
			String name = form.getTermName();
			String simId = form.getSimcard();

			String gpssn = form.getDeviceId();
			String url = form.getImgUrl();
			String entCode = form.getEntCode();
			String typeCode = form.getTypeCode();
			String locateType = form.getLocateType();
			String carTypeId = form.getCarTypeId();
			String isAllocate = form.getIsAllocate();
			String driverNum = form.getDriverNumber();
			TEntTermtype type = new TEntTermtype();
			type.setTypeCode(typeCode);

			TTermGroup group = new TTermGroup();
			group.setId(groupId);

			TTerminal tTargetObject = new TTerminal();

			tTargetObject.setDeviceId(gpssn);
			tTargetObject.setImgUrl(url);
			tTargetObject.setEntCode(entCode);
			tTargetObject.setSimcard(simId);
			tTargetObject.setTermName(name);
			tTargetObject.setTEntTermtype(type);
			tTargetObject.setLocateType(locateType);
			if (!locateType.equals("0"))
				tTargetObject.setCarTypeId(Long.parseLong(carTypeId));
			tTargetObject.setIsAllocate(isAllocate);
			tTargetObject.setDriverNumber(driverNum);
			RefTermGroupId refid = new RefTermGroupId(tTargetObject, group);

			RefTermGroup refgroup = new RefTermGroup();
			refgroup.setId(refid);

			Set set = new HashSet();
			set.add(refgroup);
			tTargetObject.setRefTermGroups(set);

			tTargetObjectDao.save(tTargetObject);

			// this.refTermGroup.save(refgroup);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
	}

	/**
	 * ����idsɾ��TTargetObject
	 */
	public void delTTargetObjectByIds(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			tTargetObjectDao.delTTargetObjectById(ids[i]);
		}
	}

	public TTerminal getTTargetObjectById(String id) {
		return tTargetObjectDao.findById(id);
	}

	/**
	 * �����ն���Ϣ
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean update(TerminalForm form, String old_group_id) {
		try {
			Long groupId = form.getGroupId();

			String name = form.getTermName();
			String gpssn = form.getDeviceId();
			String simId = form.getSimcard();
			String url = form.getImgUrl();
			String entCode = form.getEntCode();
			String typeCode = form.getTypeCode();
			String cartypeId = form.getCarTypeId();
			String isAllocate = form.getIsAllocate();
			String driverNumber = form.getDriverNumber();
			String locateType = form.getLocateType();
			// String subCompany = form.getSubCompany();

			// �ն�����
			TEntTermtype type = this.tTerminalTypeDao.findById(typeCode);
			// ԭʼ��
			TTermGroup oldGroup = null;
			if (!old_group_id.equals("null"))
				oldGroup = this.tTargetGroupDao.findById(Long
						.parseLong(old_group_id));
			// �µ���
			TTermGroup newGroup = this.tTargetGroupDao.findById(Long
					.valueOf(groupId));

			TTerminal tTargetObject = tTargetObjectDao.findById(gpssn);
			tTargetObject.setDeviceId(gpssn);
			tTargetObject.setImgUrl(url);
			tTargetObject.setEntCode(entCode);
			tTargetObject.setSimcard(simId);
			tTargetObject.setTermName(name);
			tTargetObject.setTEntTermtype(type);
			// tTargetObject.setSubCompany(subCompany);

			if (!locateType.equals("0"))
				tTargetObject.setCarTypeId(Long.valueOf(cartypeId));

			tTargetObject.setIsAllocate(isAllocate);
			tTargetObject.setDriverNumber(driverNumber);
			tTargetObject.setLocateType(locateType);

			// ɾ����ʷ������ϵ
			RefTermGroupId reftid = null;
			RefTermGroup old_ref_group = null;
			if (oldGroup != null) {
				reftid = new RefTermGroupId(tTargetObject, oldGroup);
				old_ref_group = (RefTermGroup) this.RTermGroupDAO
						.getHibernateTemplate()
						.load(RefTermGroup.class, reftid);// findById(reftid);
			}

			// �µĹ�����ϵ
			RefTermGroupId newid = new RefTermGroupId(tTargetObject, newGroup);
			RefTermGroup new_ref = new RefTermGroup();
			new_ref.setId(newid);

			if (oldGroup != null && !oldGroup.getId().equals(newGroup.getId())) {
				this.RTermGroupDAO.delete(old_ref_group);
			} else if (old_ref_group != null) {
				new_ref = old_ref_group;
			}

			Set set = new HashSet();
			set.add(new_ref);
			tTargetObject.setRefTermGroups(set);

			return tTargetObjectDao.update(tTargetObject);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
	}

	/**
	 * �����ն���Ϣ
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean update(TTerminal tTargetObject, String group_id,
			String old_group_id) {
		try {
			Long groupId = Long.parseLong(group_id);
			// ԭʼ��
			TTermGroup oldGroup = null;
			if (!old_group_id.equals("null"))
				oldGroup = this.tTargetGroupDao.findById(Long
						.parseLong(old_group_id));
			// �µ���
			TTermGroup newGroup = this.tTargetGroupDao.findById(Long
					.valueOf(groupId));

			// TTerminal tTargetObject = tTargetObjectDao.findById(gpssn);

			// ɾ����ʷ������ϵ
			RefTermGroupId reftid = null;
			RefTermGroup old_ref_group = null;
			if (oldGroup != null) {
				reftid = new RefTermGroupId(tTargetObject, oldGroup);
				old_ref_group = (RefTermGroup) this.RTermGroupDAO
						.getHibernateTemplate()
						.load(RefTermGroup.class, reftid);// findById(reftid);
			}

			// �µĹ�����ϵ
			RefTermGroupId newid = new RefTermGroupId(tTargetObject, newGroup);
			RefTermGroup new_ref = new RefTermGroup();
			new_ref.setId(newid);

			if (oldGroup != null && !oldGroup.getId().equals(newGroup.getId())) {
				this.RTermGroupDAO.delete(old_ref_group);
			} else if (old_ref_group != null) {
				new_ref = old_ref_group;
			}

			Set set = new HashSet();
			set.add(new_ref);
			tTargetObject.setRefTermGroups(set);

			return tTargetObjectDao.update(tTargetObject);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
	}

	/**
	 * ���ݸ�id�õ������
	 * 
	 * @param id
	 * @return
	 */
	public TTermGroup findTTargetGroupByParentId(Long id) {
		return tTargetGroupDao.findByParentId(id);
	}

	/**
	 * ��ȡ�����ն�����
	 */
	@SuppressWarnings("rawtypes")
	public List getAllTermType() {
		// TODO Auto-generated method stub
		return this.tTerminalTypeDao.findAll();
	}

	public TEntTermtype getTermTypeByCode(String code) {
		return this.tTerminalTypeDao.findById(code);
	}

	@SuppressWarnings("rawtypes")
	public RefTermGroup getTermGroup(TTerminal obj) {
		RefTermGroup reg = null;
		List reglist = this.RTermGroupDAO.findByProperty("TTerminal", obj);
		if (reglist != null && reglist.size() > 0) {
			reg = (RefTermGroup) reglist.get(0);
		}
		return reg;

	}

	/**
	 * �鿴�ն��Ƿ����
	 */
	public boolean findTermById(String id) {
		TTerminal term = this.tTargetObjectDao.findById(id);
		if (term != null) {
			return true;
		}
		return false;
	}

	/**
	 * ����SIM��ѯ
	 */
	public boolean findTermBySim(String sim) {
		TTerminal term = this.tTargetObjectDao.findTermBySim(sim);
		if (term != null) {
			return true;
		}
		return false;
	}

	/**
	 * ����SIM��ѯ
	 */
	public TTerminal findTermBySimcard(String sim) {
		TTerminal term = this.tTargetObjectDao.findTermBySim(sim);
		return term;
	}

	/**
	 * ����ID��ѯ�ն���Ϣ
	 */
	public TTerminal findTerminal(String id) {
		TTerminal term = null;

		term = this.tTargetObjectDao.findById(id);

		return term;
	}

	@SuppressWarnings("rawtypes")
	public List findTerminalByTermName(String termName, String entCode,
			boolean isVague) {
		// TODO Auto-generated method stub
		return tTargetObjectDao.findTerminalByTermName(termName, entCode,
				isVague);
	}

	@SuppressWarnings("rawtypes")
	public List findByExample(TTerminal tTerminal) {
		return tTargetObjectDao.findByExample(tTerminal);
	}

	public Page<Object[]> listTerminal(String entCode, Long userId, int pageNo,
			int pageSize, String searchValue) {
		return this.tTargetObjectDao.listTerminal(entCode, userId, pageNo,
				pageSize, searchValue);
	}

	public void saveSpeedStruction(String deviceId, String speedAlarmLimit,
			String speedAlarmLast) {
		TStructions structions = new TStructions();
		TTerminal terminal = new TTerminal();
		terminal.setDeviceId(deviceId);
		structions.setTTerminal(terminal);

	}

	public void deleteTTerminal(TTerminalBean terminalBean) {
		TTerminal terminal = findTerminal(terminalBean.getDeviceId());
		tTargetObjectDao.delete(terminal);
		// struction
		TStructions structions = structionDAO.findByDeviceId(terminalBean
				.getDeviceId());
		if (structions != null) {
			structions.setState("2");
			structionDAO.attachDirty(structions);
		}
		// speedcase
		TSpeedCase speedCase = speedCaseDao.findByDeviceId(terminalBean
				.getDeviceId());
		if (speedCase != null) {
			speedCase.setFlag("0");
			speedCaseDao.attachDirty(speedCase);
		}

		/** ���ն���Ϣ��memcache��ɾ��* */
		Constants.TERMINAL_CACHE.delete(terminalBean.getDeviceId());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveTTerminal(TTerminalBean terminalBean) {
		TTerminal terminal = new TTerminal();
		terminal.setDeviceId(terminalBean.getDeviceId());
		terminal.setEntCode(terminalBean.getEntCode());
		TEntTermtype entTermtype = new TEntTermtype();
		entTermtype.setTypeCode(terminalBean.getTypeCode());
		terminal.setTEntTermtype(entTermtype);
		terminal.setTermName(terminalBean.getTermName());
		terminal.setSimcard(terminalBean.getSimcard());
		terminal.setProvince(terminalBean.getProvince());
		terminal.setCity(terminalBean.getCity());
		terminal.setStartTime(terminalBean.getStartTime());
		terminal.setEndTime(terminalBean.getEndTime());
		terminal.setGetherInterval(terminalBean.getGetherInterval());
		terminal.setUsageFlag(0L);
		terminal.setCrtdate(Calendar.getInstance().getTime());
		terminal.setIsAllocate(terminalBean.getIsAllocate());
		terminal.setDriverNumber(terminalBean.getDriverNumber());
		terminal.setLocateType(terminalBean.getLocateType());
		terminal.setTermdesc(terminalBean.getTermDesc());
		terminal.setImgUrl(terminalBean.getImgUrl());
		terminal.setWeek(terminalBean.getWeek());
		terminal.setImsi(terminalBean.getImsi());
		terminal.setExpirationTime(terminalBean.getSetExpirationTime());// �ն˵���ʱ��
		// gps
		if (terminalBean.getLocateType() != null
				&& terminalBean.getLocateType().equals("1")) {
			terminal.setVehicleNumber(terminalBean.getVehicleNumber());
			terminal.setVehicleType(terminalBean.getVehicleType());
			terminal.setDriverNumber(terminalBean.getDriverNumber());
			terminal.setOilSpeedLimit(terminalBean.getOilSpeedLimit());
			terminal.setSpeedAlarmLimit(terminalBean.getSpeedAlarmLimit());
			terminal.setSpeedAlarmLast(terminalBean.getSpeedAlarmLast());
			terminal.setHoldAlarmFlag(terminalBean.getHoldAlarmFlag());
			if (terminalBean.getCarTypeInfo() != null
					&& !terminalBean.getCarTypeInfo().equals("")) {
				terminal.setCarTypeInfoId(terminalBean.getCarTypeInfo());
			}
		} else {// �ֻ�
			terminal.setVehicleNumber(terminalBean.getTermName());// ���ƺ����ն�����ֵһ��
		}
		// ��
		TTermGroup termGroup = new TTermGroup();
		termGroup.setId(terminalBean.getGroupId());
		RefTermGroupId refTermGroupId = new RefTermGroupId();
		refTermGroupId.setTTermGroup(termGroup);
		refTermGroupId.setTTerminal(terminal);
		RefTermGroup refTermGroup = new RefTermGroup();
		refTermGroup.setId(refTermGroupId);
		Set refTermGroups = new HashSet();
		refTermGroups.add(refTermGroup);
		terminal.setRefTermGroups(refTermGroups);
		tTargetObjectDao.save(terminal);
		if (terminalBean.getLocateType().equals("1")) {
			long seq = structionDAO.getCurrentSequence();
			// �·�ָ���
			TerminalFactory tf = new TerminalFactory();
			com.sosgps.wzt.directl.idirectl.Terminal t = tf
					.createTerminalBySn(terminalBean.getDeviceId());
			// �����ϱ�ָ��
			com.sosgps.wzt.directl.idirectl.TerminalSetting setting = t
					.createTerminalSetting();
			String instruction = setting.setSendIntervalTime(String
					.valueOf(seq), null, terminalBean.getGetherInterval()
					.toString());// Ƶ��ָ��
			TStructions transientInstances = new TStructions();
			// transientInstances.setCreateman(userInfo.getUserAccount());//
			// ������
			TTerminal tim = new TTerminal();
			transientInstances.setId(seq);
			tim.setDeviceId(terminalBean.getDeviceId());
			transientInstances.setTTerminal(tim);// ���ٱ��������ն�
			transientInstances.setInstruction(instruction); // �·�ָ��
			transientInstances.setState("1");// ������״̬
			transientInstances.setParam("�ϱ�Ƶ��:"
					+ terminalBean.getGetherInterval().intValue());
			transientInstances.setType("0");// �����ϱ�����ָ��
			structionDAO.save(transientInstances);
			if (terminalBean.getSpeedAlarmLimit() > 0) {
				// ���ٱ���ָ��
				com.sosgps.wzt.directl.idirectl.Alarm alarm = t.createAlarm();

				// instruction = alarm.setSpeedAlarm("", "1", terminalBean
				// .getSpeedAlarmLimit().toString(), terminalBean
				// .getSpeedAlarmLast().toString());
				seq = this.structionDAO.getCurrentSequence();
				instruction = alarm.setSpeedAlarm(String.valueOf(seq), "1",
						terminalBean.getSpeedAlarmLimit().toString());

				TStructions transientInstances2 = new TStructions();
				// transientInstances.setCreateman(userInfo.getUserAccount());//
				// ������
				TTerminal tim2 = new TTerminal();
				transientInstances2.setId(seq);
				tim2.setDeviceId(terminalBean.getDeviceId());
				transientInstances2.setTTerminal(tim2);// ���ٱ��������ն�
				transientInstances2.setInstruction(instruction); // �·�ָ��
				transientInstances2.setState("1");// ������״̬
				if (!terminalBean.getSpeedAlarmLimit().equals("0"))
					transientInstances2.setParam("��������:"
							+ terminalBean.getSpeedAlarmLimit());
				else
					transientInstances2.setParam("ȡ���˳��ٱ���");
				transientInstances2.setType("3");// ���ٱ�������ָ��
				structionDAO.save(transientInstances2);
				// speedcase
				TSpeedCase speedCase = speedCaseDao.findByDeviceId(terminalBean
						.getDeviceId());
				if (speedCase != null) {
					speedCase.setFlag("0");
					speedCaseDao.attachDirty(speedCase);
				}
				TSpeedCase transientInstance = new TSpeedCase();
				transientInstance.setDeviceId(terminalBean.getDeviceId());
				transientInstance.setMaxSpeed(terminalBean.getSpeedAlarmLimit()
						.floatValue());
				transientInstance.setCrtdate(Calendar.getInstance().getTime());
				transientInstance.setFlag("1");
				speedCaseDao.save(transientInstance);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateTTerminal(TTerminalBean terminalBean) {
		TTerminal terminal = findTerminal(terminalBean.getDeviceId());
		// terminal.setDeviceId(terminalBean.getDeviceId());
		terminal.setEntCode(terminalBean.getEntCode());
		TEntTermtype entTermtype = new TEntTermtype();
		entTermtype.setTypeCode(terminalBean.getTypeCode());
		terminal.setTEntTermtype(entTermtype);
		terminal.setTermName(terminalBean.getTermName());
		terminal.setSimcard(terminalBean.getSimcard());
		terminal.setProvince(terminalBean.getProvince());
		terminal.setCity(terminalBean.getCity());
		terminal.setStartTime(terminalBean.getStartTime());
		terminal.setEndTime(terminalBean.getEndTime());
		terminal.setGetherInterval(terminalBean.getGetherInterval());

		terminal.setDriverNumber(terminalBean.getDriverNumber());
		terminal.setLocateType(terminalBean.getLocateType());
		terminal.setTermdesc(terminalBean.getTermDesc());
		terminal.setImgUrl(terminalBean.getImgUrl());
		terminal.setWeek(terminalBean.getWeek());
		terminal.setImsi(terminalBean.getImsi());
		// terminal.setExpirationTime(terminalBean.getSetExpirationTime());//�ն˵���ʱ���޸�

		// gps
		if (terminalBean.getLocateType() != null
				&& terminalBean.getLocateType().equals("1")) {
			terminal.setVehicleNumber(terminalBean.getVehicleNumber());
			terminal.setVehicleType(terminalBean.getVehicleType());
			terminal.setDriverNumber(terminalBean.getDriverNumber());
			terminal.setOilSpeedLimit(terminalBean.getOilSpeedLimit());
			terminal.setSpeedAlarmLimit(terminalBean.getSpeedAlarmLimit());
			terminal.setSpeedAlarmLast(terminalBean.getSpeedAlarmLast());
			terminal.setHoldAlarmFlag(terminalBean.getHoldAlarmFlag());
			if (terminalBean.getCarTypeInfo() != null
					&& !terminalBean.getCarTypeInfo().equals("")) {
				terminal.setCarTypeInfoId(terminalBean.getCarTypeInfo());
			}
		} else {// �ֻ�
			terminal.setVehicleNumber(terminalBean.getTermName());// ���ƺ����ն�����ֵһ��
		}
		// ������
		Set refTermGroups = terminal.getRefTermGroups();
		RefTermGroup refTermGroup = null;
		TTermGroup termGroup = null;
		if (refTermGroups != null && refTermGroups.size() > 0) {
			refTermGroup = (RefTermGroup) refTermGroups.iterator().next();
			termGroup = refTermGroup.getId().getTTermGroup();
		}
		if (termGroup == null) {// δ������
			TTermGroup termGroup2 = new TTermGroup();
			termGroup2.setId(terminalBean.getGroupId());
			RefTermGroupId refTermGroupId = new RefTermGroupId();
			refTermGroupId.setTTermGroup(termGroup2);
			refTermGroupId.setTTerminal(terminal);
			RefTermGroup refTermGroup2 = new RefTermGroup();
			refTermGroup2.setId(refTermGroupId);
			refTermGroup2.setTTermGroup(termGroup2);
			refTermGroup2.setTTerminal(terminal);
			Set refTermGroups2 = new HashSet();
			refTermGroups2.add(refTermGroup2);
			terminal.setRefTermGroups(refTermGroups2);
		} else if (termGroup.getId() != terminalBean.getGroupId()) {
			// ɾ��
			RTermGroupDAO.delete(refTermGroup);
			TTermGroup termGroup2 = new TTermGroup();
			termGroup2.setId(terminalBean.getGroupId());
			RefTermGroupId refTermGroupId = new RefTermGroupId();
			refTermGroupId.setTTermGroup(termGroup2);
			refTermGroupId.setTTerminal(terminal);
			RefTermGroup refTermGroup2 = new RefTermGroup();
			refTermGroup2.setId(refTermGroupId);
			refTermGroup2.setTTermGroup(termGroup2);
			refTermGroup2.setTTerminal(terminal);
			Set refTermGroups2 = new HashSet();
			refTermGroups2.add(refTermGroup2);
			terminal.setRefTermGroups(refTermGroups2);
		}
		tTargetObjectDao.update(terminal);
		if (terminalBean.getGroupId() != null) {
			TTermGroup tg = tTargetObjectDao.findTermGroupById(terminalBean
					.getGroupId());// ��ȡ������
			if (tg != null && tg.getGroupName() != null && tg.getId() != null
					&& terminal.getDeviceId() != null
					&& terminal.getTermName() != null
					&& terminal.getSimcard() != null) {
				tTargetObjectDao.updateEmployeeAttend(terminal.getDeviceId(),
						terminal.getTermName(), terminal.getSimcard(),
						tg.getGroupName(), tg.getId());
			}
		}
		// gps
		if (terminalBean.getLocateType() != null
				&& terminalBean.getLocateType().equals("1")) {
			// todo
			// �·�ָ���
			TerminalFactory tf = new TerminalFactory();
			com.sosgps.wzt.directl.idirectl.Terminal t = tf
					.createTerminalBySn(terminalBean.getDeviceId());
			// �����ϱ�ָ��
			com.sosgps.wzt.directl.idirectl.TerminalSetting setting = t
					.createTerminalSetting();
			long seq = structionDAO.getCurrentSequence();
			String instruction = setting.setSendIntervalTime(String
					.valueOf(seq), null, terminalBean.getGetherInterval()
					.toString());// Ƶ��ָ��
			TStructions transientInstances = new TStructions();
			// transientInstances.setCreateman(userInfo.getUserAccount());//
			// ������
			TTerminal tim = new TTerminal();
			transientInstances.setId(seq);
			tim.setDeviceId(terminalBean.getDeviceId());
			transientInstances.setTTerminal(tim);// ���ٱ��������ն�
			transientInstances.setInstruction(instruction); // �·�ָ��
			transientInstances.setState("1");// ������״̬
			transientInstances.setParam("�ϱ�Ƶ��:"
					+ terminalBean.getGetherInterval().intValue());
			transientInstances.setType("0");// �����ϱ�����ָ��
			structionDAO.save(transientInstances);
			if (terminalBean.getSpeedAlarmLimit() > 0) {
				// ���ٱ���ָ��
				com.sosgps.wzt.directl.idirectl.Alarm alarm = t.createAlarm();
				// instruction = alarm.setSpeedAlarm("", "1", terminalBean
				// .getSpeedAlarmLimit().toString(), terminalBean
				// .getSpeedAlarmLast().toString());
				seq = structionDAO.getCurrentSequence();
				instruction = alarm.setSpeedAlarm(String.valueOf(seq), "1",
						terminalBean.getSpeedAlarmLimit().toString());

				TStructions transientInstances2 = new TStructions();
				// transientInstances.setCreateman(userInfo.getUserAccount());//
				// ������
				TTerminal tim2 = new TTerminal();
				transientInstances2.setId(seq);
				tim2.setDeviceId(terminalBean.getDeviceId());
				transientInstances2.setTTerminal(tim2);// ���ٱ��������ն�
				transientInstances2.setInstruction(instruction); // �·�ָ��
				transientInstances2.setState("1");// ������״̬
				if (!terminalBean.getSpeedAlarmLimit().equals("0"))
					transientInstances2.setParam("��������:"
							+ terminalBean.getSpeedAlarmLimit());
				else
					transientInstances2.setParam("ȡ���˳��ٱ���");
				transientInstances2.setType("3");// ���ٱ�������ָ��
				structionDAO.save(transientInstances2);
				// speedcase
				TSpeedCase speedCase = speedCaseDao.findByDeviceId(terminalBean
						.getDeviceId());
				if (speedCase != null) {
					speedCase.setFlag("0");
					speedCaseDao.attachDirty(speedCase);
				}
				TSpeedCase transientInstance = new TSpeedCase();
				transientInstance.setDeviceId(terminalBean.getDeviceId());
				transientInstance.setMaxSpeed(terminalBean.getSpeedAlarmLimit()
						.floatValue());
				transientInstance.setCrtdate(Calendar.getInstance().getTime());
				transientInstance.setFlag("1");
				speedCaseDao.save(transientInstance);
			}
		}
		/** ���ն���Ϣ���浽memcache��* */
		Terminal termCached = new Terminal();
		BeanUtils.copyProperties(terminal, termCached);
		termCached.setGroupId(terminalBean.getGroupId());
		Constants.TERMINAL_CACHE.set(terminalBean.getDeviceId(), termCached,
				10 * Memcache.EXPIRE_DAY);// �ն�ʱ�䵽��,oracle_job���޸��ն˱�ʶΪ�����ڡ�,��˻���10��
	}

	public Long findTermNum(String entCode) {
		return tTargetObjectDao.findTermNum(entCode);
	}

	@SuppressWarnings("unchecked")
	public List<TTerminal> findTerminal(String deviceId, String entCode,
			boolean isVague) {
		return this.tTargetObjectDao.findTerminal(deviceId, entCode, isVague);
	}

	// add by wangzhen
	public List<TTerminal> findAllTerminal(String entCode) {
		return this.tTargetObjectDao.findAllTerminal(entCode);
	}

	// add by wangzhen
	public List<String> getDeviceId(String entCode) {
		return this.tTargetObjectDao.getDeviceId(entCode);
	}

	public TermParamConfig getTermParamConfigByDeviceIdAndType(String deviceId,
			Integer type) {
		return this.tTargetObjectDao.findTermParamConfigByDeviceIdAndType(
				deviceId, type);
	}

	public Integer updateTermConfig(TermParamConfig termPc) {
		return this.tTargetObjectDao.updateTermConfig(termPc);
	}

	public Integer deleteTermParamConfig(String deviceIds) {
		return this.tTargetObjectDao.deleteTermParamConfig(deviceIds);
	}

	public int countTermParamConfig(String deviceIds) {
		return this.tTargetObjectDao.countTermParamConfig(deviceIds);
	}

	public List<ModuleParamConfig> getAllModule() {
		return this.tTargetObjectDao.getAllModule();
	}

	public Integer saveTermParamConfig(TermParamConfig termPar0) {
		return this.tTargetObjectDao.saveTermParamConfig(termPar0);
	}

	public String queryTerminalTree(String entCode, Long userId,
			String searchValue) {
		Page list = tTargetObjectDao.listTerminal(entCode, userId, 0, 65535,
				searchValue);
		StringBuffer treeStr = new StringBuffer();
		if (list != null && list.getResult() != null) {
			Iterator i = list.getResult().iterator();
			while (i.hasNext()) {
				Object[] userObj = (Object[]) i.next();
				TTerminal terminal = (TTerminal) userObj[0];// �ն�
				TTermGroup group = (TTermGroup) userObj[1];// ��
				TEntTermtype tp = (TEntTermtype) userObj[2];// �ն�����
				String name = terminal.getTermName();
				String deviceId = terminal.getDeviceId();
				// ͨ��deviceId�������һ���ϴ�����
				TLastLocrecord locrecord = terminalManageDao
						.findByLastLocrecord(deviceId);
				String simcard = terminal.getSimcard();
				TEntTermtype termType = terminal.getTEntTermtype();
				// δ����BOSSֱ��ͬ�������ģ�
				if (termType == null) {
					continue;
				}
				String typeCode = termType.getTypeCode();
				String imgUrl = terminal.getImgUrl();
				String locateType = terminal.getLocateType();// �ն����ͣ�0��LBS��1��GPS��
				Long carTypeId = terminal.getCarTypeId();// ��������Ϣid
				String driverNumber = terminal.getDriverNumber();// ˾���ֻ�����
				String vehicleNumber = terminal.getVehicleNumber();
				if (imgUrl == null && locateType.equals("0")) {
					imgUrl = "persion";
				} else if (imgUrl == null && locateType.equals("1")) {
					imgUrl = "car";
				}
				// add ��Դ 2010-07-30 ��� �ն˹�����ʼ����ʱ��
				String workBeginTime = terminal.getStartTime() == null ? "8:00"
						: terminal.getStartTime().trim();// ��ʼ����ʱ��
				String workEndTime = terminal.getEndTime() == null ? "18:00"
						: terminal.getEndTime().trim();// ��������ʱ��
				String week = terminal.getWeek() == null ? "127" : terminal
						.getWeek() + "";// ��������ʱ��
				// treeStr.append("{id: '" +
				// CharTools.javaScriptEscape(deviceId) +
				// "@#"+CharTools.killNullString(locateType)+
				// "@#"+CharTools.killNullString(simcard)+"@#"+CharTools.killNullString(vehicleNumber)+"',");
				treeStr.append("{id: '" + CharTools.javaScriptEscape(deviceId)
						+ "@#" + CharTools.javaScriptEscape(locateType) + "@#"
						+ CharTools.javaScriptEscape(simcard) + "@#"
						+ CharTools.javaScriptEscape(vehicleNumber) + "@#"
						+ CharTools.javaScriptEscape(workBeginTime) + "@#"
						+ CharTools.javaScriptEscape(workEndTime) + "@#"
						+ imgUrl + "@#" + week + "',");
				// end modify
				treeStr.append("text: '" + CharTools.javaScriptEscape(name)
						+ "',");
				TTerminal tl = terminalManageDao.findTerminalById(deviceId);
				Long nowState = tl.getExpirationFlag();// �ն˵���״̬
				if (locateType.equals("0")) {
					if (nowState == 1) {
						treeStr.append("iconCls: 'icon-userend',");// �ն�ʱ�䵽��
					} else {
						if (locrecord != null) {
							Date gpstime = locrecord.getGpstime();
							Calendar c = Calendar.getInstance();
							c.add(Calendar.HOUR_OF_DAY, -1);
							if (gpstime.after(c.getTime())) {// 1Сʱ������ЧGPS����
								Calendar cc = Calendar.getInstance();
								cc.add(Calendar.MINUTE, -15);
								if (gpstime.after(cc.getTime())) {// 15����������ЧGPS����
									treeStr.append("iconCls: 'icon-user',");
								} else {// 15����������Ч����
									// �ж�15�������Ƿ�����Ч����
									treeStr.append("iconCls: 'icon-user1',");// ����Ч����
								}
							} else {
								// 1Сʱ����GPS���ݸ���
								if (locrecord.getLastTime().after(c.getTime())) {
									treeStr.append("iconCls: 'icon-user1',");// ����Ч����
								} else {
									treeStr.append("iconCls: 'icon-user2',");// 1Сʱ��������Ϊ�ҵ�
								}
							}
						} else {
							treeStr.append("iconCls: 'icon-user2',");// ����Ч����
						}
						treeStr.append("checked: false,");
					}
				} else {
					if (nowState == 1) {
						treeStr.append("iconCls: 'icon-carend',");// �ն�ʱ�䵽��
					} else {
						if (locrecord != null) {
							Date gpstime = locrecord.getGpstime();
							Calendar ca = Calendar.getInstance();
							// ca.add(Calendar.HOUR_OF_DAY, -24);
							ca.add(Calendar.MINUTE, -10);
							if (gpstime.after(ca.getTime())) {// 24Сʱ����GPS����
								// �ж�24Сʱ��acc״̬
								if (locrecord.getAccStatus() != null
										&& locrecord.getAccStatus().equals("1")) {
									treeStr.append("iconCls: 'icon-car',");// acc״̬����
								} else {
									treeStr.append("iconCls: 'icon-car1',");// acc״̬��Ч
								}
							} else {// 24Сʱ��������
								treeStr.append("iconCls: 'icon-car2',");// acc״̬��Ч
							}
						} else {
							treeStr.append("iconCls: 'icon-car2',");// ����Ч����
						}
						treeStr.append("checked: false,");
					}
				}
				treeStr.append("leaf: true ");
				treeStr.append("},");
			}
			if (treeStr.length() > 0) {
				treeStr.deleteCharAt(treeStr.length() - 1);
			}
		}
		return treeStr.toString();
	}

	public boolean saveVehicleMsg(TVehicleMsg transientInstance) {
		try {
			this.vehicleMsgDAO.save(transientInstance);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateVehicleMsg(TVehicleMsg transientInstance) {
		try {
			this.vehicleMsgDAO.update(transientInstance);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void deleteVehicleMsg(TVehicleMsg transientInstance) {
		try {
			this.vehicleMsgDAO.delete(transientInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Page<Object[]> listTerminalByDeviceId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds) {
		return this.tTargetObjectDao.listTerminalByDeviceId(entCode, userId,
				pageNo, pageSize, searchValue, deviceIds);
	}
}
