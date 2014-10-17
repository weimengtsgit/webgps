package com.sosgps.wzt.system.dao;

import org.sos.taglibs.pagination.PageIterator;

import com.sosgps.wzt.system.model.TEntValue;


/**
 * @Title: EnterpManageDao.java
 * @Description: 
 * @Copyright:
 * @Date: 2009-4-1 обнГ04:49:35
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yang.lei
 * @version 1.0
 */
public interface EnterpManageDao {
	public void deleteEnterprise(String entCode);
    public void modifyEntInfo(TEntValue tEntValue);
    public TEntValue getTEntValue(String entCode);
    public PageIterator getEntInfoList(int currentPage, int pageSize);
}
