package com.sos.lbsgateway.service;

public class CommonGatewayServicePortTypeProxy implements com.sos.lbsgateway.service.CommonGatewayServicePortType {
  private String _endpoint = null;
  private com.sos.lbsgateway.service.CommonGatewayServicePortType commonGatewayServicePortType = null;
  
  public CommonGatewayServicePortTypeProxy() {
    _initCommonGatewayServicePortTypeProxy();
  }
  
  public CommonGatewayServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initCommonGatewayServicePortTypeProxy();
  }
  
  private void _initCommonGatewayServicePortTypeProxy() {
    try {
      commonGatewayServicePortType = (new com.sos.lbsgateway.service.CommonGatewayServiceLocator()).getCommonGatewayServiceHttpPort();
      if (commonGatewayServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)commonGatewayServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)commonGatewayServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (commonGatewayServicePortType != null)
      ((javax.xml.rpc.Stub)commonGatewayServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sos.lbsgateway.service.CommonGatewayServicePortType getCommonGatewayServicePortType() {
    if (commonGatewayServicePortType == null)
      _initCommonGatewayServicePortTypeProxy();
    return commonGatewayServicePortType;
  }
  
  public com.sos.directl.base.TerminalInfo[] getAllOnlineTerms() throws java.rmi.RemoteException{
    if (commonGatewayServicePortType == null)
      _initCommonGatewayServicePortTypeProxy();
    return commonGatewayServicePortType.getAllOnlineTerms();
  }
  
  public java.lang.String[] getAllOnlineTerminals() throws java.rmi.RemoteException{
    if (commonGatewayServicePortType == null)
      _initCommonGatewayServicePortTypeProxy();
    return commonGatewayServicePortType.getAllOnlineTerminals();
  }
  
  public boolean isOnLine(java.lang.String in0) throws java.rmi.RemoteException{
    if (commonGatewayServicePortType == null)
      _initCommonGatewayServicePortTypeProxy();
    return commonGatewayServicePortType.isOnLine(in0);
  }
  
  
}