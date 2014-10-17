package com.sosgps.wzt.system.service;

import org.sos.taglibs.pagination.PageIterator;

import com.sosgps.wzt.system.model.TEntValue;

/**
 * @Title: EnterpManageService.java
 * @Description: 
 * @Copyright:
 * @Date: 2009-4-1 обнГ04:48:30
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yang.lei
 * @version 1.0
 */
public interface EnterpManageService {
	public void deleteEnterprise(String[] entCodes);
    public void modifyEntInfo(TEntValue tEntValue);
    public TEntValue getTEntValue(String entCode);
    public PageIterator getEntInfoList(int currentPage, int pageSize);
}
