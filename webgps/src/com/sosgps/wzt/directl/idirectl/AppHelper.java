package com.sosgps.wzt.directl.idirectl;

import java.net.URL;

public class AppHelper {
    private AppHelper() {
    }

    /**
     * �õ�webĿ¼���ڵľ���·��
     * @return String webĿ¼���ڵľ���·��
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
     * ��ȡclasses�ļ��ľ���·��
     * @return String
     */
    private String getClassFilePath() {
        String strClassName = getClass().getName(); //��ȡ���ļ���
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
