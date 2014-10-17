package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * 模块参数配置表
 * @author Administrator
 *
 */
public  class ModuleParamConfig implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;
    private Long id;
	private String name;
	private String content;
	private Date createOn;
	private String createBy;
	private Date LastUpdateOn;
	private String lastUpdateBy;
	private Integer type;
	private Integer states;
	
    public ModuleParamConfig() {}
    
    public ModuleParamConfig(String name, String content, Long createOn, String createBy,
            Long lastUpdateOn, String lastUpdateBy, Integer type, Integer states) {
        this.name = name;
        this.content = content;
        this.createBy = createBy;
        this.lastUpdateBy = lastUpdateBy;
        this.type = type;
        this.states = states;
    }


    
    public Long getId() {
        return id;
    }


    
    public void setId(Long id) {
        this.id = id;
    }


    
    public String getName() {
        return name;
    }


    
    public void setName(String name) {
        this.name = name;
    }


    
    public String getContent() {
        return content;
    }


    
    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateBy() {
        return createBy;
    }


    
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }


    public String getLastUpdateBy() {
        return lastUpdateBy;
    }


    public Date getCreateOn() {
        return createOn;
    }

    
    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    
    public Date getLastUpdateOn() {
        return LastUpdateOn;
    }

    
    public void setLastUpdateOn(Date lastUpdateOn) {
        LastUpdateOn = lastUpdateOn;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }


    
    public Integer getType() {
        return type;
    }


    
    public void setType(Integer type) {
        this.type = type;
    }


    
    public Integer getStates() {
        return states;
    }


    
    public void setStates(Integer states) {
        this.states = states;
    }
	
	

}