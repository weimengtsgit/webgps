package com.autonavi.lbsgateway.service;

//import org.apache.axis.client.Call;
//import org.apache.axis.client.Service;
//import javax.xml.rpc.ServiceException;

import com.autonavi.directl.Config;

import java.rmi.RemoteException;

public class WebServiceClientUtil {
	private static String endpoint ="http://172.17.40.55:8888/SDUDPServer/services/CommonGatewayService";// Config.getInstance().getProperty(
			//"endpoint");

	private static String endpoint1 = Config.getInstance().getProperty(
			"endpoint1");

	public WebServiceClientUtil() {
		endpoint ="http://172.17.40.55:8888/SDUDPServer/services/CommonGatewayService";
	}

	/**
	 * webservice客户端调用方法
	 * 
	 * @param endpoint
	 *            String
	 * @param methodName
	 *            String
	 * @param uri
	 *            String
	 * @return Call
	 * @throws ServiceException
	 */
//	public static Call clientMethod(String endpoint, String methodName,
//			String uri) {
//		Call call = null;
//		Service service = new Service();
//		try {
//			call = (Call) service.createCall();
//			call.setTargetEndpointAddress(endpoint);
//			call.setOperationName(methodName);
//			call.setUseSOAPAction(true);
//			call.setSOAPActionURI(uri);
//
//		} catch (ServiceException ex) {
//			ex.printStackTrace();
//		}
//		return call;
//	}
//
//	public String[] getAllOnlineTerminals() {
//		String[] ret = null;
//		Call call = clientMethod(endpoint, "getAllOnlineTerminals", "");
//		call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_ARRAY);
//		try {
//			ret = (String[]) call.invoke(new Object[] {});
//			for (int i = 0; i < ret.length; i++) {
//				System.out.println(ret);
//			}
//		} catch (RemoteException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//		return ret;
//	}
//
//	public static void main(String[] args) {
//		WebServiceClientUtil c = new WebServiceClientUtil();
//		c.getAllOnlineTerminals();
//	}

}
