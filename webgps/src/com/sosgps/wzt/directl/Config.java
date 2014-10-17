package com.sosgps.wzt.directl;

import java.util.Properties;
import java.io.InputStream;
import java.io.*;

public class Config {
  static Config instance =null;
  public Config() {

  }
  public static Config getInstance(){
    if(instance==null){
      instance = new Config();
    }
    return instance;
  }

  public  String getString(String prop) {
    String ret = "";
    InputStream is = getClass().getResourceAsStream("/config.properties");

    Properties prop1 = new Properties();
    if (is != null) {
      try {
        prop1.load(is);
        ret = prop1.getProperty(prop);
        is.close();
      }
      catch (IOException ex) {
      }
    }
    return ret;
  }

  public String getProperty(String prop) {
    return getString(prop);
  }
}
