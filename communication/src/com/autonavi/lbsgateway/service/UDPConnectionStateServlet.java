package com.autonavi.lbsgateway.service;


import java.util.Date;
import java.util.Timer;

 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class UDPConnectionStateServlet extends HttpServlet {

	Timer timer = null;
	/**
	 * Constructor of the object.
	 */
	public UDPConnectionStateServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
		if (timer != null){
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
		timer = new Timer(true);
		TermStateService tss = new TermStateService();
		timer.schedule(tss, new Date(System.currentTimeMillis()+ 1*60*1000), 1000*60);
	}

}
