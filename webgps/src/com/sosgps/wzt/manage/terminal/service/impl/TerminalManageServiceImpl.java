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
	private TerminalManageDao terminalManageDao;// �ն˹������ݲ�ӿ�ʵ��
	private RefTermGroupDao refTermGroupDao;// �ն������ն������ݲ�ӿ�ʵ��

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
	 * �����ն���sos
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
		
		// ��ѯ�����ѷ�����ն�
		List terminals = terminalManageDao.findByTermGroupId(termGroupId, "1");
		StringBuffer treeStr = new StringBuffer();
		if (terminals != null || terminals.size() != 0) {
			for (Iterator it = terminals.iterator(); it.hasNext();) {
				Object obj = it.next();
				if (obj != null && obj instanceof TTerminal) {
					TTerminal terminal = (TTerminal) obj;
					String name = terminal.getTermName();
					String deviceId = terminal.getDeviceId();
					//ͨ��deviceId�������һ���ϴ�����
					TLastLocrecord locrecord=terminalManageDao.findByLastLocrecord(deviceId);
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
					if(imgUrl == null && locateType.equals("0")){
						imgUrl = "persion";
					}else if(imgUrl == null && locateType.equals("1")){
						imgUrl = "car";
					}
					//add ��Դ 2010-07-30 ��� �ն˹�����ʼ����ʱ��
					String workBeginTime = terminal.getStartTime() == null ? "8:00" : terminal.getStartTime().trim();//��ʼ����ʱ��
					String workEndTime = terminal.getEndTime() == null ? "18:00" :terminal.getEndTime().trim();//��������ʱ��
					String week = terminal.getWeek() == null ? "127" :terminal.getWeek()+"";//��������ʱ��
					//treeStr.append("{id: '" + CharTools.javaScriptEscape(deviceId) + "@#"+CharTools.killNullString(locateType)+ "@#"+CharTools.killNullString(simcard)+"@#"+CharTools.killNullString(vehicleNumber)+"',");
					treeStr.append("{id: '" + CharTools.javaScriptEscape(deviceId) + "@#"+CharTools.javaScriptEscape(locateType)+ "@#"+CharTools.javaScriptEscape(simcard)+"@#"+CharTools.javaScriptEscape(vehicleNumber)+"@#"+CharTools.javaScriptEscape(workBeginTime)+"@#"+CharTools.javaScriptEscape(workEndTime)+"@#"+imgUrl+"@#"+week+"',");
					//end modify
					treeStr.append("text: '" + CharTools.javaScriptEscape(name) + "',");
					TTerminal tl=terminalManageDao.findTerminalById(deviceId);
					Long nowState=tl.getExpirationFlag();//�ն˵���״̬
					if(locateType.equals("0")){
						if(nowState==1){
							treeStr.append("iconCls: 'icon-userend',");// �ն�ʱ�䵽��
						}else{
							if(locrecord!=null){
								Date gpstime=locrecord.getGpstime();
								/*Calendar c = Calendar.getInstance();
								//c.add(Calendar.HOUR_OF_DAY, -1);
								c.add(Calendar.MINUTE, -15);
								if (gpstime.after(c.getTime())) {// 1Сʱ������ЧGPS����
									treeStr.append("iconCls: 'icon-user',");
								} else {// 1Сʱ������Ч����
										// �ж�1Сʱ���Ƿ�����Ч����
									if (locrecord.getLastTime().after(c.getTime())) {
										treeStr.append("iconCls: 'icon-user1',");// ����Ч����
									} else {
										treeStr.append("iconCls: 'icon-user2',");// ����Ч����
									}
								}*/

								Calendar c = Calendar.getInstance();
								c.add(Calendar.HOUR_OF_DAY, -1);
								if (gpstime.after(c.getTime())) {// 1Сʱ������ЧGPS����
									Calendar cc = Calendar.getInstance();
									//cc.add(Calendar.MINUTE, -15);
									cc.add(Calendar.MINUTE, persionGreyInterval);
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
								
								
								
							}else{
								treeStr.append("iconCls: 'icon-user2',");// ����Ч����
							}
							treeStr.append("checked: false,");
						}
					}else{
						if(nowState==1){
							treeStr.append("iconCls: 'icon-carend',");// �ն�ʱ�䵽��
						}else{
							if(locrecord!=null){
								Date gpstime = locrecord.getGpstime();
								Calendar ca = Calendar.getInstance();
								//ca.add(Calendar.HOUR_OF_DAY, -24);
								//ca.add(Calendar.MINUTE, -10);
								ca.add(Calendar.MINUTE, carGreyInterval);
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
							}
							else{
							treeStr.append("iconCls: 'icon-car2',");// ����Ч����
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
	 * ͨ���ն���id���������ն��б�
	 */
	public String findByTermGroupId(Long termGroupId, String action,
			String isOndblclick) {
		if (terminalManageDao == null) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		// ��ѯ�����ѷ�����ն�
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
					// δ����BOSSֱ��ͬ�������ģ�
					if (termType == null) {
						continue;
					}
					String typeCode = termType.getTypeCode();
					// String lbsReqtor = termType.getLbsReqtor();
					// String lbsSubuser = termType.getLbsSubuser();
					String imgUrl = terminal.getImgUrl();
					String locateType = terminal.getLocateType();// �ն����ͣ�0��LBS��1��GPS��
					Long carTypeId = terminal.getCarTypeId();// ��������Ϣid
					String driverNumber = terminal.getDriverNumber();// ˾���ֻ�����
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
					// LBS��GPS����ͼ��
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
	 * ͨ���ն���id�������¸����ն��б�
	 */
	public String findSpecialByTermGroupId(Long termGroupId, String type,
			String action, String isOndblclick) {
		if (terminalManageDao == null) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		// ��ѯ�����ѷ���ĸ����ն�
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
					// δ����BOSSֱ��ͬ�������ģ�
					if (termType == null) {
						continue;
					}
					String typeCode = termType.getTypeCode();
					// String lbsReqtor = termType.getLbsReqtor();
					// String lbsSubuser = termType.getLbsSubuser();
					String imgUrl = terminal.getImgUrl();
					String locateType = terminal.getLocateType();// �ն����ͣ�0��LBS��1��GPS��
					Long carTypeId = terminal.getCarTypeId();// ��������Ϣid
					String driverNumber = terminal.getDriverNumber();// ˾���ֻ�����
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
					// LBS��GPS����ͼ��
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
			// ɾ���ն�������
			refTermGroupDao.deleteAll(deviceIds);
			// �����¹�ϵ
			for (int i = 0; i < deviceIds.length; i++) {
				String deviceId = deviceIds[i];
				// �����ն������ն����ϵ��
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
	 * ͨ���ն���id������������ն��б�
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
		// ��ѯ�����ѷ�����ն�
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
					if(imgUrl == null && locateType.equals("0")){
						imgUrl = "persion";
					}else if(imgUrl == null && locateType.equals("1")){
						imgUrl = "car";
					}
					//add ��Դ 2010-07-30 ��� �ն˹�����ʼ����ʱ��
					String workBeginTime = terminal.getStartTime() == null ? "8:00" : terminal.getStartTime().trim();//��ʼ����ʱ��
					String workEndTime = terminal.getEndTime() == null ? "18:00" :terminal.getEndTime().trim();//��������ʱ��
					String week = terminal.getWeek() == null ? "127" :terminal.getWeek()+"";//��������ʱ��
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
