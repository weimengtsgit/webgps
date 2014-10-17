package com.sosgps.v21.visit.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VisitStat {

	/** 部门名称 */
	private String departName;

	/** 员工名称 */
	private String employeeName;

	private Map<String, Set<String>> dateCustomerMap = new HashMap<String, Set<String>>();
	
	/** 一天的所有签到时间 **/
	private Map<String,List<Long>> signInTimeMap = new HashMap<String,List<Long>>();
	/** 一天的所有签退时间 **/
	private Map<String,List<Long>> signOutTimeMap = new HashMap<String,List<Long>>();

	/** 记录在dateCustomerMap中拜访客户数最多的 */
	private int maxCustomerCount;

	public int getMaxCustomerCount() {
		return maxCustomerCount;
	}

	public void setMaxCustomerCount(int maxCustomerCount) {
		this.maxCustomerCount = maxCustomerCount;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Map<String, Set<String>> getDateCustomerMap() {
		return dateCustomerMap;
	}

	public void setDateCustomerMap(Map<String, Set<String>> dateCustomerMap) {
		this.dateCustomerMap = dateCustomerMap;
	}

	public Map<String, List<Long>> getSignInTimeMap() {
		return signInTimeMap;
	}

	public void setSignInTimeMap(Map<String, List<Long>> signInTimeMap) {
		this.signInTimeMap = signInTimeMap;
	}

	public Map<String, List<Long>> getSignOutTimeMap() {
		return signOutTimeMap;
	}

	public void setSignOutTimeMap(Map<String, List<Long>> signOutTimeMap) {
		this.signOutTimeMap = signOutTimeMap;
	}
}
