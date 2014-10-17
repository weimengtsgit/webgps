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

	// �����ϱ� ����ϸ��ѯ
	public String listPromotionDetails(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UserInfo userInfo) throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String poiName = request.getParameter("searchValue");// �ͻ�����

		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);
		String approvedStr = request.getParameter("duration");// ״̬:-1:ȫ��,0:δ���,1:���
		int approved = CharTools.str2Integer(approvedStr, -1);// ״̬
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

		// add by 2012-12-17   ��ѯ�����ϱ���־��¼
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "��ѯ�����ϱ���ϸ");
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
									.getCreateOn())) + "',");// ����
					jsonSp.append("groupName:'"
							+ CharTools.javaScriptEscape(promotion
									.getGroupName()) + "',");// ����
					jsonSp.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(promotion
									.getTerminalName()) + "',");// Ա������
					 jsonSp.append("poiName:'"
					 + CharTools.javaScriptEscape(promotion.getPoiName())
					 + "',");// �ͻ�����
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
					jsonSp.append("approved:'" + promotion.getApproved() + "'");// ״̬
					jsonSp.append("},");

				}
				if (jsonSp.length() > 0) {
					jsonSp.deleteCharAt(jsonSp.length() - 1);
				}
			}
		}
		return "{totalp:" + totalp + ",data:[" + jsonSp.toString() + "]}";
	}

	// �����ϱ� ����

	public String listPromotionExpExcel(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UserInfo userInfo) throws Exception {
		// ��request�л�ȡ����
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String poiName = request.getParameter("searchValue");// �ͻ�����
		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);
		String approvedStr = request.getParameter("duration");// ״̬:-1:ȫ��,0:δ���,1:���
		int approved = CharTools.str2Integer(approvedStr, -1);// ״̬
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
		//  add by 2012-12-17 zss ���������ϱ���ϸ  ��־��¼
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "���������ϱ���ϸ");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_PromotionaExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);

		Page<Promotion> list = promotionDao.listPromotionDetails(entCode, 1,
				65536, startDateL, endDateL, approved, poiName, deviceIds);
		ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
		// header
		excelWorkBook.addHeader("����", 15);
		excelWorkBook.addHeader("����", 15);
		excelWorkBook.addHeader("Ա������", 15);
		excelWorkBook.addHeader("�ͻ�����", 15);
		excelWorkBook.addHeader("�־���", 15);
		excelWorkBook.addHeader("Բ���",15);
		excelWorkBook.addHeader("����", 15);
		excelWorkBook.addHeader("�ͻ���ϵ��",15);
		excelWorkBook.addHeader("�۸��ǩ", 15);
		excelWorkBook.addHeader("��ǩͲ",15);
		excelWorkBook.addHeader("KT��", 15);
		excelWorkBook.addHeader("�й���",15);
		excelWorkBook.addHeader("POP", 15);
		excelWorkBook.addHeader("�̸�",15);
		excelWorkBook.addHeader("״̬", 15);
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
						promotion.getApproved() == 0 ? "δ���" : "���");
			}

		}
		excelWorkBook.write();
		return "{result:\"1\"}";
	}

	// �����ϱ� ����ύ
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
		optLog.setOptDesc(userInfo.getUserAccount() + "��˴����ϱ�");
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
