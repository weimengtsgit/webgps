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
 * 2009-06-18 增加终端同时写入创建时间 //modfiy 2009-06-18 默认为可用状态
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
	 * 根据groupId得到TTargetGroup
	 * 
	 * @param groupId
	 * @return
	 */
	public TTermGroup getTTargetGroupByGroupId(Long groupId) {
		return tTargetGroupDao.findById(groupId);
	}

	/**
	 * 获得组/部门结构
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getTTargetGroup(String empCode) {
		return tTargetGroupDao.getTTargetGroup(empCode);
	}

	/**
	 * 根据groupId获得组所管理TTargetObject列表对象
	 */
	@SuppressWarnings("rawtypes")
	public List getGroupTTargetObject(Long groupId) {
		return tTargetObjectDao.findByGroupId(groupId);
	}

	/**
	 * 根据groupId获得组管理外TTargetObject列表对象
	 */
	@SuppressWarnings("rawtypes")
	public List getUnGroupTTargetObject(Long groupId) {
		return tTargetObjectDao.findNoGroupId(groupId);
	}

	/**
	 * 增加TTargetObject
	 * 
	 * @param tTargetObject
	 */
	public void saveTTargetObject(TTerminal tTargetObject) {
		tTargetObjectDao.save(tTargetObject);
	}

	/**
	 * add by yanglei 2009-03-25 14:14:00 新增Boss同步终端数据
	 * 
	 * @param entCode
	 * @param deviceId
	 * @param locateType
	 *            - 定位类型，0:LBS 1:GPS';
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
		// modfiy zhangwei 2009-06-18 默认为可用状态
		// transientInstance.setUsageFlag(new Long(1));
		transientInstance.setUsageFlag(0L);
		transientInstance.setLocateType(locateType);
		transientInstance.setIsAllocate("0");
		// add zhangwei 2009-06-18 增加
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
	 * add by yanglei 删除Boss同步终端
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
			return "2009";// 集团成员在该集团中不存在，销户失败
		}
		List entList = tEntDAO.findByProperty("entCode", entCode);
		// System.out.println("EntList is ===: " + entList);
		if (entList == null || entList.size() == 0) {
			return "2004";// 该集团不存在，销户失败
		}
		try {
			String[] ids = { phoneNumber };
			this.delTTargetObjectByIds(ids);
		} catch (Exception ex) {
			return "2005";// 集团开销户操作类型错误
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
	 * 根据ids删除TTargetObject
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
	 * 更新终端信息
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

			// 终端类型
			TEntTermtype type = this.tTerminalTypeDao.findById(typeCode);
			// 原始组
			TTermGroup oldGroup = null;
			if (!old_group_id.equals("null"))
				oldGroup = this.tTargetGroupDao.findById(Long
						.parseLong(old_group_id));
			// 新的组
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

			// 删除历史关联关系
			RefTermGroupId reftid = null;
			RefTermGroup old_ref_group = null;
			if (oldGroup != null) {
				reftid = new RefTermGroupId(tTargetObject, oldGroup);
				old_ref_group = (RefTermGroup) this.RTermGroupDAO
						.getHibernateTemplate()
						.load(RefTermGroup.class, reftid);// findById(reftid);
			}

			// 新的关联关系
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
	 * 更新终端信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean update(TTerminal tTargetObject, String group_id,
			String old_group_id) {
		try {
			Long groupId = Long.parseLong(group_id);
			// 原始组
			TTermGroup oldGroup = null;
			if (!old_group_id.equals("null"))
				oldGroup = this.tTargetGroupDao.findById(Long
						.parseLong(old_group_id));
			// 新的组
			TTermGroup newGroup = this.tTargetGroupDao.findById(Long
					.valueOf(groupId));

			// TTerminal tTargetObject = tTargetObjectDao.findById(gpssn);

			// 删除历史关联关系
			RefTermGroupId reftid = null;
			RefTermGroup old_ref_group = null;
			if (oldGroup != null) {
				reftid = new RefTermGroupId(tTargetObject, oldGroup);
				old_ref_group = (RefTermGroup) this.RTermGroupDAO
						.getHibernateTemplate()
						.load(RefTermGroup.class, reftid);// findById(reftid);
			}

			// 新的关联关系
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
	 * 根据父id得到组对象
	 * 
	 * @param id
	 * @return
	 */
	public TTermGroup findTTargetGroupByParentId(Long id) {
		return tTargetGroupDao.findByParentId(id);
	}

	/**
	 * 获取所有终端类型
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
	 * 查看终端是否存在
	 */
	public boolean findTermById(String id) {
		TTerminal term = this.tTargetObjectDao.findById(id);
		if (term != null) {
			return true;
		}
		return false;
	}

	/**
	 * 根据SIM查询
	 */
	public boolean findTermBySim(String sim) {
		TTerminal term = this.tTargetObjectDao.findTermBySim(sim);
		if (term != null) {
			return true;
		}
		return false;
	}

	/**
	 * 根据SIM查询
	 */
	public TTerminal findTermBySimcard(String sim) {
		TTerminal term = this.tTargetObjectDao.findTermBySim(sim);
		return term;
	}

	/**
	 * 根据ID查询终端信息
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

		/** 将终端信息从memcache中删除* */
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
		terminal.setExpirationTime(terminalBean.getSetExpirationTime());// 终端到期时间
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
		} else {// 手机
			terminal.setVehicleNumber(terminalBean.getTermName());// 车牌号与终端名称值一样
		}
		// 组
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
			// 下发指令表
			TerminalFactory tf = new TerminalFactory();
			com.sosgps.wzt.directl.idirectl.Terminal t = tf
					.createTerminalBySn(terminalBean.getDeviceId());
			// 连续上报指令
			com.sosgps.wzt.directl.idirectl.TerminalSetting setting = t
					.createTerminalSetting();
			String instruction = setting.setSendIntervalTime(String
					.valueOf(seq), null, terminalBean.getGetherInterval()
					.toString());// 频率指令
			TStructions transientInstances = new TStructions();
			// transientInstances.setCreateman(userInfo.getUserAccount());//
			// 创建人
			TTerminal tim = new TTerminal();
			transientInstances.setId(seq);
			tim.setDeviceId(terminalBean.getDeviceId());
			transientInstances.setTTerminal(tim);// 超速报警设置终端
			transientInstances.setInstruction(instruction); // 下发指令
			transientInstances.setState("1");// 待发送状态
			transientInstances.setParam("上报频率:"
					+ terminalBean.getGetherInterval().intValue());
			transientInstances.setType("0");// 连续上报设置指令
			structionDAO.save(transientInstances);
			if (terminalBean.getSpeedAlarmLimit() > 0) {
				// 超速报警指令
				com.sosgps.wzt.directl.idirectl.Alarm alarm = t.createAlarm();

				// instruction = alarm.setSpeedAlarm("", "1", terminalBean
				// .getSpeedAlarmLimit().toString(), terminalBean
				// .getSpeedAlarmLast().toString());
				seq = this.structionDAO.getCurrentSequence();
				instruction = alarm.setSpeedAlarm(String.valueOf(seq), "1",
						terminalBean.getSpeedAlarmLimit().toString());

				TStructions transientInstances2 = new TStructions();
				// transientInstances.setCreateman(userInfo.getUserAccount());//
				// 创建人
				TTerminal tim2 = new TTerminal();
				transientInstances2.setId(seq);
				tim2.setDeviceId(terminalBean.getDeviceId());
				transientInstances2.setTTerminal(tim2);// 超速报警设置终端
				transientInstances2.setInstruction(instruction); // 下发指令
				transientInstances2.setState("1");// 待发送状态
				if (!terminalBean.getSpeedAlarmLimit().equals("0"))
					transientInstances2.setParam("超速门限:"
							+ terminalBean.getSpeedAlarmLimit());
				else
					transientInstances2.setParam("取消了超速报警");
				transientInstances2.setType("3");// 超速报警设置指令
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
		// terminal.setExpirationTime(terminalBean.getSetExpirationTime());//终端到期时间修改

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
		} else {// 手机
			terminal.setVehicleNumber(terminalBean.getTermName());// 车牌号与终端名称值一样
		}
		// 查找组
		Set refTermGroups = terminal.getRefTermGroups();
		RefTermGroup refTermGroup = null;
		TTermGroup termGroup = null;
		if (refTermGroups != null && refTermGroups.size() > 0) {
			refTermGroup = (RefTermGroup) refTermGroups.iterator().next();
			termGroup = refTermGroup.getId().getTTermGroup();
		}
		if (termGroup == null) {// 未分配组
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
			// 删除
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
					.getGroupId());// 获取组名称
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
			// 下发指令表
			TerminalFactory tf = new TerminalFactory();
			com.sosgps.wzt.directl.idirectl.Terminal t = tf
					.createTerminalBySn(terminalBean.getDeviceId());
			// 连续上报指令
			com.sosgps.wzt.directl.idirectl.TerminalSetting setting = t
					.createTerminalSetting();
			long seq = structionDAO.getCurrentSequence();
			String instruction = setting.setSendIntervalTime(String
					.valueOf(seq), null, terminalBean.getGetherInterval()
					.toString());// 频率指令
			TStructions transientInstances = new TStructions();
			// transientInstances.setCreateman(userInfo.getUserAccount());//
			// 创建人
			TTerminal tim = new TTerminal();
			transientInstances.setId(seq);
			tim.setDeviceId(terminalBean.getDeviceId());
			transientInstances.setTTerminal(tim);// 超速报警设置终端
			transientInstances.setInstruction(instruction); // 下发指令
			transientInstances.setState("1");// 待发送状态
			transientInstances.setParam("上报频率:"
					+ terminalBean.getGetherInterval().intValue());
			transientInstances.setType("0");// 连续上报设置指令
			structionDAO.save(transientInstances);
			if (terminalBean.getSpeedAlarmLimit() > 0) {
				// 超速报警指令
				com.sosgps.wzt.directl.idirectl.Alarm alarm = t.createAlarm();
				// instruction = alarm.setSpeedAlarm("", "1", terminalBean
				// .getSpeedAlarmLimit().toString(), terminalBean
				// .getSpeedAlarmLast().toString());
				seq = structionDAO.getCurrentSequence();
				instruction = alarm.setSpeedAlarm(String.valueOf(seq), "1",
						terminalBean.getSpeedAlarmLimit().toString());

				TStructions transientInstances2 = new TStructions();
				// transientInstances.setCreateman(userInfo.getUserAccount());//
				// 创建人
				TTerminal tim2 = new TTerminal();
				transientInstances2.setId(seq);
				tim2.setDeviceId(terminalBean.getDeviceId());
				transientInstances2.setTTerminal(tim2);// 超速报警设置终端
				transientInstances2.setInstruction(instruction); // 下发指令
				transientInstances2.setState("1");// 待发送状态
				if (!terminalBean.getSpeedAlarmLimit().equals("0"))
					transientInstances2.setParam("超速门限:"
							+ terminalBean.getSpeedAlarmLimit());
				else
					transientInstances2.setParam("取消了超速报警");
				transientInstances2.setType("3");// 超速报警设置指令
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
		/** 将终端信息保存到memcache中* */
		Terminal termCached = new Terminal();
		BeanUtils.copyProperties(terminal, termCached);
		termCached.setGroupId(terminalBean.getGroupId());
		Constants.TERMINAL_CACHE.set(terminalBean.getDeviceId(), termCached,
				10 * Memcache.EXPIRE_DAY);// 终端时间到期,oracle_job会修改终端标识为“到期”,因此缓存10天
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
				TTerminal terminal = (TTerminal) userObj[0];// 终端
				TTermGroup group = (TTermGroup) userObj[1];// 组
				TEntTermtype tp = (TEntTermtype) userObj[2];// 终端类型
				String name = terminal.getTermName();
				String deviceId = terminal.getDeviceId();
				// 通过deviceId查找最后一条上传数据
				TLastLocrecord locrecord = terminalManageDao
						.findByLastLocrecord(deviceId);
				String simcard = terminal.getSimcard();
				TEntTermtype termType = terminal.getTEntTermtype();
				// 未处理（BOSS直接同步过来的）
				if (termType == null) {
					continue;
				}
				String typeCode = termType.getTypeCode();
				String imgUrl = terminal.getImgUrl();
				String locateType = terminal.getLocateType();// 终端类型（0：LBS，1：GPS）
				Long carTypeId = terminal.getCarTypeId();// 车附属信息id
				String driverNumber = terminal.getDriverNumber();// 司机手机号码
				String vehicleNumber = terminal.getVehicleNumber();
				if (imgUrl == null && locateType.equals("0")) {
					imgUrl = "persion";
				} else if (imgUrl == null && locateType.equals("1")) {
					imgUrl = "car";
				}
				// add 刘源 2010-07-30 添加 终端工作开始结束时间
				String workBeginTime = terminal.getStartTime() == null ? "8:00"
						: terminal.getStartTime().trim();// 开始工作时间
				String workEndTime = terminal.getEndTime() == null ? "18:00"
						: terminal.getEndTime().trim();// 结束工作时间
				String week = terminal.getWeek() == null ? "127" : terminal
						.getWeek() + "";// 结束工作时间
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
				Long nowState = tl.getExpirationFlag();// 终端到期状态
				if (locateType.equals("0")) {
					if (nowState == 1) {
						treeStr.append("iconCls: 'icon-userend',");// 终端时间到期
					} else {
						if (locrecord != null) {
							Date gpstime = locrecord.getGpstime();
							Calendar c = Calendar.getInstance();
							c.add(Calendar.HOUR_OF_DAY, -1);
							if (gpstime.after(c.getTime())) {// 1小时内有有效GPS数据
								Calendar cc = Calendar.getInstance();
								cc.add(Calendar.MINUTE, -15);
								if (gpstime.after(cc.getTime())) {// 15分钟内有有效GPS数据
									treeStr.append("iconCls: 'icon-user',");
								} else {// 15分钟内无有效数据
									// 判断15分钟内是否有无效数据
									treeStr.append("iconCls: 'icon-user1',");// 有无效数据
								}
							} else {
								// 1小时内无GPS数据更新
								if (locrecord.getLastTime().after(c.getTime())) {
									treeStr.append("iconCls: 'icon-user1',");// 有无效数据
								} else {
									treeStr.append("iconCls: 'icon-user2',");// 1小时内无数据为灰点
								}
							}
						} else {
							treeStr.append("iconCls: 'icon-user2',");// 无无效数据
						}
						treeStr.append("checked: false,");
					}
				} else {
					if (nowState == 1) {
						treeStr.append("iconCls: 'icon-carend',");// 终端时间到期
					} else {
						if (locrecord != null) {
							Date gpstime = locrecord.getGpstime();
							Calendar ca = Calendar.getInstance();
							// ca.add(Calendar.HOUR_OF_DAY, -24);
							ca.add(Calendar.MINUTE, -10);
							if (gpstime.after(ca.getTime())) {// 24小时内有GPS数据
								// 判断24小时内acc状态
								if (locrecord.getAccStatus() != null
										&& locrecord.getAccStatus().equals("1")) {
									treeStr.append("iconCls: 'icon-car',");// acc状态正常
								} else {
									treeStr.append("iconCls: 'icon-car1',");// acc状态无效
								}
							} else {// 24小时内无数据
								treeStr.append("iconCls: 'icon-car2',");// acc状态无效
							}
						} else {
							treeStr.append("iconCls: 'icon-car2',");// 无无效数据
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
