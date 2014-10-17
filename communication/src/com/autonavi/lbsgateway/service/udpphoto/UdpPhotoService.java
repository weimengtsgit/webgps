package com.autonavi.lbsgateway.service.udpphoto;

import java.sql.Connection;
import java.util.Date;
import java.util.Timer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public class UdpPhotoService extends HttpServlet {

	Timer timer = null;
	Connection conn = null;
	private static final int START_SERVICE_TIME = 5 * 60 * 1000;
	private static final int SAVE_PHOTO_PERIOD = 5 * 60 * 1000;

	/**
	 * Constructor of the object.
	 */
	public UdpPhotoService() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {
		timer = new Timer(true);
		UdpPhotoSave udpPhotoSave = new UdpPhotoSave();
		timer.schedule(udpPhotoSave, new Date(
				System.currentTimeMillis() + START_SERVICE_TIME), SAVE_PHOTO_PERIOD);
	}

}
