/**
 * TerminalInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sos.directl.base;

public class TerminalInfo  implements java.io.Serializable {
    private java.lang.String gpsSn;

    private java.lang.String linkway;

    private java.lang.String simcard;

    private java.lang.String termcode;

    private java.lang.String termcodeDesc;

    public TerminalInfo() {
    }

    public TerminalInfo(
           java.lang.String gpsSn,
           java.lang.String linkway,
           java.lang.String simcard,
           java.lang.String termcode,
           java.lang.String termcodeDesc) {
           this.gpsSn = gpsSn;
           this.linkway = linkway;
           this.simcard = simcard;
           this.termcode = termcode;
           this.termcodeDesc = termcodeDesc;
    }


    /**
     * Gets the gpsSn value for this TerminalInfo.
     * 
     * @return gpsSn
     */
    public java.lang.String getGpsSn() {
        return gpsSn;
    }


    /**
     * Sets the gpsSn value for this TerminalInfo.
     * 
     * @param gpsSn
     */
    public void setGpsSn(java.lang.String gpsSn) {
        this.gpsSn = gpsSn;
    }


    /**
     * Gets the linkway value for this TerminalInfo.
     * 
     * @return linkway
     */
    public java.lang.String getLinkway() {
        return linkway;
    }


    /**
     * Sets the linkway value for this TerminalInfo.
     * 
     * @param linkway
     */
    public void setLinkway(java.lang.String linkway) {
        this.linkway = linkway;
    }


    /**
     * Gets the simcard value for this TerminalInfo.
     * 
     * @return simcard
     */
    public java.lang.String getSimcard() {
        return simcard;
    }


    /**
     * Sets the simcard value for this TerminalInfo.
     * 
     * @param simcard
     */
    public void setSimcard(java.lang.String simcard) {
        this.simcard = simcard;
    }


    /**
     * Gets the termcode value for this TerminalInfo.
     * 
     * @return termcode
     */
    public java.lang.String getTermcode() {
        return termcode;
    }


    /**
     * Sets the termcode value for this TerminalInfo.
     * 
     * @param termcode
     */
    public void setTermcode(java.lang.String termcode) {
        this.termcode = termcode;
    }


    /**
     * Gets the termcodeDesc value for this TerminalInfo.
     * 
     * @return termcodeDesc
     */
    public java.lang.String getTermcodeDesc() {
        return termcodeDesc;
    }


    /**
     * Sets the termcodeDesc value for this TerminalInfo.
     * 
     * @param termcodeDesc
     */
    public void setTermcodeDesc(java.lang.String termcodeDesc) {
        this.termcodeDesc = termcodeDesc;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TerminalInfo)) return false;
        TerminalInfo other = (TerminalInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.gpsSn==null && other.getGpsSn()==null) || 
             (this.gpsSn!=null &&
              this.gpsSn.equals(other.getGpsSn()))) &&
            ((this.linkway==null && other.getLinkway()==null) || 
             (this.linkway!=null &&
              this.linkway.equals(other.getLinkway()))) &&
            ((this.simcard==null && other.getSimcard()==null) || 
             (this.simcard!=null &&
              this.simcard.equals(other.getSimcard()))) &&
            ((this.termcode==null && other.getTermcode()==null) || 
             (this.termcode!=null &&
              this.termcode.equals(other.getTermcode()))) &&
            ((this.termcodeDesc==null && other.getTermcodeDesc()==null) || 
             (this.termcodeDesc!=null &&
              this.termcodeDesc.equals(other.getTermcodeDesc())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getGpsSn() != null) {
            _hashCode += getGpsSn().hashCode();
        }
        if (getLinkway() != null) {
            _hashCode += getLinkway().hashCode();
        }
        if (getSimcard() != null) {
            _hashCode += getSimcard().hashCode();
        }
        if (getTermcode() != null) {
            _hashCode += getTermcode().hashCode();
        }
        if (getTermcodeDesc() != null) {
            _hashCode += getTermcodeDesc().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TerminalInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://base.directl.sos.com", "TerminalInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gpsSn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://base.directl.sos.com", "gpsSn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkway");
        elemField.setXmlName(new javax.xml.namespace.QName("http://base.directl.sos.com", "linkway"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("simcard");
        elemField.setXmlName(new javax.xml.namespace.QName("http://base.directl.sos.com", "simcard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("termcode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://base.directl.sos.com", "termcode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("termcodeDesc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://base.directl.sos.com", "termcodeDesc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
