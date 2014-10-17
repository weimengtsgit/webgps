 package com.autonavi.directl;

import org.apache.log4j.*;

 

public class Log {
	 private static boolean isDebug = false;
	    private static Log instance = null;
	    static Logger outLogger = Logger.getLogger("outLogger");
	    static Logger errorLogger = Logger.getLogger("errorLogger");
	    static Logger debugLog = Logger.getLogger("outDebugLog");
	    static Logger XWRJLoger = Logger.getLogger("outXWRJLog");
	    static Logger jhsLoger = Logger.getLogger("outJHSLogger");
	    
	    static Logger tcpLinkLoger = Logger.getLogger("outTcpLogger");
	    
	    private Log() {
	    	 
	    }

	    public static synchronized Log getInstance() {
	        if (instance == null) {
	            instance = new Log();
	            java.net.URL url = instance.filepath();
	            if (url != null && !isDebug) {
	                PropertyConfigurator.configure(url);
	            }
	        }
	        return instance;
	    }

	    public java.net.URL filepath() {
	        java.net.URL url = this.getClass().getClassLoader().getResource(
	                "log4j.properties");
	        return url;
	    }

	    public void outLog(String info) {
	        if (outLogger.isDebugEnabled()) {
	        	System.out.println(info);
	        } else {
	            outLogger.info(info);
	        }

	    }

	    public void outDebugLog(String info) {
	        if (debugLog.isDebugEnabled()) {
	            System.out.println(info);
	        } else {
	            debugLog.info(info);
	        }
	    }

	    public void errorLog(String info, Exception e) {
	        if (errorLogger.isDebugEnabled()) {
	        	System.out.println(info);
	        } else {
	            errorLogger.error(info, e);
	        }
	    }
	    
	    public void tcpLinkLoger(String info) {
			if (tcpLinkLoger.isDebugEnabled()) {
				System.out.println(info);
			} else {
				tcpLinkLoger.info(info);
			}
		}
 
	    
	    public void outXWRJLoger(String info) {
			if (outLogger.isDebugEnabled()) {
				System.out.println(info);
			} else {
				outLogger.info(info);
			}
		}
	    public void outJHSLoger(String info) {
	    	 
			if (jhsLoger.isDebugEnabled()) {
				System.out.println(info);
			} else {
				jhsLoger.info(info);
			}
		}
}
