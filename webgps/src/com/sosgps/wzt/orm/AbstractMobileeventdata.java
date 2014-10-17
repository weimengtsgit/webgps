package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * AbstractConfigure entity provides the base persistence definition of the
 * Configure entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractMobileeventdata implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;
    private Long id;
	private String type;
	private String deviceId;
	private Long state;
	private Date gpstime;
	private Date inputdate;
	private String termName;
	private String groupName;
	private String simcard;
	
	private TEnt TEnt;
	private TTerminal TTerminal;
	public AbstractMobileeventdata(){
		
	}
	public AbstractMobileeventdata(Long id,String type,String deviceId,Long state,Date gpstime,Date inputdate,TEnt TEnt,TTerminal TTerminal){
		this.id=id;
		this.type=type;
		this.deviceId=deviceId;
		this.state=state;
		this.gpstime=gpstime;
		this.inputdate=inputdate;
		this.TEnt=TEnt;
		this.TTerminal=TTerminal;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
	public Date getGpstime() {
		return gpstime;
	}
	public void setGpstime(Date gpstime) {
		this.gpstime = gpstime;
	}
	public Date getInputdate() {
		return inputdate;
	}
	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}
	public TEnt getTEnt() {
		return TEnt;
	}
	public void setTEnt(TEnt tEnt) {
		TEnt = tEnt;
	}
	public TTerminal getTTerminal() {
		return TTerminal;
	}
	public void setTTerminal(TTerminal tTerminal) {
		TTerminal = tTerminal;
	}
    
    public String getTermName() {
        return termName;
    }
    
    public void setTermName(String termName) {
        this.termName = termName;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getSimcard() {
        return simcard;
    }
    
    public void setSimcard(String simcard) {
        this.simcard = simcard;
    }
}