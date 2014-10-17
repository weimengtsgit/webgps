package com.sosgps.wzt.orm;

import java.util.Date;


/**
 * AbstractTStructions generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractTStructions  implements java.io.Serializable {


    // Fields    

     private Long id;
     private TTerminal TTerminal;
     private String instruction;
     private String state;
     private String type;
     private String param;
     private String reply;
     private Date createdate;
     private String createman;


    // Constructors

    /** default constructor */
    public AbstractTStructions() {
    }

    
    /** full constructor */
    public AbstractTStructions(TTerminal TTerminal, String instruction, String state, String type, String param, String reply, Date createdate, String createman) {
        this.TTerminal = TTerminal;
        this.instruction = instruction;
        this.state = state;
        this.type = type;
        this.param = param;
        this.reply = reply;
        this.createdate = createdate;
        this.createman = createman;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public TTerminal getTTerminal() {
        return this.TTerminal;
    }
    
    public void setTTerminal(TTerminal TTerminal) {
        this.TTerminal = TTerminal;
    }

    public String getInstruction() {
        return this.instruction;
    }
    
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getState() {
        return this.state;
    }
    
    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getParam() {
        return this.param;
    }
    
    public void setParam(String param) {
        this.param = param;
    }

    public String getReply() {
        return this.reply;
    }
    
    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getCreatedate() {
        return this.createdate;
    }
    
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getCreateman() {
        return this.createman;
    }
    
    public void setCreateman(String createman) {
        this.createman = createman;
    }
   








}