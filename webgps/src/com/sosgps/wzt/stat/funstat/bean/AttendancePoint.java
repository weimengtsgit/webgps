/**
 * 
 */
package com.sosgps.wzt.stat.funstat.bean;

/**
 * @author xiaojun.luan
 * 
 */
public class AttendancePoint {
	public static final String SUCCEED="succeed";
	public static final String FAILED="failed";

	private String time;
	private String loc_desc;
	private String ruleContent;
	private String effectPeriod;
	private String ruleArea;
	private String result;

	public String getRuleArea() {
		return ruleArea;
	}

	public void setRuleArea(String ruleArea) {
		this.ruleArea = ruleArea;
	}

	public String getEffectPeriod() {
		return effectPeriod;
	}

	public void setEffectPeriod(String effectPeriod) {
		this.effectPeriod = effectPeriod;
	}

	public String getRuleContent() {
		return ruleContent;
	}

	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLoc_desc() {
		return loc_desc;
	}

	public void setLoc_desc(String loc_desc) {
		this.loc_desc = loc_desc;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
