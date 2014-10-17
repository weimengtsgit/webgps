/**
 * CommonGatewayServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sos.lbsgateway.service;

public interface CommonGatewayServicePortType extends java.rmi.Remote {
    public com.sos.directl.base.TerminalInfo[] getAllOnlineTerms() throws java.rmi.RemoteException;
    public java.lang.String[] getAllOnlineTerminals() throws java.rmi.RemoteException;
    public boolean isOnLine(java.lang.String in0) throws java.rmi.RemoteException;
}
