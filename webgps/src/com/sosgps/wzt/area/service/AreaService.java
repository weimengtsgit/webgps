/**
 * 
 */
package com.sosgps.wzt.area.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TArea;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;

/**
 * @author shiguang.zhou
 * 
 */
public interface AreaService {
	public Page<TArea> listArea(String entCode, int pageNo, int pageSize,
			String searchValue);

	public void addArea(String entCode, String name, String areaPoints,
			String areaType);

	public void updateArea(Long areaId, String name, String areaPoints,
			String areaType);

	public void deleteAreas(String[] areaIds);

	public void deleteRefTermAreas(String[] deviceIds);
	
	public void deleteRefTermAreasByDeviceIdsAndAreaIds( String deviceId, String[] areaIds) ;

	public void saveRefTermArea(String[] deviceIds, String[] areaId,
			String alarmType, String startTime, String endTime);

	public Page<Object[]> listRefTermArea(String entCode, String alarmType,
			int pageNo, int pageSize, String searchValue);

	// sos根据终端id查关联区域报警区域
	public Page<TArea> queryAreaByDeviceId(String deviceId);
}
