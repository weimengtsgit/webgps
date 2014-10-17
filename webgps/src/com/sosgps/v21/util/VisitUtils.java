package com.sosgps.v21.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.sos.helper.SpringHelper;

import com.sosgps.v21.model.Visit;
import com.sosgps.v21.target.service.TargetService;
import com.sosgps.v21.visit.model.VisitStat;

/**
 * ����ת��������(ǩ��ǩ��)
 * 
 * @author liuhx
 * 
 */
public class VisitUtils {

	private static final Logger logger = Logger.getLogger(VisitUtils.class);

	/**
	 * ��ǩ��ǩ�����ݴ�List<Visit>��ʽת���ɻ��ܺ��Excel
	 * 
	 * @param data
	 *            ǩ��ǩ������(ת��ǰ)
	 * @return ǩ��ǩ�˻�������(ת����)
	 */
	public static Workbook convertVisitStatFromObjectToExcel(
			List<Visit> visits, Class<? extends Workbook> clazz)
			throws InstantiationException, IllegalAccessException {
		TargetService targetService = (TargetService) SpringHelper
				.getBean("targetService");

		/** ��ǩ��ǩ�����ݻ��� * */
		Set<String> dateSet = new LinkedHashSet<String>();
		//List<Long>  dateTimelist = new ArrayList<Long>();
		Map<String, VisitStat> contentMap = new HashMap<String, VisitStat>();

		VisitStat vs = null;
		Long groupId = null;
		String deviceId = null;
		String departName = null;
		String employeeName = null;
		String customerName = null;
		String day = null;
		Set<String> customerNames = null;
		List<Long> signInTimeList = null;
		List<Long> signOutTimeList = null;
		Long signInTime = null;
		Long signOutTime = null;
		
		for (Visit visit : visits) {
			// ��ȡ����ȫ����
			groupId = visit.getGroupId();
			deviceId = visit.getDeviceId();
			departName = TargetUtils.getGroupFullName(groupId, targetService);
			//modify by wangzhen
			//employeeName = visit.getVehicleNumber();
			employeeName = visit.getTermName();
			customerName = visit.getPoiName();
			signInTime = visit.getSignInTime();
			signOutTime = visit.getSignOutTime();
			
			day = DateUtils.dateTimeToStr(new Date(visit.getCreateOn()),
					"yyyy-MM-dd");
			dateSet.add(day);

			if (contentMap.get(deviceId) == null) {
				vs = new VisitStat();
				vs.setDepartName(departName);
				vs.setEmployeeName(employeeName);
				customerNames = new LinkedHashSet<String>();
				signInTimeList = new ArrayList<Long>();
				customerNames.add(customerName);
				vs.getDateCustomerMap().put(day, customerNames);
				if(signInTime != null) {
					signInTimeList.add(signInTime);
				}
				vs.getSignInTimeMap().put(day, signInTimeList);
				
				signOutTimeList = new ArrayList<Long>();
				if(signOutTime != null) {
					signOutTimeList.add(signOutTime);
				}
				vs.getSignOutTimeMap().put(day, signOutTimeList);
				contentMap.put(deviceId, vs);
				vs.setMaxCustomerCount(1);
			} else {
				vs = contentMap.get(deviceId);
				if (vs.getDateCustomerMap().get(day) == null) {
					customerNames = new LinkedHashSet<String>();
					customerNames.add(customerName);
					vs.getDateCustomerMap().put(day, customerNames);
					signInTimeList = new ArrayList<Long>();
					if(signInTime != null) {
						signInTimeList.add(signInTime);
					}
					vs.getSignInTimeMap().put(day, signInTimeList);
					signOutTimeList = new ArrayList<Long>();
					if(signOutTime != null) {
						signOutTimeList.add(signOutTime);
					}
					vs.getSignOutTimeMap().put(day, signOutTimeList);
					if (vs.getMaxCustomerCount() < 1) {
						vs.setMaxCustomerCount(1);
					}
				} else {
					Set<String> customerSet = vs.getDateCustomerMap().get(day);
					customerSet.add(customerName);
					List<Long> signInTimes = vs.getSignInTimeMap().get(day);
					if(signInTime != null) {
						signInTimes.add(signInTime);
					}
					
					List<Long> signOutTimes = vs.getSignOutTimeMap().get(day);
					if(signOutTime != null) {
						signOutTimes.add(signOutTime);
					}
					if (vs.getMaxCustomerCount() < customerSet.size()) {
						vs.setMaxCustomerCount(customerSet.size());
					}
				}
			}
		}
	
		Workbook wb = clazz.newInstance();
		CellStyle defaultCs = getDefaultCellStyle(wb);
		CellStyle headerCs = getHeaderCellStyle(wb);
		Sheet sheet = wb.createSheet();

		/** ��װͷ��Ϣ * */
		addCell(sheet, 0, 1, 0, 0, "����", headerCs, true);
		addCell(sheet, 0, 1, 1, 1, "Ա������", headerCs, false);
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 5000);
		int columenum = 2;
		for (String date : dateSet) {
			addCell(sheet, 0, 0, columenum, columenum + 2, date, headerCs, false);
			addCell(sheet, 1, 1, columenum, columenum, "����ǩ��ʱ��", headerCs, false);
			sheet.setColumnWidth(columenum, 5000);
			addCell(sheet, 1, 1, columenum + 1, columenum + 1, "�ͻ�����", headerCs, false);
			sheet.setColumnWidth(columenum + 1, 5000);
			addCell(sheet, 1, 1, columenum + 2, columenum + 2,  "���ǩ��ʱ��", headerCs, false);
			sheet.setColumnWidth(columenum + 2, 5000);
			columenum = columenum + 3;
		}

