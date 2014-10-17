package com.sosgps.v21.target.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.sos.helper.SpringHelper;

import com.sosgps.v21.dao.CashDao;
import com.sosgps.v21.dao.CostDao;
import com.sosgps.v21.dao.SignBillDao;
import com.sosgps.v21.dao.TargetDao;
import com.sosgps.v21.dao.TargetTemplateDao;
import com.sosgps.v21.dao.VisitDao;
import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.model.TargetTemplate;
import com.sosgps.v21.target.service.TargetService;
import com.sosgps.v21.util.DateUtils;
import com.sosgps.wzt.group.dao.GroupDAO;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.terminal.dao.TerminalDAO;
import com.sosgps.wzt.terminal.service.TerminalService;
import com.sosgps.wzt.util.CharTools;

public class TargetServiceImpl implements TargetService {

	private TargetDao targetDao;
	private GroupDAO groupDao;
	private TargetTemplateDao targetTemplateDao;
	private SignBillDao signBillDao;
	private CashDao cashDao;
	private CostDao costDao;
	private VisitDao visitDao;
	private TerminalDAO terminalDao;

	public CashDao getCashDao() {
		return cashDao;
	}

	public void setCashDao(CashDao cashDao) {
		this.cashDao = cashDao;
	}

	public CostDao getCostDao() {
		return costDao;
	}

	public void setCostDao(CostDao costDao) {
		this.costDao = costDao;
	}

	public TTerminal getTerminal(String deviceId) {
		return terminalDao.findById(deviceId);
	}

	public TerminalDAO getTerminalDao() {
		return terminalDao;
	}

	public void setTerminalDao(TerminalDAO terminalDao) {
		this.terminalDao = terminalDao;
	}
	
	public VisitDao getVisitDao() {
		return visitDao;
	}

	public void setVisitDao(VisitDao visitDao) {
		this.visitDao = visitDao;
	}

	public SignBillDao getSignBillDao() {
		return signBillDao;
	}

	public void setSignBillDao(SignBillDao signBillDao) {
		this.signBillDao = signBillDao;
	}

	public TargetTemplateDao getTargetTemplateDao() {
		return targetTemplateDao;
	}

	public void setTargetTemplateDao(TargetTemplateDao targetTemplateDao) {
		this.targetTemplateDao = targetTemplateDao;
	}

	public TargetDao getTargetDao() {
		return targetDao;
	}

	public GroupDAO getGroupDao() {
		return groupDao;
	}

	public void setTargetDao(TargetDao targetDao) {
		this.targetDao = targetDao;
	}

	public void setGroupDao(GroupDAO groupDao) {
		this.groupDao = groupDao;
	}

	public List getTerminalAndGroup(String entCode) {
		return targetDao.getTerminalAndGroup(entCode);
	}

	public TTermGroup getTermGroup(Long groupId) {
		return groupDao.findById(groupId);
	}

	public void addKpi(List<Kpi> kpiList, TUser operator) {
		long currentTime = new Date().getTime();
		for (Kpi kpi : kpiList) {
			kpi.setCreateOn(currentTime);
			kpi.setCreateBy(operator.getUserName());
			targetDao.addKpi(kpi);
		}
	}

	public void updateKpi(List<Kpi> kpiList, TUser operator) {
		long currentTime = new Date().getTime();
		for (Kpi kpi : kpiList) {
			kpi.setLastUpdateOn(currentTime);
			kpi.setLastUpdateBy(operator.getUserName());
			targetDao.updateKpi(kpi);
		}
	}

	public List<Kpi> getKpi(String entCode, int type) {
		return targetDao.getKpi(entCode, type);
	}

	public List<Kpi> getKpi(String entCode, String type) {
		return targetDao.getKpi(entCode, type);
	}

	public void updateTarget(List<TargetTemplate> targetList, TUser operator) {
		long currentTime = new Date().getTime();
		for (TargetTemplate target : targetList) {
			target.setLastUpdateOn(currentTime);
			target.setLastUpdateBy(operator.getUserName());
			targetDao.updateTarget(target);
		}
	}

