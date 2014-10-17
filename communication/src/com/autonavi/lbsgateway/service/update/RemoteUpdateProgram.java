/**
 * 
 */
package com.autonavi.lbsgateway.service.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Hashtable;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
 
import com.autonavi.lbsgateway.GPRSThreadList;
import com.autonavi.lbsgateway.bean.TerminalUDPAddress;

/**
 * @author shiguang.zhou
 * 
 */
public class RemoteUpdateProgram {

	 
	private java.io.InputStream is = null;
 
	private String localFileName;
 
	private int readSize = 256;
 
	private int curPackNo;
 
	private Date date;

	 
	
	  private Hashtable dataHash = new Hashtable();
	
	  private static Hashtable deviceIdRemote = new Hashtable();
 	  
	  public void setDeviceidRemote(String deviceid, int curPackNo, Date date ){}
	

	/**
	 * @return the dataHash
	 */
	public Hashtable getDataHash() {
		return this.dataHash;
	}

	/**
	 * @param dataHash
	 *            the dataHash to set
	 */
	public void setDataHash(Hashtable dataHash) {
		this.dataHash = dataHash;
	}

	 

	public RemoteUpdateProgram() {
		this.localFileName = Config.getInstance()
				.getString("remoteFileDir");
		java.io.File file = new java.io.File("e:\\desk\\01605035.JPG");
		try {
			this.is = new java.io.FileInputStream(file);
			this.readFileData();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 

	public synchronized void addByteObj(String key, DataByte db) {
		if (key != null && db != null) {
			this.dataHash.put(key, db);
		}
	}

	public byte[] getPackBytes(String key) {
		byte[] ret = null;
		if (key != null) {
			DataByte byteObj = (DataByte) this.dataHash.get(key);
			if (byteObj != null)
				ret = byteObj.getBcont();
		}
		return ret;
	}

	public  synchronized void checkIsResponse(String deviceId) {
		Date preDate = this.getDate();
		if (preDate == null) {
			return;
		}
		Date curDate = new Date();
		String interval = Config.getInstance().getString("checkUpdateTime");
		String ivl = (interval == null || interval.trim().length() <= 0) ? "5"
				: interval;
		int val = Integer.parseInt(ivl);

		if ((curDate.getTime() - preDate.getTime()) > val * 1000) {

			 
		}
	}
 
	public void readFileData() {
		File f = new File("d:\\t.jpg");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			
			int leng = 0;
			int packNo = 0;
			while (leng != -1) {
				byte[] retB = new byte[readSize];
				leng = is.read(retB);
				if (leng != -1) {
					if (leng < readSize) {
						byte[] bmp = new byte[leng];
						System.arraycopy(retB, 0, bmp, 0, bmp.length);
						DataByte data = new DataByte();
						data.setBcont(bmp);
						this.addByteObj((packNo + 1) + "", data);
						fos.write(bmp);
					} else {
						DataByte dbyte = new DataByte();
						dbyte.setBcont(retB);
						this.addByteObj((packNo + 1) + "", dbyte);
						fos.write(retB);
					}
				}
				packNo++;

			}
			fos.close();
		} catch (Exception e) {
	 		}finally{
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	class DataByte {

		private byte[] bcont;

		/**
		 * @return the bcont
		 */
		public byte[] getBcont() {
			return this.bcont;
		}

		/**
		 * @param bcont
		 *            the bcont to set
		 */
		public void setBcont(byte[] bcont) {
			this.bcont = bcont;
		}

	}

	public static void main(String[] args) {
 
	}

	/**
	 * @return the curPackNo
	 */
	public int getCurPackNo() {
		return this.curPackNo;
	}

	/**
	 * @param curPackNo
	 *            the curPackNo to set
	 */
	public synchronized void setCurPackNo(int curPackNo) {
		this.curPackNo = curPackNo;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public synchronized void setDate(Date date) {
		this.date = date;
	}

	 
	 
}
