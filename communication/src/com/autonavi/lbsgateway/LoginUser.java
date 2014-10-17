package com.autonavi.lbsgateway;

/**
 * µ«»Î”√ªß
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: www.mapabc.com</p>
 *
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class LoginUser {
  private String urid;
  private String urname;
  private String pssword;
  private String epid;
  private String ip;
  public LoginUser() {
  }
  public void setUrid(String urid) {
    this.urid = urid;
  }

  public void setUrname(String urname) {
    this.urname = urname;
  }

  public void setPssword(String pssword) {
    this.pssword = pssword;
  }

  public void setEpid(String epid) {
    this.epid = epid;
  }

  public String getUrid() {
    return urid;
  }

  public String getUrname() {
    return urname;
  }

  public String getPssword() {
    return pssword;
  }

  public String getEpid() {
    return epid;
  }
  public String getIP(){
    return this.ip;
  }
  public void setIP(String ip){
    this.ip=ip;
  }
}
