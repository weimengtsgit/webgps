package com.sosgps.wzt.orm;
// Generated by MyEclipse - Hibernate Tools

import java.util.Date;


/**
 * ShandongLogSend generated by MyEclipse - Hibernate Tools
 */
public class ShandongLogSend extends AbstractShandongLogSend implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public ShandongLogSend() {
    }

	/** minimal constructor */
    public ShandongLogSend(Date createTime, String desmobile, String content, Long ecode, Long rcode, String createSubTime, Long contentType, Long gwkind) {
        super(createTime, desmobile, content, ecode, rcode, createSubTime, contentType, gwkind);        
    }
    
    /** full constructor */
    public ShandongLogSend(Date createTime, String longid, String desmobile, String content, Long ecode, Long rcode, String sendid, Date reptTime, String createSubTime, Long contentType, Long gwkind, String city, String province, String hwyid, Long messagetype) {
        super(createTime, longid, desmobile, content, ecode, rcode, sendid, reptTime, createSubTime, contentType, gwkind, city, province, hwyid, messagetype);        
    }
   
}