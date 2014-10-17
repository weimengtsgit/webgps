package com.sosgps.wzt.util;

import java.io.*;
import java.util.*;

public class Config {
    static Config instance = null;
    public Config() {

    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getString(String propFileName, String prop) {
        String ret = "";
        InputStream is = getClass().getResourceAsStream("/"+propFileName);

        Properties prop1 = new Properties();
        if (is != null) {
            try {
                prop1.load(is);
                ret = prop1.getProperty(prop);
                is.close();
            } catch (IOException ex) {
            }
        }
        return ret;
    }


    public static void main(String[] args) {
    	String propFileName = "config.properties";
		String prop = "test";
		String s = Config.getInstance().getString(propFileName, prop);
		System.out.println(s);
	}
}
