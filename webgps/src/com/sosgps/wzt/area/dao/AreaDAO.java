/**
 * 
 */
package com.sosgps.wzt.area.dao;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefTermAreaalarm;
import com.sosgps.wzt.orm.TArea;

/**
 * @author shiguang.zhou
 * 
 */
public interface AreaDAO {
	public void save(TArea transientInstance);

	public void delete(TArea persistentInstance);

	public TArea findById(java.lang.Long id);

	public void attachDirty(TArea instance);

	public Page<TArea> listArea(String entCode, int pageNo, int pageSize,
			String searchValue);
	
	public void deleteRefTermAreasByDeviceIdsAndAreaIds( String deviceId, Long[] areaIds) ;
	
	public void deleteRefTermAreasByDeviceIds(String[] deviceIds);

	public void saveRefTermArea(RefTermAreaalarm transientInstance);

	public List findRefTermAreasByDeviceId(String deviceId);

	public Page<Object[]> listRefTermArea(String entCode, String alarmType,
			int pageNo, int pageSize, String searchValue);
	// sos根据终端id查关联区域报警区域
	public Page<TArea> queryAreaByDeviceId(String deviceId);
}
