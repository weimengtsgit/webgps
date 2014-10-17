package com.sosgps.v21.util;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.sosgps.v21.model.TravelCost;

/**
 * ����ת��������(������Ϣ����)
 * 
 * @author wangzhen
 * 
 */
public class TravelCostUtils {

	private static final Logger logger = Logger.getLogger(TravelCostUtils.class);

	

	/**
	 * ��������Ϣ��List<TravelCost>��ʽת����Excel
	 * ������Ϣ����
	 * @param data
	 *            ������Ϣ(ת��ǰ)
	 * @return ������Ϣ(ת����)
	 */
	public static Workbook convertTravelCostFromObjectToExcel(List<TravelCost> travelCosts,
			Class<? extends Workbook> clazz) throws InstantiationException,
			IllegalAccessException {
	    DecimalFormat df = new DecimalFormat("#.##");
		Workbook wb = clazz.newInstance();
		CellStyle defaultCs = getDefaultCellStyle(wb);
		CellStyle headerCs = getHeaderCellStyle(wb);
		Sheet sheet = wb.createSheet();
        logger.info("new(������Ϣ����)Excelstart...");
		/** ��װͷ��Ϣ * */
		addCell(sheet, 0, 1, 0, 0, "Ա������", headerCs, true);
		addCell(sheet, 0, 1, 1, 1, "����", headerCs, false);
		sheet.setColumnWidth(1, 5000);
		addCell(sheet, 0, 1, 2, 2, "�ֻ�����", headerCs, false);  
		sheet.setColumnWidth(2, 5000);
		addCell(sheet, 0, 1, 3, 3, "��ʼʱ��", headerCs, false);
		sheet.setColumnWidth(3, 6000);
		addCell(sheet, 0, 1, 4, 4, "����ʱ��", headerCs, false);
		sheet.setColumnWidth(4, 6000);
		addCell(sheet, 0, 1, 5, 5, "�ϱ�������", headerCs, false);
		sheet.setColumnWidth(5, 6000);
		addCell(sheet, 0, 1, 6, 6, "�ϱ�Ŀ�ĵ�", headerCs, false);
		sheet.setColumnWidth(6, 6000);
		addCell(sheet, 0, 1, 7, 7, "ʵ�ʳ�����", headerCs, false);
		sheet.setColumnWidth(7, 6000);
		addCell(sheet, 0, 1, 8, 8, "ʵ��Ŀ�ĵ�", headerCs, false);
		sheet.setColumnWidth(8, 6000);
		addCell(sheet, 0, 1, 9, 9, "��������", headerCs, false);
		sheet.setColumnWidth(9, 6000);
		addCell(sheet, 0, 0, 10, 12, "��ͨ", headerCs, false);
		addCell(sheet, 0, 0, 13, 14, "ס��", headerCs, false);
		addCell(sheet, 0, 0, 15, 17, "�����", headerCs, false);
		addCell(sheet, 0, 0, 18, 19, "���ڽ�ͨ", headerCs, false);
		addCell(sheet, 0, 0, 20, 22, "����", headerCs, false);
		addCell(sheet, 0, 1, 23, 23, "���С��", headerCs, false);
		addCell(sheet, 0, 1, 24, 24, "״̬", headerCs, false);
		//��ͨ������
		addCell(sheet, 1, 1, 10, 10, "����", headerCs, false);
		addCell(sheet, 1, 1, 11, 11, "��Ԫ��", headerCs, false);
		addCell(sheet, 1, 1, 12, 12, "���ݣ��ţ�", headerCs, false);
		 //ס�޵�����
        addCell(sheet, 1, 1, 13, 13, "��Ԫ��", headerCs, false);
        addCell(sheet, 1, 1, 14, 14, "���ݣ��ţ�", headerCs, false);
        //�����
        addCell(sheet, 1, 1, 15, 15, "����", headerCs, false);
        addCell(sheet, 1, 1, 16, 16, "��Ԫ��", headerCs, false);
        addCell(sheet, 1, 1, 17, 17, "���ݣ��ţ�", headerCs, false);
        //���ڽ�ͨ
        addCell(sheet, 1, 1, 18, 18, "��Ԫ��", headerCs, false);
        addCell(sheet, 1, 1, 19, 19, "���ݣ��ţ�", headerCs, false);
        //����
        addCell(sheet, 1, 1, 20, 20, "��Ŀ", headerCs, false);
        addCell(sheet, 1, 1, 21, 21, "��Ԫ��", headerCs, false);
        addCell(sheet, 1, 1, 22, 22, "���ݣ��ţ�", headerCs, false);
        
		
		/** ��װ���� * */
		int rownum = 2;
		Double countTrafficCost = 0.0;
		Integer countTrafficBills = 0;
		Double countHotelCost = 0.0;
		Integer countHotelBills = 0;
		Double countSubsidyCost = 0.0;
		Integer countSubsidyBills = 0;
		Double countCityTraCost = 0.0;
		Integer countCityTraBills = 0;
		Double countOtherCost = 0.0;
		Integer countOtherBills = 0;
		
		for (TravelCost travelCost : travelCosts) {
			addCell(sheet, rownum, rownum, 0, 0, travelCost.getTermName(),defaultCs, true);
			addCell(sheet, rownum, rownum, 1, 1, travelCost.getGroupName(), defaultCs,false);
			if(travelCost.getSimcard() != null) {
			    addCell(sheet, rownum, rownum, 2, 2, travelCost.getSimcard(), defaultCs,false); 
			}else {
			    addCell(sheet, rownum, rownum, 2, 2, "", defaultCs,false);
			}
			
			addCell(sheet, rownum, rownum, 3, 3, DateUtils.dateTimeToStr(travelCost.getStartTravelTime(),"yyyy-MM-dd"), defaultCs,false);
			if(travelCost.getEndTravelTime() != null) {
			    addCell(sheet, rownum, rownum, 4, 4, DateUtils.dateTimeToStr(travelCost.getEndTravelTime(),"yyyy-MM-dd"), defaultCs,false); 
			} else {
			    addCell(sheet, rownum, rownum, 4, 4, "", defaultCs,false); 
			}
			
			addCell(sheet, rownum, rownum, 5, 5, travelCost.getLeavePlace(), defaultCs,false);
			addCell(sheet, rownum, rownum, 6, 6, travelCost.getArrivePlace(), defaultCs,false);
			addCell(sheet, rownum, rownum, 7, 7, travelCost.getStartTravelDesc(), defaultCs,false);
			if(travelCost.getEndTravelDesc() != null) {
			    addCell(sheet, rownum, rownum, 8, 8, travelCost.getEndTravelDesc(), defaultCs,false);
			} else {
			    addCell(sheet, rownum, rownum, 8, 8, "", defaultCs,false);
			}
            if(travelCost.getTask() != null){
                addCell(sheet, rownum, rownum, 9, 9, travelCost.getTask(), defaultCs,false); 
            } else {
                addCell(sheet, rownum, rownum, 9, 9, "", defaultCs,false);
            }
            Integer trafficTools = null;
            if(travelCost.getTraffic() != null) {
                trafficTools = travelCost.getTraffic();
            } else {
                trafficTools = -1;
            }
            String traffic = "";
            if(0 == trafficTools) {
                traffic = "��";
            } else if(2 == trafficTools) {
                traffic = "����";
            } else if(3 == trafficTools) {
                traffic = "�ִ�";
            } else if(4 == trafficTools) {
                traffic = "�ɻ�";
            } else if(5 == trafficTools) {
                traffic = "����";
            }
            addCell(sheet, rownum, rownum, 10, 10, traffic, defaultCs,false);
            Double trafficCost = 0.0;
            Integer trafficBills = 0;
            if(null != travelCost.getTrafficCost()) {
                trafficCost = travelCost.getTrafficCost();
            }
            if(null != travelCost.getTrafficBills()) {
                trafficBills = travelCost.getTrafficBills();
            }
            countTrafficCost = countTrafficCost + Double.valueOf(df.format(trafficCost));
            countTrafficBills = countTrafficBills + trafficBills;
            addCell(sheet, rownum, rownum, 11, 11,df.format(trafficCost) , defaultCs,false);
            addCell(sheet, rownum, rownum, 12, 12,df.format(trafficBills), defaultCs,false);
			Double hotelCost = 0.0;
			Integer hotelBills = 0;
			if(null != travelCost.getHotelCost()) {
			    hotelCost = travelCost.getHotelCost();
            }
            if(null != travelCost.getHotelBills()) {
                hotelBills = travelCost.getHotelBills();
            }
            countHotelCost = countHotelCost + Double.valueOf(df.format(hotelCost));
            countHotelBills = countHotelBills + hotelBills;
            addCell(sheet, rownum, rownum, 13, 13,df.format(hotelCost) , defaultCs,false);
            addCell(sheet, rownum, rownum, 14, 14,df.format(hotelBills), defaultCs,false);
            addCell(sheet, rownum, rownum, 15, 15,travelCost.getSubsidyDay(), defaultCs,false);
            Double subsidyCost = 0.0;
            Integer subsidyBills = 0;
            if(null != travelCost.getSubsidyCost()) {
                subsidyCost = travelCost.getSubsidyCost();
            }
            if(null != travelCost.getSubsidyBills()) {
                subsidyBills = travelCost.getSubsidyBills();
            }
            countSubsidyCost = countSubsidyCost + Double.valueOf(df.format(subsidyCost));
            countSubsidyBills = countSubsidyBills + subsidyBills;
            addCell(sheet, rownum, rownum, 16, 16,df.format(subsidyCost), defaultCs,false);
            addCell(sheet, rownum, rownum, 17, 17,df.format(subsidyBills), defaultCs,false);
            Double cityTrafficCost = 0.0;
            Integer cityTrafficBills = 0;
            if(null != travelCost.getCityTrafficCost()) {
                cityTrafficCost = travelCost.getCityTrafficCost();
            }
            if(null != travelCost.getCityTrafficBills()) {
                cityTrafficBills = travelCost.getCityTrafficBills();
            }
            
            countCityTraBills = countCityTraBills + cityTrafficBills;
            countCityTraCost = countCityTraCost + Double.valueOf(df.format(cityTrafficCost));
            
            addCell(sheet, rownum, rownum, 18, 18,df.format(cityTrafficCost) , defaultCs,false);
            addCell(sheet, rownum, rownum, 19, 19,df.format(cityTrafficBills), defaultCs,false);
            addCell(sheet, rownum, rownum, 20, 20,travelCost.getOtherIterms(), defaultCs,false);
            
            Double otherCost = 0.0;
            Integer otherBills = 0;
            if(null != travelCost.getOtherCost()) {
                otherCost = travelCost.getOtherCost();
            }
            if(null != travelCost.getOtherBills()) {
                otherBills = travelCost.getOtherBills();
            }
            countOtherBills = countOtherBills + otherBills;
            countOtherCost = countOtherCost + Double.valueOf(df.format(otherCost));
            addCell(sheet, rownum, rownum, 21, 21,df.format(otherCost) , defaultCs,false);
            addCell(sheet, rownum, rownum, 22, 22,df.format(otherBills), defaultCs,false);
//            Double countRowCost = trafficCost * trafficBills + hotelCost*hotelBills + subsidyCost*subsidyBills+ 
//                   cityTrafficBills*cityTrafficCost + otherCost * otherBills;
            Double countRowCost = trafficCost + hotelCost+ subsidyCost+ 
                   cityTrafficCost + otherCost;
            addCell(sheet, rownum, rownum, 23, 23,df.format(countRowCost), defaultCs,false);
            //addCell(sheet, rownum, rownum, 24, 24,"", defaultCs,false);
            Integer reviewStates = 0;
            if(travelCost.getReviewStates() != null) {
                reviewStates = travelCost.getReviewStates();
            }
            if(reviewStates == 0) {
                addCell(sheet, rownum, rownum, 24, 24,"δ���", defaultCs,false);
            } else {
                addCell(sheet, rownum, rownum, 24, 24,"�����", defaultCs,false);
            }
			rownum++;
		}
		
		addCell(sheet, rownum, rownum, 0, 9,"�ϼ�", defaultCs,true);
		addCell(sheet, rownum, rownum, 10,10,"", defaultCs,false);
		addCell(sheet, rownum, rownum, 11,11,countTrafficCost, defaultCs,false);
		addCell(sheet, rownum, rownum, 12,12,countTrafficBills, defaultCs,false);
		addCell(sheet, rownum, rownum, 13,13,countHotelCost, defaultCs,false);
        addCell(sheet, rownum, rownum, 14,14,countHotelBills, defaultCs,false);
        addCell(sheet, rownum, rownum, 15,15,"", defaultCs,false);
        addCell(sheet, rownum, rownum, 16,16,countSubsidyCost, defaultCs,false);
        addCell(sheet, rownum, rownum, 17,17,countSubsidyBills, defaultCs,false);
        addCell(sheet, rownum, rownum, 18,18,countCityTraCost, defaultCs,false);
        addCell(sheet, rownum, rownum, 19,19,countCityTraBills, defaultCs,false);
        addCell(sheet, rownum, rownum, 20,20,"", defaultCs,false);
        addCell(sheet, rownum, rownum, 21,21,countOtherCost, defaultCs,false);
        addCell(sheet, rownum, rownum, 22,22,countOtherBills, defaultCs,false);
//        Double coutAllCost = countTrafficCost * countTrafficBills + countHotelBills * countHotelCost + 
//                countSubsidyBills* countSubsidyCost + countCityTraBills* countCityTraCost + countOtherBills*countOtherCost;
        Double coutAllCost = countTrafficCost + countHotelCost + 
                countSubsidyCost + countCityTraCost + countOtherCost;
        addCell(sheet, rownum, rownum, 23,23,df.format(coutAllCost), defaultCs,false);
        addCell(sheet, rownum, rownum, 24,24,"", defaultCs,false);
        
		logger.info("new(������Ϣ��������)Excelend...");
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

	/**
	 * ������ת��Ϊ�ַ�������ǰ̨
	 * @param list
	 * @return
	 */
    public static String convertTravelCostFromObjectToJson(List<TravelCost> list,List<TravelCost> travelCosts) {
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuffer jsonSb = new StringBuffer();
        Double countTrafficCost = 0.0;
        Integer countTrafficBills = 0;
        Double countHotelCost = 0.0;
        Integer countHotelBills = 0;
        Double countSubsidyCost = 0.0;
        Integer countSubsidyBills = 0;
        Double countCityTraCost = 0.0;
        Integer countCityTraBills = 0;
        Double countOtherCost = 0.0;
        Integer countOtherBills = 0;
        
        for (TravelCost travelCost : travelCosts) {
            Double trafficCost = 0.0;
            Integer trafficBills = 0;
            if(null != travelCost.getTrafficCost()) {
                trafficCost = travelCost.getTrafficCost();
            }
            if(null != travelCost.getTrafficBills()) {
                trafficBills = travelCost.getTrafficBills();
            }
            countTrafficCost = countTrafficCost + Double.valueOf(df.format(trafficCost));
            countTrafficBills = countTrafficBills + trafficBills;
            
            Double hotelCost = 0.0;
            Integer hotelBills = 0;
            if(null != travelCost.getHotelCost()) {
                hotelCost = travelCost.getHotelCost();
            }
            if(null != travelCost.getHotelBills()) {
                hotelBills = travelCost.getHotelBills();
            }
            countHotelCost = countHotelCost + Double.valueOf(df.format(hotelCost));
            countHotelBills = countHotelBills + hotelBills;
            
            Double subsidyCost = 0.0;
            Integer subsidyBills = 0;
            if(null != travelCost.getSubsidyCost()) {
                subsidyCost = travelCost.getSubsidyCost();
            }
            if(null != travelCost.getSubsidyBills()) {
                subsidyBills = travelCost.getSubsidyBills();
            }
            countSubsidyCost = countSubsidyCost + Double.valueOf(df.format(subsidyCost));
            countSubsidyBills = countSubsidyBills + subsidyBills;
           
            Double cityTrafficCost = 0.0;
            Integer cityTrafficBills = 0;
            if(null != travelCost.getCityTrafficCost()) {
                cityTrafficCost = travelCost.getCityTrafficCost();
            }
            if(null != travelCost.getCityTrafficBills()) {
                cityTrafficBills = travelCost.getCityTrafficBills();
            }
            
            countCityTraBills = countCityTraBills + cityTrafficBills;
            countCityTraCost = countCityTraCost + Double.valueOf(df.format(cityTrafficCost));
            
         
            Double otherCost = 0.0;
            Integer otherBills = 0;
            if(null != travelCost.getOtherCost()) {
                otherCost = travelCost.getOtherCost();
            }
            if(null != travelCost.getOtherBills()) {
                otherBills = travelCost.getOtherBills();
            }
            countOtherBills = countOtherBills + otherBills;
            countOtherCost = countOtherCost + Double.valueOf(df.format(otherCost));
        }
//        Double coutAllCost = countTrafficCost * countTrafficBills + countHotelBills * countHotelCost + 
//                countSubsidyBills* countSubsidyCost + countCityTraBills* countCityTraCost + countOtherBills*countOtherCost;
        Double coutAllCost = countTrafficCost + countHotelCost + 
                countSubsidyCost + countCityTraCost + countOtherCost;
        
        logger.info("new(������Ϣ��������)Excelend...");
        for (TravelCost travelCost : list) {
            jsonSb.append("{");
            jsonSb.append("id:'" +travelCost.getId() + "',");// id
            if(travelCost.getTermName() != null) {
                jsonSb.append("termName:'" +travelCost.getTermName() + "',");
            } else {
                jsonSb.append("termName:'',");
            }
            if(travelCost.getGroupName() != null) {
                jsonSb.append("groupName:'" +travelCost.getGroupName() + "',");
            } else {
                jsonSb.append("groupName:'',");
            }
            
            if(travelCost.getSimcard() != null) {
                jsonSb.append("simcard:'" +travelCost.getSimcard() + "',");
            } else {
                jsonSb.append("simcard:'',");
            }
            jsonSb.append("startTravelTime:'" +DateUtils.dateTimeToStr(travelCost.getStartTravelTime(),"yyyy-MM-dd") + "',");
            if(travelCost.getEndTravelTime() != null) {
                jsonSb.append("endTravelTime:'" +DateUtils.dateTimeToStr(travelCost.getEndTravelTime(),"yyyy-MM-dd")+ "',"); 
            } else {
                jsonSb.append("endTravelTime:'',"); 
            }
            
            jsonSb.append("leavePlace:'" +travelCost.getLeavePlace() + "',");
            jsonSb.append("arrivePlace:'" +travelCost.getArrivePlace() + "',");
            jsonSb.append("startTravelDesc:'" +travelCost.getStartTravelDesc() + "',");
            if(travelCost.getEndTravelDesc() != null) {
                jsonSb.append("endTravelDesc:'" +travelCost.getEndTravelDesc() + "',");
            } else {
                jsonSb.append("endTravelDesc:'',");
            }
            
            if(travelCost.getTask() == null) {
                jsonSb.append("task:'',");
            } else {
                jsonSb.append("task:'" +travelCost.getTask() + "',");
            }
           
            String traffic = "";
            if(travelCost.getTraffic() != null) {
                if(0 == travelCost.getTraffic()) {
                    traffic = "��";
                } else if (1 == travelCost.getTraffic()){
                    traffic = "����";
                } else if (2 == travelCost.getTraffic()){
                    traffic = "�ִ�";
                } else if (3 == travelCost.getTraffic()){
                    traffic = "�ɻ�";
                } else if (4 == travelCost.getTraffic()){
                    traffic = "����";
                } 
            }
            
            jsonSb.append("traffic:'" +traffic + "',");
            Double trafficCost = 0.0;
            Integer trafficBills = 0;
            if(travelCost.getTrafficCost() != null) {
                trafficCost = travelCost.getTrafficCost(); 
            } 
            if(travelCost.getTrafficBills() != null) {
                trafficBills = travelCost.getTrafficBills(); 
            }
            jsonSb.append("trafficCost:'" +df.format(trafficCost) + "',");
            jsonSb.append("trafficBills:'" +trafficBills+ "',");
            Double hotelCost = 0.0;
            Integer hotelBills = 0;
            if(travelCost.getHotelCost() != null) {
                hotelCost = travelCost.getHotelCost();
            }
            if(travelCost.getHotelBills() != null) {
                hotelBills = travelCost.getHotelBills();
            }
            jsonSb.append("hotelCost :'" +df.format(hotelCost) + "',");
            jsonSb.append("hotelBills:'" +hotelBills + "',");
            
            Double subsidyCost = 0.0;
            Integer subsidyBills = 0;
            if(travelCost.getSubsidyCost() != null) {
                subsidyCost = travelCost.getSubsidyCost();
            }
            if(travelCost.getSubsidyBills() != null) {
                subsidyBills = travelCost.getSubsidyBills(); 
            }
            if(travelCost.getSubsidyDay() == null) {
                jsonSb.append("subsidyDay:'0',"); 
            } else {
                jsonSb.append("subsidyDay:'"+travelCost.getSubsidyDay()+"',"); 
            }
            jsonSb.append("subsidyCost :'" +df.format(subsidyCost)+ "',");
            jsonSb.append("subsidyBills:'" +subsidyBills + "',");
            
            Double cityTrafficCost = 0.0;
            Integer cityTrafficBills = 0;
            if(travelCost.getCityTrafficCost() != null) {
                cityTrafficCost = travelCost.getCityTrafficCost();  
            }
            if(travelCost.getCityTrafficBills() != null) {
                cityTrafficBills = travelCost.getCityTrafficBills();
            }
            jsonSb.append("cityTrafficCost :'" +df.format(cityTrafficCost)+ "',");
            jsonSb.append("cityTrafficBills:'" +cityTrafficBills + "',");
            
            Double otherCost = 0.0;
            Integer otherBills = 0;
            if(travelCost.getOtherCost() != null) {
                otherCost = travelCost.getOtherCost();  
            }
            if(travelCost.getOtherBills() != null) {
                otherBills = travelCost.getOtherBills();
            }
            if(travelCost.getOtherIterms() == null) {
                jsonSb.append("otherIterms :'',");
            }else {
                jsonSb.append("otherIterms :'" +travelCost.getOtherIterms()+ "',");  
            }
            
            jsonSb.append("otherCost :'" +df.format(otherCost)+ "',");
            jsonSb.append("otherBills:'" +otherBills + "',");
            
            Double countRowCost = 0.0;
//            countRowCost = otherBills*otherCost + cityTrafficBills*cityTrafficCost + subsidyBills*subsidyCost +
//                    hotelBills*hotelCost + trafficBills*trafficCost;
            countRowCost = Double.valueOf(df.format(otherCost)) + Double.valueOf(df.format(cityTrafficCost)) 
                    + Double.valueOf(df.format(subsidyCost)) +
                   Double.valueOf(df.format(hotelCost)) + Double.valueOf(df.format(trafficCost));
            
            jsonSb.append("countRowCost:'" +df.format(countRowCost) + "',");   
            Integer reviewStates = travelCost.getReviewStates();
            String reviwStatesJson = "";
            if(reviewStates == null || reviewStates == 0) {
                reviwStatesJson = "δ���";
            } else {
                reviwStatesJson = "�����";
            }
            jsonSb.append("reviewStates:'" +reviwStatesJson+ "',");  
            jsonSb.append("total:'"+df.format(coutAllCost)+"',");
            jsonSb.append("totalTraCost:'"+df.format(countTrafficCost)+"',");
            jsonSb.append("totalTraBills:'"+countTrafficBills+"',");
            jsonSb.append("totalHotelCost:'"+df.format(countHotelCost)+"',");
            jsonSb.append("totalHotelBills:'"+countHotelBills+"',");
            jsonSb.append("totalSubsidyCost:'"+df.format(countSubsidyCost)+"',");
            jsonSb.append("totalSubsidyBills:'"+countSubsidyBills+"',");
            jsonSb.append("totalCityTraCost:'"+df.format(countCityTraCost)+"',");
            jsonSb.append("totalCityTraBills:'"+countCityTraBills+"',");
            jsonSb.append("totalOtherCost:'"+df.format(countOtherCost)+"',");
            jsonSb.append("totalOtherBills:'"+countOtherBills+"'");
            jsonSb.append("},");

        }
        if (jsonSb.length() > 0) {
            jsonSb.deleteCharAt(jsonSb.length() - 1);
        }
        return jsonSb.toString();
    }
    
    /**
     * ������ܵĲ�����Ϣ�ϼ�
     * @param travelCosts
     * @return
     */
    public static String convertTravelCostFromObjectToTotalJson(List<TravelCost> travelCosts) {
        StringBuffer json = new StringBuffer();
        Double countTrafficCost = 0.0;
        Integer countTrafficBills = 0;
        Double countHotelCost = 0.0;
        Integer countHotelBills = 0;
        Double countSubsidyCost = 0.0;
        Integer countSubsidyBills = 0;
        Double countCityTraCost = 0.0;
        Integer countCityTraBills = 0;
        Double countOtherCost = 0.0;
        Integer countOtherBills = 0;
        
        for (TravelCost travelCost : travelCosts) {
            Double trafficCost = 0.0;
            Integer trafficBills = 0;
            if(null != travelCost.getTrafficCost()) {
                trafficCost = travelCost.getTrafficCost();
            }
            if(null != travelCost.getTrafficBills()) {
                trafficBills = travelCost.getTrafficBills();
            }
            countTrafficCost = countTrafficCost + trafficCost;
            countTrafficBills = countTrafficBills + trafficBills;
            
            Double hotelCost = 0.0;
            Integer hotelBills = 0;
            if(null != travelCost.getHotelCost()) {
                hotelCost = travelCost.getHotelCost();
            }
            if(null != travelCost.getHotelBills()) {
                hotelBills = travelCost.getHotelBills();
            }
            countHotelCost = countHotelCost + hotelCost;
            countHotelBills = countHotelBills + hotelBills;
            
            Double subsidyCost = 0.0;
            Integer subsidyBills = 0;
            if(null != travelCost.getSubsidyCost()) {
                subsidyCost = travelCost.getSubsidyCost();
            }
            if(null != travelCost.getSubsidyBills()) {
                subsidyBills = travelCost.getSubsidyBills();
            }
            countSubsidyCost = countSubsidyCost + subsidyCost;
            countSubsidyBills = countSubsidyBills + subsidyBills;
           
            Double cityTrafficCost = 0.0;
            Integer cityTrafficBills = 0;
            if(null != travelCost.getCityTrafficCost()) {
                cityTrafficCost = travelCost.getCityTrafficCost();
            }
            if(null != travelCost.getCityTrafficBills()) {
                cityTrafficBills = travelCost.getCityTrafficBills();
            }
            
            countCityTraBills = countCityTraBills + cityTrafficBills;
            countCityTraCost = countCityTraCost + cityTrafficCost;
            
         
            Double otherCost = 0.0;
            Integer otherBills = 0;
            if(null != travelCost.getOtherCost()) {
                otherCost = travelCost.getOtherCost();
            }
            if(null != travelCost.getOtherBills()) {
                otherBills = travelCost.getOtherBills();
            }
            countOtherBills = countOtherBills + otherBills;
            countOtherCost = countOtherCost + otherCost;
        }
        Double coutAllCost = countTrafficCost * countTrafficBills + countHotelBills * countHotelCost + 
                countSubsidyBills* countSubsidyCost + countCityTraBills* countCityTraCost + countOtherBills*countOtherCost;
        
        json.append("{");
        json.append("total:'"+coutAllCost+"',");
        json.append("totalTraCost:'"+countTrafficCost+"',");
        json.append("totalTraBills:'"+countTrafficBills+"',");
        json.append("totalHotelCost:'"+countHotelCost+"',");
        json.append("totalHotelBills:'"+countHotelBills+"',");
        json.append("totalSubsidyCost:'"+countSubsidyCost+"',");
        json.append("totalSubsidyBills:'"+countSubsidyBills+"',");
        json.append("totalTraCost:'"+countCityTraCost+"',");
        json.append("totalTraBills:'"+countCityTraBills+"',");
        json.append("totalOtherCost:'"+countOtherCost+"',");
        json.append("totalOtherBills:'"+countOtherBills+"'");
        json.append("}");
        logger.info("new(������Ϣ��������)Excelend...");
        return json.toString();
    }
}
