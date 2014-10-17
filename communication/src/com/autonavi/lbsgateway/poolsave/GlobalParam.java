package com.autonavi.lbsgateway.poolsave;

import java.util.HashMap;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;

import com.autonavi.directl.Log;
 
public class GlobalParam {
  static GlobalParam instance;
  java.util.HashMap globalParam=new HashMap();
  public GlobalParam() {
  }
  public static GlobalParam getInstance(){
    if(instance==null){
      instance = new GlobalParam();
    }
    return instance;
  }
  public String[] getEpidAndUrid(Connection conn,String simcard){
     if(simcard==null || simcard.trim().length()==0)return null;
    String[] ret=(String[])globalParam.get(simcard);
    if(ret!=null){
      return ret;
    }
     String sql = "select t1.epid,t3.urid from ep_users t1 ,ep_terminals t2 ,ep_utmapscmitems t3 where t1.urid=t3.urid and t3.loctmid=t2.tmid and t2.simcard='" +
                simcard + "'";
    Statement stmt = null;
    String[] result =null;
   try {
       stmt = conn.createStatement();
       java.sql.ResultSet rs = stmt.executeQuery(sql);
       if (rs.next()) {
            result=new String[3];
           result[0] = rs.getString("epid");
           result[1] = rs.getString("urid");
           globalParam.put(simcard,result);
       }
   } catch (SQLException ex) {
      Log.getInstance().errorLog(ex.getMessage(),ex);
   }
   finally{
   try {
     stmt.close();
   }
   catch (SQLException ex1) {
      Log.getInstance().errorLog(ex1.getMessage(),ex1);
   }
   }
   ret=(String[])globalParam.get(simcard);
   return ret;
}
}