	public void importTarget(String entCode, int type, int year,
			List<TargetTemplate> targetList, TUser operator) {
		if (targetList == null || targetList.size() < 1) 
			return;
		long currentTime = new Date().getTime();
		int count = targetDao.getTargetCount(entCode, type, year);
		// 1.第一次录入,2.模板类型被修改后第一次录入
		if (count == 0) {
			count = targetDao.getTargetCount(entCode, type);
			if (count == 0) {
				count = targetDao.getTargetCount(entCode);
				// 模板类型被修改后第一次录入,删除历史数据
				if (count > 0) {
					targetDao.deleteTarget(entCode);
				}
			}
			for (TargetTemplate target : targetList) {
				target.setCreateOn(currentTime);
				target.setCreateBy(operator.getUserName());
			}
			targetDao.addTarget(targetList);
		}
		// 模板数据修改
		else {
			StringBuffer sb = new StringBuffer();
			sb.append("delete from t_target_template t where t.ent_code='"
					+ entCode + "' and t.type=" + type + " and t.year=" + year
					+ " and (");
			for (TargetTemplate target : targetList) {
				sb.append(" (" 
						+ "t.target_on=" + target.getTargetOn() + ") or");
				
				target.setCreateOn(currentTime);
				target.setCreateBy(operator.getUserName());
			}
			
			sb.delete(sb.length() - 2, sb.length());
			sb.append(")");
			targetDao.executeSql(sb.toString());
			targetDao.addTarget(targetList);
		}
	}

	public List<TargetTemplate> getTarget(String entCode, String vehicleNumber,
			int type, int year, int targetOn, int start, int limit) {
		return targetDao.getTarget(entCode, vehicleNumber, type, year,
				targetOn, start, limit);
	}

	public int getTargetCount(String entCode, String vehicleNumber, int type,
			int year, int targetOn) {
		return targetDao.getTargetCount(entCode, vehicleNumber, type, year,
				targetOn);
	}

	public List<TargetTemplate> getTarget(String entCode, String vehicleNumber,
			int type, int year, int start, int limit) {
		return targetDao.getTarget(entCode, vehicleNumber, type, year, start,
				limit);
	}
	
	/**
	 * 获取企业级别的指标（分页）
	 */
	public List getEntTarget(String entCode,
			int type, int year, int start, int limit) {
		return targetDao.getEntTarget(entCode,type, year, start,
				limit);
	}
	
	/**
	 * 获取企业级别的指标
	 */
	public Map<String,Object[]> getEntTarget(String entCode,
			int type, int year) {
		Map<String,Object[]> targetMap = new HashMap<String, Object[]>();
		List targetList = targetDao.getEntTarget(entCode,type, year);
		
		for(int i = 0;i<targetList.size();i++) {
			Object[] nextObj =(Object[])targetList.get(i);
			targetMap.put(String.valueOf(nextObj[0]), (Object[])targetList.get(i));
		}
		return targetMap;
	}


	public int getTargetCount(String entCode, String vehicleNumber, int type,
			int year) {
		return targetDao.getTargetCount(entCode, vehicleNumber, type, year);
	}
	
	public int getEntTargetCount(String entCode, int type,
			int year) {
		return targetDao.getEntTargetCount(entCode, type, year);
	}

	public Map<String, TargetTemplate> getTarget(String entCode, int type,
			int year) {
		Map<String, TargetTemplate> targetMap = new HashMap<String, TargetTemplate>();
		List<TargetTemplate> targetList = targetDao.getTarget(entCode, type,
				year);
		if (targetList != null && targetList.size() > 0) {
			for (TargetTemplate target : targetList) {
				targetMap.put(
						target.getDeviceId() + "_" + target.getTargetOn(),
						target);
			}
		}
		return targetMap;
	}

