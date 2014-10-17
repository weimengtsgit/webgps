package com.sosgps.wzt.manage.terminal.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.terminal.dao.RefTermGroupDao;
import com.sosgps.wzt.manage.terminal.dao.TerminalManageDao;
import com.sosgps.wzt.manage.terminal.service.TerminalManageService;
import com.sosgps.wzt.orm.RefTermGroup;
import com.sosgps.wzt.orm.RefTermGroupId;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TEntTermtype;
import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.util.CharTools;

public class TerminalManageServiceImpl implements TerminalManageService {
	private TerminalManageDao terminalManageDao;// 终端管理数据层接口实例
	private RefTermGroupDao refTermGroupDao;// 终端属于终端组数据层接口实例

	public RefTermGroupDao getRefTermGroupDao() {
		return refTermGroupDao;
	}

	public void setRefTermGroupDao(RefTermGroupDao refTermGroupDao) {
		this.refTermGroupDao = refTermGroupDao;
	}

	public TerminalManageDao getTerminalManageDao() {
		return terminalManageDao;
	}

	public void setTerminalManageDao(TerminalManageDao terminalManageDao) {
		this.terminalManageDao = terminalManageDao;
	}

	public boolean add() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean edit() {
		// TODO Auto-generated method stub
		return false;
	}

	public List findByEntId(String entCode) {
		return terminalManageDao.findByEntCode(entCode);
	}
	
