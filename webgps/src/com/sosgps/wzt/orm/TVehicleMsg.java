package com.sosgps.wzt.orm;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class TVehicleMsg extends AbstractTVehicleMsg
  implements Serializable
{
  public TVehicleMsg()
  {
  }

  public TVehicleMsg(String deviceId, String entCode, String model, String vehicleNum, Date saleDate, String saleMethods, String dealer, String installers, Long contractValue, Long loanAmount, Date repaymentPeriod, String claimAct, Date crtDate)
  {
    super(deviceId, entCode, model, vehicleNum, saleDate, 
      saleMethods, dealer, installers, contractValue, loanAmount, 
      repaymentPeriod, claimAct, crtDate);
  }
}