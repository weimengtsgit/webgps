package com.autonavi.lbsgateway.status;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.autonavi.directl.base.TerminalInfo;
import com.autonavi.lbsgateway.GPRSThreadList;

public class TermOnlineStatus extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public TermOnlineStatus() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		 
		PrintWriter out = response.getWriter();
		
		String[] onlineTerms = this.getAllOnlineTerminals();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println("在线终端号码：");
			for (int i=0; i<onlineTerms.length; i++){
			   out.println(onlineTerms[i]);
			}
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String[] onlineTerms = this.getAllOnlineTerminals();
		
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		 out.println("在线终端号码：");
		for (int i=0; i<onlineTerms.length; i++){
		   out.println(onlineTerms[i]);
		}
		 
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}
	
//	 获取所有在线的终端
	public String[] getAllOnlineTerminals() {

		GPRSThreadList map = com.autonavi.lbsgateway.GPRSThreadList
				.getInstance();

		String[] sims = new String[map.size()];

		StringBuffer sb = new StringBuffer();

		Iterator set = map.keySet().iterator();

		int offset = 0;

		while (set.hasNext()) {

			TerminalInfo info = new TerminalInfo();

			String simcard =  (String) set.next();
			
			info.setGpsSn(simcard);
			
			sims[offset++] = simcard;
		}

		return sims;
	}
	

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
