/**
 * 
 */
package com.sos;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.sos.lbsgateway.service.CommonGatewayServiceLocator;
import com.sos.lbsgateway.service.CommonGatewayServicePortType;
import com.sosgps.wzt.directl.Config;

/**
 * @author shiguang.zhou
 *
 */
public class TestWebService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonGatewayServiceLocator cgsl = new CommonGatewayServiceLocator();
		  java.lang.String CommonGatewayServiceHttpPort_address = Config.getInstance().getString("endpoint");
		cgsl.setCommonGatewayServiceHttpPortEndpointAddress(CommonGatewayServiceHttpPort_address);
		try {
			CommonGatewayServicePortType cgsp = cgsl.getCommonGatewayServiceHttpPort();

			 
			  String[] sims = cgsp.getAllOnlineTerminals();
			
			for (int i=0; i<sims.length; i++){
				System.out.println("-----------"+sims[i]);
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
