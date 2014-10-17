/**
 * 
 */
package com.sosgps.wzt.struction.dao;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TStructions;

/**
 * <p>Title:StructionDAO.java</p>
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2008</p>
 * <p>Company:ͼ�˿Ƽ�</p>
 * @author shiguang.zhou
 * @date: 2008-12-21
 * @time: ����02:06:31	
 * 
 */
public interface StructionDAO {

	public void save(TStructions stru);
	
	public TStructions findByDeviceId(String deviceId);

	public void attachDirty(TStructions structions);
	public long getCurrentSequence();
	//ָ����Ϣͳ��
	public Page<Object[]> listStructionsRecord(int pageNo, int pageSize, String startDate, 
				String endDate,String deviceIds,String type);
}
