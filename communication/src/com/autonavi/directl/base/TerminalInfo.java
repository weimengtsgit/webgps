package com.autonavi.directl.base;

/**
 * <p>Title: GPS网关</p>
 *
 * <p>Description: 终端实体类</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: www.mapabc.com</p>
 *
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class TerminalInfo {
  private String gpsSn;
  private String simcard;
  private String termcode;
  private String linkway;
  private String termcodeDesc;
  public TerminalInfo() {
  }

  public void setGpsSn(String gpsSn) {
    this.gpsSn = gpsSn;
  }

  public void setSimcard(String simcard) {
    this.simcard = simcard;
  }

  public void setTermcode(String termcode) {
    this.termcode = termcode;
  }

  public void setLinkway(String linkway) {
    this.linkway = linkway;
  }

  public void setTermcodeDesc(String termcodeDesc) {
    this.termcodeDesc = termcodeDesc;
  }

  public String getGpsSn() {
    return gpsSn;
  }

  public String getSimcard() {
    return simcard;
  }

  public String getTermcode() {
    return termcode;
  }

  public String getLinkway() {
    return linkway;
  }

  public String getTermcodeDesc() {
    return termcodeDesc;
  }
}
