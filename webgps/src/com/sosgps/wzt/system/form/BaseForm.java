package com.sosgps.wzt.system.form;

import org.apache.struts.action.ActionForm;

public class BaseForm extends ActionForm {
private String method;

public String getMethod() {
	return method;
}

public void setMethod(String method) {
	this.method = method;
}
}
