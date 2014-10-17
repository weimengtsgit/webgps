package com.sosgps.wzt.system.form;

@SuppressWarnings("serial")
public class LoginForm extends BaseForm {
	private String userAccount ;
	private String password;
	private String empCode;
	private String loginType;
	private String validateCode;
	private String edition;
	private String cooperation;
	
	public String getCooperation() {
		return cooperation;
	}
	public void setCooperation(String cooperation) {
		this.cooperation = cooperation;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
}