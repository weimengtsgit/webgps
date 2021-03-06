package com.sosgps.wzt.orm;

import java.util.Date;


/**
 * AbstractTLbsLocrecord generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractTLbsLocrecord  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String simcard;
     private String x;
     private String y;
     private String entCode;
     private Date lbstime;
     private String errorCode;
     private String errorDesc;
     private String locDesc;
     private String lngx;
     private String laty;
     private Long type;


    // Constructors

    /** default constructor */
    public AbstractTLbsLocrecord() {
    }

    
    /** full constructor */
    public AbstractTLbsLocrecord(String simcard, String x, String y, String entCode, Date lbstime, String errorCode, String errorDesc, String locDesc, String lngx, String laty, Long type) {
        this.simcard = simcard;
        this.x = x;
        this.y = y;
        this.entCode = entCode;
        this.lbstime = lbstime;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.locDesc = locDesc;
        this.lngx = lngx;
        this.laty = laty;
        this.type = type;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getSimcard() {
        return this.simcard;
    }
    
    public void setSimcard(String simcard) {
        this.simcard = simcard;
    }

    public String getX() {
        return this.x;
    }
    
    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return this.y;
    }
    
    public void setY(String y) {
        this.y = y;
    }

    public String getEntCode() {
        return this.entCode;
    }
    
    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public Date getLbstime() {
        return this.lbstime;
    }
    
    public void setLbstime(Date lbstime) {
        this.lbstime = lbstime;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return this.errorDesc;
    }
    
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getLocDesc() {
        return this.locDesc;
    }
    
    public void setLocDesc(String locDesc) {
        this.locDesc = locDesc;
    }

    public String getLngx() {
        return this.lngx;
    }
    
    public void setLngx(String lngx) {
        this.lngx = lngx;
    }

    public String getLaty() {
        return this.laty;
    }
    
    public void setLaty(String laty) {
        this.laty = laty;
    }

    public Long getType() {
        return this.type;
    }
    
    public void setType(Long type) {
        this.type = type;
    }
   








}