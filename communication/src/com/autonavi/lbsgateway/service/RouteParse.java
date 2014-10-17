package com.autonavi.lbsgateway.service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.text.DecimalFormat; 
/**
 * ����������������
 * @author jiang
 *
 */
public class RouteParse {
	
	HeadInfo headInfo =new HeadInfo();
	BodyInfo bodyInfo = new BodyInfo();
	byte[] naviData;
	private int cur=0;
	private ArrayList longitudeList =new ArrayList();
	private ArrayList latitudeList =new ArrayList();
	private void setNaviData(byte[] bs){
		naviData = new byte[bs.length];
		ByteBuffer bf = ByteBuffer.allocate(bs.length);
		bf.clear();
		bf.put(bs);
		bf.flip();
		bf.get(naviData, 0, bs.length);
	}
	
	
	public byte[] getNaviData(){
		return  naviData;
	}
	
	private class HeadInfo{
		 short CRC16 ;
		 short version;
		 int ds;
	}
	
	private class BodyInfo{
		int ds ;
		int distance ;
		int runTime ;
		ArrayList naviInfoList= new ArrayList();
	}
	private String getEnCoord(double coord){
		String ret=null;
		try{
			ret=com.mapabc.encrptor.EncrptorFactory.getInstance().getStrFormCoordinate(coord, 0);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return ret;
	}
	
	public String getEnLongitude(){
		String strLongitude="";
		for(int i=0;i<longitudeList.size();i++){
			double tempx=Double.parseDouble(longitudeList.get(i).toString());
			strLongitude+=getEnCoord(tempx)+",";
		}
		if(strLongitude.trim().length()>0){
			strLongitude=strLongitude.substring(0,strLongitude.length()-1);
		}
		
		return strLongitude;
	}
	
	
	public String getEnLatitude(){
		String strLongitude="";
		for(int i=0;i<latitudeList.size();i++){
			double tempx=Double.parseDouble(latitudeList.get(i).toString());
			strLongitude+=getEnCoord(tempx)+",";
		}
		if(strLongitude.trim().length()>0){
			strLongitude=strLongitude.substring(0,strLongitude.length()-1);
		}
		return strLongitude;
	}
	

	public double[] getNaviLongitude(){
		double[] dblLongitude=new double[longitudeList.size()];
		for(int i=0;i<longitudeList.size();i++){
			dblLongitude[i]=Double.parseDouble(longitudeList.get(i).toString());
		}
		return dblLongitude;
	}
	
	public double[] getNaviLatitude(){
		double[] dblLatitude=new double[latitudeList.size()];
		
		for(int i=0;i<latitudeList.size();i++){
			dblLatitude[i]=Double.parseDouble(latitudeList.get(i).toString());
		}
		return dblLatitude;
	}
	
	
	//�ֶε�����Ϣ
	private class FDNaviInfo
	{
		short index;
		short length;
		ArrayList roadDetail= new ArrayList();
		int baseNaviInfo;
		ArrayList naviDetail= new ArrayList();
	}
	
	private class NaviInfo{
		int naviFlag;
		int soundID;
		int pictureID;
		int flashID;
		byte[] bsText;
		byte[] cdFX;
		byte[] mbInfo;
		byte[] rkjt;
		byte[] jtaq;
		
	}
	
	
	
	private class Road{
		short startIndex;
		String name;
	}
	private class RoadType{
		int startIndex;
		String name; 
	}
	private class RoadInfo{
		public short roadFlag;

		public String time;
		public String DJ;
		public String FX;
		public short pointCount;
		public double[] xs;
		public double[] ys;
		public int roadCount;
		public ArrayList RoadList = new ArrayList();
		public int fromWayCount;
		public ArrayList RoadTypeList = new ArrayList();
		
		public int cds;
		public String cdInfo;
		public byte[] RKbmpID;
		public String RKInfo;
	}

	
	
	public static String bytesToHexString(byte[] bs) {
		String s = "";
		for (int i = 0; i < bs.length; i++) {
			String tmp = Integer.toHexString(bs[i] & 0xff);
			if (tmp.length() < 2) {
				tmp = "0" + tmp;
			}
			s = s + tmp;
		}
		return s;
	}
	public static String bytes2BinaryString(byte[] bs) {
		String ret = "";
		for (int i = 0; i < bs.length; i++) {
		
			byte b = bs[i];
			String tmp = Integer.toBinaryString(b);
			while (tmp.length() < 8) {
				tmp = "0" + tmp;
			}
			ret = ret + tmp;
		}
		return ret;
	}
	
	public static long byte2Long(byte[] decBytes) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < decBytes.length; i++) {
			String s = Integer.toHexString(decBytes[i] & 0xFF).toUpperCase();
			if (s.length() < 2) {
				s = "0" + s;
			}
			str.append(s);
		}
		long ret = Long.parseLong(str.toString(), 16);
		
