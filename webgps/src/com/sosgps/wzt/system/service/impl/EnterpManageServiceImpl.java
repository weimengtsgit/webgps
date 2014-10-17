package com.sosgps.wzt.system.service.impl;

import org.sos.taglibs.pagination.PageIterator;

import com.sosgps.wzt.system.dao.EnterpManageDao;
import com.sosgps.wzt.system.model.TEntValue;
import com.sosgps.wzt.system.service.EnterpManageService;

/**
 * @Title: EnterpManageServiceImpl.java
 * @Description: 
 * @Copyright:
 * @Date: 2009-4-1 ÏÂÎç04:49:02
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yang.lei
 * @version 1.0
 */
public class EnterpManageServiceImpl implements EnterpManageService {
    public EnterpManageDao enterpManageDao;
    
	public EnterpManageDao getEnterpManageDao() {
		return enterpManageDao;
	}

	public void setEnterpManageDao(EnterpManageDao enterpManageDao) {
		this.enterpManageDao = enterpManageDao;
	}

	public void deleteEnterprise(String[] entCodes) {
		if(entCodes!=null) {
			for (int i = 0; i < entCodes.length; i++) {
				enterpManageDao.deleteEnterprise(entCodes[i]);
			}
		}
	}
	
	public TEntValue getTEntValue(String entCode) {
		return enterpManageDao.getTEntValue(entCode);
	}

	public void modifyEntInfo(TEntValue entValue) {
		enterpManageDao.modifyEntInfo(entValue);
	}
	
	public PageIterator getEntInfoList(int currentPage, int pageSize) {
		return enterpManageDao.getEntInfoList(currentPage, pageSize);
	}

}
