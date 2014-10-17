package com.sosgps.v21.cash.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sosgps.v21.dao.PromotionDao;
import com.sosgps.v21.dao.TargetDao;
import com.sosgps.v21.dao.TargetTemplateDao;
import com.sosgps.v21.cash.service.PromotionService;

import com.sosgps.v21.model.Promotion;
import com.sosgps.v21.signBill.service.impl.SignBillServiceImpl;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class PromotionServiceImpl implements PromotionService {

	private static final Logger logger = LoggerFactory
			.getLogger(SignBillServiceImpl.class);

	private PromotionDao promotionDao;

	private TargetDao targetDao;

	private TargetTemplateDao targetTemplateDao;

	// 促销上报 ，明细查询
	public String listPromotionDetails(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UserInfo userInfo) throws Exception {
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String poiName = request.getParameter("searchValue");// 客户名称

		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);
		String approvedStr = request.getParameter("duration");// 状态:-1:全部,0:未审核,1:审核
		int approved = CharTools.str2Integer(approvedStr, -1);// 状态
		if (st == null || et == null || deviceIds.equals("")) {
			return "{result:\"9\"}";
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		Long startDateL = startDate.getTime();
		Long endDateL = endDate.getTime();
		if (start == null || limit == null || userInfo == null) {
			return "{result:\"9\"}";
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// add by 2012-12-17   查询促销上报日志记录
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "查询促销上报明细");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_PromotionaRreported);
		LogFactory.newLogInstance("optLogger").info(optLog);

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSp = new StringBuffer();
		int totalp = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Promotion> list = promotionDao.listPromotionDetails(entCode,
					pageNo, pageSize, startDateL, endDateL, approved, poiName,
					deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				totalp = list.getTotalCount();
				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				DecimalFormat df = new DecimalFormat("#.##");
				for (Promotion promotion : list.getResult()) {
					Double promotionAmountD = promotion.getPromotion1() == null ? 0D
							: promotion.getPromotion1().doubleValue();
					Double  promotionAmount2D = promotion.getPromotion2() == null ? 0D
							: promotion.getPromotion2().doubleValue();
					Double  promotionAmount3D = promotion.getPromotion3() == null ? 0D
							: promotion.getPromotion3().doubleValue();
					Double  promotionAmount4D = promotion.getPromotion4() == null ? 0D
							: promotion.getPromotion4().doubleValue();
					Double  promotionAmount5D = promotion.getPromotion5() == null ? 0D
							: promotion.getPromotion5().doubleValue();
					Double  promotionAmount6D = promotion.getPromotion6() == null ? 0D
							: promotion.getPromotion6().doubleValue();
					Double  promotionAmount7D = promotion.getPromotion7() == null ? 0D
							: promotion.getPromotion7().doubleValue();
					Double  promotionAmount8D = promotion.getPromotion8() == null ? 0D
							: promotion.getPromotion8().doubleValue();
					Double  promotionAmount9D = promotion.getPromotion9() == null ? 0D
							: promotion.getPromotion9().doubleValue();
					Double  promotionAmount10D = promotion.getPromotion10() == null ? 0D
							: promotion.getPromotion10().doubleValue();

					jsonSp.append("{");
					jsonSp.append("id:'" + promotion.getId() + "',");// id
					jsonSp.append("createOn:'"
							+ DateUtility.dateTimeToStr(new Date(promotion
									.getCreateOn())) + "',");// 日期
					jsonSp.append("groupName:'"
							+ CharTools.javaScriptEscape(promotion
									.getGroupName()) + "',");// 部门
					jsonSp.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(promotion
									.getTerminalName()) + "',");// 员工姓名
					 jsonSp.append("poiName:'"
					 + CharTools.javaScriptEscape(promotion.getPoiName())
					 + "',");// 客户名称
					jsonSp.append("promotionAmount:'" + df.format(promotionAmountD)
							+ "',");
					jsonSp.append("promotionAmount2:'" + df.format(promotionAmount2D)
							+ "',");
					jsonSp.append("promotionAmount3:'" + nf.format(promotionAmount3D)
							+ "',");
					jsonSp.append("promotionAmount4:'" + df.format(promotionAmount4D)
							+ "',");
					jsonSp.append("promotionAmount5:'" + df.format(promotionAmount5D)
							+ "',");
					jsonSp.append("promotionAmount6:'" + df.format(promotionAmount6D)
							+ "',");
					jsonSp.append("promotionAmount7:'" + df.format(promotionAmount7D)
							+ "',");
					jsonSp.append("promotionAmount8:'" + df.format(promotionAmount8D)
							+ "',");
					jsonSp.append("promotionAmount9:'" + df.format(promotionAmount9D)
							+ "',");
					jsonSp.append("promotionAmount10:'" + df.format(promotionAmount10D)
							+ "',");
					jsonSp.append("approved:'" + promotion.getApproved() + "'");// 状态
					jsonSp.append("},");

				}
				if (jsonSp.length() > 0) {
					jsonSp.deleteCharAt(jsonSp.length() - 1);
				}
			}
		}
		return "{totalp:" + totalp + ",data:[" + jsonSp.toString() + "]}";
	}

	// 促销上报 表导出

	public String listPromotionExpExcel(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UserInfo userInfo) throws Exception {
		// 从request中获取参数
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String poiName = request.getParameter("searchValue");// 客户名称
		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);
		String approvedStr = request.getParameter("duration");// 状态:-1:全部,0:未审核,1:审核
		int approved = CharTools.str2Integer(approvedStr, -1);// 状态
		System.out.println(st + et + deviceIds);
		if (st == null || et == null || deviceIds.equals("")) {
			return "{result:\"9\"}";
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		Long startDateL = startDate.getTime();
		Long endDateL = endDate.getTime();
		if (userInfo == null) {
			return "{result:\"9\"}";
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		//  add by 2012-12-17 zss 导出促销上报明细  日志记录
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "导出促销上报明细");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_PromotionaExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);

		Page<Promotion> list = promotionDao.listPromotionDetails(entCode, 1,
				65536, startDateL, endDateL, approved, poiName, deviceIds);
		ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
		// header
		excelWorkBook.addHeader("日期", 15);
		excelWorkBook.addHeader("部门", 15);
		excelWorkBook.addHeader("员工姓名", 15);
		excelWorkBook.addHeader("客户名称", 15);
		excelWorkBook.addHeader("分酒器", 15);
		excelWorkBook.addHeader("圆珠笔",15);
		excelWorkBook.addHeader("打火机", 15);
		excelWorkBook.addHeader("客户联系卡",15);
		excelWorkBook.addHeader("价格标签", 15);
		excelWorkBook.addHeader("牙签筒",15);
		excelWorkBook.addHeader("KT板", 15);
		excelWorkBook.addHeader("中国结",15);
		excelWorkBook.addHeader("POP", 15);
		excelWorkBook.addHeader("烟缸",15);
		excelWorkBook.addHeader("状态", 15);
		int row = 0;
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			DecimalFormat df = new DecimalFormat("#.##");
			for (Promotion promotion : list.getResult()) {
				Double promotionAmountD = promotion.getPromotion1() == null ? 0D
						: promotion.getPromotion1().doubleValue();
				Double  promotionAmount2D = promotion.getPromotion2() == null ? 0D
						: promotion.getPromotion2().doubleValue();
				Double  promotionAmount3D = promotion.getPromotion3() == null ? 0D
						: promotion.getPromotion3().doubleValue();
				Double  promotionAmount4D = promotion.getPromotion4() == null ? 0D
						: promotion.getPromotion4().doubleValue();
				Double  promotionAmount5D = promotion.getPromotion5() == null ? 0D
						: promotion.getPromotion5().doubleValue();
				Double  promotionAmount6D = promotion.getPromotion6() == null ? 0D
						: promotion.getPromotion6().doubleValue();
				Double  promotionAmount7D = promotion.getPromotion7() == null ? 0D
						: promotion.getPromotion7().doubleValue();
				Double  promotionAmount8D = promotion.getPromotion8() == null ? 0D
						: promotion.getPromotion8().doubleValue();
				Double  promotionAmount9D = promotion.getPromotion9() == null ? 0D
						: promotion.getPromotion9().doubleValue();
				Double  promotionAmount10D = promotion.getPromotion10() == null ? 0D
						: promotion.getPromotion10().doubleValue();

				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row, DateUtility
						.dateTimeToStr(new Date(promotion.getCreateOn())));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(promotion.getGroupName()));
				excelWorkBook
						.addCell(col++, row, CharTools
								.javaScriptEscape(promotion.getTerminalName()));
				 excelWorkBook.addCell(col++, row,
				 CharTools.javaScriptEscape(promotion.getPoiName()));
				excelWorkBook.addCell(col++, row, df.format(promotionAmountD));
				excelWorkBook.addCell(col++, row, df.format(promotionAmount2D));
				excelWorkBook.addCell(col++, row, df.format(promotionAmount3D));
				excelWorkBook.addCell(col++, row, df.format(promotionAmount4D));
				excelWorkBook.addCell(col++, row, df.format(promotionAmount5D));
				excelWorkBook.addCell(col++, row, df.format(promotionAmount6D));
				excelWorkBook.addCell(col++, row, df.format(promotionAmount7D));
				excelWorkBook.addCell(col++, row, df.format(promotionAmount8D));
				excelWorkBook.addCell(col++, row, df.format(promotionAmount9D));
				excelWorkBook.addCell(col++, row, df.format(promotionAmount10D));

				excelWorkBook.addCell(col++, row,
						promotion.getApproved() == 0 ? "未审核" : "审核");
			}

		}
		excelWorkBook.write();
		return "{result:\"1\"}";
	}

	// 促销上报 审核提交
	public String promotionApproved(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UserInfo userInfo) throws Exception {
		String ids = request.getParameter("ids");
		ids = CharTools.javaScriptEscape(ids);
		if (ids.equals("") || userInfo == null) {
			return "{result:\"9\"}";
		}
		Long[] idsL = CharTools.convertionToLong(ids.split(","));
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// searchLog
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "审核促销上报");
		optLog.setFunFType(LogConstants.LOG_Approved);
		optLog.setFunCType(LogConstants.LOG_Approved_Cash);
		LogFactory.newLogInstance("optLogger").info(optLog);
		boolean flag = promotionDao.promotionApproved(idsL, entCode);
		if (flag) {
			return "{result:\"1\"}";
		} else {
			return "{result:\"3\"}";
		}

	}

	public PromotionDao getPromotionDao() {
		return promotionDao;
	}

	public void setPromotionDao(PromotionDao promotionDao) {
		this.promotionDao = promotionDao;
	}

	public TargetDao getTargetDao() {
		return targetDao;
	}

	public void setTargetDao(TargetDao targetDao) {
		this.targetDao = targetDao;
	}

	public TargetTemplateDao getTargetTemplateDao() {
		return targetTemplateDao;
	}

	public void setTargetTemplateDao(TargetTemplateDao targetTemplateDao) {
		this.targetTemplateDao = targetTemplateDao;
	}

	
}
