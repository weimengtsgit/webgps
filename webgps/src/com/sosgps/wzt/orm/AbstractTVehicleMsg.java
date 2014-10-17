package com.sosgps.wzt.orm;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public abstract class AbstractTVehicleMsg
  implements Serializable
{
  private Long id;
  private String deviceId;
  private String entCode;
  private String model;
  private String vehicleNum;
  private Date saleDate;
  private String saleMethods;
  private String dealer;
  private String installers;
  private Long contractValue;
  private Long loanAmount;
  private Date repaymentPeriod;
  private String claimAct;
  private Date crtDate;

  public Long getId()
  {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AbstractTVehicleMsg(String deviceId, String entCode, String model, String vehicleNum, Date saleDate, String saleMethods, String dealer, String installers, Long contractValue, Long loanAmount, Date repaymentPeriod, String claimAct, Date crtDate)
  {
    this.deviceId = deviceId;
    this.entCode = entCode;
    this.model = model;
    this.vehicleNum = vehicleNum;
    this.saleDate = saleDate;
    this.saleMethods = saleMethods;
    this.dealer = dealer;
    this.installers = installers;
    this.contractValue = contractValue;
    this.loanAmount = loanAmount;
    this.repaymentPeriod = repaymentPeriod;
    this.claimAct = claimAct;
    this.crtDate = crtDate;
  }

  public String getDeviceId() {
    return this.deviceId;
  }

  public void setDeviceId(String deviceId)
  {
    this.deviceId = deviceId;
  }

  public String getEntCode()
  {
    return this.entCode;
  }

  public void setEntCode(String entCode)
  {
    this.entCode = entCode;
  }

  public String getModel()
  {
    return this.model;
  }

  public void setModel(String model)
  {
    this.model = model;
  }

  public String getVehicleNum()
  {
    return this.vehicleNum;
  }

  public void setVehicleNum(String vehicleNum)
  {
    this.vehicleNum = vehicleNum;
  }

  public Date getSaleDate()
  {
    return this.saleDate;
  }

  public void setSaleDate(Date saleDate)
  {
    this.saleDate = saleDate;
  }

  public String getSaleMethods()
  {
    return this.saleMethods;
  }

  public void setSaleMethods(String saleMethods)
  {
    this.saleMethods = saleMethods;
  }

  public String getDealer()
  {
    return this.dealer;
  }

  public void setDealer(String dealer)
  {
    this.dealer = dealer;
  }

  public String getInstallers()
  {
    return this.installers;
  }

  public void setInstallers(String installers)
  {
    this.installers = installers;
  }

  public Long getContractValue()
  {
    return this.contractValue;
  }

  public void setContractValue(Long contractValue)
  {
    this.contractValue = contractValue;
  }

  public Long getLoanAmount()
  {
    return this.loanAmount;
  }

  public void setLoanAmount(Long loanAmount)
  {
    this.loanAmount = loanAmount;
  }

  public Date getRepaymentPeriod()
  {
    return this.repaymentPeriod;
  }

  public void setRepaymentPeriod(Date repaymentPeriod)
  {
    this.repaymentPeriod = repaymentPeriod;
  }

  public String getClaimAct()
  {
    return this.claimAct;
  }

  public void setClaimAct(String claimAct)
  {
    this.claimAct = claimAct;
  }

  public Date getCrtDate()
  {
    return this.crtDate;
  }

  public void setCrtDate(Date crtDate)
  {
    this.crtDate = crtDate;
  }

  public AbstractTVehicleMsg()
  {
  }
}