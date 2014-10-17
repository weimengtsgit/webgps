package com.sosgps.v21.visit.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.sosgps.v21.model.Visit;
import com.sosgps.wzt.system.common.UserInfo;

public interface VisitService {

    public List<Visit> queryVisitsByCondition(Map<String, Object> paramMap, String entCode);
  
    public String getVisitsByTime(UserInfo userInfo);
    public String getVisitHisByTime(UserInfo userInfo);
    public String getCusVisitsByTime(UserInfo userInfo);
    public String getCusVisitHisByTime(UserInfo userInfo);
    public String listVisitDetails(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
    public String listVisitRanks(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
    public String listVisitRanksExpExcel(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
    public String getGaugeByTargetType(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
    public String getGaugeByTargetTypeCus(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
    public String listCustomVisitCountTj(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
    public String listCustomVisitCountTjByCustom(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
    public String listVisitCountTjSql(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
    public String listVisitCountTjByCustom(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception;
    
}
