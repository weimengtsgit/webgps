package com.autonavi.lbsgateway.service;

import java.sql.Connection;
import java.util.Date;
import java.util.Timer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public class DealInstructionServlet extends HttpServlet {
	
	Timer timer = null;
	Connection conn = null;

	/**
	 * Constructor of the object.
	 */
	public DealInstructionServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		if (timer != null){
			timer.cancel();
			timer = null;
		}
		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
		
		timer = new Timer(true);
		 
		ReadInstruction readInstruct = new ReadInstruction();
		//TermStateService tss = new TermStateService();
		//readInstruct.run();
		
		timer.schedule(readInstruct, new Date(System.currentTimeMillis()+ 5*60*1000 ), 30000);
		
		//timer.schedule(tss, new Date(System.currentTimeMillis()), 5000);
	}

}