	/**
	 * 获取v2.1首页所需报表值
	 * 
	 * @param userInfo
	 */
	/*public String getV21Main(UserInfo userInfo) {
		StringBuffer json = new StringBuffer();
		String entCode = userInfo.getEmpCode();
		String targetTemplateType = userInfo.getTargetTemplateType();
		int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
				.valueOf(targetTemplateType);
		Date d = new Date();
		int targetOn = DateUtils.getTargetOnInThisYear(d, targetTemplateType_);
		int year = DateUtils.getCurrentYear(d);
		// 取得1,2,3,4,5的区间维护值
		String types = "1,2,3,4,5";// 1-签单额达成率,2-回款额达成率,3-费用额使用率,4-员工出访达成率,5-客户拜访覆盖率
		List<Kpi> meterInterval = targetDao.getKpi(entCode, types);
		if (meterInterval != null && meterInterval.size() > 0) {
			json.append("meterInterval:{");
			StringBuffer sb_ = new StringBuffer();
			for (Iterator iterator = meterInterval.iterator(); iterator
					.hasNext();) {
				Kpi kpi_ = (Kpi) iterator.next();
				int type = kpi_.getType();
				// 区间值
				String value = kpi_.getValue();
				String[] values = value.split(",");
				String red = "0";
				String yellow = "0";
				String green = "0";
				// 解析区间值
				if (values.length == 3) {
					red = values[0];
					yellow = values[1];
					green = values[2];
				}
				if (type == 1) {// 签单额达成率
					sb_.append("signBillInterval:{red:" + red + ", yellow:"
							+ yellow + ", green:" + green + "},");
				} else if (type == 2) {// 回款额达成率
					sb_.append("cashInterval:{red:" + red + ", yellow:"
							+ yellow + ", green:" + green + "},");
				} else if (type == 3) {// 费用额使用率
					sb_.append("costInterval:{red:" + green + ", yellow:"
							+ yellow + ", green:" + red + "},");
				} else if (type == 4) {// 员工出访达成率
					sb_.append("visitInterval:{red:" + red + ", yellow:"
							+ yellow + ", green:" + green + "},");
				} else if (type == 5) {// 客户拜访覆盖率
					sb_.append("cusVisitInterval:{red:" + red + ", yellow:"
							+ yellow + ", green:" + green + "},");
				}
			}
			if (sb_.length() > 0) {
				sb_.deleteCharAt(sb_.length() - 1);
			}
			json.append(sb_.toString());
			json.append("},");
		} else {
			json.append("meterInterval:{");
			json.append("signBillInterval:{red:60, yellow:90, green:160},");
			json.append("cashInterval:{red:60, yellow:90, green:160},");
			json.append("costInterval:{red:60, yellow:90, green:160},");
			json.append("visitInterval:{red:60, yellow:90, green:160},");
			json.append("cusVisitInterval:{red:60, yellow:90, green:160}");
			json.append("},");
		}
		// 根据目标维护类型(周/旬/月)取得当前(周/旬/月)时间范围内的所有目标维护数据
		List<Object[]> curTargetTemplateCounts = targetTemplateDao
				.getCurrentTargetTemplateCount(entCode, targetTemplateType_,
						year, targetOn);
		String billAmount = "0";
		String cashAmount = "0";
		String costAmount = "0";
		String visitAmount = "0";
		if (curTargetTemplateCounts != null
				&& curTargetTemplateCounts.size() >= 1) {
			Object[] curTargetTemplateCounts_ = curTargetTemplateCounts.get(0);
			Double billAmountD = (Double) curTargetTemplateCounts_[0];
			billAmount = CharTools.killNullDouble2String(billAmountD, "0");
			Double cashAmountD = (Double) curTargetTemplateCounts_[1];
			cashAmount = CharTools.killNullDouble2String(cashAmountD, "0");
			Double costAmountD = (Double) curTargetTemplateCounts_[2];
			costAmount = CharTools.killNullDouble2String(costAmountD, "0");
			Long visitAmountL = (Long) curTargetTemplateCounts_[3];
			visitAmount = CharTools.killNullLong2String(visitAmountL, "0");
		}
		json.append("curTarTemplates:{");
		json.append("billAmount:" + billAmount + ",");
		json.append("cashAmount:" + cashAmount + ",");
		json.append("costAmount:" + costAmount + ",");
		json.append("visitAmount:" + visitAmount + ",");
		json.append("cusVisitAmount:" + visitAmount + "},");
		Long startTime = DateUtils.getStartTimeOfWeek(d);
		Long endTime = d.getTime();
		startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d);
		endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
		// 取得目标模板时间范围内签单额总值
		List<Double> signBillCounts = signBillDao.getSignBillCount(startTime,
				endTime, entCode, 0, 1);
		String signBillCounts_ = "0";
		if (signBillCounts != null && signBillCounts.size() >= 1) {
			Double signBillCountsD_ = signBillCounts.get(0);
			signBillCounts_ = CharTools.killNullDouble2String(signBillCountsD_,
					"0");
		}
		json.append("signBillCounts:" + signBillCounts_ + ",");
		// 当前签单额趋势值
		List<Object[]> signBills = signBillDao.getSignBillsByTime(startTime,
				endTime, entCode);
		json.append("signBills:[");
		if (signBills != null && signBills.size() >= 1) {
			StringBuffer sb = new StringBuffer();
			for (Iterator iterator = signBills.iterator(); iterator.hasNext();) {
				Object[] object_ = (Object[]) iterator.next();
				BigDecimal sign_bills_ = (BigDecimal) object_[0];
				String sign_billsS_ = "0";
				if (sign_bills_ != null) {
					Double sign_billsD_ = sign_bills_.doubleValue();
					sign_billsS_ = CharTools.killNullDouble2String(
							sign_billsD_, "0");
				}
				String createon_ = (String) object_[1];
				sb.append("{value:" + sign_billsS_ + ", date:'" + createon_
						+ "'},");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			json.append(sb.toString());
		}
		json.append("],");

		// 取得目标模板时间范围内回款额总值
		List<Double> cashCounts = cashDao.getCashCount(startTime, endTime,
				entCode, 1, 0);
		String cashCounts_ = "0";
		if (cashCounts != null && cashCounts.size() >= 1) {
			Double cashCountsD_ = cashCounts.get(0);
			cashCounts_ = CharTools.killNullDouble2String(cashCountsD_, "0");
		}
		json.append("cashCounts:" + cashCounts_ + ",");
		// startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_,
		// d);
		// endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
		// 当前回款额趋势值
		List<Object[]> cashs = cashDao.getCashsByTime(startTime, endTime,
				entCode);
		json.append("cashs:[");
		if (cashs != null && cashs.size() >= 1) {
			StringBuffer sb = new StringBuffer();
			for (Iterator iterator = cashs.iterator(); iterator.hasNext();) {
				Object[] object_ = (Object[]) iterator.next();
				BigDecimal cashs_ = (BigDecimal) object_[0];
				String cashsS_ = "0";
				if (cashs_ != null) {
					Double cashsD_ = cashs_.doubleValue();
					cashsS_ = CharTools.killNullDouble2String(cashsD_, "0");
				}
				String createon_ = (String) object_[1];
				sb.append("{value:" + cashsS_ + ", date:'" + createon_ + "'},");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			json.append(sb.toString());
		}
		json.append("],");

		// 取得目标模板时间范围内回款额总值
		List<Double> costCounts = costDao.getCostCount(startTime, endTime,
				entCode);
		String costCounts_ = "0";
		if (costCounts != null && costCounts.size() >= 1) {
			Double costCountsD_ = costCounts.get(0);
			costCounts_ = CharTools.killNullDouble2String(costCountsD_, "0");
		}
		json.append("costCounts:" + costCounts_ + ",");
		// startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_,
		// d);
		// endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
		// 当前回款额趋势值
		List<Object[]> costs = costDao.getCostsByTime(startTime, endTime,
				entCode);
		json.append("costs:[");
		if (costs != null && costs.size() >= 1) {
			StringBuffer sb = new StringBuffer();
			for (Iterator iterator = costs.iterator(); iterator.hasNext();) {
				Object[] object_ = (Object[]) iterator.next();
				BigDecimal costs_ = (BigDecimal) object_[0];
				String costsS_ = "0";
				if (costs_ != null) {
					Double costsD_ = costs_.doubleValue();
					costsS_ = CharTools.killNullDouble2String(costsD_, "0");
				}
				String createon_ = (String) object_[1];
				sb.append("{value:" + costsS_ + ", date:'" + createon_ + "'},");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			json.append(sb.toString());
		}
		json.append("],");

		// 取得目标模板时间范围内员工出访客户数
		List<Long> visitCounts = visitDao.getVisitCount(startTime, endTime,
				entCode);
		String visitCounts_ = "0";
		if (visitCounts != null && visitCounts.size() >= 1) {
			Long visitCountsL_ = visitCounts.get(0);
			visitCounts_ = CharTools.killNullLong2String(visitCountsL_, "0");
		}
		json.append("visitCounts:" + visitCounts_ + ",");
		// startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_,
		// d);
		// endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
		// 当前员工出访客户趋势值
		List<Object[]> visits = visitDao.getVisitsByTime(startTime, endTime,
				entCode);
		json.append("visits:[");
		if (visits != null && visits.size() >= 1) {
			StringBuffer sb = new StringBuffer();
			for (Iterator iterator = visits.iterator(); iterator.hasNext();) {
				Object[] object_ = (Object[]) iterator.next();
				BigDecimal visits_ = (BigDecimal) object_[0];
				String visitsS_ = "0";
				if (visits_ != null) {
					Double visitsD_ = visits_.doubleValue();
					visitsS_ = CharTools.killNullDouble2String(visitsD_, "0");
				}
				String createon_ = (String) object_[1];
				sb
						.append("{value:" + visitsS_ + ", date:'" + createon_
								+ "'},");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			json.append(sb.toString());
		}
		json.append("],");

		// 取得目标模板时间范围内客户拜访覆盖数
		List<Long> cusVisitCounts = visitDao.getCusVisitCount(startTime,
				endTime, entCode);
		String cusVisitCounts_ = "0";
		if (cusVisitCounts != null && cusVisitCounts.size() >= 1) {
			Long cusVisitCountsD_ = cusVisitCounts.get(0);
			cusVisitCounts_ = CharTools.killNullLong2String(cusVisitCountsD_,
					"0");
		}
		json.append("cusVisitCounts:" + cusVisitCounts_ + ",");
		// startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_,
		// d);
		// endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
		// 当前客户拜访覆盖趋势值
		List<Object[]> cusVisits = visitDao.getCusVisitsByTime(startTime,
				endTime, entCode);
		json.append("cusVisits:[");
		if (cusVisits != null && cusVisits.size() >= 1) {
			StringBuffer sb = new StringBuffer();
			for (Iterator iterator = cusVisits.iterator(); iterator.hasNext();) {
				Object[] object_ = (Object[]) iterator.next();
				BigDecimal cusVisits_ = (BigDecimal) object_[0];
				String cusVisitsS_ = "0";
				if (cusVisits_ != null) {
					Double cusVisitsD_ = cusVisits_.doubleValue();
					cusVisitsS_ = CharTools.killNullDouble2String(cusVisitsD_,
							"0");
				}
				String createon_ = (String) object_[1];
				sb.append("{value:" + cusVisitsS_ + ", date:'" + createon_
						+ "'},");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			json.append(sb.toString());
		}
		json.append("]");

		System.out.println("{" + json.toString() + "}");
		return "{" + json.toString() + "}";
	}*/


