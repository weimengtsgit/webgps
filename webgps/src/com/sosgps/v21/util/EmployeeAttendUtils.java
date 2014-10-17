package com.sosgps.v21.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.sos.helper.SpringHelper;
import org.springframework.beans.BeanUtils;

import cn.net.sosgps.memcache.Memcache;
import cn.net.sosgps.memcache.bean.Terminal;

import com.sosgps.v21.employeeAttend.service.EmployeeAttendService;
import com.sosgps.v21.model.EmployeeAttend;
import com.sosgps.v21.model.TravelCost;
import com.sosgps.v21.travelcost.service.TravelCostService;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.orm.RefTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.terminal.service.TerminalService;
import com.sosgps.wzt.util.DateUtility;

/**
 * ����ת��������(������Ϣ����)
 * 
 * @author wangzhen
 * 
 */
public class EmployeeAttendUtils {

	private static final Logger logger = Logger.getLogger(EmployeeAttendUtils.class);

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
	 private static CellStyle getTipCellStyle(Workbook wb) {
	        CellStyle cs = wb.createCellStyle();
	        Font font = wb.createFont();
	        // ���������СΪ24
	        font.setFontHeightInPoints((short) 9);
	        // ����������ʽΪ��������
	        font.setFontName("����");
	        // �Ӵ�
	        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	        // ��ɫ
	        font.setColor(Font.COLOR_RED);
	        // б��
	        // font.setItalic(true);
	        // ���ɾ����
	        // font.setStrikeout(true);
	        // ��������ӵ���ʽ��
	        cs.setFont(font);
	        return cs;
	    }

	/**
	 * ����ĳ����һ���ж�����
	 * @param year
	 * @param month
	 * @return
	 */
	
    public static int days(int year,int month) {
        int days = 0;
        if(month!=2) {
           switch(month){
               case 1:
               case 3:
               case 5:
               case 7:
               case 8:
               case 10:
               case 12:days = 31 ;break;
               case 4:
               case 6:
               case 9:
               case 11:days = 30;
           }
      }else {
       if(year%4==0 && year%100!=0 || year%400==0) {
           days = 29;
       }else {
           days = 28;
       }
      }
      return days;
     }
    /**
     * ����ĳ�����ڼ�
     * @param y ��
     * @param m ��
     * @param d ��
     * @return 
     */
    public static String week(Integer y,Integer m,Integer d)
    {
         if(m==1) {m=13;y--;}
         if(m==2) {m=14;y--;}
         int week=(d+2*m+3*(m+1)/5+y+y/4-y/100+y/400)%7; 
         String weekstr="";
         switch(week)
         {
             case 0: weekstr="һ"; break;
             case 1: weekstr="��"; break;
             case 2: weekstr="��"; break;
             case 3: weekstr="��"; break;
             case 4: weekstr="��"; break;
             case 5: weekstr="��"; break;
             case 6: weekstr="��"; break;
          }
          return weekstr;  
     }
    /**
     * ������ת��Ϊ���
     * @param employeeAttends
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Workbook convertEmployeeAttendFromObjectToExcel(
            List<EmployeeAttend> employeeAttends,String yearStr,String monthStr, Class<? extends Workbook> clazz) throws InstantiationException,
            IllegalAccessException{
        Integer year = Integer.valueOf(yearStr);
        Integer month = Integer.valueOf(monthStr);
        
        Workbook wb = clazz.newInstance();
        CellStyle defaultCs = getDefaultCellStyle(wb);
        CellStyle headerCs = getHeaderCellStyle(wb);
        CellStyle tipCs = getTipCellStyle(wb);
        Sheet sheet = wb.createSheet();
        logger.info("new(��Ա���ڻ���)Excelstart...");
        /** ��װͷ��Ϣ * */
        //addCell(sheet, 0, 0, 0, 0, "", headerCs, true);
        //addCell(sheet, 1, 1, 0, 0, "", headerCs, true);
        int lastRowNum = 1;
        //ͷ����Ϣ
        addCell(sheet, 0, lastRowNum, 0, 0, "Ա������", headerCs, true);
        addCell(sheet, 0, lastRowNum, 1, 1, "����", headerCs, false);
        addCell(sheet, 0, lastRowNum, 2, 2, "�ֻ�����", headerCs, false); 
        
