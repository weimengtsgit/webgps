package com.autonavi.lbsgateway;

import org.jdom.Document;

import java.io.IOException;
import java.io.InputStream;
import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.Element;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
 
import java.io.*;

/**
 * GPRS����Ԥ������
 * �Ѹ�GPRS�����������ļ��Ƚϣ��жϳ�Ϊ�Ŀ��ն�����
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: www.mapabc.com</p>
 *
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class PrepareParse {
  // public static final int
  private static final String ConfigFilename = "terminallist.properties";
  private static PrepareParse instance;
  private static Document document;
  public PrepareParse() {
    this.loadXml();
  }

  public static PrepareParse getInstance() {
    if (instance == null ||document==null ) {
      instance = new PrepareParse();
    }
    return instance;
  }

  public void reloadXml(){
    this.loadXml();
  }

  private void loadXml() {
    SAXBuilder sb = new SAXBuilder();
    InputStream is = getClass().getResourceAsStream("/" + ConfigFilename);
    document = null;
    try {
      document = sb.build(is);
    }
    catch (JDOMException ex) {
      Log.getInstance().outLog("���ش���!���ض�ȡ:" + ConfigFilename + "ʧ��!��������������.");
      Log.getInstance().errorLog("���ش���!���ض�ȡ:" + ConfigFilename + "ʧ��!��������������.",
                                 ex);
    } catch (IOException e) {
		// TODO �Զ����� catch ��
		e.printStackTrace();
	}  
  }


  /**
   * ����GPS���ݣ��õ�GPS����#�ն����ݰ�ͷ��ʶ��#�ն����ݰ�β��ʶ��
   * @param hexDictate String
   * @return String
   */
  public String getTerminalCode(String hexDictate){
    String dictate="";
    String split="#";
    String none="null";
    if(hexDictate==null)return dictate;
    Element root = document.getRootElement();
    java.util.List children = root.getChildren("Terminal");
    for (int i = 0; i < children.size(); i++) {
      Element e = (Element) children.get(i);
      String hexStart = e.getChild("Hex-Start").getText().trim();
      String hexEnd = e.getChild("Hex-End").getText().trim();
      if (hexStart != null && hexEnd != null && hexEnd.length() > 0 &&
          hexStart.length() > 0)
      {//��ͷ�Ͱ�β��Ҫ���бȽ�
        String[] startArray=hexStart.split(",");
        for(int j=0;j<startArray.length;j++){
          if( hexDictate.startsWith(startArray[j])&& hexDictate.endsWith(hexEnd)){
            dictate=e.getChild("Terminal-name").getText().trim();
            return dictate+split+hexStart+split+ hexEnd;
        }
        }
      }
      else if(hexStart != null &&hexStart.length() > 0 )
      {//�Ƚϰ�ͷ
         String[] startArray=hexStart.split(",");
         for(int j=0;j<startArray.length;j++){
           if( hexDictate.startsWith(startArray[j])){
             dictate=e.getChild("Terminal-name").getText().trim();
             return dictate+split+hexStart+split+none;
        }
      }
      }
      else if(hexEnd != null &&hexEnd.length() > 0 )
      {//�Ƚϰ�β
        if( hexDictate.endsWith(hexEnd)){
          dictate=e.getChild("Terminal-name").getText().trim();
           return dictate+split+none+split+ hexEnd;
        }
      }
    }
    return dictate;
  }
  
  
  /**
   *��ȡ�����ļ�ͷβ��ʶ��
   * @param hexDictate String
   * @return String����
   */
//  public String getTermHeadTail(String hexDictate){
//    String dictate="";
//    String split="#";
//    String none="null";
//    String[] ht = new String[2];
//    if(hexDictate==null)return dictate;
//    
//    Element root = document.getRootElement();
//    java.util.List children = root.getChildren("Terminal");
//    for (int i = 0; i < children.size(); i++) {
//      Element e = (Element) children.get(i);
//      String hexStart = e.getChild("Hex-Start").getText().trim();
//      String hexEnd = e.getChild("Hex-End").getText().trim();
//      
//      }
//      
//      
//    }
//    return dictate;
//  }

  public static void main(String[] args) {
 
    String dop  = "SDGPS,0001234,2,136.223424,28.234234,23.23,453.34,2007-8-29 17:34:39";
    try {
       
   dop = Tools.bytesToHexString(dop.getBytes("ISO8859-1"));
   
   }
    catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
    }
    System.out.println(dop);
    String pname=PrepareParse.getInstance().getTerminalCode(dop);
    System.out.println(pname);
  }
}
