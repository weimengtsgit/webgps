package com.sosgps.wzt.stat.dao;

import java.util.Date;
import java.util.List;
/**
 * ��ϸ�����ļ�dao�ӿ�
 * @author magiejue
 * @add by zhumingzhe 2010-09-15
 */
public interface TReportFilepathDao {
	//����ļ�·������
	public List getReportFilepath(String deviceIds,String endCode,Date startDate,Date endDate);
}
