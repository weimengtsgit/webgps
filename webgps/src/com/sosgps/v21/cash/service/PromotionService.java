package com.sosgps.v21.cash.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import com.sosgps.wzt.system.common.UserInfo;

public interface PromotionService {
	
	//��ϸ��ѯ
	public String listPromotionDetails(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
	
	//add 2012-12-12 �����ϱ�����
	public String listPromotionExpExcel(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo)throws Exception;
	// �����ϱ� ���
	public String promotionApproved(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;

	
}
