package com.sosgps.wzt.excel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PoiExcelSheet {

	public HSSFWorkbook book = null;

	public HSSFSheet sheet = null;

	HSSFRow dataRow = null;

	HSSFCell dataCell = null;

	public PoiExcelSheet(FileInputStream in) throws IOException {
		book = new HSSFWorkbook(in);
		// sheet = book.getSheetAt(0);//���ص�һ��sheet
	}

	public PoiExcelSheet(FileInputStream in, String sheetName)
			throws IOException {
		book = new HSSFWorkbook(in);
		sheet = book.getSheet(sheetName);// ��������ΪsheetName��sheet
	}

	public PoiExcelSheet(FileInputStream in, int i) throws IOException {
		book = new HSSFWorkbook(in);
		sheet = book.getSheetAt(i);// ���ص�i��sheet����0��ʼ
	}

	public void setCellValue(String A1, String value) {
		int col = A1.toUpperCase().charAt(0) - 'A';
		int row = Integer.parseInt(A1.substring(1)) - 1;
		System.out.println("A1=" + row + "," + col);
		setCellValue(row, col, value);
	}

	public void setCellValue(int row, int col, String value) {
		try {
			HSSFRichTextString richValue = new HSSFRichTextString(value);
			dataRow = sheet.getRow(row);
			dataCell = dataRow.getCell((short) col);
			dataCell.setCellValue(richValue);
		} catch (NullPointerException e) {
			// �����ھͲ���һ����Ԫ��
			HSSFRichTextString richValue = new HSSFRichTextString(value);
			dataRow = sheet.createRow(row);
			dataCell = dataRow.createCell((short) col);
			dataCell.setCellValue(richValue);
			System.out.println("�õ�Ԫ�񲻴��ڣ��½�һ����Ԫ��");
		}
	}

	public String getCellValue(int row, int col) {
		try {
			dataRow = sheet.getRow(row);
			dataCell = dataRow.getCell((short) col);
			int cellType = dataCell.getCellType();
			switch (cellType) {
			case HSSFCell.CELL_TYPE_BOOLEAN:
				return String.valueOf(dataCell.getBooleanCellValue());
			case HSSFCell.CELL_TYPE_ERROR:
				return String.valueOf(dataCell.getErrorCellValue());
			case HSSFCell.CELL_TYPE_NUMERIC:
				return String.valueOf(dataCell.getNumericCellValue());
			case HSSFCell.CELL_TYPE_STRING:
				return String.valueOf(dataCell.getRichStringCellValue());
			case HSSFCell.CELL_TYPE_BLANK:
				return String.valueOf(dataCell.getRichStringCellValue());
			case HSSFCell.CELL_TYPE_FORMULA:
				return String.valueOf(dataCell.getNumericCellValue());
			}
		} catch (Exception e) {
			System.out.println("��ȡ��Ԫ��ֵʧ�ܣ�" + e);
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return "";
	}

	public String getCellValue(String A1) throws Exception {
		int col = A1.toUpperCase().charAt(0) - 'A';
		int row = Integer.parseInt(A1.substring(1)) - 1;
		return getCellValue(row, col);
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public void setBook(HSSFWorkbook book) {
		this.book = book;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public HSSFWorkbook getBook() {
		return book;
	}

	public int getRowCount() {
		return sheet.getPhysicalNumberOfRows();
	}

	public int getSheetCount() {
		return book.getNumberOfSheets();
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir = "D:\\report\\bjdy\\13846133704_2010-08-26.xls";
		// FileInputStream in = new FileInputStream(dir);
		// PoiExcelSheet poiSheet = new PoiExcelSheet(in);
		PoiExcelSheet poiSheet = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("abc");

		try {
			List<String> pathes = new java.util.ArrayList();
			pathes.add("d:\\report\\bjdy\\13846132764_2010-08-26.xls");
			pathes.add("d:\\report\\bjdy\\13846132764_2010-08-27.xls");

			pathes.add("d:\\report\\bjdy\\13846133704_2010-08-26.xls");
			pathes.add("d:\\report\\bjdy\\13846133704_2010-08-27.xls");
			

			pathes.add("d:\\report\\bjdy\\13846142184_2010-08-26.xls");
			pathes.add("D:\\report\\bjdy\\13846142184_2010-08-27.xls");
			

			pathes.add("D:\\report\\bjdy\\13846199284_2010-08-26.xls");
			pathes.add("D:\\report\\bjdy\\13846199284_2010-08-27.xls");
			

			pathes.add("D:\\report\\bjdy\\13945481264_2010-08-26.xls");
			pathes.add("D:\\report\\bjdy\\13945481264_2010-08-27.xls");
			

			pathes.add("D:\\report\\bjdy\\13945481664_2010-08-26.xls");
			pathes.add("D:\\report\\bjdy\\13945481664_2010-08-27.xls");
			
			

			pathes.add("D:\\report\\bjdy\\13945481664_2010-08-27.xls");
		
			int count =0;
			int counts =0;
			String sname = "";
			HSSFSheet sheetname = null;
			int tmp =0;
			for (int x = 0; x < pathes.size(); x++) {
				dir = pathes.get(x);
				FileInputStream in = new FileInputStream(dir);
				poiSheet = new PoiExcelSheet(in);
				// System.out.println("sheet��Ŀ�� "
				// + poiSheet.getBook().getNumberOfSheets());
				// System.out.println("");
				// ��ǰexcel sheet count ѭ��ÿ��sheet
				
				for (int y = 0; y < poiSheet.getSheetCount(); y++) {
					HSSFSheet childSheet = poiSheet.getBook().getSheetAt(y);
					
					// �����sheet 1
					if (y == 0) {
						// ���sheet ����
//						System.out.println("sheetName="
//								+ childSheet.getSheetName());
						int s = 0;
						if(x != 0){
							s=1;
						}
						for (int r=s ; r < childSheet
								.getPhysicalNumberOfRows(); r++) {
							
							HSSFRow row = sheet.createRow(count++);
							
							for (short c = 0; c < childSheet.getRow(r)
									.getPhysicalNumberOfCells(); c++) {// ѭ������sheet�ж�Ӧ�ĵ�Ԫ����
								HSSFCell cell = childSheet.getRow(r).getCell(c);
								HSSFCell cells = row.createCell(c);
								
								// System.out.println("cell:: " + cell);
								String value = null;

								if (cell == null)
									continue;
								// System.out.println("cell.getCellType():: "
								// + cell.getCellType());
								switch (cell.getCellType()) {

								case HSSFCell.CELL_TYPE_NUMERIC:
									value = "" + cell.getNumericCellValue();

									break;

								case HSSFCell.CELL_TYPE_STRING:
									value = cell.getStringCellValue();
									cells.setCellValue(value);

									break;
								case HSSFCell.CELL_TYPE_BLANK:
									;
									break;
								default:
								}
							}
						}
					} else {
						// ��ǰsheet ����
						// ���sheet ����
//						System.out.println("sheetName1="
//								+ childSheet.getSheetName());
						
						//�ظ�sheet���� ���½�sheet
						if(!sname.equals(childSheet.getSheetName())){
							sheetname = wb.createSheet(childSheet.getSheetName());
							sname = childSheet.getSheetName();
							counts=0;
							tmp = 0;
						}
						else{
							tmp=1;
						}

						
						int s = 0;
						if(tmp != 0){
							s=1;
						}
						for (int r = s; r < childSheet
								.getPhysicalNumberOfRows(); r++) {
							
							HSSFRow row = sheetname.createRow(counts++);
							
							for (short c = 0; c < childSheet.getRow(r)
									.getPhysicalNumberOfCells(); c++) {// ѭ������sheet�ж�Ӧ�ĵ�Ԫ����
								HSSFCell cell = childSheet.getRow(r).getCell(c);
								HSSFCell cells = row.createCell(c);
								// System.out.println("cell:: " + cell);
								String value = null;

								if (cell == null)
									continue;
								// System.out.println("cell.getCellType():: "
								// + cell.getCellType());
								switch (cell.getCellType()) {

								case HSSFCell.CELL_TYPE_NUMERIC:
									value = "" + cell.getNumericCellValue();

									break;

								case HSSFCell.CELL_TYPE_STRING:
									value = cell.getStringCellValue();
									cells.setCellValue(value);
									break;
								case HSSFCell.CELL_TYPE_BLANK:
									;
									break;
								default:
								}
								// System.out.println("value :: " + value);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		OutputStream out = new FileOutputStream("d:\\report\\test.xls");
		 wb.write(out);
		 out.close();
		// try {
		//
		// for (int x = 0; x < 8; x++) {
		// FileInputStream in = new FileInputStream(dir);
		// poiSheet = new PoiExcelSheet(in);
		// System.out.println("sheet��Ŀ�� "
		// + poiSheet.getBook().getNumberOfSheets());
		//
		// for (int i = 0; i < poiSheet.getBook().getNumberOfSheets(); i++) {//
		// ѭ��sheet
		// System.out.println("==========��ʼ�� " + i
		// + " ��sheet============");
		// HSSFSheet childSheet = poiSheet.getBook().getSheetAt(i);
		//
		// if (i == 0) {
		// for (int r = 0; r < childSheet.getPhysicalNumberOfRows(); r++) {//
		// ѭ����
		//
		// HSSFRow row = sheet.createRow(r); // row
		// for (short c = 0; c <
		// childSheet.getRow(r).getPhysicalNumberOfCells(); c++) {//
		// ѭ������sheet�ж�Ӧ�ĵ�Ԫ����
		// HSSFCell cell = childSheet.getRow(r).getCell(c);
		// HSSFCell cells = row.createCell(c);
		//
		// System.out.println("cell:: " + cell);
		// String value = null;
		//
		// if (cell == null)
		// continue;
		// System.out.println("cell.getCellType():: "
		// + cell.getCellType());
		// switch (cell.getCellType()) {
		//
		// case HSSFCell.CELL_TYPE_NUMERIC:
		// value = "" + cell.getNumericCellValue();
		// cells.setCellValue(value);
		// break;
		//
		// case HSSFCell.CELL_TYPE_STRING:
		// value = cell.getStringCellValue();
		// cells.setCellValue(value);
		// break;
		// case HSSFCell.CELL_TYPE_BLANK:
		// ;
		// break;
		// default:
		// }
		// System.out.println("value :: " + value);
		// }
		// }
		// }
		//
		// else {
		// for (int r = 0; r < childSheet
		// .getPhysicalNumberOfRows(); r++) {// ѭ����
		//
		// HSSFSheet sheetname = wb.createSheet(childSheet
		// .getSheetName()
		// + r + x);
		// HSSFRow row = sheetname.createRow(r); // row
		// for (short c = 0; c < childSheet.getRow(r)
		// .getPhysicalNumberOfCells(); c++) {// ѭ������sheet�ж�Ӧ�ĵ�Ԫ����
		// HSSFCell cell = childSheet.getRow(r).getCell(c);
		// HSSFCell cells = row.createCell(c);
		//
		// System.out.println("cell:: " + cell);
		// String value = null;
		//
		// if (cell == null)
		// continue;
		// System.out.println("cell.getCellType():: "
		// + cell.getCellType());
		// switch (cell.getCellType()) {
		//
		// case HSSFCell.CELL_TYPE_NUMERIC:
		// value = "" + cell.getNumericCellValue();
		// cells.setCellValue(value);
		// break;
		//
		// case HSSFCell.CELL_TYPE_STRING:
		// value = cell.getStringCellValue();
		// cells.setCellValue(value);
		// break;
		// case HSSFCell.CELL_TYPE_BLANK:
		// ;
		// break;
		// default:
		// }
		// System.out.println("value :: " + value);
		// }
		// }
		// }
		//
		// }
		// }
		//
		// // HSSFRow row = sheet.getRow(0);//��
		// // HSSFCell cell = row.getCell((short) 0);//��Ԫ��
		// // �����Ԫ���ݣ�cell.getStringCellValue() ����ȡ���ڵ�Ԫ��ֵ
		// } catch (Exception e) {
		// System.out.println("������xlRead() : " + e);
		// }
		// OutputStream out = new FileOutputStream("d:\\report\\test.xls");
		// wb.write(out);
		// out.close();

	}
}
