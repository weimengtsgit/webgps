package com.sosgps.wzt.orm;

import java.io.Serializable;
import java.util.Date;

/**
 * 移植客户系统
 * @author Administrator
 *
 */
public class TMobileeventdata extends AbstractMobileeventdata implements
		Serializable {
	
    private static final long serialVersionUID = 1L;
    public TMobileeventdata(){};
	public TMobileeventdata(Long id,String type,String deviceId,Long state,Date gpstime,Date inputdate,TEnt TEnt,TTerminal TTerminal){
		super(id,type,deviceId,state,gpstime,inputdate,TEnt,TTerminal);
	};
}
