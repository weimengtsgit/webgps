package com.sosgps.v21.util;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.sosgps.v21.model.EmailSendLog;
import com.sosgps.wzt.util.DateUtility;

/**
 * ����ת��������(�ʼ�������־)
 * 
 * @author liuhx
 * 
 */
public class EmailSendLogUtils {

	private static final Logger logger = Logger
			.getLogger(EmailSendLogUtils.class);

	/**
	 * ���ʼ�������־���ݴ�List<EmailSendLog>��ʽת����Excel
	 * 
	 * @param data
	 *            �ʼ�������־����(ת��ǰ)
	 * @return �ʼ�������־����(ת����)
	 */
	public static Workbook convertEmailSendLogFromObjectToExcel(
			List<EmailSendLog> logs, Class<? extends Workbook> clazz)
			throws InstantiationException, IllegalAccessException {
		Workbook wb = clazz.newInstance();
		CellStyle defaultCs = getDefaultCellStyle(wb);
		CellStyle headerCs = getHeaderCellStyle(wb);
		Sheet sheet = wb.createSheet();

		/** ��װͷ��Ϣ * */
		addCell(sheet, 0, 0, 0, 0, "����ʱ��", headerCs, true);
		sheet.setColumnWidth(0, 5000);
		addCell(sheet, 0, 0, 1, 1, "��ҵ����", headerCs, false);
		addCell(sheet, 0, 0, 2, 2, "��ҵ����", headerCs, false);
		addCell(sheet, 0, 0, 3, 3, "����", headerCs, false);
		addCell(sheet, 0, 0, 4, 4, "ְ��", headerCs, false);
		addCell(sheet, 0, 0, 5, 5, "����", headerCs, false);
		sheet.setColumnWidth(5, 5000);
		addCell(sheet, 0, 0, 6, 6, "�ʼ�����", headerCs, false);
		addCell(sheet, 0, 0, 7, 7, "��������", headerCs, false);
		addCell(sheet, 0, 0, 8, 8, "״̬", headerCs, false);

		/** ��װ���� * */
		int rownum = 1;
		for (EmailSendLog log : logs) {
			addCell(sheet, rownum, rownum, 0, 0, DateUtility
					.dateTimeToStr(new Date(log.getCreateon())), defaultCs,
					true);
			addCell(sheet, rownum, rownum, 1, 1, log.getEntCode(), defaultCs,
					false);
			addCell(sheet, rownum, rownum, 2, 2, log.getEntName(), defaultCs,
					false);
			addCell(sheet, rownum, rownum, 3, 3, log.getContactName(),
					defaultCs, false);
			addCell(sheet, rownum, rownum, 4, 4, log.getPosition(), defaultCs,
					false);
			addCell(sheet, rownum, rownum, 5, 5, log.getEmail(), defaultCs,
					false);
			addCell(sheet, rownum, rownum, 6, 6, "", defaultCs, false);
			addCell(sheet, rownum, rownum, 7, 7, log.getContent(), defaultCs,
					false);
			addCell(sheet, rownum, rownum, 8, 8, "", defaultCs, false);
			rownum++;
		}

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
		cs.setWrapText(false);
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
			int startcolumenum, int endcolumenum, int value, CellStyle cs,
			boolean create) {
		Cell cell = addCellStyle(sheet, startrownum, endrownum, startcolumenum,
				endcolumenum, cs, create);
		cell.setCellValue(value);
	}

	private static void addCell(Sheet sheet, int startrownum, int endrownum,
			int startcolumenum, int endcolumenum, double value, CellStyle cs,
			boolean create) {
		Cell cell = addCellStyle(sheet, startrownum, endrownum, startcolumenum,
				endcolumenum, cs, create);
		cell.setCellValue(value);
	}

}
