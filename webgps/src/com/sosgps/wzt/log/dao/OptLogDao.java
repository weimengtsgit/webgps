package com.sosgps.wzt.log.dao;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;;
/**
 * <p>Title: OptLogDao</p>
 * <p>Description: 操作日志Dao接口</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 图盟科技</p>
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public interface OptLogDao {
	public void save(TOptLog transientInstance) throws RuntimeException;
	public boolean deleteAll(Long[] ids);
	public Page<TOptLog> queryOptLog(String entCode,
			String deviceIds, String startTime, String endTime, String pageNo,
			String pageSize, String paramName, String paramValue, String vague,
			boolean autoCount, String type);

	// sos操作日志
	public Page<TOptLog> listOptLog(String entCode, int pageNo, int pageSize,
			Date startTime, Date endTime, String searchValue);
}
