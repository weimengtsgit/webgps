/**
 * 
 */
package com.sosgps.wzt.excel;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * @author xiaojun.luan
 * 
 */
public class ExcelWorkBook {
	private WritableWorkbook workbook;
	private WritableSheet workSheet;

	private WritableCellFormat curFormat;
	private WritableCellFormat hearderFormat;
	private String sheetName = "1";
	private int headerIndex;
	private int sheetIndex = 0;

	public ExcelWorkBook(OutputStream os) {
		try {
			createWorkbook(os);
			setFormat();
			headerIndex = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ExcelWorkBook(String sheetName, OutputStream os) {
		try {
			this.sheetName = sheetName;
			createWorkbook(os);
			setFormat();
			headerIndex = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ExcelWorkBook(HttpServletResponse response) throws IOException {
		this(response.getOutputStream());
		response.setHeader("Content-Disposition",
				"attachment; filename=download.xls");
	}
	
	public ExcelWorkBook(String sheetName, HttpServletResponse response) throws IOException {
		this(sheetName, response.getOutputStream());
		response.setHeader("Content-Disposition",
				"attachment; filename=download.xls");
	}

	private void createWorkbook(OutputStream os) throws IOException {
		workbook = Workbook.createWorkbook(os);
		workSheet = workbook.createSheet(sheetName, 0);
	}

	public void setFormat() {
		curFormat = new WritableCellFormat();// ÓÃÓÚtitle
		hearderFormat = new WritableCellFormat();
		try {
			curFormat.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			curFormat.setAlignment(jxl.format.Alignment.CENTRE);
			hearderFormat.setBackground(jxl.format.Colour.GRAY_25);
			hearderFormat.setAlignment(jxl.format.Alignment.CENTRE);
			hearderFormat.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			hearderFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addHeader(String cont) {
		addHeader(cont, 10);
	}

	public void addHeader(String cont, int width) {
		workSheet.setColumnView(headerIndex, width);
		addCell(headerIndex, 0, cont, hearderFormat);
		headerIndex++;
	}
	public void addHeader(String cont, int width,int startRow,int colSize,int rowSize){
		workSheet.setColumnView(headerIndex, width);
		if(colSize>1 || rowSize>1){
			mergeCells(headerIndex, startRow, headerIndex+colSize-1, startRow+rowSize-1);
		}
		addCell(headerIndex, startRow, cont, hearderFormat);
		headerIndex+=colSize;
	}
	public int getHeaderIndex() {
		return headerIndex;
	}

	public void setHeaderIndex(int headerIndex) {
		this.headerIndex = headerIndex;
	}

	public void mergeCells(int col1, int row1, int col2, int row2) {
		try {
			workSheet.mergeCells(col1, row1, col2, row2);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addCell(int col, int row, String cont,
			WritableCellFormat format) {
		Label label = new Label(col, row, cont, format);
		try {
			workSheet.addCell(label);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addCell(int col, int row, String cont) {
		addCell(col, row, cont, curFormat);
	}

	public void write() {
		try {
			workbook.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(workbook!=null) workbook.close();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public void addWorkSheet(String sheetName){
		this.sheetName = sheetName;
		sheetIndex ++;
		this.workSheet = this.workbook.createSheet(sheetName, sheetIndex);
		this.headerIndex = 0;
	}
}
