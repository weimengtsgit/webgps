package com.autonavi.directl.parse;

import java.io.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.JDOMException;

import com.autonavi.directl.Log;

public class ParseFactory {
  public ParseFactory() {
  }
  /**
   * 得到该类型的终端实现类名称
   * @param pTerminalType：终端类型
   * @return
   */
  private synchronized String getParseClassName(String pTerminalType) {
    String parseClassName = null;
    SAXBuilder sb = new SAXBuilder();
    InputStream is = getClass().getResourceAsStream("/terminallist.properties");
    Document doc = null;
    try {
      doc = sb.build(is);
    }
    catch (JDOMException ex) {
      System.out.println(ex.getMessage());
    } catch (IOException e) {
		// TODO 自动生成 catch 块
		e.printStackTrace();
	}  
    Element root = doc.getRootElement();
    java.util.List children = root.getChildren("Terminal");
    for (int i = 0; i < children.size(); i++) {
      Element e = (Element) children.get(i);
      String terminalType=e.getChild("Terminal-name").getText();
      if(pTerminalType.equalsIgnoreCase(terminalType)){
       Element eParseClass= e.getChild("Parse-class");
       if(eParseClass==null){
      Log.getInstance().outLog(terminalType +" 没有找到解析类!");
       Log.getInstance().errorLog(terminalType +" 没有找到解析类!",null);
       parseClassName=null;
     }else{
         parseClassName=e.getChild("Parse-class").getText();
       }
        return parseClassName;
      }
    }
    return parseClassName;
  }

  public synchronized ParseBase createParse(String deviceType) throws ClassNotFoundException,
      IllegalAccessException, InstantiationException {
    String className = getParseClassName(deviceType);
    if (className == null) {
      return null;
    }
    Class c = Class.forName(className);
    ParseBase tl = (ParseBase) c.newInstance();
    return tl;
  }
}
