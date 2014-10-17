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
		// sheet = book.getSheetAt(0);//返回第一个sheet
	}

	public PoiExcelSheet(FileInputStream in, String sheetName)
			throws IOException {
		book = new HSSFWorkbook(in);
		sheet = book.getSheet(sheetName);// 返回名称为sheetName的sheet
	}

	public PoiExcelSheet(FileInputStream in, int i) throws IOException {
		book = new HSSFWorkbook(in);
		sheet = book.getSheetAt(i);// 返回第i个sheet，从0开始
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
			// 不存在就插入一个单元格
			HSSFRichTextString richValue = new HSSFRichTextString(value);
			dataRow = sheet.createRow(row);
			dataCell = dataRow.createCell((short) col);
			dataCell.setCellValue(richValue);
			System.out.println("该单元格不存在，新建一个单元格");
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
			System.out.println("获取单元格值失败：" + e);
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
				// System.out.println("sheet数目： "
				// + poiSheet.getBook().getNumberOfSheets());
				// System.out.println("");
				// 当前excel sheet count 循环每个sheet
				
				for (int y = 0; y < poiSheet.getSheetCount(); y++) {
					HSSFSheet childSheet = poiSheet.getBook().getSheetAt(y);
					
					// 如果是sheet 1
					if (y == 0) {
						// 输出sheet 名称
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
									.getPhysicalNumberOfCells(); c++) {// 循环该子sheet行对应的单元格项
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
						// 当前sheet 行数
						// 输出sheet 名称
//						System.out.println("sheetName1="
//								+ childSheet.getSheetName());
						
						//重复sheet名字 不新建sheet
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
									.getPhysicalNumberOfCells(); c++) {// 循环该子sheet行对应的单元格项
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
		// System.out.println("sheet数目： "
		// + poiSheet.getBook().getNumberOfSheets());
		//
		// for (int i = 0; i < poiSheet.getBook().getNumberOfSheets(); i++) {//
		// 循环sheet
		// System.out.println("==========开始第 " + i
		// + " 个sheet============");
		// HSSFSheet childSheet = poiSheet.getBook().getSheetAt(i);
		//
		// if (i == 0) {
		// for (int r = 0; r < childSheet.getPhysicalNumberOfRows(); r++) {//
		// 循环该
		//
		// HSSFRow row = sheet.createRow(r); // row
		// for (short c = 0; c <
		// childSheet.getRow(r).getPhysicalNumberOfCells(); c++) {//
		// 循环该子sheet行对应的单元格项
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
		// .getPhysicalNumberOfRows(); r++) {// 循环该
		//
		// HSSFSheet sheetname = wb.createSheet(childSheet
		// .getSheetName()
		// + r + x);
		// HSSFRow row = sheetname.createRow(r); // row
		// for (short c = 0; c < childSheet.getRow(r)
		// .getPhysicalNumberOfCells(); c++) {// 循环该子sheet行对应的单元格项
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
		// // HSSFRow row = sheet.getRow(0);//行
		// // HSSFCell cell = row.getCell((short) 0);//单元格
		// // 输出单元内容，cell.getStringCellValue() 就是取所在单元的值
		// } catch (Exception e) {
		// System.out.println("已运行xlRead() : " + e);
		// }
		// OutputStream out = new FileOutputStream("d:\\report\\test.xls");
		// wb.write(out);
		// out.close();

	}
}
