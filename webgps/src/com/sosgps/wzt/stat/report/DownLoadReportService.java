package com.sosgps.wzt.stat.report;

import java.util.List;

public interface DownLoadReportService {

	/**
	 * �������ر��� ,�Ƿ�����ѹ���ļ�
	 */
	public String generateDownloadReport(String entCode, List<?> pathes,
			boolean isZip);

	/**
	 * 
	 * @return ѹ���ļ�·��
	 */
	public String zipDownloadReport(String entCode, String filePath);

	public String readReport();
}
