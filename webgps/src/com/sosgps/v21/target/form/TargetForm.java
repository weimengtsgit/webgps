package com.sosgps.v21.target.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class TargetForm extends ActionForm {
	private Integer year;
	private Integer type;
	private FormFile targetFile;// �ϴ��ļ�

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public FormFile getTargetFile() {
		return targetFile;
	}

	public void setTargetFile(FormFile targetFile) {
		this.targetFile = targetFile;
	}
}
