package com.sosgps.wzt.orm;
// Generated by MyEclipse - Hibernate Tools

import java.util.Date;


/**
 * TAlarmTjYear generated by MyEclipse - Hibernate Tools
 */
public class TAlarmTjYear extends AbstractTAlarmTjYear implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TAlarmTjYear() {
    }

    
    /** full constructor */
    public TAlarmTjYear(String deviceId, Long speedAlarm, Long inactiveAlarm, Long areaAlarm, Long lineAlarm, Date tjdate, Long emerAlarm, Long callRecord, Long shortmessRecord) {
        super(deviceId, speedAlarm, inactiveAlarm, areaAlarm, lineAlarm, tjdate, emerAlarm, callRecord, shortmessRecord);        
    }
   
}
