package com.sosgps.v21.mobileMonitoring.dao;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;

public interface MobileeventdataDao {
	//��ѯ�ֻ�����״̬
	Page<Object []> queryMobile(String keyWords, Date startTime,Date entTime,String type,String deviceId,int pageNo, int pageSize);
	//��ѯͳ���ֻ����ػ���������
	Page<Object []> countMobileeventdata(String entName, String entCode, String termName, Date startTime,Date entTime,int pageNo, int pageSize);
}
