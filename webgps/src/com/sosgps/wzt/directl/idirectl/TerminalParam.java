package  com.sosgps.wzt.directl.idirectl;
/**
 * 终端初始化参数类
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TerminalParam {
  private String simCard;//SIM卡号
  private String smsUser;//发送短信企业用户
  private String smsPassword;//发送短信企业密码
  private String protocolPwd;//终端通讯协议密码
  private String seriesNo;//终端系列号
  private boolean GPRSModal;//终端通讯协议
  private String oemCode;
  private String typeCode;//终端类型码
  
  /**
 * @return the typeCode
 */
public String getTypeCode() {
	return this.typeCode;
}
/**
 * @param typeCode the typeCode to set
 */
public void setTypeCode(String typeCode) {
	this.typeCode = typeCode;
}
public String getOemCode() {
	return this.oemCode;
}
public void setOemCode(String oemCode) {
	this.oemCode = oemCode;
}
public TerminalParam() {
  }
  public void setSimCard(String simCard) {
    this.simCard = simCard;
  }

  public void setSmsUser(String smsUser) {
    this.smsUser = smsUser;
  }

  public void setSmsPassword(String smsPassword) {
    this.smsPassword = smsPassword;
  }

  public void setProtocolPwd(String protocolPwd) {
    this.protocolPwd = protocolPwd;
  }

  public void setSeriesNo(String seriesNo) {
    this.seriesNo = seriesNo;
  }

  public void setGPRSModal(boolean isGPRSModal) {
    this.GPRSModal = isGPRSModal;
  }

  public String getSimCard() {
    return simCard;
  }

  public String getSmsUser() {
    return smsUser;
  }

  public String getSmsPassword() {
    return smsPassword;
  }

  public String getProtocolPwd() {
    return protocolPwd;
  }

  public String getSeriesNo() {
    return seriesNo;
  }

  public boolean getGPRSModal() {
    return GPRSModal;
  }
}
