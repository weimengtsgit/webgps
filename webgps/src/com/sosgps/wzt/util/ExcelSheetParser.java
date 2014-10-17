/**
 * 
 */
package com.sosgps.wzt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author shiguang.zhou
 * 
 */
public class ExcelSheetParser {
	// log4j

	private HSSFWorkbook workbook;// ������
	private HashMap<String, List> subCompanyMap;

	public ExcelSheetParser() {
		try {
			// ��ȡ������workbook
			InputStream is = getClass().getResourceAsStream(
			"/sub_company.xls");
			 
			workbook = new HSSFWorkbook(is);
			subCompanyMap = this.getDatasInSheet(0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap getDatasInSheet(int sheetNumber) {
		HashMap<String, List> result = new HashMap<String, List>();

		// ���ָ����sheet
		HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
		// ���sheet������
		int rowCount = sheet.getLastRowNum();

		if (rowCount < 1) {
			return result;
		}

		// ������row

		// ����ж���
		HSSFRow row = sheet.getRow(0);
		if (null != row) {
			
			// ��ñ����е�Ԫ��ĸ���
			int cellCount = row.getLastCellNum();
			// ������cell

			for (short cellIndex = 0; cellIndex < cellCount; cellIndex++) {
				String key = null;
				List<Object> rowData = new ArrayList<Object>();
				
				for (int rowi = 0; rowi <= rowCount; rowi++) {
					 
					HSSFRow srow = sheet.getRow(rowi);
					if (srow != null) {
						HSSFCell scell = srow.getCell(cellIndex);
						if (scell != null) {
							Object cont = this.getCellString(scell);
							if (rowi == 0) {
								key = cont.toString();//��һ����ΪKEY
								continue;
							}

							String scont = null;
							int index = -1;
							if (cont != null) {
								scont = cont.toString().trim();
								index = scont.lastIndexOf(".");
							}
							if (index != -1)
								scont = scont.substring(0, index);
							if (scont != null)
								rowData.add(scont);//����һ�����ݼ��뵽LIST����ΪKEY��VALUE
						}
					}

				}
				result.put(key, rowData);
			}

		}

		return result;
	}

	private Object getCellString(HSSFCell cell) {
		// TODO Auto-generated method stub
		Object result = null;
		if (cell != null) {
			// ��Ԫ�����ͣ�Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
			int cellType = cell.getCellType();
			switch (cellType) {
			case HSSFCell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();// getRichStringCellValue().getString();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				result = cell.getNumericCellValue();
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				result = cell.getNumericCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				result = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				result = null;
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				result = null;
				break;
			default:
				System.out.println("ö������������");
				break;
			}
		}
		return result;
	}
	/**
	 * �����ֻ������ǰ6λ�жϺ��������ƶ��ֹ�˾
	 * @param file
	 * @return
	 */
	public   String getSubCompanyByNumber(String number){
		String subCompany = null;
		if (number == null)
			return null;		 
		 
		Iterator<String> it = subCompanyMap.keySet().iterator();
		
		while (it.hasNext()) {
			String key = it.next();
			List<String> list = subCompanyMap.get(key);
			if (list.contains(number)){
				subCompany = key;
				break;
			}

		}
		
		return subCompany;
	}

	// test
	public static void main(String[] args) {
		 
		ExcelSheetParser parser = new ExcelSheetParser();
		String key = parser.getSubCompanyByNumber("1358941");
		System.out.println(key);
//		HashMap<String, List> datas = parser.getDatasInSheet(0);
//		Iterator<String> it = datas.keySet().iterator();
//		while (it.hasNext()) {
//			String key = it.next();
//			List<String> list = datas.get(key);
//			System.out.println(key);
//			for (int i = 0; i < list.size(); i++) {
//				System.out.println(list.get(i));
//			}
//
//		}
	}

}