        //����ͷ��
        int colNum = 3;
        int j = 1;
        //1��31����
        for(int i = 3;i < colNum + 31;i++) {
            addCell(sheet, 0, lastRowNum-1, i, i, j + "", headerCs, false);
            j++;
        }
        //�����һ��ĳ�����ж�����
        Integer days = days(year,month);
        j = 1;
        for(int i= 3;i < colNum + 31;i ++) {
            String week = week(year,month,j);
            if(j > days) {
                addCell(sheet, lastRowNum, lastRowNum, i, i, "", headerCs, false);
            } else {
                addCell(sheet, lastRowNum, lastRowNum, i, i, week, headerCs, false);
            }
            j++;
        }
        colNum = 3 + 31;
        addCell(sheet, 0,lastRowNum,colNum, colNum, "��������", headerCs, false);
        addCell(sheet, 0,lastRowNum,colNum+1, colNum+1, "ȱ������", headerCs, false);
        addCell(sheet, 0,lastRowNum,colNum+2, colNum+2, "�Ѹ�����", headerCs, false);
        addCell(sheet, 0,lastRowNum,colNum+3, colNum+3, "�ٵ�����", headerCs, false);
        addCell(sheet, 0,lastRowNum,colNum+4, colNum+4, "�������", headerCs, false);
        int rownum = lastRowNum + 1;
        
