package com.sosgps.v21.model;

/**
 * 企业相关配置实体
 * 
 * @author liuhx
 * 
 */
public class Kpi implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer type;// 0-目标维护模板类型,1-签单额达成率,2-回款额达成率,3-费用额使用率,4-员工出访达成率,5-客户拜访覆盖率

    private String key;

    private String value; //type = 0-value:0周,1旬,2月;1-value:60,90,160签单额仪表盘红黄绿刻度区间值;2-value:60,90,160回款额仪表盘红黄绿刻度区间值;

    private String entCode;

    private Long entId;

    private Integer states;

    private Long createOn;

    private String createBy;

    private Long lastUpdateOn;

    private String lastUpdateBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Integer getStates() {
        return states;
    }

    public void setStates(Integer states) {
        this.states = states;
    }

    public Long getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Long createOn) {
        this.createOn = createOn;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getLastUpdateOn() {
        return lastUpdateOn;
    }

    public void setLastUpdateOn(Long lastUpdateOn) {
        this.lastUpdateOn = lastUpdateOn;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
