package com.sosgps.wzt.stat.report.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.sosgps.wzt.excel.PoiExcelSheet;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.stat.action.VisitStatAction;
import com.sosgps.wzt.stat.report.DownLoadReportService;
import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.terminal.dao.TerminalDAO;
import com.sosgps.wzt.util.DateUtility;

public class DownLoadReportServiceImpl implements DownLoadReportService {

	private static final Logger logger = Logger
			.getLogger(VisitStatAction.class);

	private TerminalDAO terminalDAO;
	private String rarPath;

	public void setTerminalDAO(TerminalDAO terminalDAO) {
		this.terminalDAO = terminalDAO;
	}

	public void setRarPath(String rarPath) {
		this.rarPath = rarPath;
	}

	public String generateDownloadReport(String entCode,
			java.util.List<?> pathes, boolean isZip) {
		String ret = null;
		if (pathes.size() == 0) {
			logger.info("generateDownloadReport query result is zero");
			return null;
		}
		// long endTime = System.currentTimeMillis();

		logger.info("generateDownloadReport-HSSFWorkbook ");
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("汇总");
		PoiExcelSheet poiSheet = null;
		logger.info("generateDownloadReport-PoiExcelSheet ");
		String dir = "";
		int count = 0;
		int counts = 0;
		// String sname = "";
		// HSSFWorkbook book = null;
		HSSFSheet sheetname = null;
		FileInputStream in = null;

		logger.info("generateDownloadReport-pathes.size() " + pathes.size());

		int tmp = 0;
		Map<String, String> deviceNames = new HashMap<String, String>();
		// 遍历所有Excel文件的路径
		for (int x = 0; x < pathes.size(); x++) {
			dir = (String) pathes.get(x);
			System.out.println("dir: " + dir);
			// this.logger.info("read in file=" + dir);
			try {
				in = new FileInputStream(dir);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("generateDownloadReport-read file '" + dir
						+ "' is not exist");
				continue;
			}
			// 获得文件名
			String fileName = StringUtils.replace(dir.substring(
					dir.lastIndexOf(File.separator), dir.length()),
					File.separator, "");
			// 获得终端的Device ID
			String deviceId = fileName.substring(0, fileName.indexOf("_"));
			String deviceName = null;
			if (!deviceNames.containsKey(deviceId)) {
				TTerminal tml = terminalDAO.findTermById(deviceId);
				if (tml != null && tml.getTermName() != null) {
					deviceName = tml.getTermName();
				} else {
					logger.error("device is not exists, deviceId: " + deviceId);
				}
			}

			try {
				poiSheet = new PoiExcelSheet(in);

			} catch (IOException ie) {
				ie.printStackTrace();
				logger.error("generateDownloadReport-PoiExcelSheet instance error");
				continue;
			}
			// System.out.println("sheet数目： "
			// + poiSheet.getBook().getNumberOfSheets());
			// System.out.println("");
			// 当前excel sheet count 循环每个sheet

			for (int y = 0; y < poiSheet.getSheetCount(); y++) {
				HSSFSheet childSheet = poiSheet.getBook().getSheetAt(y);

				// 如果是sheet 1
				if (y == 0) {
					// 输出sheet 名称
					// this.logger.info("sheetName="
					// + childSheet.getSheetName());
					int s = 0;
					if (x != 0) {
						s = 1;
					}
					for (int r = s; r < childSheet.getPhysicalNumberOfRows(); r++) {

						HSSFRow row = sheet.createRow(count++);

						for (int c = 0; c < childSheet.getRow(r)
								.getPhysicalNumberOfCells(); c++) {// 循环该子sheet行对应的单元格项
							HSSFCell cell = childSheet.getRow(r).getCell(c);
							HSSFCell cells = row.createCell(c);

							String value = null;

							if (cell == null)
								continue;

							switch (cell.getCellType()) {

							case HSSFCell.CELL_TYPE_NUMERIC:
								value = "" + cell.getNumericCellValue();
								cells.setCellValue(value);
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

					// 重复sheet名字 不新建sheet
					if (!deviceNames.containsKey(deviceId)) {

						sheetname = wb.createSheet(deviceName);
						// sname = childSheet.getSheetName();
						counts = 0;
						tmp = 0;
						deviceNames.put(deviceId, deviceName);
					} else {
						sheetname = wb.getSheet(deviceNames.get(deviceId));
						tmp = 1;
					}
					int s = 0;
					if (tmp != 0) {
						s = 1;
					}
					for (int r = s; r < childSheet.getPhysicalNumberOfRows(); r++) {

						HSSFRow row = sheetname.createRow(counts++);

						for (int c = 0; c < childSheet.getRow(r)
								.getPhysicalNumberOfCells(); c++) {// 循环该子sheet行对应的单元格项
							HSSFCell cell = childSheet.getRow(r).getCell(
									(int) c);
							HSSFCell cells = row.createCell(c);
							// System.out.println("cell:: " + cell);
							String value = null;

							if (cell == null)
								continue;
							// System.out.println("cell.getCellType()::
							// "
							// + cell.getCellType());
							switch (cell.getCellType()) {

							case HSSFCell.CELL_TYPE_NUMERIC:
								value = "" + cell.getNumericCellValue();
								cells.setCellValue(value);
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
		// 定义生成临时文件的存放位置
		String filePath = Constants.TEMP_PATH;
		String currentTime = DateUtility.getCurrentDateTime("yyyyMMddHH24mmss");

		String fileName = entCode + "" + currentTime + ".xls";
		try {
			java.io.OutputStream out = new FileOutputStream(filePath + fileName);
			ret = filePath + fileName;
			wb.write(out);
			out.close();
		} catch (Exception ex) {
			logger.error("generateDownloadReport-FileOutputStream to "
					+ filePath + fileName + " error," + ex.getMessage());
			ret = null;
		}

		// 压缩文件
		if (isZip && ret != null) {
			// ret = this.zipDownloadReport(entCode, ret);
			ret = zipReport(entCode, ret);
		}
		return ret;
	}

	/**
	 * 
	 * @return
	 */
	private String zipReport(String entCode, String filePathName) {
		try {
			String osName = System.getProperty("os.name");
			if (osName.toLowerCase().startsWith("windows")) {
				return zipReportOnWinX(entCode, filePathName);
			} else if (osName.toLowerCase().contains("linux")) {
				return zipDownloadReport(entCode, filePathName);
			} else {
				return filePathName;
			}
		} catch (Exception e) {
			logger.error("zip failure, filePathName: " + filePathName);
			return filePathName;
		}
	}

	/**
	 * 
	 * @param rarPath
	 * @return
	 * @throws IOException
	 */
	private String zipReportOnWinX(String entCode, String filePathName)
			throws Exception {

		String currentTime = DateUtility.getCurrentDateTime("yyyyMMddHH24mmss");
		String zipFileName = entCode + currentTime + ".zip";
		Runtime rt = Runtime.getRuntime();
		Process p;
		int exitVal;

		p = rt.exec(this.rarPath + "winrar.exe a " + Constants.TEMP_PATH
				+ zipFileName + " " + filePathName);
		exitVal = p.waitFor();
		if (exitVal != 0) {
			throw new Exception("zip failure!ERRORCODE:" + exitVal);
		}
		return Constants.TEMP_PATH + zipFileName;
	}

	/**
	 * 
	 * @return 压缩文件路径
	 */
	public String zipDownloadReport(String entCode, String filePathName) {
		String currentTime = DateUtility.getCurrentDateTime("yyyyMMddHH24mmss");
		String zipFileName = entCode + currentTime + ".zip";

		// 压缩命令
		String commond = "zip -q -m -D " + Constants.TEMP_PATH + zipFileName
				+ " " + filePathName;

		try {
			logger.info("commond:" + commond);
			// 执行压缩命令
			Process child = Runtime.getRuntime().exec(commond);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					child.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			String result = sb.toString();

			logger.info("result:" + result);
		} catch (Exception e) {
			logger.error("commond error," + commond, e);
			zipFileName = null;
		}
		return Constants.TEMP_PATH + zipFileName;
	}

	public String readReport() {
		return null;
	}
}