        //��װ����
        for(EmployeeAttend empAtte : employeeAttends) {
            int colnum = 0;
            addCell(sheet, rownum, rownum, colnum, colnum, empAtte.getTermName(),defaultCs, true);
            addCell(sheet, rownum, rownum, colnum+1, colnum+1, empAtte.getGroupName(), defaultCs,false);
            addCell(sheet, rownum, rownum, colnum+2, colnum+2, empAtte.getSimcard(), defaultCs,false);
            Map<Integer,Integer> empAtteMap = empAtte.getAttendStateMap();
            
            j = 1;
            for(int i= 3;i < (colnum + 3) + 31;i++) {
                Integer attendStates = null;
                if(j < 10) {
                    attendStates = Integer.valueOf(year + "" + month + "0" + j);  //20120809
                } else {
                    attendStates = Integer.valueOf(year + "" + month + "" + j);  
                }
                String display = "";
                if(empAtteMap.containsKey(attendStates)) {
                   Integer sates =  empAtteMap.get(attendStates);
                   if(0 == sates) {
                         display ="��";
                     } else if(1 == sates){
                         display ="��";
                     } else if(2 == sates) {
                         display ="x";
                     } else if(3 == sates) {
                         display ="��";
                     } else if(4 == sates) {
                         display ="��";
                     } else if(5 == sates) {
                         display ="-";
                     } 
                }
                addCell(sheet, rownum, rownum, colnum+3+j-1, colnum+3+j-1, display, defaultCs,false);
                j++;
            }
            addCell(sheet, rownum, rownum, (colnum + 3) + 31, (colnum + 3) + 31, empAtte.getAttendDays()+"", defaultCs,false);
            addCell(sheet, rownum, rownum, (colnum + 3) + 32, (colnum + 3) + 32, empAtte.getNoAttendDays()+"", defaultCs,false);
            addCell(sheet, rownum, rownum, (colnum + 3) + 33, (colnum + 3) + 33, ""+empAtte.getNoWorkDays(), defaultCs,false);
            addCell(sheet, rownum, rownum, (colnum + 3) + 34, (colnum + 3) + 34, ""+empAtte.getLateOutDays(), defaultCs,false);
            addCell(sheet, rownum, rownum, (colnum + 3) + 35, (colnum + 3) + 35, ""+empAtte.getVacateDays(), defaultCs,false);
            rownum++;
        }
        logger.info("new(������Ϣ��������)Excelend...");
        return wb;
    }

    /**
     * �����Ա������Ϣ��
     * ȱ������
     * �Ѹ�����...
     * @param employeeAttends
     * @param attendanceDate
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<EmployeeAttend> assemEmployeeAttendInfo(
            List<EmployeeAttend> employeeAttends, String year,String month,String entCode) {
        EmployeeAttendService empAtteService = (EmployeeAttendService) SpringHelper.getBean("employeeAttendService");
        TravelCostService travelCostService = (TravelCostService) SpringHelper.getBean("travelCostService");
        List<EmployeeAttend> empAtteInfos = new ArrayList<EmployeeAttend>();
        
        //���˲������������
        Set<String> deviceIdSet = new HashSet<String>();
        Map<String,List<EmployeeAttend>> empAtteMap = new HashMap<String,List<EmployeeAttend>>();
        List<EmployeeAttend> empAtteList = new ArrayList<EmployeeAttend>();
        for(EmployeeAttend empAtt : employeeAttends) {
            String deviceId = empAtt.getDeviceId();
            boolean isAdd = deviceIdSet.add(deviceId);
            if(isAdd) {
                for(EmployeeAttend empAtt2 : employeeAttends) {
                    if(empAtt2.getDeviceId().equals(deviceId)) {
                        empAtteList.add(empAtt2);
                    }
                }
                empAtteMap.put(deviceId, empAtteList);
            }else {
                continue;
            }
        }
        
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
      //��ȡ��ǰ������
        @SuppressWarnings("deprecation")
        Date currentDate = new Date();
        Integer currentDateInt = Integer.valueOf(formatter.format(currentDate));
        Set<String> key = empAtteMap.keySet();
        for (Iterator it = key.iterator(); it.hasNext();) {
            String deviceId = (String) it.next();
            //����������
            Map<Integer,Integer> dayAttend = new HashMap<Integer,Integer>();
            for(int i=1;i<=31;i++) {
                Integer attendStates = null;
                if(i < 10 ) {
                    attendStates = Integer.valueOf(year+month+"0"+i);
                }else {
                    attendStates = Integer.valueOf(year+month+i);
                }
                dayAttend.put(attendStates, -1);
            }
            int attendanceDays = 0;//������������
            int noattendanceDays = 0;//ȱ������
            int noWorkPlaceDays = 0;//�Ѹ�����
            int lateOutDays = 0;//�ٵ���������;
            Double vacateDays = 0.0;//�������
            EmployeeAttend empAttInfo = new EmployeeAttend();
            for(EmployeeAttend empAtte : empAtteMap.get(deviceId)) { 
                empAttInfo.setId(empAtte.getId());
                empAttInfo.setGroupName(empAtte.getGroupName());
                empAttInfo.setTermName(empAtte.getTermName());
                empAttInfo.setSimcard(empAtte.getSimcard());
                if(dayAttend.containsKey(empAtte.getAttendanceDate())) {
                    dayAttend.put(empAtte.getAttendanceDate(),empAtte.getAttendStates());
                    int states = empAtte.getAttendStates();
                    //0���������ڣ�1 �Ѹ� 2 ȱ�ڣ�3 ���4��٣�5 �ٵ�����
                    if(states == 0) {
                        attendanceDays++;
                    } else if(states == 1){
                        noWorkPlaceDays++;
                    } else if(states == 2) {
                        noattendanceDays++;
                    } else if(states == 4) {
                        vacateDays = vacateDays + empAtte.getVacateDay();
                    } else if(states == 5) {
                        lateOutDays++;
                    }
                }
            }//end for for(EmployeeAttend empAtte : empAtteMap.get(s))
            Set<Integer> timeKey = dayAttend.keySet();
            for (Iterator it2 = timeKey.iterator(); it2.hasNext();) {
                Integer attendanceDate = (Integer) it2.next(); //20120405
                String day = String.valueOf(attendanceDate);
                day = day.substring(day.length()-2, day.length());
                String week = week(Integer.valueOf(year),Integer.valueOf(month), Integer.valueOf(day));
                if(-1 == dayAttend.get(attendanceDate)) {
                    if(attendanceDate > currentDateInt) {
                        continue;
                    }else if("��".equals(week) || "��".equals(week)){
                        continue;
                    }else {
                        TravelCost travelCost = travelCostService.getTravelCost(deviceId, entCode, 0);
                        if(travelCost == null) {
                            /*
                             * �ж���û�����
                             * �У��������
                             * �ޣ�ȱ��
                             */
                            //4Ϊ�������
                            boolean isVacate = empAtteService.judgeVacate(deviceId,entCode,attendanceDate,4);
                            if(isVacate) {
                                //���
                                dayAttend.put(attendanceDate,4);
                            }else {
                                //ȱ��
                                dayAttend.put(attendanceDate,2);
                                noattendanceDays++;
                            }
                        } else {
                            //����
                            dayAttend.put(attendanceDate,3);
                        }
                    }
                }
            }//end of for (Iterator it2 = timeKey.iterator(); it2.hasNext();)
        
            empAttInfo.setAttendDays(attendanceDays);
            empAttInfo.setNoWorkDays(noWorkPlaceDays);
            empAttInfo.setLateOutDays(lateOutDays);
            empAttInfo.setNoAttendDays(noattendanceDays);
            empAttInfo.setAttendStateMap(dayAttend);
            empAtteInfos.add(empAttInfo);
     }//end of for (Iterator it = key.iterator(); it.hasNext();)
     return empAtteInfos;
  }//end public 
    
    /**
     * ������װ��ΪJSON����
     * @param empAtteInfo
     * @return
     */
    public static String convertEmployeeAttendFromObjectToJson(List<EmployeeAttend> empAtteInfo,String year,String month) {
        StringBuffer jsonSb = new StringBuffer();
        
        for (EmployeeAttend emp : empAtteInfo) {
            jsonSb.append("{");
            jsonSb.append("id:'" +emp.getId() + "',");// id
            jsonSb.append("termName:'" +emp.getTermName() + "',");
            jsonSb.append("groupName:'" +emp.getGroupName() + "',");
            jsonSb.append("simcard:'" +emp.getSimcard() + "',");
            //Map<Integer,Integer> empMap = emp.getAttendStateMap();
			Integer mountCount = DateUtils.getMonthCountByStr(year+""+month, "yyyyMM");
			int attendDaysi = 0;
			int noattendDaysi = 0;
			int noWorkDaysi = 0;
			int lateOutDaysi = 0;
			int vacateDaysi = 0;
			
            for(int i = 1;i <= mountCount;i++) {
                String attendDate = year+month;
                if(i < 10) {
                    attendDate = attendDate+"0" + i;
                }else {
                    attendDate = attendDate+ ""+i;
                }
                int states = 2;
                if((emp.getAttendanceDate()+"").equals(attendDate)){
                	states = emp.getAttendStates();
                }
                Date arriveTimeDate = DateUtility.strToDate(attendDate, "yyyyMMdd");
		        Date dd = new Date();
		    	if(arriveTimeDate.after(dd)){
		    		states = -1;
		    	}
		    	
                if(states == 2){
                	noattendDaysi++;
                }else if(states == 0){
                	attendDaysi++;
                }else if(states == 1){
                	noWorkDaysi++;
                }else if(states == 4){
                	vacateDaysi++;
                }else if(states == 5){
                	lateOutDaysi++;
                }
                /*Integer states = null;
                if(empMap.containsKey(Integer.valueOf(attendDate))) {
                    states = empMap.get(Integer.valueOf(attendDate));
//                    if(0 == states) {
//                        display = "��";
//                    } else if(1 == states){
//                        display = "��";
//                    } else if(2 == states) {
//                        display = "��";
//                    } else if(3 == states) {
//                        display = "��";
//                    } else if(4 == states) {
//                        display = "��";
//                    } else if(5 == states) {
//                        display = "-";
//                    } else if(-1 == states) {
//                        display = "";
//                    }
                }*/
                jsonSb.append("day"+i+":'" +states+ "',");
            }
            jsonSb.append("attendDays:'"+attendDaysi+"',");
            jsonSb.append("noattendDays:'"+noattendDaysi+"',");
            jsonSb.append("noWorkDays:'"+noWorkDaysi+"',");
            jsonSb.append("lateOutDays:'"+lateOutDaysi+"',");
            jsonSb.append("vacateDays:'"+vacateDaysi+"'");
            jsonSb.append("},");

        }
        if (jsonSb.length() > 0) {
            jsonSb.deleteCharAt(jsonSb.length() - 1);
        }
        return jsonSb.toString();
    }

    /**
     * ת������
     * @param attendanceDate
     * @param string
     * @return
     * @throws ParseException
     */
    public static String convertDate(String attendanceDate, String string) throws ParseException {
        String utcDate = "";
        if("start".equals(string)) {
             utcDate = attendanceDate+ " 00:00:00";
        } else if("end".equals(string)) {
            utcDate = attendanceDate+ " 23:59:59";
        } 
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date d = form.parse(utcDate);
        return String.valueOf(d.getTime());
    }
    /**
     * ת��Ϊ��˾����ָ�굼��
     * @param list
     * @param class1
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public static Workbook convertCompanyAttendFromObjectToExcel(List<Object[]> list,
            Class<? extends Workbook> clazz,String yearStr,String monthStr, int dCount) throws InstantiationException, IllegalAccessException {
        Integer year = Integer.valueOf(yearStr);
        Integer month = Integer.valueOf(monthStr);
        
        Workbook wb = clazz.newInstance();
        CellStyle defaultCs = getDefaultCellStyle(wb);
        CellStyle headerCs = getHeaderCellStyle(wb);
        Sheet sheet = wb.createSheet();
        logger.info("new(��Ա���ڻ���)Excelstart...");
        /** ��װͷ��Ϣ * */
        int lastRowNum = 1;
        addCell(sheet, 0, lastRowNum, 0, 0, "", headerCs, true);
        
        int colNum = 1;
        int j = 1;
        //1��31����
        for(int i = 1;i < colNum + 31;i++) {
            addCell(sheet, 0, lastRowNum-1, i, i, j + "", headerCs, false);
            j++;
        }
        //�����һ��ĳ�����ж�����
        Integer days = days(year,month);
        j = 1;
        for(int i= 1;i < colNum + 31;i ++) {
            String week = week(year,month,j);
            if(j > days) {
                addCell(sheet, lastRowNum, lastRowNum, i, i, "", headerCs, true);
            } else {
                addCell(sheet, lastRowNum, lastRowNum, i, i, week, headerCs, true);
            }
            j++;
        }
        addCell(sheet, 0, 2, 0, 0, "��������", headerCs, true);
        addCell(sheet, 0, 3, 0, 0, "ȱ������", headerCs, true);
        addCell(sheet, 0, 4, 0, 0, "�Ѹ�����", headerCs, true);
        addCell(sheet, 0, 5, 0, 0, "�ٵ���������", headerCs, true);
        addCell(sheet, 0, 6, 0, 0, "�������", headerCs, true);
        //addCell(sheet, 0, 8, 0, 0, "��������", headerCs, true);
        int colNo = 1;
        for(Object[] obj : list) {
      	  int attend0 = (BigDecimal)obj[0] == null ? 0 : ((BigDecimal)obj[0]).intValue();
      	  int attend1 = (BigDecimal)obj[1] == null ? 0 : ((BigDecimal)obj[1]).intValue();
      	  int attend3 = (BigDecimal)obj[2] == null ? 0 : ((BigDecimal)obj[2]).intValue();
      	  int attend4 = (BigDecimal)obj[3] == null ? 0 : ((BigDecimal)obj[3]).intValue();
      	  int attend5 = (BigDecimal)obj[4] == null ? 0 : ((BigDecimal)obj[4]).intValue();
            int rowNo = 2; 
            //��������
            addCell(sheet, rowNo, rowNo, colNo, colNo, attend0+"", defaultCs, true);
            //ȱ������
            addCell(sheet, rowNo+1, rowNo+1, colNo, colNo, (dCount - attend0 - attend1 - attend3 - attend4 - attend5), defaultCs, true);
            //�Ѹ�����
            addCell(sheet, rowNo+1, rowNo+1, colNo, colNo, attend1+"", defaultCs, true);
            //�ٵ���������
            addCell(sheet, rowNo+1, rowNo+1, colNo, colNo, attend5+"", defaultCs, true);
            //�������
            addCell(sheet, rowNo+1, rowNo+1, colNo, colNo, attend4+"", defaultCs, true);
            //��������
            //addCell(sheet, rowNo+1, rowNo+1, colNo, colNo, attend3+"", defaultCs, true);
            colNo++;
        }
        return wb;
    }
    
    /**
     * ����˾������ת��ΪJSON����
     * @param list
     * @return
     */
