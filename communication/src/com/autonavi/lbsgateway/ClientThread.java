package com.autonavi.lbsgateway;

import java.io.*;
import java.net.*;
import com.autonavi.directl.Log;
 
import com.autonavi.directl.TerminalFactory;
import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.directl.idirectl.*;
import com.autonavi.lbsgateway.clientserver.ClientSocketChannel;
  
import java.sql.*;
 

public class ClientThread implements Runnable  {
  public ClientSocketChannel csc;
  private byte[] socketBytes;
  public ClientThread(ClientSocketChannel csc,byte[] socketBytes) {
    this.csc = csc;
    this.socketBytes=socketBytes;
  }

  public void run() {
    String line = null;
      try {
        line = new String(this.socketBytes);
        if(line==null || line.trim().length()==0)return;
          try {
            String requestXml = line.trim();
            Log.getInstance().outLog("ClientThread接收请求数据:" + requestXml);
            if (requestXml != null) {
              String requestID = null;//ProtocolUtility.getRequestID(requestXml);
              parseRequestXml(requestID, requestXml);
            }
          }
          catch (Exception ex1) {
        	  
            Log.getInstance().outLog("ClientThread解析数据出现异常:" + ex1 + " " + line);
            Log.getInstance().errorLog("ClientThread解析数据出现异常:" + line, ex1);
            ex1.printStackTrace();
          }
      }
      catch (Exception ex) {
        Log.getInstance().outLog("读取数据出现异常:" + ex.getMessage() + " " + line);
        Log.getInstance().errorLog("读取数据出现异常:" + line, ex);
      }
  }



  //判断该用户是否合法
  public boolean isExistLoginUser(String epcode, String user, String pass) {
    if (epcode == null || user == null || pass == null ||
        epcode.trim().length() == 0 || user.trim().length() == 0 ||
        pass.trim().length() == 0) {
      return false;
    }
    java.sql.Connection conn = DbOperation.getConnection();
    java.sql.Statement stmt = null;
    java.sql.ResultSet rs = null;
    boolean b = false;
    try {
      stmt = conn.createStatement();
      String sql =
          "select t.* from ep_sysusers t ,epaccounts t2 where t2.epcode='" +
          epcode + "' and  t.username='" + user + "' and t.password='" + pass +
          "' and t.epid=t2.epid";
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        LoginUser loginUser = new LoginUser();
        loginUser.setEpid(rs.getString("epid"));
        loginUser.setUrid(rs.getString("SUID"));
        loginUser.setPssword(rs.getString("PASSWORD"));
        loginUser.setUrname(rs.getString("Username"));
        loginUser.setIP(this.csc.getSocketChannel().socket().getInetAddress().getHostAddress()+":"+  this.csc.getSocketChannel().socket().getPort() );
        this.csc.setLoginUser(loginUser);
        b = true;
      }
    }
    catch (SQLException ex) {
    }
    finally {
    	DbOperation.release(stmt, rs, null, conn);
      //com.mapabc.db.DBConnectionManager.close(conn, stmt, rs);
    }
    return b;
  }

  public String sentData(String data) {
    if (data == null || data.trim().length() == 0) {
      return "error";
    }
    String ret = "error";
    try {
      this.csc.getDataSendTask().insertData(data);
      if( this.csc.getDataSendTask().dataList.size()>5){
      Log.getInstance().outLog("============================>  发现阻塞线程，IP："+this.csc.getLoginUser().getIP()+"，目前阻塞数量为："+this.csc.getDataSendTask().dataList.size());
      }
      ret = "ok";
    }
    catch (Exception ex) {
      Log.getInstance().outLog("发送数据给客户端 出现异常:" + ex.getMessage() + " " + data);
      Log.getInstance().errorLog("发送数据给客户端 出现异常:" + data, ex);
    }
    return ret;
  }

//解析客户端发出的命令请求
  private void parseRequestXml(String requestID, String requestXml) {

    
  }

//发送消息
  public void sentMsg(String simcards, String content) {
    
  }

//设置服务中心
  public void setCenterNum( ) {
    
  }

  //设置回传电话
  public void setCallBack( ) {
    
  }

  public void setTerminalState( ) {
   
  }

  //速度报警
  public void setSpeedAlarmState( ) {
    
  }

  //向终端发送指令
  public void sentCmdToTerminal(String simcards, String requestID, String param) {
	  
	 
  }

  //定距离回传
  public void setSendPerDistance( ) {
     
  }

  //区域报警
  public void setRangleAlarm( ) {
    
  }

  //遥控熄火
  public void setFlameout( ) {
    
  }

  //轨迹存储设置
  public void setSaveContrail( ) {
     
  }

 


//得到合法的手机
  private String getValidSimcard(String simcards) {
    return null;
  }

  //得到合法的手机
  private String getValidSimcard( ) {
    String validSimcard = "";
 
    return validSimcard;
  }

 



  //把byte数组转换成16进制字符
  private String bytesToHexString(byte[] bs) {
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


}