		/** ��װ���� * */
		int rownum = 2;
		Collection<VisitStat> vsList = contentMap.values();
		Iterator<VisitStat> it = vsList.iterator();
		while (it.hasNext()) {
			VisitStat visitStat = it.next();
			int maxCustomerCount = visitStat.getMaxCustomerCount();
			if (maxCustomerCount < 1) {
				continue;
			}
			addCell(sheet, rownum, rownum + maxCustomerCount - 1, 0, 0,
					visitStat.getDepartName(), defaultCs, true);
			addCell(sheet, rownum, rownum + maxCustomerCount - 1, 1, 1,
					visitStat.getEmployeeName(), defaultCs, false);
			columenum = 2;
			Map<String, Set<String>> dateCustomerMap = visitStat
					.getDateCustomerMap();
			Map<String,List<Long>> signInTimes = visitStat
					.getSignInTimeMap();
			Map<String,List<Long>> signOutTimes = visitStat
					.getSignOutTimeMap();
			Long latestOutTime = null;
			 Long latestInTime = null;
			 
			for (String date : dateSet) {
				
				Set<String> customerSet = dateCustomerMap.get(date);
				List<Long> inTimes = signInTimes.get(date);
				List<Long> outTimes = signOutTimes.get(date);
				
				String customers;
				if (customerSet == null || customerSet.size() < 1)
					customers = "";
				else
					customers = StringUtils.join(customerSet.toArray(), "\n");
				    if(inTimes != null && inTimes.size() != 0) {
				    	latestInTime = Collections.min(inTimes);
				    	addCell(sheet, rownum, rownum + maxCustomerCount - 1,
								columenum, columenum, DateUtils.dateTimeToStr(new Date(latestInTime)), defaultCs, false);
				    } else {
				    	addCell(sheet, rownum, rownum + maxCustomerCount - 1,
								columenum, columenum, "", defaultCs, false);
				    }
				     
				    addCell(sheet, rownum, rownum + maxCustomerCount - 1,
						    columenum + 1, columenum + 1, customers, defaultCs, false);
					 if(outTimes != null && outTimes.size() != 0) {
					    	latestOutTime = Collections.max(outTimes);
					    	addCell(sheet, rownum, rownum + maxCustomerCount - 1,
									columenum + 2, columenum + 2, DateUtils.dateTimeToStr(new Date(latestOutTime)), defaultCs, false);
					    } else {
					    	addCell(sheet, rownum, rownum + maxCustomerCount - 1,
									columenum + 2, columenum + 2, "", defaultCs, false);
					    }
				
				   columenum = columenum + 3;
			}
			rownum = rownum + maxCustomerCount;
		}
		
