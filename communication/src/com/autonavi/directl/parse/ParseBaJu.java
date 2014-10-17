package com.autonavi.directl.parse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.lbsgateway.GBLTerminalList;
 
//长虹项目协议解析
public class ParseBaJu extends ParseBase {
  
//<?xml version="1.0" encoding="GB2312" ?>
//	<mapabc version="1.0">
//		<request userId="13855961414" swId="13855961414">
//			<posReportRequest>
//				<posReportList>
//					<posReport>
//						<pos>
//						<y>144263008</y>
//						<x>418734208</x>
//						</pos>
//					<speed>43.9</speed>
//					<status>0</status>
//					<direction>208</direction>
//					<time>2009-04-21 08:50:06</time>
//					</posReport>
//				</posReportList>
//			</posReportRequest>
//			</request>
//	</mapabc>
	@Override
	public void parseGPRS(String hexString) {
		hexString=hexString.substring(8);//删除baju
		byte[] bs = Tools.fromHexString(hexString);
		//String content= new String(bs);
	    SAXBuilder sb = new SAXBuilder();
	    try {
	      InputStream is = new ByteArrayInputStream(bs);
	      Document doc = sb.build(is);
	      Element root = doc.getRootElement();
	      Element eRequest = root.getChild("request");
	      String deviceId = eRequest.getAttributeValue("userId");
	      String phnum = GBLTerminalList.getInstance().getSimcardNum(deviceId);
	      if (phnum == null || hexString == null
					|| phnum.trim().length() == 0
					|| hexString.trim().length() == 0) {
	    	  Log.getInstance().outJHSLoger("系统中没有适配到指定的终端：device_id="+deviceId);
				return;
			}
	      this.setPhnum(phnum);
	      this.setDeviceSN(deviceId);
	      
	      Element posReq = eRequest.getChild("posReportRequest");
	      Element posList = posReq.getChild("posReportList");
	      Element posReport = posList.getChild("posReport");
	      Element pos = posReport.getChild("pos");
	      String x = pos.getChild("x").getTextTrim();
	      String y = pos.getChildText("y");
	      float lng = Float.parseFloat(x) / 3600000f;
          float lat = Float.parseFloat(y) / 3600000f;
//          com.mapabc.geom.CoordCvtAPI cca = new com.mapabc.geom.CoordCvtAPI();
//          com.mapabc.geom.DPoint point = cca.decryptConvert(lng, lat);
//          float  yx = (float)point.getX();
//          float yy = (float)point.getY();
          
	      String s = posReport.getChildText("speed");
	      String status = posReport.getChildText("status");
	      String direction = posReport.getChildText("direction");
	      String time = posReport.getChildText("time");
	       
 	      this.setCoordX(lng+"");
	      this.setCoordY(lat+"");
	      this.setTime(time);
	      this.setSpeed(s);
	      this.setDirection(direction);
	      this.sentPost(true);
	      Log.getInstance().outJHSLoger(this.getDeviceSN()+"交互式导航位置信息：x="+lng+",y="+lat+",time="+time);
	    }
	    catch (Exception ex) {
	    	System.out.println(ex.getMessage());
	    	Log.getInstance().outLog("debug_info_1004"+ex.getMessage());
	    	ex.printStackTrace();
	      
	    }		

	}

	@Override
	public void parseSMS(String phnum, String content) {
		// TODO 自动生成方法存根

	}
	
	public static void main(String[] args){
		ParseBaJu bj = new ParseBaJu();
		String s = "<?xml version=\"1.0\" encoding=\"GB2312\" ?><mapabc version=\"1.0\"><request userId=\"13855961414\" swId=\"13855961414\"><posReportRequest><posReportList><posReport><pos><y>144263008</y><x>418734208</x></pos><speed>43.9</speed><status>0</status><direction>208</direction><time>2009-04-21 08:50:06</time></posReport></posReportList></posReportRequest></request></mapabc>";
		String hexString = Tools.bytesToHexString(s.getBytes());
		bj.parseGPRS(hexString);
	}

}
