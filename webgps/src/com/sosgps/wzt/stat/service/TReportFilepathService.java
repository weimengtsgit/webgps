package com.sosgps.wzt.stat.service;

import java.util.Date;
import java.util.List;
/**
 * ��ϸ��־�����ļ�service
 * @author magiejue
 * @add by zhumingzhe 2010-09-15
 */
public interface TReportFilepathService {
	////����ļ�·������
	public List getFilepathes(String deviceIds,String entCode,Date startDate,Date endDate);
}
