package com.sosgps.v21.targetTemplate.service.impl;

import com.sosgps.v21.dao.TargetTemplateDao;
import com.sosgps.v21.signBill.service.SignBillService;
import com.sosgps.v21.targetTemplate.service.TargetTemplateService;

public class TargetTemplateServiceImpl implements TargetTemplateService {
	private TargetTemplateDao targetTemplateDao;

	public TargetTemplateDao getTargetTemplateDao() {
		return targetTemplateDao;
	}

	public void setTargetTemplateDao(TargetTemplateDao targetTemplateDao) {
		this.targetTemplateDao = targetTemplateDao;
	}

	public void getMonthMeter(String entCode) {
		// TODO Auto-generated method stub
		//����1�������ǩ���ۼƶ�
		
		//���µ�Ŀ��ֵ
		//targetTemplateDao.getCurrentTargetTemplateCount(entCode, type, year, targetOn);
	}
	
}
