package com.autonavi.lbsgateway.service;

import javax.servlet.*;
import java.net.*;
import javax.servlet.http.*;

import com.autonavi.directl.Log;

import java.io.*;

public class HttpUtil {
  public HttpUtil() {
  }
  public static  String getRealPath(javax.servlet.http.HttpServlet servlet,String relatePath){
    String path=servlet.getServletContext().getRealPath("/");
   java.io.File file=new java.io.File(path);
   file=new java.io.File(file,relatePath);
    return file.toString();
  }
  public static  String getURLPath(HttpServletRequest request,String relatePath){
    javax.servlet.http.HttpUtils httpUtils=new javax.servlet.http.HttpUtils();
    String requestURL=httpUtils.getRequestURL(request).toString();
    String baseRequestURL=requestURL.substring(0,requestURL.length()-request.getServletPath().length());
    baseRequestURL+="/"+relatePath+"";
    return baseRequestURL;
  }
  public static byte[] getURLData(String url)throws Exception{
    URLConnection c = null;
    URL imageURL = null;
    DataInputStream is = null;
    byte[] btemp;
    try {
      imageURL = new URL(url);
      c = imageURL.openConnection();
      is = new DataInputStream(c.getInputStream());
      java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
      byte[] bufferByte = new byte[256];
      int l = -1;
      int downloadSize = 0;
      while ( (l = is.read(bufferByte)) > -1) {
        downloadSize += l;
        out.write(bufferByte, 0, l);
        out.flush();
      }
      btemp = out.toByteArray();
      out.close();
    }
    catch (Exception ex) {
      throw ex;
    }
    finally {
      try {
        if(is!=null)
        is.close();
      }
      catch (Exception ex) {
     //   ex.printStackTrace();
      }
    }

    return btemp;
  }
  public static byte[] getPostData(String url,String encoding){
    try{
      byte[] b = url.getBytes("GBK");
      return b;
    }catch(Exception ex){
      ex.printStackTrace();
      return null;
    }

  }
  public static String getURLDecoderStr(String s){
    return java.net.URLDecoder.decode(s);
  }

  /**
   * 转换相对路径为绝对路径
   * @param baseURLString
   * @param realateString
   * @return
   */
  public static String getAbsURLString(String baseURLString ,String realateString)throws Exception{
    System.out.println("realateString="+realateString);
    System.out.println("baseURLString="+baseURLString);
    if(realateString.toLowerCase().startsWith("http://"))
    {
      //入口参数已经是绝对路径不用转换
      return realateString;
    }
    java.net.URL baseURL=new java.net.URL(baseURLString);
    if(realateString.startsWith("/")){
      //相对根
      String retStr=baseURL.getProtocol()+"://"+baseURL.getHost();
      if(baseURL.getPort()!=80&&baseURL.getPort()!=-1){
        retStr+=":"+baseURL.getPort();
      }
      retStr+=realateString;

      return retStr;
    }else{
      int lastIndex=baseURLString.lastIndexOf("/");
      if(lastIndex>-1)
      baseURLString=baseURLString.substring(0,lastIndex);
    }
    java.util.StringTokenizer stk=new java.util.StringTokenizer(realateString,"/");

    while(stk.hasMoreTokens()){
      String eleString=stk.nextToken();
      if(eleString.equals("..")){
        //上一个级别的目录
        int lastIndex=baseURLString.lastIndexOf("/");
        if(lastIndex>-1){
          baseURLString=baseURLString.substring(0,lastIndex);
        }
      }else if(eleString.equals(".")){

      }else{
          baseURLString+="/"+eleString;
      }
    }
    System.out.println("absurl="+baseURLString);
    return baseURLString;
  }
  public static String getURLData(String url,String encoding)throws Exception{
    byte[] b=getURLData(url);
    String r=new String(b,encoding);
    return r;
  }
  public static byte[] getPostData(String url){
    try{
      byte[] b = url.getBytes("UTF-8");
      return b;
    }catch(Exception ex){
      ex.printStackTrace();
      return null;
    }

  }
  //post data
  public static byte[] getPostURLData(String urlStr,byte[] postData)throws Exception{
   URL url = new URL(urlStr);
   URLConnection urlConn = null;
   DataOutputStream printout=null;
   DataInputStream input=null;
   java.io.ByteArrayOutputStream bout=new java.io.ByteArrayOutputStream();
   try {
     urlConn = url.openConnection();
     urlConn.setDoInput(true);
     // Let the RTS know that we want to do output.
     urlConn.setDoOutput(true);
     // No caching, we want the real thing.
     urlConn.setUseCaches(false);
     urlConn.setRequestProperty
         ("Content-Type", "application/x-www-form-urlencoded");
     urlConn.setRequestProperty("Content=length", String.valueOf(postData.length));
     printout = new DataOutputStream(urlConn.getOutputStream());
//     printout.write(postData);
     for (int i = 0; i < postData.length; i++) {
       printout.write(postData[i]);
     }
     printout.flush();
     printout.close();
     input = new DataInputStream(urlConn.getInputStream());
     byte[] bufferByte = new byte[256];
     int l = -1;
    int downloadSize = 0;
    while ( (l = input.read(bufferByte)) > -1) {
      downloadSize += l;
      bout.write(bufferByte, 0, l);
      bout.flush();
    }
    	byte[] btemp = bout.toByteArray();
    	System.out.println("bout.toByteArray 成功！");
    	
    	
     input.close();
     input = null;
     return btemp;
   }catch(Exception ex){
	   Log.getInstance().errorLog("请求路径导航失败", ex);
     throw ex;
   }finally{

   }

  }
  public static byte[] getPostData(HttpServletRequest request, HttpServletResponse response)throws Exception{
    java.io.DataInputStream servletIn = null;
      java.io.ByteArrayOutputStream bout=null;
      byte[] inByte =null;
      try{
        servletIn = new java.io.DataInputStream(request.getInputStream());
        bout = new java.io.ByteArrayOutputStream();
        byte[] bufferByte = new byte[256];
        int l = -1;
        while ( (l = servletIn.read(bufferByte)) > -1) {
          bout.write(bufferByte, 0, l);
          bout.flush();
        }
        inByte= bout.toByteArray();
      }catch(Exception ex){
        try{
           servletIn.close();
           bout.close();
        }catch(Exception ex2){}
      }
      if(inByte==null||inByte.length==0){
        return null ;
      }
     return inByte;
  }


}
