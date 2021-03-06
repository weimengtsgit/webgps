package com.sosgps.wzt.orm;

import java.util.HashSet;
import java.util.Set;


/**
 * AbstractTDict generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractTDict  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String dictName;
     private String dictCode;
     private String usageFlag;
     private Set TDictItems = new HashSet(0);


    // Constructors

    /** default constructor */
    public AbstractTDict() {
    }

	/** minimal constructor */
    public AbstractTDict(String dictName, String dictCode) {
        this.dictName = dictName;
        this.dictCode = dictCode;
    }
    
    /** full constructor */
    public AbstractTDict(String dictName, String dictCode, String usageFlag, Set TDictItems) {
        this.dictName = dictName;
        this.dictCode = dictCode;
        this.usageFlag = usageFlag;
        this.TDictItems = TDictItems;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getDictName() {
        return this.dictName;
    }
    
    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictCode() {
        return this.dictCode;
    }
    
    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getUsageFlag() {
        return this.usageFlag;
    }
    
    public void setUsageFlag(String usageFlag) {
        this.usageFlag = usageFlag;
    }

    public Set getTDictItems() {
        return this.TDictItems;
    }
    
    public void setTDictItems(Set TDictItems) {
        this.TDictItems = TDictItems;
    }
   








}