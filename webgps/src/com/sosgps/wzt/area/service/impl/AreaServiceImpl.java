/**
 * 
 */
package com.sosgps.wzt.area.service.impl;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.sosgps.wzt.area.dao.AreaDAO;
import com.sosgps.wzt.area.service.AreaService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefTermAreaalarm;
import com.sosgps.wzt.orm.TArea;
import com.sosgps.wzt.orm.TTerminal;

/**
 * @author shiguang.zhou
 * 
 */
public class AreaServiceImpl implements AreaService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(AreaServiceImpl.class);

	private AreaDAO areaDAO;

	public AreaDAO getAreaDAO() {
		return areaDAO;
	}

	public void setAreaDAO(AreaDAO areaDAO) {
		this.areaDAO = areaDAO;
	}

	public Page<TArea> listArea(String entCode, int pageNo, int pageSize,
			String searchValue) {
		try {
			return areaDAO.listArea(entCode, pageNo, pageSize, searchValue);
		} catch (Exception e) {
			logger.error("≤È—Ø«¯”Ú¡–±Ì ß∞‹", e);
			return null;
		}
	}

	public void addArea(String entCode, String name, String areaPoints,
			String areaType) {
		try {
			TArea transientInstance = new TArea();
			transientInstance.setName(name);
			transientInstance.setEpCode(entCode);
			transientInstance.setXy(areaPoints);
			transientInstance.setAreaType(areaType);
			transientInstance.setFlag("1");
			transientInstance.setCreatedate(Calendar.getInstance().getTime());
			// transientInstance.setCreateman(createman);
			areaDAO.save(transientInstance);
		} catch (Exception e) {
			logger.error("‘ˆº”«¯”Ú ß∞‹", e);
		}
	}

	public void updateArea(Long areaId, String name, String areaPoints,
			String areaType) {
		try {
			TArea instance = areaDAO.findById(areaId);
			instance.setName(name);
			instance.setXy(areaPoints);
			instance.setAreaType(areaType);
			areaDAO.attachDirty(instance);
		} catch (Exception e) {
			logger.error("–ﬁ∏ƒ«¯”Ú ß∞‹", e);
		}
	}

	public void deleteAreas(String[] areaIds) {
		try {
			for (int i = 0; i < areaIds.length; i++) {
				TArea persistentInstance = areaDAO.findById(Long
						.parseLong(areaIds[i]));
				if (persistentInstance != null)
					areaDAO.delete(persistentInstance);
			}
		} catch (Exception e) {
			logger.error("…æ≥˝«¯”Ú ß∞‹", e);
		}
	}

	public void deleteRefTermAreasByDeviceIdsAndAreaIds( String deviceId, String[] areaIds) {
		
		try {
			Long[] aids = new Long[areaIds.length];
			for(int i=0;i<areaIds.length;i++){
				aids[i] = Long.parseLong(areaIds[i]);
			}
			areaDAO.deleteRefTermAreasByDeviceIdsAndAreaIds(deviceId, aids);
		} catch (Exception e) {
			logger.error("…æ≥˝÷’∂À…Ë÷√«¯”Ú ß∞‹", e);
		}
	}

	
	public void deleteRefTermAreas(String[] deviceIds) {
		try {
			areaDAO.deleteRefTermAreasByDeviceIds(deviceIds);
		} catch (Exception e) {
			logger.error("…æ≥˝÷’∂À…Ë÷√«¯”Ú ß∞‹", e);
		}
	}

	public void saveRefTermArea(String[] deviceIds, String[] areaIds,
			String alarmType, String startTime, String endTime) {
		try {
			areaDAO.deleteRefTermAreasByDeviceIds(deviceIds);
			for (int i = 0; i < deviceIds.length; i++) {
				String deviceId = deviceIds[i];
				TTerminal terminal = new TTerminal();
				terminal.setDeviceId(deviceId);
				for(int j = 0; j < areaIds.length; j ++){
					RefTermAreaalarm ref = new RefTermAreaalarm();
					ref.setTTerminal(terminal);
					Long area_id = Long.parseLong(areaIds[j]);
					TArea area = new TArea();
					area.setId(area_id);
					ref.setTArea(area);
					ref.setAlarmType(Long.parseLong(alarmType));
					ref.setStartTime(startTime);
					ref.setEndTime(endTime);
					areaDAO.saveRefTermArea(ref);
				}
			}
		} catch (Exception e) {
			logger.error("÷’∂À…Ë÷√«¯”Ú ß∞‹", e);
		}
	}

	public Page<Object[]> listRefTermArea(String entCode, String alarmType,
			int pageNo, int pageSize, String searchValue) {
		try {
			return areaDAO.listRefTermArea(entCode, alarmType, pageNo, pageSize, searchValue);
		} catch (Exception e) {
			logger.error("≤È—Ø÷’∂À…Ë÷√«¯”Ú¡–±Ì ß∞‹", e);
			return null;
		}
	}

	public Page<TArea> queryAreaByDeviceId(String deviceId) {
		try {
			return areaDAO.queryAreaByDeviceId(deviceId);
		} catch (Exception e) {
			logger.error("≤È—Ø÷’∂À…Ë÷√«¯”Ú¡–±Ì ß∞‹", e);
			return null;
		}
	}

}
