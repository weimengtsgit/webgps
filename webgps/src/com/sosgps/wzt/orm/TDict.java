package com.sosgps.wzt.orm;
// Generated by MyEclipse - Hibernate Tools

import java.util.Set;


/**
 * TDict generated by MyEclipse - Hibernate Tools
 */
public class TDict extends AbstractTDict implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TDict() {
    }

	/** minimal constructor */
    public TDict(String dictName, String dictCode) {
        super(dictName, dictCode);        
    }
    
    /** full constructor */
    public TDict(String dictName, String dictCode, String usageFlag, Set TDictItems) {
        super(dictName, dictCode, usageFlag, TDictItems);        
    }
   
}