	public String getV21Main(UserInfo userInfo) {
		StringBuffer json = new StringBuffer();
		String entCode = userInfo.getEmpCode();
		String targetTemplateType = userInfo.getTargetTemplateType();
		int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
				.valueOf(targetTemplateType);
		Date d = new Date();
		int targetOn = DateUtils.getTargetOnInThisYear(d, targetTemplateType_);
		int year = DateUtils.getCurrentYear(d);
		// 取得1,2,3,4,5的区间维护值
		String types = "1,2,3,4,5";// 1-签单额达成率,2-回款额达成率,3-费用额使用率,4-员工出访达成率,5-客户拜访覆盖率
		List<Kpi> gaugeKpi = targetDao.getKpi(entCode, types);
		if (gaugeKpi != null && gaugeKpi.size() > 0) {
			json.append("gaugeKpi:{");
			StringBuffer sb_ = new StringBuffer();
			for (Iterator<Kpi> iterator = gaugeKpi.iterator(); iterator
					.hasNext();) {
				Kpi kpi_ = (Kpi) iterator.next();
				int type = kpi_.getType();
				// 区间值
				String value = kpi_.getValue();
				String[] values = value.split(",");
				String red = "0";
				String yellow = "0";
				String green = "0";
				// 解析区间值
				if (values.length == 3) {
					red = values[0];
					yellow = values[1];
					green = values[2];
				}
				if (type == 1) {// 签单额达成率区间值
					sb_.append("signBillGaugeKpi:{red:" + red + ", yellow:"
							+ yellow + ", green:" + green + "},");
				} else if (type == 2) {// 回款额达成率区间值
					sb_.append("cashGaugeKpi:{red:" + red + ", yellow:"
							+ yellow + ", green:" + green + "},");
				} else if (type == 3) {// 费用额使用率区间值
					sb_.append("costGaugeKpi:{red:" + green + ", yellow:"
							+ yellow + ", green:" + red + "},");
				} else if (type == 4) {// 员工出访达成率区间值
					sb_.append("visitGaugeKpi:{red:" + red + ", yellow:"
							+ yellow + ", green:" + green + "},");
				} else if (type == 5) {// 客户拜访覆盖率区间值
					sb_.append("cusVisitGaugeKpi:{red:" + red + ", yellow:"
							+ yellow + ", green:" + green + "},");
				}
			}
			if (sb_.length() > 0) {
				sb_.deleteCharAt(sb_.length() - 1);
			}
			json.append(sb_.toString());
			json.append("},");
		} else {
			json.append("gaugeKpi:{");
			json.append("signBillGaugeKpi:{red:60, yellow:90, green:160},");
			json.append("cashGaugeKpi:{red:60, yellow:90, green:160},");
			json.append("costGaugeKpi:{red:160, yellow:90, green:60},");
			json.append("visitGaugeKpi:{red:60, yellow:90, green:160},");
			json.append("cusVisitGaugeKpi:{red:60, yellow:90, green:160}");
			json.append("},");
		}
		// 根据目标维护类型(周/旬/月)取得当前(周/旬/月)时间范围内的所有目标维护数据
		List<Object[]> curTargetTemplateCounts = targetTemplateDao
				.getCurrentTargetTemplateCount(entCode, targetTemplateType_,
						year, targetOn);
		String billAmount = "0";
		String cashAmount = "0";
		String costAmount = "0";
		String visitAmount = "0";
		if (curTargetTemplateCounts != null
				&& curTargetTemplateCounts.size() >= 1) {
			Object[] curTargetTemplateCounts_ = curTargetTemplateCounts.get(0);
			Double billAmountD = (Double) curTargetTemplateCounts_[0];
			billAmount = CharTools.killNullDouble2String(billAmountD, "0");
			Double cashAmountD = (Double) curTargetTemplateCounts_[1];
			cashAmount = CharTools.killNullDouble2String(cashAmountD, "0");
			Double costAmountD = (Double) curTargetTemplateCounts_[2];
			costAmount = CharTools.killNullDouble2String(costAmountD, "0");
			Long visitAmountL = (Long) curTargetTemplateCounts_[3];
			visitAmount = CharTools.killNullLong2String(visitAmountL, "0");
		}
		json.append("curTarTemplates:{");
		json.append("billAmount:" + billAmount + ",");
		json.append("cashAmount:" + cashAmount + ",");
		json.append("costAmount:" + costAmount + ",");
		json.append("visitAmount:" + visitAmount + ",");
		json.append("cusVisitAmount:" + visitAmount + "},");
		
		System.out.println("{" + json.toString() + "}");
		return "{" + json.toString() + "}";
	}