		return ret;
	}
	
	private short bytesToShort(byte[] bs){
		ByteBuffer bf = ByteBuffer.allocate(bs.length);
		bf.clear();
		bf.put(bs);
		bf.flip();
		return bf.getShort();
	}
	
	private int bytesToInt(byte[] bs){
		ByteBuffer bf = ByteBuffer.allocate(bs.length);
		bf.clear();
		bf.put(bs);
		bf.flip();
		return bf.getInt();
	}
	
	private short getShortFromNaviData(){
		byte[] tempbs = new byte[2];
		byte[] dest = new byte[2];
		System.arraycopy(this.naviData, cur, tempbs, 0, tempbs.length);
		for(int i=0;i<tempbs.length;i++  ){
			dest[i]=tempbs[tempbs.length-(i+1)];
		}
		cur+=dest.length;
		return this.bytesToShort(dest);
	}
	private int getIntFromNaviData(){
		byte[] tempbs = new byte[4];
		byte[] dest = new byte[4];
		System.arraycopy(this.naviData, cur, tempbs, 0, tempbs.length);
		for(int i=0;i<tempbs.length;i++  ){
			dest[i]=tempbs[tempbs.length-(i+1)];
		}
		cur+=dest.length;
		return this.bytesToInt(dest);
	}	
	private byte[] getBytesFormNaviData( int len ){
		byte[] dest = new byte[len];
		System.arraycopy(this.naviData, cur, dest, 0,len);
		cur+=dest.length;
		return dest;
	}
	
	
	  public static String getStr(String str) {
		    try {
		      String temp_p = str;
		      byte[] temp_t = temp_p.getBytes("ISO8859-1");
		      String temp = new String(temp_t, "GBK");
		      return temp;
		    }
		    catch (Exception e) {
		    }
		    return "null";
		  }
	
	
	
	
	
	
	
	
	public void parse(byte[] routebs){
		longitudeList.clear();
		latitudeList.clear();
		this.setNaviData(routebs);
		headInfo.CRC16 =this.getShortFromNaviData();
		headInfo.version=this.getShortFromNaviData();
		headInfo.ds=this.getIntFromNaviData();
		bodyInfo.ds= this.getIntFromNaviData();
		bodyInfo.distance=this.getIntFromNaviData();
		bodyInfo.runTime=this.getIntFromNaviData();
		while( cur < routebs.length){
		FDNaviInfo fdNaviInfo = new FDNaviInfo();
		fdNaviInfo.index= this.getShortFromNaviData();
		fdNaviInfo.length=this.getShortFromNaviData();		
		RoadInfo roadInfo = new RoadInfo();
		roadInfo.roadFlag =this.getShortFromNaviData();
		//��ʻʱ��
		 if( (	roadInfo.roadFlag & 0x0001 ) ==0x0001 ){
			byte[] bs= getBytesFormNaviData(1);
			String binaryString=bytes2BinaryString(bs);
			String dw="";
			String tempBinary="";
			if( binaryString.charAt(0)=='0'   ){
				dw="����";
			}else{
				dw="Сʱ";
			}
			tempBinary="0"+binaryString.substring(1);
			roadInfo.time=Integer.parseInt(tempBinary, 2)+dw;
			//System.out.println("��ʹʱ��:"+roadInfo.time );
		 }
		 
		 
		 //�ȼ�����
		 if( (	roadInfo.roadFlag & 0x0002 ) ==0x0002 ){
				byte[] bs= getBytesFormNaviData(1);
				String binaryString=bytes2BinaryString(bs);
				String sPart1="0000"+binaryString.substring(0,4)  ;
				String sPart2="0000"+binaryString.substring(4)  ;
				int bPart1= Integer.parseInt(sPart1, 2);
				int bPart2= Integer.parseInt(sPart2, 2);
				if ( ( bPart1 & 0x01)==0x01 ){
					roadInfo.DJ="���ٹ�·";
				}else if ( ( bPart1 & 0x02)==0x02 ){
					roadInfo.DJ="����";
				}else if ( ( bPart1 & 0x03)==0x03 ){
					roadInfo.DJ="ʡ��";
				}else if ( ( bPart1 & 0x04)==0x04 ){
					roadInfo.DJ="�繫·";
				}else if ( ( bPart1 & 0x05)==0x05 ){
					roadInfo.DJ="������ڲ���·";
				}else if ( ( bPart1 & 0x06)==0x06 ){
					roadInfo.DJ="��Ҫ��֡����п���·";
				}else if ( ( bPart1 & 0x07)==0x07 ){
					roadInfo.DJ="��Ҫ��·";
				}else if ( ( bPart1 & 0x08)==0x08 ){
					roadInfo.DJ="��Ҫ��·";
				}else if ( ( bPart1 & 0x09)==0x09 ){
					roadInfo.DJ="��ͨ��·";
				}else if ( ( bPart1 & 0x0A)==0x0A ){
					roadInfo.DJ="�ص�";
				}

				if ( ( bPart2 & 0x01)==0x01 ){
					roadInfo.FX="��";
				}else if ( ( bPart2 & 0x02)==0x02 ){
					roadInfo.FX="����";
				}else if ( ( bPart2 & 0x03)==0x03 ){
					roadInfo.FX="��";
				}else if ( ( bPart2 & 0x04)==0x04 ){
					roadInfo.FX="����";
				}else if ( ( bPart2 & 0x05)==0x05 ){
					roadInfo.FX="��";
				}else if ( ( bPart2 & 0x06)==0x06 ){
					roadInfo.FX="����";
				}else if ( ( bPart2 & 0x07)==0x07 ){
					roadInfo.FX="��";
				}else if ( ( bPart2 & 0x08)==0x08 ){
					roadInfo.FX="����";
				}				
				//System.out.println("�ȼ�����"+roadInfo.DJ+","+ roadInfo.FX);

		 }		
			//�����
		 if( (	roadInfo.roadFlag & 0x0004 ) ==0x0004 ){
			 DecimalFormat df2  = new DecimalFormat("###.000000");
			 roadInfo.pointCount= this.getShortFromNaviData();
			// System.out.println("����� count:"+roadInfo.pointCount);
			 roadInfo.xs= new double[roadInfo.pointCount];
			 roadInfo.ys= new double[roadInfo.pointCount];
			 for(short i=0;i< roadInfo.pointCount;i++)
			 {
					int tempx=this.getIntFromNaviData();
					int tempy=this.getIntFromNaviData();
					double fx=(double) tempx/3600000;
					double fy=(double)tempy/3600000;
					roadInfo.xs[i]=Double.parseDouble(df2.format(fx))    ;
					roadInfo.ys[i]=Double.parseDouble(df2.format(fy))    ;
					//System.out.println(roadInfo.xs[i]+","+roadInfo.ys[i]);
					longitudeList.add(roadInfo.xs[i]);
					latitudeList.add(roadInfo.ys[i]);
			 }
			
		 }
		 
		 //��·����
		 if( (	roadInfo.roadFlag & 0x0008 ) ==0x0008 ){
			byte[] bs= getBytesFormNaviData(1);
			roadInfo.roadCount= (int)bs[0];		
			//System.out.println("��·����:"+roadInfo.roadCount );
			for(int i=0;i< roadInfo.roadCount ;i++){
				Road road = new Road();
				road.startIndex=this.getShortFromNaviData();
				byte[] nameLen= getBytesFormNaviData(1);				
				int len=(int)nameLen[0];
				len=len*2;
				byte[] bName=  getBytesFormNaviData(len);			
					try {
						road.name=new String(bName,"GBK");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println("��·���ƣ�"+len+","+road.startIndex+","+road.name);
			}
		 }		 
			//��·����
		 if( (	roadInfo.roadFlag & 0x0010 ) ==0x0010 ){
			 byte[]formWayCount= getBytesFormNaviData(1);
			 roadInfo.fromWayCount=(int)formWayCount[0];
			 for(int i=0;i<roadInfo.fromWayCount;i++ ){
				 RoadType roadType = new RoadType();
				 roadType.startIndex= this.getShortFromNaviData();
				 byte[]btype= getBytesFormNaviData(1);
				 String binaryString=bytes2BinaryString(btype);				
				 int intBtype= (int)btype[0];
				 if(intBtype==1   ){
					 roadType.name="��·";
				 }else if( intBtype==3){
					 roadType.name="�߼�";
				 }else if( intBtype==4){
					 roadType.name="����";
				 }else if( intBtype==6){
					 roadType.name="�ѵ�";
				 }else if( intBtype==7){
					 roadType.name="��·";
				 }else if( intBtype==9){
					 roadType.name="����";
				 }else if( intBtype==10){
					 roadType.name="���";
				 }else if( intBtype==11){
					 roadType.name="��תר�õ�";
				 }else if( intBtype==12){
					 roadType.name="��תר�õ�";
				 }else if( intBtype==13){
					 roadType.name="��תר�õ�";
				 }else if( intBtype==14){
					 roadType.name="��תר�õ�";
				 }else if( intBtype==15){
					 roadType.name="��ͨ��·";
				 }
				 roadInfo.RoadTypeList.add(roadType);
				 //System.out.println("��·���ͣ�"+roadInfo.fromWayCount+","+roadType.startIndex+","+roadType.name);
			 }
			 
		 }

			//������
		 if( (	roadInfo.roadFlag & 0x0020 ) ==0x0020 ){
			 byte[]bs= this.getBytesFormNaviData(1);
			 roadInfo.cds=(int)bs[0];
			// System.out.println("������:"+ roadInfo.cds);
		 }		 
		 
		 //������Ϣ
		 if( (	roadInfo.roadFlag & 0x0040 ) ==0x0040 ){
			 byte[]bs= this.getBytesFormNaviData(1);
			 int len=(int)bs[0];
			 byte[]bsInfo= this.getBytesFormNaviData(len);
			 roadInfo.cdInfo = new String(bsInfo);
			 //System.out.println("������Ϣ," + len+","+roadInfo.cdInfo );
		 }		
		 
		 //·��λͼID
		 if( (	roadInfo.roadFlag & 0x0080 ) ==0x0080 ){
			 byte[]bs= this.getBytesFormNaviData(1);
			 int len=(int)bs[0];
			 roadInfo.RKbmpID = this.getBytesFormNaviData(len);
			 //System.out.println("·��λͼID," + len+","+roadInfo.RKbmpID );			  
		 }

		 //·����Ϣ
		 if( (	roadInfo.roadFlag & 0x0100 ) ==0x0100 ){
			 byte[]bs= this.getBytesFormNaviData(1);
			 int len=(int)bs[0];
			 len=len*2;
			 roadInfo.RKInfo = new String( this.getBytesFormNaviData(len));
			 //System.out.println("·����Ϣ," + len+","+roadInfo.RKInfo );	
		 }			 
		 
		 fdNaviInfo.baseNaviInfo= this.getIntFromNaviData();
		 NaviInfo naviInfo= new NaviInfo();
		 byte[] temp=this.getBytesFormNaviData(1);
		 naviInfo.naviFlag  = (int)temp[0];
		 if(  (naviInfo.naviFlag &0x0001)==0x0001  ){
			// System.out.println("����ID");
			 naviInfo.soundID=this.getIntFromNaviData();
		 }
		 if(  (naviInfo.naviFlag &0x0002)==0x0002  ){
			// System.out.println("ͼƬID");
			 naviInfo.pictureID=this.getIntFromNaviData();
		 }
		 if(  (naviInfo.naviFlag &0x0004)==0x0004  ){
			// System.out.println("��ƵID");
			 naviInfo.flashID=this.getIntFromNaviData();
		 }
		 if(  (naviInfo.naviFlag &0x0008)==0x0008  ){
			 //System.out.println("�ı�");
			 byte[]bs= this.getBytesFormNaviData(1);
			 int ilen=(int)bs[0];
			 naviInfo.bsText=this.getBytesFormNaviData(ilen*2);
		 }
		 if(  (naviInfo.naviFlag &0x0010)==0x0010  ){
			 //System.out.println("��������");
			 byte[]bs= this.getBytesFormNaviData(1);
			 int ilen=(int)bs[0];
			 naviInfo.cdFX=this.getBytesFormNaviData(ilen);
		 }
		 if(  (naviInfo.naviFlag &0x0020)==0x0020  ){
			 //System.out.println("Ŀ����Ϣ");
			 byte[]bs= this.getBytesFormNaviData(1);
			 int ilen=(int)bs[0];
			 naviInfo.mbInfo=this.getBytesFormNaviData(ilen*2);
		 }
		 if(  (naviInfo.naviFlag &0x0040)==0x0040  ){
			 //System.out.println("·�ڼ�ͷ");
			 byte[]bs= this.getBytesFormNaviData(1);
			 int ilen=(int)bs[0];
			 naviInfo.rkjt=this.getBytesFormNaviData(ilen);
		 } 
		 if(  (naviInfo.naviFlag &0x0080)==0x0080  ){
			 //System.out.println("��ͨ��ȫ");
			 byte[]jtaqCount= this.getBytesFormNaviData(1);
			 int aqCount=(int)jtaqCount[0];
			 for(int i=0;i<aqCount; i++){
				 byte[] b=this.getBytesFormNaviData(1);
				 this.getIntFromNaviData();
				 this.getIntFromNaviData();
			 }
		 }
		 fdNaviInfo.roadDetail.add(roadInfo);
		 fdNaviInfo.naviDetail.add(naviInfo);
		 this.bodyInfo.naviInfoList.add(fdNaviInfo);
		}
		
	}
	
	
	
}
