package com.sosgps.wzt.terminal.dao;

import com.sosgps.wzt.orm.TVehicleMsg;

public abstract interface VehicleMsgDAO
{
  public abstract void save(TVehicleMsg paramTVehicleMsg);

  public abstract void update(TVehicleMsg paramTVehicleMsg);

  public abstract void delete(TVehicleMsg paramTVehicleMsg);
}