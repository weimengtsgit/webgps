package com.autonavi.lbsgateway.service;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;


public class Route {
	
	
	
	
	public byte[] getRouteBytes(String startX,String startY,String endX,String endY, String[] viaXS,String [] viaYS ){
		String routeService="";
		try{
			routeService=Config.getInstance().getString("routeService");
		}catch(Exception ex){
			routeService="http://60.247.103.20:7140/route";
		}
		if(routeService==null || routeService.trim().length()<=0){
			routeService="http://60.247.103.20:7140/route";
		}
		
		try{
			String requestxml="";
			String viaXml="";
			if(startX!=null && startX.trim().length()>0 && startY!=null && startY.trim().length()>0  ){
				if(viaXS!=null && viaXS.length>0 && viaYS!=null && viaYS.length>0 ){
					for(int i=0;i<viaXS.length;i++ ){
						if(viaXS[i]!=null && viaXS[i].trim().length()>0 &viaYS[i]!=null && viaYS[i].trim().length()>0    ){
							viaXml+="<viapoint><x>"+viaXS[i]+"</x><y>"+viaYS[i]+"</y></viapoint>";
						}
					}
				}		
				requestxml="<route Type=0 Flag=1966539 Vers=2.0><startpoint><x>"+startX+"</x><y>"+startY+"</y></startpoint>"+viaXml+"<endpoint><x>"+endX+"</x><y>"+endY+"</y></endpoint></route>";
				byte[] route;
				route = HttpUtil.getPostURLData(routeService, requestxml.getBytes());
				return route;
			}else{
				 Log.getInstance().outLog("endPoint = NULL OR endPoint=null");
			}
		
		}catch (Exception ex){
			 Log.getInstance().errorLog("请求导航路径出现异常:"+ ex.getMessage(),ex);
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param viaXS
	 * @param viaYS
	 * @return
	 */
	public RouteParse requestNaviCoords(String startX,String startY,String endX,String endY, String[] viaXS,String [] viaYS ){
		String routeService="";
		try{
			routeService=Config.getInstance().getString("routeService");
		}catch(Exception ex){
			routeService="http://60.247.103.20:7140/route";
		}
		if(routeService==null || routeService.trim().length()<=0){
			routeService="http://60.247.103.20:7140/route";
		}
		
		RouteParse routeParse;
		try{
			String requestxml="";
			String viaXml="";
		if(startX!=null && startX.trim().length()>0 && startY!=null && startY.trim().length()>0  ){
			if(viaXS!=null && viaXS.length>0 && viaYS!=null && viaYS.length>0 ){
				for(int i=0;i<viaXS.length;i++ ){
					if(viaXS[i]!=null && viaXS[i].trim().length()>0 &viaYS[i]!=null && viaYS[i].trim().length()>0    ){
						viaXml+="<viapoint><x>"+viaXS[i]+"</x><y>"+viaYS[i]+"</y></viapoint>";
					}
				}
			}		
			requestxml="<route Type=0 Flag=1966539 Dtinfo=0><startpoint><x>"+startX+"</x><y>"+startY+"</y></startpoint>"+viaXml+"<endpoint><x>"+endX+"</x><y>"+endY+"</y></endpoint>";
			byte[] route;
			route = HttpUtil.getPostURLData(routeService, requestxml.getBytes());
			routeParse=new RouteParse();
			routeParse.parse(route);
			return routeParse;
		}else{
			System.out.println("endPoint = NULL OR endPoint=null");
		}
		
		}catch (Exception ex){
			System.out.println("请求导航路径出现异常:"+ ex.getMessage());
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args){
		Route route = new Route();
		String startX="JIOMSPKQRLHDL";
		String startY="LQGXVSHOHHLH";		
		String endX="JIOMSRONOLLHL";
		String endY="LQGXXSHUHDLD";	
		String[] viaXS={"JIOMSQQNKHLHH","JIOMSSHVNLLDL"};
		String[] viaYS={"LQGXVSOVDLHL","LQGXVTMSLLDL"};	
		
		RouteParse rp=	route.requestNaviCoords(startX, startY, endX, endY, viaXS, viaYS);
		String eny=rp.getEnLatitude();
		String enx=rp.getEnLongitude();
		System.out.println(eny);
		
		
		
	}
	
	
	

}