	/**
	 * 返回终端树sos
	 * @param termGroupId
	 * @return
	 */
	public String findByTermGroupId(Long termGroupId, TEnt tEnt) {
		if (terminalManageDao == null) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		int carGreyInterval = tEnt.getCarGreyInterval() == null ? -10 : -(tEnt.getCarGreyInterval().intValue()/60);
		int persionGreyInterval = tEnt.getPersionGreyInterval() == null ? -15 : -(tEnt.getPersionGreyInterval().intValue()/60);
		
		// 查询组下已分配的终端
		List terminals = terminalManageDao.findByTermGroupId(termGroupId, "1");
		StringBuffer treeStr = new StringBuffer();
		if (terminals != null || terminals.size() != 0) {
			for (Iterator it = terminals.iterator(); it.hasNext();) {
				Object obj = it.next();
				if (obj != null && obj instanceof TTerminal) {
					TTerminal terminal = (TTerminal) obj;
					String name = terminal.getTermName();
					String deviceId = terminal.getDeviceId();
					//通过deviceId查找最后一条上传数据
					TLastLocrecord locrecord=terminalManageDao.findByLastLocrecord(deviceId);
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
					if(imgUrl == null && locateType.equals("0")){
						imgUrl = "persion";
					}else if(imgUrl == null && locateType.equals("1")){
						imgUrl = "car";
					}
					//add 刘源 2010-07-30 添加 终端工作开始结束时间
					String workBeginTime = terminal.getStartTime() == null ? "8:00" : terminal.getStartTime().trim();//开始工作时间
					String workEndTime = terminal.getEndTime() == null ? "18:00" :terminal.getEndTime().trim();//结束工作时间
					String week = terminal.getWeek() == null ? "127" :terminal.getWeek()+"";//结束工作时间
					//treeStr.append("{id: '" + CharTools.javaScriptEscape(deviceId) + "@#"+CharTools.killNullString(locateType)+ "@#"+CharTools.killNullString(simcard)+"@#"+CharTools.killNullString(vehicleNumber)+"',");
					treeStr.append("{id: '" + CharTools.javaScriptEscape(deviceId) + "@#"+CharTools.javaScriptEscape(locateType)+ "@#"+CharTools.javaScriptEscape(simcard)+"@#"+CharTools.javaScriptEscape(vehicleNumber)+"@#"+CharTools.javaScriptEscape(workBeginTime)+"@#"+CharTools.javaScriptEscape(workEndTime)+"@#"+imgUrl+"@#"+week+"',");
					//end modify
					treeStr.append("text: '" + CharTools.javaScriptEscape(name) + "',");
					TTerminal tl=terminalManageDao.findTerminalById(deviceId);
					Long nowState=tl.getExpirationFlag();//终端到期状态
					if(locateType.equals("0")){
						if(nowState==1){
							treeStr.append("iconCls: 'icon-userend',");// 终端时间到期
						}else{
							if(locrecord!=null){
								Date gpstime=locrecord.getGpstime();
								/*Calendar c = Calendar.getInstance();
								//c.add(Calendar.HOUR_OF_DAY, -1);
								c.add(Calendar.MINUTE, -15);
								if (gpstime.after(c.getTime())) {// 1小时内有有效GPS数据
									treeStr.append("iconCls: 'icon-user',");
								} else {// 1小时内无有效数据
										// 判断1小时内是否有无效数据
									if (locrecord.getLastTime().after(c.getTime())) {
										treeStr.append("iconCls: 'icon-user1',");// 有无效数据
									} else {
										treeStr.append("iconCls: 'icon-user2',");// 无无效数据
									}
								}*/

								Calendar c = Calendar.getInstance();
								c.add(Calendar.HOUR_OF_DAY, -1);
								if (gpstime.after(c.getTime())) {// 1小时内有有效GPS数据
									Calendar cc = Calendar.getInstance();
									//cc.add(Calendar.MINUTE, -15);
									cc.add(Calendar.MINUTE, persionGreyInterval);
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
								
								
								
							}else{
								treeStr.append("iconCls: 'icon-user2',");// 无无效数据
							}
							treeStr.append("checked: false,");
						}
					}else{
						if(nowState==1){
							treeStr.append("iconCls: 'icon-carend',");// 终端时间到期
						}else{
							if(locrecord!=null){
								Date gpstime = locrecord.getGpstime();
								Calendar ca = Calendar.getInstance();
								//ca.add(Calendar.HOUR_OF_DAY, -24);
								//ca.add(Calendar.MINUTE, -10);
								ca.add(Calendar.MINUTE, carGreyInterval);
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
							}
							else{
							treeStr.append("iconCls: 'icon-car2',");// 无无效数据
						}
							treeStr.append("checked: false,");
						}
					}
					treeStr.append("leaf: true ");
					treeStr.append("},");
				}
			}
			if (treeStr.length() > 0) {
				treeStr.deleteCharAt(treeStr.length() - 1);
			}
			
			//System.out.println(treeStr);
		}
		return treeStr.toString();
	}
	
	/**
	 * 通过终端组id查找组下终端列表
	 */
	public String findByTermGroupId(Long termGroupId, String action,
			String isOndblclick) {
		if (terminalManageDao == null) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		// 查询组下已分配的终端
		List terminals = terminalManageDao.findByTermGroupId(termGroupId, "1");

		StringBuffer re = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		re.append("<tree>");
		if (terminals != null || terminals.size() != 0) {
			for (Iterator it = terminals.iterator(); it.hasNext();) {
				Object obj = it.next();
				if (obj != null && obj instanceof TTerminal) {
					TTerminal terminal = (TTerminal) obj;
					String name = terminal.getTermName();
					String deviceId = terminal.getDeviceId();
					String simcard = terminal.getSimcard();
					TEntTermtype termType = terminal.getTEntTermtype();
					// 未处理（BOSS直接同步过来的）
					if (termType == null) {
						continue;
					}
					String typeCode = termType.getTypeCode();
					// String lbsReqtor = termType.getLbsReqtor();
					// String lbsSubuser = termType.getLbsSubuser();
					String imgUrl = terminal.getImgUrl();
					String locateType = terminal.getLocateType();// 终端类型（0：LBS，1：GPS）
					Long carTypeId = terminal.getCarTypeId();// 车附属信息id
					String driverNumber = terminal.getDriverNumber();// 司机手机号码
					re.append("<tree ");
					re.append("text=\"");
					re.append(name);
					re.append("\" value=\"");
					re.append(typeCode);
					re.append("\" action=\"");
					re.append(action);
					re.append("\" isOndblclick=\"");
					re.append(isOndblclick);
					re.append("\" checked=\"false\" id=\"");
					re.append(deviceId);
					if (locateType == null) {
						locateType = "1";
					}
					// LBS、GPS区别图标
					if (locateType.equals("0")) {
						re.append("\" icon=\"../images/icon/3.gif");
					} else {
						re.append("\" icon=\"../images/icon/4.gif");
					}
					re.append("\" expend1=\"");
					re.append(locateType);
					re.append("\" expend2=\"");
					re.append(imgUrl);
					re.append("\" expend3=\"");
					re.append(carTypeId);
					re.append("\" expend4=\"");
					re.append(driverNumber);
					re.append("\" expend5=\"");
					re.append(simcard);
					re.append("\" />");
				}
			}
		}
		re.append("</tree>");
		return re.toString();
	}

	/**
	 * 通过终端组id查找组下个别终端列表
	 */
	public String findSpecialByTermGroupId(Long termGroupId, String type,
			String action, String isOndblclick) {
		if (terminalManageDao == null) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		// 查询组下已分配的个别终端
		List terminals = terminalManageDao.findSpecialByTermGroupId(
				termGroupId, type, "1");

		StringBuffer re = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		re.append("<tree>");
		if (terminals != null || terminals.size() != 0) {
			for (Iterator it = terminals.iterator(); it.hasNext();) {
				Object obj = it.next();
				if (obj != null && obj instanceof TTerminal) {
					TTerminal terminal = (TTerminal) obj;
					String name = terminal.getTermName();
					String deviceId = terminal.getDeviceId();
					TEntTermtype termType = terminal.getTEntTermtype();
					// 未处理（BOSS直接同步过来的）
					if (termType == null) {
						continue;
					}
					String typeCode = termType.getTypeCode();
					// String lbsReqtor = termType.getLbsReqtor();
					// String lbsSubuser = termType.getLbsSubuser();
					String imgUrl = terminal.getImgUrl();
					String locateType = terminal.getLocateType();// 终端类型（0：LBS，1：GPS）
					Long carTypeId = terminal.getCarTypeId();// 车附属信息id
					String driverNumber = terminal.getDriverNumber();// 司机手机号码
					re.append("<tree ");
					re.append("text=\"");
					re.append(name);
					re.append("\" value=\"");
					re.append(typeCode);
					re.append("\" action=\"");
					re.append(action);
					re.append("\" isOndblclick=\"");
					re.append(isOndblclick);
					re.append("\" checked=\"false\" id=\"");
					re.append(deviceId);
					if (locateType == null) {
						locateType = "1";
					}
					// LBS、GPS区别图标
					if (locateType.equals("0")) {
						re.append("\" icon=\"../images/icon/3.gif");
					} else {
						re.append("\" icon=\"../images/icon/4.gif");
					}
					re.append("\" expend1=\"");
					re.append(locateType);
					re.append("\" expend2=\"");
					re.append(imgUrl);
					re.append("\" expend3=\"");
					re.append(carTypeId);
					re.append("\" expend4=\"");
					re.append(driverNumber);
					// re.append("\" expend1=\"");
					// re.append(lbsReqtor);
					re.append("\" />");
				}
			}
		}
		re.append("</tree>");
		return re.toString();
	}

	public boolean updateRefTermGroup(String[] deviceIds, String groupId) {
		boolean re = false;
		if (refTermGroupDao == null) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.ERROR_DAO_INSTANCE_NULL);
			return re;
		}
		Long gId = 0L;
		try {
			gId = Long.parseLong(groupId);
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.ERROR_TYPE_TRANSFORM_WRONG, e);
		}
		try {
			// 删除终端所属组
			refTermGroupDao.deleteAll(deviceIds);
			// 增加新关系
			for (int i = 0; i < deviceIds.length; i++) {
				String deviceId = deviceIds[i];
				// 增加终端所属终端组关系表
				RefTermGroupId refId = new RefTermGroupId();
				TTerminal t = new TTerminal();
				t.setDeviceId(deviceId);
				refId.setTTerminal(t);
				TTermGroup group = new TTermGroup();
				group.setId(gId);
				refId.setTTermGroup(group);
				RefTermGroup ref = new RefTermGroup();
				ref.setId(refId);
				refTermGroupDao.save(ref);
			}
			re = true;
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL,
					e);
		}
		return re;
	}

	/**
	 * 通过终端组id数组查找组下终端列表
	 * 
	 * @param termGroupIds
	 * @return
	 */
	public List findByTermGroupIds(Long[] termGroupIds) {
		if (terminalManageDao == null) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		try {
			List terminals = terminalManageDao.findByTermGroupIds(termGroupIds);
			return terminals;
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL,
					e);
		}
		return null;
	}
	
	public String findByTermGroupIdNoCheck(Long termGroupId) {
		if (terminalManageDao == null) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		// 查询组下已分配的终端
		List terminals = terminalManageDao.findByTermGroupId(termGroupId, "1");
		
		StringBuffer treeStr = new StringBuffer();
		if (terminals != null || terminals.size() != 0) {
			for (Iterator it = terminals.iterator(); it.hasNext();) {
				Object obj = it.next();
				if (obj != null && obj instanceof TTerminal) {
					TTerminal terminal = (TTerminal) obj;
					String name = terminal.getTermName();
					String deviceId = terminal.getDeviceId();
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
					if(imgUrl == null && locateType.equals("0")){
						imgUrl = "persion";
					}else if(imgUrl == null && locateType.equals("1")){
						imgUrl = "car";
					}
					//add 刘源 2010-07-30 添加 终端工作开始结束时间
					String workBeginTime = terminal.getStartTime() == null ? "8:00" : terminal.getStartTime().trim();//开始工作时间
					String workEndTime = terminal.getEndTime() == null ? "18:00" :terminal.getEndTime().trim();//结束工作时间
					String week = terminal.getWeek() == null ? "127" :terminal.getWeek()+"";//结束工作时间
					//treeStr.append("{id: '" + CharTools.killNullString(deviceId) + "@#"+CharTools.killNullString(locateType)+ "@#"+CharTools.killNullString(simcard)+"@#"+CharTools.killNullString(vehicleNumber)+"',");
					treeStr.append("{id: '" + CharTools.javaScriptEscape(deviceId) + "@#"+CharTools.javaScriptEscape(locateType)+ "@#"+CharTools.javaScriptEscape(simcard)+"@#"+CharTools.javaScriptEscape(vehicleNumber)+"@#"+CharTools.javaScriptEscape(workBeginTime)+"@#"+CharTools.javaScriptEscape(workEndTime)+"@#"+imgUrl+"@#"+week+"',");
					//end modify
					treeStr.append("text: '" + CharTools.javaScriptEscape(name) + "',");
					if(locateType.equals("0")){
						treeStr.append("iconCls: 'icon-user',");
					}else{
						treeStr.append("iconCls: 'icon-car',");
					}
					//treeStr.append("checked: false,");
					treeStr.append("leaf: true ");
					treeStr.append("},");
				}
			}
			if (treeStr.length() > 0) {
				treeStr.deleteCharAt(treeStr.length() - 1);
			}
		}
		return treeStr.toString();
	}
}
