package com.autonavi.directl.base;

import org.jdom.Document;
 
import java.io.IOException;
import java.io.InputStream;
import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.Element;
import java.util.*;

/**
 * <p>Title: GPS网关</p>
 *
 * <p>Description: 接收网关接收到的GPS上行数据</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: www.mapabc.com</p>
 *
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class GPSReceive {
  public GPSReceive() {
  }
  public TerminalInfo parseAndDeal(byte[] b){
    if(b==null)return null;
    String input = new String(b).trim();
    SAXBuilder sb = new SAXBuilder();
    InputStream is = getClass().getResourceAsStream("/gpscharactermapping.xml");
    Document doc = null;
    try {
      doc = sb.build(is);
    }
    catch (JDOMException ex) {
      com.autonavi.directl.Log.getInstance().outLog("读取gpsfuncmapping.xml异常："+ex.getMessage());
    } catch (IOException e) {
		// TODO 自动生成 catch 块
		e.printStackTrace();
	}  
    Element root = doc.getRootElement();
    List gpss = root.getChildren("gps");
    String termcode = null;
    String desc = null;
    for (int i = 0; i < gpss.size(); i++) {
      Element e = (Element) gpss.get(i);
      Element charas = e.getChild("characters");
      List characters = charas.getChildren("character");
      //String[][] s = new String[characters.size()][2];
      boolean flag = false;
      for(int j=0;j<characters.size();j++){
        Element ee = (Element) characters.get(j);
        String sort = ee.getChild("sort").getText();
        String value = ee.getChild("value").getText();
        if(sort.equals("start")){
          if(!input.startsWith(value)){
            flag = true;
            //break;
          }
        }else{
          if(!input.endsWith(value)){
            flag = true;
            //break;
          }
        }
      }
      if(flag)continue;
      termcode = e.getChild("type").getText();
      desc = e.getChild("desc").getText();
      break;
    }
    TerminalInfo ti = new TerminalInfo();
    ti.setTermcode(termcode);
    ti.setTermcodeDesc(desc);
    return ti;
  }
  //test
  public static void main(String[] args){
    GPSReceive gr = new GPSReceive();
    byte[] b = null;
    TerminalInfo ti = null;
    b="GDAT2222222GDAT2222222".getBytes();
    ti = gr.parseAndDeal(b);
    if (ti != null) {
      System.out.println("gpsSn: " + ti.getGpsSn() + "\tsimcard: " +
                         ti.getSimcard() + "\ttermcode: " +
                         ti.getTermcode() + "\tlinkeway: " + ti.getLinkway() +
                         "\termcodeDesc: " + ti.getTermcodeDesc());
    }
    b="$haha!$haha!".getBytes();
    b="$GPRMC1111111111$GPRMC22222222222".getBytes();
    ti = gr.parseAndDeal(b);
    if (ti != null) {
      System.out.println("gpsSn: " + ti.getGpsSn() + "\tsimcard: " +
                         ti.getSimcard() + "\ttermcode: " +
                         ti.getTermcode() + "\tlinkeway: " + ti.getLinkway() +
                         "\termcodeDesc: " + ti.getTermcodeDesc());
    }

  }
}
