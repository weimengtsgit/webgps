package com.sosgps.wzt.orm;

import java.text.DecimalFormat;

/**
 * AbstractTCarspeedTjDay generated by MyEclipse Persistence Tools
 */

public abstract class AbstractTCarspeedTjDay implements java.io.Serializable {

	// Fields

	private Long id;

	private String deviceId;

	private Double low4T;

	private Double low4R;

	private Double high4low8T;

	private Double high4low8R;

	private Double high8low12T;

	private Double high8low12R;

	private Double high12low16T;

	private Double high12low16R;

	private Double high16T;

	private Double high16R;

	private Double maxspeed;

	private String dateDay;
	
	private String low4str="";
	private String high4low8str="";
	private String high8low12str="";
	private String high12low16str="";
	private String high16str="";
	

	// Constructors

	/** default constructor */
	public AbstractTCarspeedTjDay() {
	}

	/** minimal constructor */
	public AbstractTCarspeedTjDay(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AbstractTCarspeedTjDay(Long id, String deviceId, Double low4T,
			Double low4R, Double high4low8T, Double high4low8R,
			Double high8low12T, Double high8low12R, Double high12low16T,
			Double high12low16R, Double high16T, Double high16R, Double maxspeed,
			String dateDay) {
		this.id = id;
		this.deviceId = deviceId;
		this.low4T = low4T;
		this.low4R = low4R;
		this.high4low8T = high4low8T;
		this.high4low8R = high4low8R;
		this.high8low12T = high8low12T;
		this.high8low12R = high8low12R;
		this.high12low16T = high12low16T;
		this.high12low16R = high12low16R;
		this.high16T = high16T;
		this.high16R = high16R;
		this.maxspeed = maxspeed;
		this.dateDay = dateDay;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Double getLow4T() {
		return this.low4T;
	}

	public void setLow4T(Double low4T) {
		this.low4T = low4T;
	}

	public Double getLow4R() {
		return this.low4R;
	}

	public void setLow4R(Double low4R) {
		this.low4R = low4R;
	}

	public Double getHigh4low8T() {
		return this.high4low8T;
	}

	public void setHigh4low8T(Double high4low8T) {
		this.high4low8T = high4low8T;
	}

	public Double getHigh4low8R() {
		return this.high4low8R;
	}

	public void setHigh4low8R(Double high4low8R) {
		this.high4low8R = high4low8R;
	}

	public Double getHigh8low12T() {
		return this.high8low12T;
	}

	public void setHigh8low12T(Double high8low12T) {
		this.high8low12T = high8low12T;
	}

	public Double getHigh8low12R() {
		return this.high8low12R;
	}

	public void setHigh8low12R(Double high8low12R) {
		this.high8low12R = high8low12R;
	}

	public Double getHigh12low16T() {
		return this.high12low16T;
	}

	public void setHigh12low16T(Double high12low16T) {
		this.high12low16T = high12low16T;
	}

	public Double getHigh12low16R() {
		return this.high12low16R;
	}

	public void setHigh12low16R(Double high12low16R) {
		this.high12low16R = high12low16R;
	}

	public Double getHigh16T() {
		return this.high16T;
	}

	public void setHigh16T(Double high16T) {
		this.high16T = high16T;
	}

	public Double getHigh16R() {
		return this.high16R;
	}

	public void setHigh16R(Double high16R) {
		this.high16R = high16R;
	}

	public Double getMaxspeed() {
		return this.maxspeed;
	}

	public void setMaxspeed(Double maxspeed) {
		this.maxspeed = maxspeed;
	}

	public String getDateDay() {
		return this.dateDay;
	}

	public void setDateDay(String dateDay) {
		this.dateDay = dateDay;
	}

	
	
	
	public String getHigh12low16str() {
		double d=high12low16T;
		if(d!=0){
			if((int)d/60>0){
				high12low16str=(int)d/60+"小时";
			}
			DecimalFormat decimalformat = new DecimalFormat("0.0");
			
			high12low16str=high12low16str+decimalformat.format(d%60)+"分钟";
			
		}
		else{
			return "0";
		}
		return high12low16str;
	}

	public void setHigh12low16str(String high12low16str) {
		this.high12low16str = high12low16str;
	}

	public String getHigh16str() {
		double d=high16T;
		if(d!=0){
			if((int)d/60>0){
				high16str=(int)d/60+"小时";
			}
			DecimalFormat decimalformat = new DecimalFormat("0.0");
			
			high16str=high16str+decimalformat.format(d%60)+"分钟";
			
		}
		else{
			return "0";
		}
		return high16str;
	}

	public void setHigh16str(String high16str) {
		this.high16str = high16str;
	}

	public String getHigh4low8str() {
		double d=high4low8T;
		if(d!=0){
			if((int)d/60>0){
				high4low8str=(int)d/60+"小时";
			}
			DecimalFormat decimalformat = new DecimalFormat("0.0");
			
			high4low8str=high4low8str+decimalformat.format(d%60)+"分钟";
			
		}
		else{
			return "0";
		}
		return high4low8str;
	}

	public void setHigh4low8str(String high4low8str) {
		this.high4low8str = high4low8str;
	}

	public String getHigh8low12str() {
		double d=high8low12T;
		if(d!=0){
			if((int)d/60>0){
				high8low12str=(int)d/60+"小时";
			}
			DecimalFormat decimalformat = new DecimalFormat("0.0");
			
			high8low12str=high8low12str+decimalformat.format(d%60)+"分钟";
			
		}
		else{
			return "0";
		}
		return high8low12str;
		

	}

	public void setHigh8low12str(String high8low12str) {
		this.high8low12str = high8low12str;
	}

	public String getLow4str() {
		double d=low4T;
		if(d!=0){
			if((int)d/60>0){
				low4str=(int)d/60+"小时";
			}
			DecimalFormat decimalformat = new DecimalFormat("0.0");
			
			low4str=low4str+decimalformat.format(d%60)+"分钟";
			
		}
		else{
			return "0";
		}
		return low4str;
	}

	public void setLow4str(String low4str) {
		this.low4str = low4str;
	}
	
	
	
	
	
	
	

}