package com.sosgps.wzt.orm;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TReportFilepath entity. @author MyEclipse Persistence Tools
 */
public class TReportFilepath extends AbstractTReportFilepath implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TReportFilepath() {
	}

	/** minimal constructor */
	public TReportFilepath(Long id) {
		super(id);
	}

	/** full constructor */
	public TReportFilepath(Long id, String deviceId, String entCode,
			String filePath, Date reportDate, Date createDate) {
		super(id, deviceId, entCode, filePath, reportDate, createDate);
	}

}
