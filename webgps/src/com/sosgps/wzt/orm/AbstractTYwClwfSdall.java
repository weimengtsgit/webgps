package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * AbstractTYwClwfSdall generated by MyEclipse Persistence Tools
 */

public abstract class AbstractTYwClwfSdall implements java.io.Serializable {

	// Fields

	private Long wfid;

	private String xh;

	private String hpzl;

	private String hphm;

	private Date wfsj;

	private String wfdz;

	private String wfxw;

	private String cjjg;

	private String clbj;

	private Date clsj;

	private Date gxsj;

	private Date drsj;

	private String jdcsyr;

	private String dh;

	// Constructors

	/** default constructor */
	public AbstractTYwClwfSdall() {
	}

	/** full constructor */
	public AbstractTYwClwfSdall(String xh, String hpzl, String hphm, Date wfsj,
			String wfdz, String wfxw, String cjjg, String clbj, Date clsj,
			Date gxsj, Date drsj, String jdcsyr, String dh) {
		this.xh = xh;
		this.hpzl = hpzl;
		this.hphm = hphm;
		this.wfsj = wfsj;
		this.wfdz = wfdz;
		this.wfxw = wfxw;
		this.cjjg = cjjg;
		this.clbj = clbj;
		this.clsj = clsj;
		this.gxsj = gxsj;
		this.drsj = drsj;
		this.jdcsyr = jdcsyr;
		this.dh = dh;
	}

	// Property accessors

	public Long getWfid() {
		return this.wfid;
	}

	public void setWfid(Long wfid) {
		this.wfid = wfid;
	}

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getHpzl() {
		return this.hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getHphm() {
		return this.hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public Date getWfsj() {
		return this.wfsj;
	}

	public void setWfsj(Date wfsj) {
		this.wfsj = wfsj;
	}

	public String getWfdz() {
		return this.wfdz;
	}

	public void setWfdz(String wfdz) {
		this.wfdz = wfdz;
	}

	public String getWfxw() {
		return this.wfxw;
	}

	public void setWfxw(String wfxw) {
		this.wfxw = wfxw;
	}

	public String getCjjg() {
		return this.cjjg;
	}

	public void setCjjg(String cjjg) {
		this.cjjg = cjjg;
	}

	public String getClbj() {
		return this.clbj;
	}

	public void setClbj(String clbj) {
		this.clbj = clbj;
	}

	public Date getClsj() {
		return this.clsj;
	}

	public void setClsj(Date clsj) {
		this.clsj = clsj;
	}

	public Date getGxsj() {
		return this.gxsj;
	}

	public void setGxsj(Date gxsj) {
		this.gxsj = gxsj;
	}

	public Date getDrsj() {
		return this.drsj;
	}

	public void setDrsj(Date drsj) {
		this.drsj = drsj;
	}

	public String getJdcsyr() {
		return this.jdcsyr;
	}

	public void setJdcsyr(String jdcsyr) {
		this.jdcsyr = jdcsyr;
	}

	public String getDh() {
		return this.dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

}