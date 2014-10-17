package com.sosgps.wzt.log.form;

import com.sosgps.wzt.stat.funstat.form.TimeStatForm;
import com.sosgps.wzt.system.form.BaseForm;

public class LoginLogForm extends TimeStatForm{
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
