package com.sosgps.wzt.locate.service.impl;

import org.sos.helper.SpringHelper;

import com.sosgps.wzt.locate.service.LocateService;

public class GetPosDesc {
	LocateService tLoccateService = (LocateService) SpringHelper
	.getBean("tLoccateService");
	public void getPosDesc(){
		this.tLoccateService.updateNoEncryptionAndNoLocDesc();
	}
}