	public int getStaffCount(String entCode) {
		return targetDao.getStaffCount(entCode);
	}
     
	/**
	 * 获取某个企业某个年下的某个月份（旬、周）的指标
	 * @param entCode
	 * @param targetOn
	 * @param type
	 * @param year
	 * @return
	 * @author wangzhen
	 */
	public List<TargetTemplate> getTarget(String entCode, Integer targetOn,
			Integer type, Integer year) {
		
		return targetDao.getTarget(entCode, targetOn, type, year);
	}
    
	//add by wangzhen 
	public void updateEntTarget(List<TargetTemplate> avgTargets, TUser user) {
		long currentTime = new Date().getTime();
		//删除某一年某一粒度下的所有员工指标
		for(TargetTemplate target : avgTargets) {
			try {
				targetDao.deleteTarget(target.getEntCode(), target.getType(),target.getYear(), target.getTargetOn());
				target.setCreateBy(user.getUserName());
				target.setCreateOn(currentTime);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		//将用户修改的内容进行更新
		try {
			targetDao.addTarget(avgTargets);
		}catch (Exception e) {
			
		}
	}

	public List<TargetTemplate> getTarget(String entCode) {
		List<TTerminal> tterminals = targetDao.getTerminal(entCode);
		List<TargetTemplate> tt = new ArrayList<TargetTemplate>();
		TerminalService tS = (TerminalService) SpringHelper
				.getBean("tTargetObjectService");
		for(TTerminal tte : tterminals) {
			TargetTemplate target = new TargetTemplate();
			target.setDeviceId(tte.getDeviceId());
			target.setStates(0);
			target.setGroupId(tS.getTermGroup(tS.findTerminal(tte.getDeviceId())).getTTermGroup().getId());
			target.setGroupName(tS.getTermGroup(tS.findTerminal(tte.getDeviceId())).getTTermGroup().getGroupName());
			//modify by wangzhen start 2012-09-12
//			target.setVehicleNumber(tte.getVehicleNumber());
			target.setTerminalName(tte.getTermName());
			//modify by wangzhen end 2012-09-12
			tt.add(target);
		}
		return tt;
	}

	public List<TargetTemplate> fillTarget(String entCode, Integer type,
			Integer year, Integer targetOn, List<TargetTemplate> noTargets) {
		List<TargetTemplate> newTT = new ArrayList<TargetTemplate>();
		for(TargetTemplate tt : noTargets) {
			tt.setType(type);
			tt.setYear(year);
			tt.setEntCode(entCode);
			tt.setTargetOn(targetOn);
			newTT.add(tt);
		}
		return newTT;
	}
}
