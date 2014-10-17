/**
 * 
 */
package com.sosgps.wzt.system.form;

/**
 * @author xiaojun.luan
 *
 */
public class RefEntCalledNumberForm extends BaseForm {
	private String entCode;
	private String entName;
	private String calledNumber;
	private String buttonName;
	private String numberName;
	public String getNumberName() {
		return numberName;
	}
	public void setNumberName(String numberName) {
		this.numberName = numberName;
	}
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public String getEntCode() {
		return entCode;
	}
	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}
	public String getEntName() {
		return entName;
	}
	public void setEntName(String entName) {
		this.entName = entName;
	}
	public String getCalledNumber() {
		return calledNumber;
	}
	public void setCalledNumber(String calledNumber) {
		this.calledNumber = calledNumber;
	}
}
