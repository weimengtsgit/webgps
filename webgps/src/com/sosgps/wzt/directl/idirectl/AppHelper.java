package com.sosgps.wzt.directl.idirectl;

import java.net.URL;

public class AppHelper {
    private AppHelper() {
    }

    /**
     * 得到web目录所在的绝对路径
     * @return String web目录所在的绝对路径
     */
    public static String getWebAppPath() {
        AppHelper util = new AppHelper();

        String clazzFilePath = util.getClassFilePath();

        String webPath = clazzFilePath.substring(1,
                                                 clazzFilePath.indexOf(
                "WEB-INF"));
        return webPath;
    }

    /**
     * 获取classes文件的绝对路径
     * @return String
     */
    private String getClassFilePath() {
        String strClassName = getClass().getName(); //获取类文件名
        String strPackageName = "";
        if (getClass().getPackage() != null) {
            strPackageName = getClass().getPackage().getName();
        }
        String strClassFileName = "";
        if (!"".equals(strPackageName)) {
            strClassFileName = strClassName.substring(strPackageName.length() +
                    1, strClassName.length());
        } else {
            strClassFileName = strClassName;
        }
        URL url = null;
        url = getClass().getResource(strClassFileName + ".class");

        String strURL = url.getFile();
        try {
            strURL = java.net.URLDecoder.decode(strURL, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /*strURL = strURL.substring(strURL.indexOf('/') + 1,
                                  strURL.lastIndexOf('/'))*/
        ;

        return strURL;
    }

    public static void main(String[] args) {
        System.out.println(AppHelper.getWebAppPath());
    }

}