//    public static String convertCompanyAttendFromObjectToJSON(List<Object[]> list) {
//        StringBuffer jsonSb = new StringBuffer();
//        int count = 1;
//      for (Object[] obj : list) {
//          jsonSb.append("{");
//          //��������
//          jsonSb.append("attendMans" + count+":'" + obj[0] + "',");
//          //ȱ������
//          jsonSb.append("noattendMans" + count+":'" + 0 + "',");
//          //�Ѹ�����
//          jsonSb.append("noworkMans" + count+":'" + obj[1] + "',");
//          //�ٵ���������
//          jsonSb.append("lateOutMans" + count+":'" + obj[5] + "',");
//          //�������
//          jsonSb.append("vacateMans" + count+":'" + obj[4] + "'");
//          jsonSb.append("},");
//          count++;
//      }
//      if (jsonSb.length() > 0) {
//          jsonSb.deleteCharAt(jsonSb.length() - 1);
//      }
//      return jsonSb.toString();
//    }
    
    /**
     * ����˾������ת��ΪJSON����
     * @param list
     * @return
     */
    public static String convertCompanyAttendFromObjectToJSON(List<Object[]> list, int dCount) {
        StringBuffer jsonSbAtte = new StringBuffer();
        StringBuffer jsonSbNoAtte = new StringBuffer();
        StringBuffer jsonSbNowork = new StringBuffer();
        StringBuffer jsonSblateOut = new StringBuffer();
        StringBuffer jsonSbvac = new StringBuffer();
        StringBuffer jsonSbcc = new StringBuffer();
        int count = 1;
        jsonSbAtte.append("{attendMans0:'��������',");
        jsonSbNoAtte.append("{attendMans0:'ȱ������',");
        jsonSbNowork.append("{attendMans0:'�Ѹ�����',");
        jsonSblateOut.append("{attendMans0:'�ٵ���������',");
        jsonSbvac.append("{attendMans0:'�������',");
        jsonSbcc.append("{attendMans0:'��������',");
        
        /*jsonSbAtte.append("{");
        jsonSbNoAtte.append("{");
        jsonSbNowork.append("{");
        jsonSblateOut.append("{");
        jsonSbvac.append("{");
        jsonSbcc.append("{");*/
      for (Object[] obj : list) {
    	  int attend0 = (BigDecimal)obj[0] == null ? 0 : ((BigDecimal)obj[0]).intValue();
    	  int attend1 = (BigDecimal)obj[1] == null ? 0 : ((BigDecimal)obj[1]).intValue();
    	  int attend3 = (BigDecimal)obj[2] == null ? 0 : ((BigDecimal)obj[2]).intValue();
    	  int attend4 = (BigDecimal)obj[3] == null ? 0 : ((BigDecimal)obj[3]).intValue();
    	  int attend5 = (BigDecimal)obj[4] == null ? 0 : ((BigDecimal)obj[4]).intValue();
    	  int attend2 = (dCount - attend0 - attend1 - attend3 - attend4 - attend5);
    	  String arriveTime = (String)obj[5];
    	  Date arriveTimeDate = DateUtility.strToDate(arriveTime, "yyyyMMdd");
    	  Date d = new Date();
    	  if(arriveTimeDate.after(d)){
    		  attend2 = 0;
    	  }
          //��������
          jsonSbAtte.append("attendMans" + count+":'" + attend0 + "',");
          //ȱ������
          jsonSbNoAtte.append("attendMans" + count+":'" + attend2 + "',");
           //�Ѹ�����
          jsonSbNowork.append("attendMans" + count+":'" + attend1 + "',");
          //�ٵ���������
          jsonSblateOut.append("attendMans" + count+":'" + attend5 + "',");
          //�������
          jsonSbvac.append("attendMans" + count+":'" + attend4 + "',");
          //��������
          jsonSbcc.append("attendMans" + count+":'" + attend3 + "',");
          count++;
      }
      jsonSbAtte.append("},");
      jsonSbNoAtte.append("},");
      jsonSbNowork.append("},");
      jsonSblateOut.append("},");
      jsonSbvac.append("},");
      jsonSbcc.append("}");
      
      if (jsonSbAtte.length() > 0) {
          jsonSbAtte.deleteCharAt(jsonSbAtte.length()-3);
      }
      if (jsonSbNoAtte.length() > 0) {
          jsonSbNoAtte.deleteCharAt(jsonSbNoAtte.length()-3);
      }
      if (jsonSbNowork.length() > 0) {
          jsonSbNowork.deleteCharAt(jsonSbNowork.length()-3);
      }
      if (jsonSblateOut.length() > 0) {
          jsonSblateOut.deleteCharAt(jsonSblateOut.length()-3);
      }
      if (jsonSbvac.length() > 0) {
          jsonSbvac.deleteCharAt(jsonSbvac.length()-3);
      }
      if (jsonSbcc.length() > 0) {
    	  jsonSbcc.deleteCharAt(jsonSbcc.length()-2);
      }
      
      String json = jsonSbAtte.toString() + jsonSbNoAtte.toString() + jsonSbNowork.toString()
              + jsonSblateOut.toString() + jsonSbvac.toString() + jsonSbcc.toString();
      return json;
    }
    

    public static HashMap<String, ArrayList<Integer>> assemEmployeeAttendInfo1(
            List<EmployeeAttend> employeeAttends, String year,String month,String entCode) {
        HashMap<String, ArrayList<Integer>> hm = new HashMap<String, ArrayList<Integer>>();
        for (EmployeeAttend emp : employeeAttends) {
            try {
                String deviceId = emp.getDeviceId();
                ArrayList<Integer> listOld = (ArrayList<Integer>)hm.get(deviceId);
                Integer mountCount = DateUtils.getMonthCountByStr(year+""+month, "yyyyMM");
                if(listOld != null && listOld.size() > 0){
                    for(int i = 1;i <= mountCount;i++) {
                        String attendDate = year+month;
                        if(i < 10) {
                            attendDate = attendDate+"0" + i;
                        }else {
                            attendDate = attendDate+ ""+i;
                        }
                        int states = 2;
                        if((emp.getAttendanceDate()+"").equals(attendDate)){
                            states = emp.getAttendStates();
                        }
                        Date arriveTimeDate = DateUtility.strToDate(attendDate, "yyyyMMdd");
                        Date dd = new Date();
                        if(arriveTimeDate.after(dd)){
                            states = -1;
                        }
                        int statesOld = listOld.get(i-1);
                        if(statesOld == 2 || statesOld == -1){
                            //listOld.remove(i);
                            listOld.set(i-1, states);
                        }
                    }
                    hm.put(deviceId, listOld);
                }else{
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for(int i = 1;i <= mountCount;i++) {
                        String attendDate = year+month;
                        if(i < 10) {
                            attendDate = attendDate+"0" + i;
                        }else {
                            attendDate = attendDate+ ""+i;
                        }
                        //ȱ��
                        int states = 2;
                        if((emp.getAttendanceDate()+"").equals(attendDate)){
                            states = emp.getAttendStates();
                        }
                        //���ù����յ�״̬���磺�����ղ��ϰ࣬����״̬Ϊ-1����ʾ
                        Date arriveTimeDate = DateUtility.strToDate(attendDate, "yyyyMMdd");
                        Date dd = new Date();
                        if(arriveTimeDate.after(dd)){
                            //��
                            states = -1;
                        }
                        list.add(states);
                    }
                    hm.put(deviceId, list);
                }
            } catch (Exception e) {
                logger.error("[assemEmployeeAttendInfo1] "+e.getStackTrace());
            }
        	
        }
        return hm;
    }
    

    public static String convertEmployeeAttendFromObjectToJson1(HashMap<String, ArrayList<Integer>> hm, 
    		HashMap<String, EmployeeAttend> hmEmploy, String year,String month) {
        StringBuffer jsonSb = new StringBuffer();
        Iterator<Entry<String, ArrayList<Integer>>> iter = hm.entrySet().iterator();
        while (iter.hasNext()) {
        	Map.Entry entry = (Map.Entry) iter.next();
        	String deviceId = (String)entry.getKey();
        	ArrayList<Integer> val = (ArrayList<Integer>)entry.getValue();
        	EmployeeAttend emp = hmEmploy.get(deviceId);
        	//�õ����ն���һ��֮��ָ���Ĺ������� add by wangzhen
        	Long week = getWeekByDeviceId(deviceId);
        	
            jsonSb.append("{");
            jsonSb.append("id:'" +emp.getId() + "',");// id
            jsonSb.append("deviceId:'"+emp.getDeviceId()+"',");
            if(emp.getTermName() != null) {
                jsonSb.append("termName:'" +emp.getTermName() + "',");
            } else {
                jsonSb.append("termName:'',");
            }
            if(emp.getGroupName() != null) {
                jsonSb.append("groupName:'" +emp.getGroupName() + "',");
            } else {
                jsonSb.append("groupName:'',");
            }
            if(emp.getSimcard() != null) {
                jsonSb.append("simcard:'" +emp.getSimcard() + "',");  
            } else {
                jsonSb.append("simcard:'',"); 
            }
            //Map<Integer,Integer> empMap = emp.getAttendStateMap();
			//Integer mountCount = DateUtils.getMonthCountByStr(year+""+month, "yyyyMM");
			int attendDaysi = 0;
			int noattendDaysi = 0;
			int noWorkDaysi = 0;
			int lateOutDaysi = 0;
			int vacateDaysi = 0;
            for(int i = 0;i < val.size();i++) {
            	int states = val.get(i);
                //�жϸ������Ƿ��ǹ����գ�������״̬��Ϊ-1 add by wangzhen �ǹ����ղ�Ҫ��ʾ�κ�״̬
            	if(!(isWorkday(week,Integer.valueOf(year),Integer.valueOf(month),i+1))) {
            	    states = -1;
            	}
                if(states == 2){
                	noattendDaysi++;
                }else if(states == 0){
                	attendDaysi++;
                }else if(states == 1){
                	noWorkDaysi++;
                }else if(states == 4){
                	vacateDaysi++;
                }else if(states == 5){
                	lateOutDaysi++;
                }
                
                jsonSb.append("day"+(i+1)+":'" +states+ "',");
            }
            jsonSb.append("attendDays:'"+attendDaysi+"',");
            jsonSb.append("noattendDays:'"+noattendDaysi+"',");
            jsonSb.append("noWorkDays:'"+noWorkDaysi+"',");
            jsonSb.append("lateOutDays:'"+lateOutDaysi+"',");
            jsonSb.append("vacateDays:'"+vacateDaysi+"'");
            jsonSb.append("},");
        }
        if (jsonSb.length() > 0) {
            jsonSb.deleteCharAt(jsonSb.length() - 1);
        }
        return jsonSb.toString();
    }
    
    /**
     * ͨ���ն�����Id��ȡ��������
     * @param deviceId
     * @return
     */
    public static Long getWeekByDeviceId(String deviceId) {
        TerminalService terminalService = (TerminalService) SpringHelper
                .getBean("tTargetObjectService");
        
        /** �ն���Ϣ* */
        Terminal termCached = Constants.TERMINAL_CACHE.get(deviceId);
        if (termCached == null) {
            TTerminal terminal = terminalService.findTerminal(deviceId);
            if (terminal != null) {
                termCached = new Terminal();
                BeanUtils.copyProperties(terminal, termCached);
                RefTermGroup refTermGroup = terminalService.getTermGroup(terminal);
                termCached.setGroupId(refTermGroup.getTTermGroup().getId());
                Constants.TERMINAL_CACHE
                        .set(deviceId, termCached, 10 * Memcache.EXPIRE_DAY);// �ն�ʱ�䵽��,oracle_job���޸��ն˱�ʶΪ�����ڡ�,��˻���10��
            }
        }
       
        return termCached.getWeek();
    }
    
    /**
     * ͨ���ն�¼��Ĺ��������õ����칤��״̬
     * @param week
     * @param day
     * @return
     */
    public static boolean isWorkday(Long week,Integer year,Integer month,Integer day) {
        String  workday = week(year,month,day);
        Double temp = 0.0;
        if("һ".equals(workday)) {
            temp = 1.0;
        } 
        if("��".equals(workday)) {
            temp = 2.0;
        } 
        if("��".equals(workday)) {
            temp = 3.0;
        } 
        if("��".equals(workday)) {
            temp = 4.0;
        } 
        if("��".equals(workday)) {
            temp = 5.0;
        } 
        if("��".equals(workday)) {
            temp = 6.0;
        } 
        if("��".equals(workday)) {
            temp = 7.0;
        } 
        
        Double weekData = Math.pow(2d, temp-1);
        String tmpData = weekData.toString();
        tmpData = tmpData.substring(0, tmpData.length()-2);
        Integer actData = Integer.valueOf(tmpData);
        if((week & actData) == actData) {
            return true;
        }
        return false;
    }

    /**
     * ������ת����Excel
     * @param excelWorkBook
     * @param hm
     * @param hmEmploy
     * @param year
     * @param month
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static ExcelWorkBook convertEmployeeAttendFromObjectToExcel1(
            ExcelWorkBook excelWorkBook, HashMap<String, ArrayList<Integer>> hm,
            HashMap<String, EmployeeAttend> hmEmploy, String year, String month) {
        Integer yeari = Integer.valueOf(year);
        Integer monthi = Integer.valueOf(month);
        try {
            excelWorkBook.addHeader("Ա������",30);
            excelWorkBook.addHeader("����", 30);
            excelWorkBook.addHeader("�ֻ�����", 30);
            Integer mountCount = DateUtils.getMonthCountByStr(year+""+month, "yyyyMM");
            for(int i= 1;i <= mountCount;i ++) {
                excelWorkBook.addHeader(i+"", 15);
            }
            excelWorkBook.addHeader("��������", 15);
            excelWorkBook.addHeader("ȱ������", 15);
            excelWorkBook.addHeader("�Ѹ�����", 15);
            excelWorkBook.addHeader("�ٵ�����", 15);
            excelWorkBook.addHeader("�������", 15);
            int col = 0;
            excelWorkBook.addCell(col++, 1, "");
            excelWorkBook.addCell(col++, 1, "");
            excelWorkBook.addCell(col++, 1, "");
            for(int i= 1;i <= mountCount;i ++) {
                String week = EmployeeAttendUtils.week(yeari,monthi,i);
                excelWorkBook.addCell(col++, 1, week);
            }
            excelWorkBook.addCell(col++, 1, "");
            excelWorkBook.addCell(col++, 1, "");
            excelWorkBook.addCell(col++, 1, "");
            excelWorkBook.addCell(col++, 1, "");
            excelWorkBook.addCell(col++, 1, "");
            int row = 2;
            Iterator<Entry<String, ArrayList<Integer>>> iter = hm.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String deviceId = (String)entry.getKey();
                ArrayList<Integer> val = ((ArrayList<Integer>)entry.getValue());
                EmployeeAttend emp = hmEmploy.get(deviceId);
                //�õ����ն���һ��֮��ָ���Ĺ������� add by wangzhen
                Long week = getWeekByDeviceId(deviceId);
                int i = 0;
                if(emp.getTermName() == null) {
                    excelWorkBook.addCell(i, row, "");
                } else {
                    excelWorkBook.addCell(i, row, emp.getTermName()+""); 
                }
                if(emp.getGroupName() == null) {
                    excelWorkBook.addCell(++i, row, "");
                } else {
                    excelWorkBook.addCell(++i, row, emp.getGroupName()+"");
                }
                if(emp.getSimcard() == null) {
                    excelWorkBook.addCell(++i, row, "");
                } else {
                    excelWorkBook.addCell(++i, row, emp.getSimcard()+"");
                }
                int attendDaysi = 0;
                int noattendDaysi = 0;
                int noWorkDaysi = 0;
                int lateOutDaysi = 0;
                int vacateDaysi = 0;
                for(int j = 0;j < val.size();j++) {
                    int states = val.get(j);
                    //�жϸ������Ƿ��ǹ����գ�������״̬��Ϊ-1 add by wangzhen �ǹ����ղ�Ҫ��ʾ�κ�״̬
                    if(!(isWorkday(week,Integer.valueOf(year),Integer.valueOf(month),j+1))) {
                        states = -1;
                    }
                    if(states == 2){
                        noattendDaysi++;
                    }else if(states == 0){
                        attendDaysi++;
                    }else if(states == 1){
                        noWorkDaysi++;
                    }else if(states == 4){
                        vacateDaysi++;
                    }else if(states == 5){
                        lateOutDaysi++;
                    }
                    String displayCon = "";
                    if(states == 0 ) {
                        displayCon = "��";
                    }
                    if(states == 1 ) {
                        displayCon = "��";
                    }
                    if(states == 2 ) {
                        displayCon = "��";
                    }
                    if(states == 3 ) {
                        displayCon = "��";
                    }
                    if(states == 4 ) {
                        displayCon = "��";
                    }
                    if(states == 5 ) {
                        displayCon = "��";
                    }
                    excelWorkBook.addCell(++i, row, displayCon);
                }
                excelWorkBook.addCell(++i, row, attendDaysi+"");
                excelWorkBook.addCell(++i, row, noattendDaysi+"");
                excelWorkBook.addCell(++i, row, noWorkDaysi+"");
                excelWorkBook.addCell(++i, row, lateOutDaysi+"");
                excelWorkBook.addCell(++i, row, vacateDaysi+"");
                row++;
            }
            return excelWorkBook;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
        
