package com.autonavi.directl.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
 
import org.logicalcobwebs.proxool.ProxoolException;        
import org.logicalcobwebs.proxool.ProxoolFacade;        
import org.logicalcobwebs.proxool.admin.SnapshotIF;

/**
 * @author a
 *
 */
public class DbUtil {
    public static Context initContext;

    public static Context ctx;

    public static DataSource ds;

    public static boolean hasInited = false;
    
    private static int activeCount = 0;  

    public static void init() {
        try {
            initContext = new InitialContext();
            ctx = (Context) initContext.lookup("java:comp/env");
            ds = (DataSource) ctx.lookup("jdbc/wzt");
            hasInited = true;
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /*
    public static Connection getConnection() {
        Connection conn = null;
        try {
            initContext = new InitialContext();
            ctx = (Context) initContext.lookup("java:comp/env");
            ds = (DataSource) ctx.lookup("jdbc/wzt");
            conn = ds.getConnection();
            return conn;
        } catch (SQLException e) {
           Log.getInstance().errorLog("建立数据库连接异常："+ e.getMessage(),e);
        } catch (NamingException ex) {
            Log.getInstance().errorLog( "连接池命名异常:"+ex.getMessage(),ex);
        }
        return null;
    }*/

    public static Connection getDirectConnection() {
        String url = "jdbc:oracle:thin:@172.17.40.52:1521:LBS";// Config.getInstance().getString("jdbcurl");
        String username = "shandongwzt"; //"system";// Config.getInstance().getString("username");
        String password = "mapabc"; // "mapabc";//Config.getInstance().getString("password");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            return DriverManager.getConnection(url, username, password);
        } catch (InstantiationException e) {
            Log.getInstance().errorLog( e.getMessage(),e);
        } catch (IllegalAccessException e) {
           Log.getInstance().errorLog( e.getMessage(),e);
        } catch (ClassNotFoundException e) {
            Log.getInstance().errorLog( e.getMessage(),e);
        } catch (SQLException e) {
            Log.getInstance().errorLog( e.getMessage(),e);
        }
        return null;
    }
    
    /**      
     * 获取连接      
     * getConnection      
     * @param name      
     * @return      
     */       
    public static Connection getConnection() {        
        try{        
        	Connection conn = null;
            Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");//proxool驱动类        
             conn = DriverManager.getConnection("proxool.dbname");     
           //此处的DBPool是在proxool.xml中配置的连接池别名       
            showSnapshotInfo();        
                    
            return conn;        
        }catch(Exception ex){       
        	Log.getInstance().errorLog("获取数据库连接异常", ex);
            ex.printStackTrace();        
        }        
        return null;        
    }    
    
    public static Connection getNodeConnection() {        
        try{        
        	Connection conn = null;
            Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");//proxool驱动类        
             conn = DriverManager.getConnection("proxool.NodeCon");     
           //此处的DBPool是在proxool.xml中配置的连接池别名       
            showNodeSnapshotInfo();        
                    
            return conn;        
        }catch(Exception ex){       
        	Log.getInstance().errorLog("获取数据库连接异常", ex);
            ex.printStackTrace();        
        }        
        return null;        
    }    
    
    /**      
     * 此方法可以得到连接池的信息      
     * showSnapshotInfo      
     */       
    private static void showSnapshotInfo(){        
        try{        
            SnapshotIF snapshot = ProxoolFacade.getSnapshot("dbname", true);        
            int curActiveCount=snapshot.getActiveConnectionCount();//获得活动连接数        
            int availableCount=snapshot.getAvailableConnectionCount();//获得可得到的连接数        
            int maxCount=snapshot.getMaximumConnectionCount() ;//获得总连接数        
            if(curActiveCount!=activeCount)//当活动连接数变化时输出的信息        
            {        
             Log.getInstance().outLog("活动连接数:"+curActiveCount+"(active)  可得到的连接数:"+availableCount+"(available)  总连接数:"+maxCount+"(max)");                     
             activeCount=curActiveCount;        
            }        
        }catch(ProxoolException e){        
            e.printStackTrace();        
        }        
    }        

    private static void showNodeSnapshotInfo(){        
        try{        
            SnapshotIF snapshot = ProxoolFacade.getSnapshot("NodeCon", true);        
            int curActiveCount=snapshot.getActiveConnectionCount();//获得活动连接数        
            int availableCount=snapshot.getAvailableConnectionCount();//获得可得到的连接数        
            int maxCount=snapshot.getMaximumConnectionCount() ;//获得总连接数        
            if(curActiveCount!=activeCount)//当活动连接数变化时输出的信息        
            {        
             Log.getInstance().outLog("node 活动连接数:"+curActiveCount+"(active)  可得到的连接数:"+availableCount+"(available)  总连接数:"+maxCount+"(max)");                     
             activeCount=curActiveCount;        
            }        
        }catch(ProxoolException e){        
            e.printStackTrace();        
        }        
    } 



}
