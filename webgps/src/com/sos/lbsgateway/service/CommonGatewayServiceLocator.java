/**
 * CommonGatewayServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sos.lbsgateway.service;

import com.sosgps.wzt.directl.Config;

public class CommonGatewayServiceLocator extends org.apache.axis.client.Service implements com.sos.lbsgateway.service.CommonGatewayService {

    public CommonGatewayServiceLocator() {
    }


    public CommonGatewayServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CommonGatewayServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CommonGatewayServiceHttpPort
    private java.lang.String CommonGatewayServiceHttpPort_address = Config.getInstance().getString("endpoint");//"http://172.17.40.55:8888/SDUDPServer/services/CommonGatewayService";

    public java.lang.String getCommonGatewayServiceHttpPortAddress() {
        return CommonGatewayServiceHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CommonGatewayServiceHttpPortWSDDServiceName = "CommonGatewayServiceHttpPort";

    public java.lang.String getCommonGatewayServiceHttpPortWSDDServiceName() {
        return CommonGatewayServiceHttpPortWSDDServiceName;
    }

    public void setCommonGatewayServiceHttpPortWSDDServiceName(java.lang.String name) {
        CommonGatewayServiceHttpPortWSDDServiceName = name;
    }

    public com.sos.lbsgateway.service.CommonGatewayServicePortType getCommonGatewayServiceHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CommonGatewayServiceHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCommonGatewayServiceHttpPort(endpoint);
    }

    public com.sos.lbsgateway.service.CommonGatewayServicePortType getCommonGatewayServiceHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sos.lbsgateway.service.CommonGatewayServiceHttpBindingStub _stub = new com.sos.lbsgateway.service.CommonGatewayServiceHttpBindingStub(portAddress, this);
            _stub.setPortName(getCommonGatewayServiceHttpPortWSDDServiceName());
            return _stub;
        } 
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCommonGatewayServiceHttpPortEndpointAddress(java.lang.String address) {
        CommonGatewayServiceHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sos.lbsgateway.service.CommonGatewayServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sos.lbsgateway.service.CommonGatewayServiceHttpBindingStub _stub = new com.sos.lbsgateway.service.CommonGatewayServiceHttpBindingStub(new java.net.URL(CommonGatewayServiceHttpPort_address), this);
                _stub.setPortName(getCommonGatewayServiceHttpPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CommonGatewayServiceHttpPort".equals(inputPortName)) {
            return getCommonGatewayServiceHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.lbsgateway.sos.com", "CommonGatewayService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.lbsgateway.sos.com", "CommonGatewayServiceHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CommonGatewayServiceHttpPort".equals(portName)) {
            setCommonGatewayServiceHttpPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