		/** ����Excel���� * */
		sheet.createFreezePane(2, 2);
		return wb;
	}

	/**
	 * ��ǩ��ǩ�����ݴ�List<Visit>��ʽת����Json
	 * 
	 * @param kpiList
	 *            ǩ��ǩ������(ת��ǰ)
	 * @return ǩ��ǩ������(ת����)
	 */
	public static String convertVisitFromObjectToJson(List<Visit> visits) {
		if (visits == null || visits.size() < 1)
			return "";

		StringBuffer json = new StringBuffer();

		/** ��װJSON * */
		for (Visit visit : visits) {
			if (json.length() < 1) {
				json.append("{");
			} else {
				json.append(",{");
			}
			//modify by wangzhen
//			json.append("vehicleNumber:'" + visit.getVehicleNumber() + "',");
			json.append("vehicleNumber:'" + visit.getTermName() + "',");
			json.append("poiName:'" + visit.getPoiName() + "',");
			json.append("signInTime:'" + visit.getSignInTime() + "',");
			json.append("signInRenderTime:'" + visit.getSignInRenderTime()
					+ "',");
			json.append("signInLat:'" + visit.getSignInLat() + "',");
			json.append("signInLng:'" + visit.getSignInLng() + "',");
			json.append("signInDistance:'" + visit.getSignInDistance() + "',");
			json.append("signOutTime:'" + visit.getSignOutTime() + "',");
			json.append("signOutRenderTime:'" + visit.getSignOutRenderTime()
					+ "',");
			json.append("signOutLat:'" + visit.getSignOutLat() + "',");
			json.append("signOutLng:'" + visit.getSignOutLng() + "',");
			json.append("signOutDistance:'" + visit.getSignOutDistance() + "'");
			json.append("}");
		}

		return json.toString();
	}

	/**
	 * ��ǩ��ǩ�����ݴ�List<Visit>��ʽת����Excel
	 * �ͻ��ݷ���ϸ��¼����
	 * @param data
	 *            ǩ��ǩ������(ת��ǰ)
	 * @return ǩ��ǩ������(ת����)
	 */
	public static Workbook convertVisitFromObjectToExcel(List<Visit> visits,
			Class<? extends Workbook> clazz) throws InstantiationException,
			IllegalAccessException {
		Workbook wb = clazz.newInstance();
		CellStyle defaultCs = getDefaultCellStyle(wb);
		CellStyle headerCs = getHeaderCellStyle(wb);
		Sheet sheet = wb.createSheet();
        logger.info("new(�ͻ��ݷ���ϸ��¼)Excelstart...");
		/** ��װͷ��Ϣ * */
		addCell(sheet, 0, 0, 0, 0, "Ա������", headerCs, true);
		addCell(sheet, 0, 0, 1, 1, "�ͻ�����", headerCs, false);
		addCell(sheet, 0, 0, 2, 2, "ǩ��λ������", headerCs, false);  
		sheet.setColumnWidth(2, 6000);
		addCell(sheet, 0, 0, 3, 3, "ǩ��ƫ��(��)", headerCs, false);
		addCell(sheet, 0, 0, 4, 4, "ǩ��ʱ��", headerCs, false);
		sheet.setColumnWidth(4, 6000);
		addCell(sheet, 0, 0, 5, 5, "ǩ��λ������", headerCs, false);
		sheet.setColumnWidth(5, 6000);
		addCell(sheet, 0, 0, 6, 6, "ǩ��ƫ��(��)", headerCs, false);
		addCell(sheet, 0, 0, 7, 7, "ǩ��ʱ��", headerCs, false);
		sheet.setColumnWidth(7, 6000);
		addCell(sheet, 0, 0, 8, 8, "ǩ�������ϴ�ʱ��", headerCs, false);
		sheet.setColumnWidth(8, 6000);
		addCell(sheet, 0, 0, 9, 9, "ǩ�������ϴ�ʱ��", headerCs, false);
		sheet.setColumnWidth(9, 6000);
		addCell(sheet, 0, 0, 10, 10, "ǩ����ȡ��ʽ", headerCs, false);
		addCell(sheet, 0, 0, 11, 11, "ǩ������", headerCs, false);
		addCell(sheet, 0, 0, 12, 12, "ǩ��γ��", headerCs, false);
		addCell(sheet, 0, 0, 13, 13, "ǩ�˻�ȡ��ʽ", headerCs, false);
		addCell(sheet, 0, 0, 14, 14, "ǩ�˾���", headerCs, false);
		addCell(sheet, 0, 0, 15, 15, "ǩ��γ��", headerCs, false);
		
		/** ��װ���� * */
		int rownum = 1;
		for (Visit visit : visits) {
			//modify by wangzhen 
//			addCell(sheet, rownum, rownum, 0, 0, visit.getVehicleNumber(),
//					defaultCs, true);
			addCell(sheet, rownum, rownum, 0, 0, visit.getTermName(),
			defaultCs, true);
			addCell(sheet, rownum, rownum, 1, 1, visit.getPoiName(), defaultCs,
					false);
			//����������ʱ��
			if (visit.getSignInTime() != null) {
				addCell(sheet,rownum,rownum,2,2,visit.getSignInDesc(),defaultCs,false);
				addCell(sheet,rownum,rownum,3,3,visit.getSignInDistance(),defaultCs,false);
				addCell(sheet, rownum, rownum, 4, 4, DateUtils.dateTimeToStr(
				        visit.getSignInRenderTime(), "yyyy-MM-dd HH:mm:ss"),
						defaultCs, false);
				addCell(sheet, rownum, rownum, 8, 8, DateUtils.dateTimeToStr(
				        visit.getSignInTime(), "yyyy-MM-dd HH:mm:ss"),
						defaultCs, false);
				
				if(null == visit.getLocationTypeIn() || 0l == visit.getLocationTypeIn()) {
					addCell(sheet,rownum,rownum,10,10,"GPS",defaultCs,false);
				}else {
					addCell(sheet,rownum,rownum,10,10,"CELLID",defaultCs,false);
				}
				
				addCell(sheet,rownum,rownum,11,11,visit.getSignInLng(),defaultCs,false);
				addCell(sheet,rownum,rownum,12,12,visit.getSignInLat(),defaultCs,false);
			} else {
				addCell(sheet,rownum,rownum,2,2,"",defaultCs,false);
				addCell(sheet,rownum,rownum,3,3,"",defaultCs,false);
				addCell(sheet, rownum, rownum, 4, 4, "",defaultCs, false);
				addCell(sheet, rownum, rownum, 8, 8, "",defaultCs, false);
				addCell(sheet,rownum,rownum,10,10,"",defaultCs,false);
				addCell(sheet,rownum,rownum,11,11,"",defaultCs,false);
				addCell(sheet,rownum,rownum,12,12,"",defaultCs,false);
			}
			
			//����������ʱ��
			if(visit.getSignOutTime() != null) {
				addCell(sheet,rownum,rownum,5,5,visit.getSignOutDesc(),defaultCs,false);
				addCell(sheet,rownum,rownum,6,6,visit.getSignOutDistance(),defaultCs,false);
				addCell(sheet, rownum, rownum, 7, 7, DateUtils.dateTimeToStr(
				        visit.getSignOutRenderTime(), "yyyy-MM-dd HH:mm:ss"),
						defaultCs, false);
				addCell(sheet, rownum, rownum, 9, 9, DateUtils.dateTimeToStr(
				        visit.getSignOutTime(), "yyyy-MM-dd HH:mm:ss"),
						defaultCs, false);
				if(null == visit.getLocationTypeOut() || 0l == visit.getLocationTypeOut()) {
					addCell(sheet,rownum,rownum,13,13,"GPS",defaultCs,false);
				}else {
					addCell(sheet,rownum,rownum,13,13,"CELLID",defaultCs,false);
				}
				addCell(sheet,rownum,rownum,14,14,visit.getSignOutLng(),defaultCs,false);
				addCell(sheet,rownum,rownum,15,15,visit.getSignOutLat(),defaultCs,false);
			} else {
				addCell(sheet,rownum,rownum,5,5,"",defaultCs,false);
				addCell(sheet,rownum,rownum,6,6,"",defaultCs,false);
				addCell(sheet, rownum, rownum, 7, 7, "",defaultCs, false);
				addCell(sheet, rownum, rownum, 9, 9, "",defaultCs, false);
				addCell(sheet,rownum,rownum,13,13,"",defaultCs,false);
				addCell(sheet,rownum,rownum,14,14,"",defaultCs,false);
				addCell(sheet,rownum,rownum,15,15,"",defaultCs,false);
			}
			rownum++;
		}
		logger.info("new(�ͻ��ݷ���ϸ��¼)Excelend...");
		return wb;
	}

	private static CellStyle getDefaultCellStyle(Workbook wb) {
		CellStyle cs = wb.createCellStyle();
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs.setWrapText(true);
		return cs;
	}

	private static CellStyle getHeaderCellStyle(Workbook wb) {
		CellStyle cs = getDefaultCellStyle(wb);
		Font font = wb.createFont();
		// ���������СΪ24
		font.setFontHeightInPoints((short) 11);
		// ����������ʽΪ��������
		font.setFontName("����");
		// �Ӵ�
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// б��
		// font.setItalic(true);
		// ���ɾ����
		// font.setStrikeout(true);
		// ��������ӵ���ʽ��
		cs.setFont(font);
		return cs;
	}

	private static Cell addCellStyle(Sheet sheet, int startrownum,
			int endrownum, int startcolumenum, int endcolumenum, CellStyle cs,
			boolean create) {
		if (sheet == null) {
			throw new IllegalArgumentException(
					"parameter 'sheet' cann't be null!");
		}
		Row row = null;
		Cell cell = null;

		if (startrownum != endrownum || startcolumenum != endcolumenum) {
			for (int i = startrownum; i <= endrownum; i++) {
				if (create)
					row = sheet.createRow(i);
				else
					row = sheet.getRow(i);
				for (int j = startcolumenum; j <= endcolumenum; j++) {
					cell = row.createCell(j);
					if (cs != null)
						cell.setCellStyle(cs);
				}
			}

			sheet.addMergedRegion(new CellRangeAddress(startrownum, endrownum,
					startcolumenum, endcolumenum));

			row = sheet.getRow(startrownum);
			cell = row.getCell(startcolumenum);
		} else {
			if (create)
				row = sheet.createRow(startrownum);
			else
				row = sheet.getRow(startrownum);
			cell = row.createCell(startcolumenum);

			if (cs != null)
				cell.setCellStyle(cs);
		}
		return cell;
	}

	private static void addCell(Sheet sheet, int startrownum, int endrownum,
			int startcolumenum, int endcolumenum, String value, CellStyle cs,
			boolean create) {
		Cell cell = addCellStyle(sheet, startrownum, endrownum, startcolumenum,
				endcolumenum, cs, create);
		cell.setCellValue(value);
	}

	private static void addCell(Sheet sheet, int startrownum, int endrownum,
			int startcolumenum, int endcolumenum, Integer value, CellStyle cs,
			boolean create) {
		Cell cell = addCellStyle(sheet, startrownum, endrownum, startcolumenum,
				endcolumenum, cs, create);
		if (value != null)
			cell.setCellValue(value);
	}

	private static void addCell(Sheet sheet, int startrownum, int endrownum,
			int startcolumenum, int endcolumenum, Double value, CellStyle cs,
			boolean create) {
		Cell cell = addCellStyle(sheet, startrownum, endrownum, startcolumenum,
				endcolumenum, cs, create);
		if (value != null)
			cell.setCellValue(value);
	}

}
