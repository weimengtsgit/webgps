package com.sosgps.wzt.stat.report;

import java.util.List;

public interface DownLoadReportService {

	/**
	 * 生成下载报表 ,是否生成压缩文件
	 */
	public String generateDownloadReport(String entCode, List<?> pathes,
			boolean isZip);

	/**
	 * 
	 * @return 压缩文件路径
	 */
	public String zipDownloadReport(String entCode, String filePath);

	public String readReport();
}
