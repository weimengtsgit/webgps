package com.autonavi.lbsgateway.gprsserver.tcp;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.CharBuffer;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.parse.ParseBase;
import com.autonavi.lbsgateway.GPRSThreadList;

/**
 * GprsSocketChannel:连接到网关的GPRS终端类 注意该类可能同时被多个线程使用 所以该类所有的方法必须加锁.
 */
public class GprsSocketTcpChannel {
	private SocketChannel socketChannel;

	private com.autonavi.directl.parse.ParseBase parseBase;// 终端解析类

	private String trcode;// 终端类型

	private String hexStart;// 包头标识，标识头标识符号,可以有多个标识符,中间用逗号隔开。

	private String hexEnd;// 包尾标识,标识尾标识符号,目前只支持一个标识符

	public GprsSocketTcpChannel(SocketChannel socketChannel) {
		this.setSocketChannel(socketChannel);
		// Log.getInstance().outLog("===============================创建一个GprsSocketTcpChannel对象");

	}

	public synchronized SocketChannel getSocketChannel() {
		return socketChannel;
	}

	private synchronized void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public synchronized String getTrcode() {
		return trcode;
	}

	public synchronized void setTrcode(String trcode) {
		this.trcode = trcode;
	}

	public synchronized String getHexStart() {
		return hexStart;
	}

	public synchronized void setHexStart(String hexstart) {
		this.hexStart = hexstart;
	}

	public synchronized String getHexEnd() {
		return hexEnd;
	}

	public synchronized void setHexEnd(String hexend) {
		this.hexEnd = hexend;
	}

	public synchronized ParseBase getParseBase() {
		return parseBase;
	}

	public synchronized void setParseBase(ParseBase parseBase) {
		this.parseBase = parseBase;
	}

	// 从Socket中读出数据流
	public synchronized byte[] readSocketBytes(SelectionKey key) {
		SocketChannel socket = this.getSocketChannel();
		byte[] ret = null;
		boolean bRead = true;
		while (bRead) {
			try {
				if (socket == null || !socket.isOpen()) {
					try {
						this.socketChannel.close();
						socket.socket().close();
						socket.close();
						key.cancel();
						if (this.getParseBase() != null) {
							GPRSThreadList.getInstance().remove2(
									this.getParseBase().getDeviceSN());
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					return null;
				}
				ByteBuffer byteBuffer = ByteBuffer.allocate(512);
				byteBuffer.clear();
				if (socket.isOpen() && !socket.socket().isClosed()) {

					int nbytes = socket.read(byteBuffer);

					if (nbytes == -1) {
						key.cancel();
						socket.socket().close();
						socket.close();
						bRead = false;

						if (this.getParseBase() != null) {
							GPRSThreadList.getInstance().remove2(
									this.getParseBase().getDeviceSN());
							Log.getInstance().errorLog(
									this.getParseBase().getDeviceSN()
											+ "读取终端数据失败,终端 关闭连接,read=-1", null);

						}
						return null;
					}
					if (nbytes > 0) {
						byteBuffer.flip();
						byte[] tmp = new byte[byteBuffer.limit()];
						byteBuffer.get(tmp, 0, byteBuffer.limit());
						if (ret == null) {
							ret = new byte[tmp.length];
							System.arraycopy(tmp, 0, ret, 0, tmp.length);
						} else {
							byte[] tmpret = new byte[ret.length];
							System.arraycopy(ret, 0, tmpret, 0, ret.length);
							ret = new byte[ret.length + tmp.length];
							System.arraycopy(tmpret, 0, ret, 0, tmpret.length);
							System.arraycopy(tmp, 0, ret, tmpret.length,
									tmp.length);
						}
					}

					if (nbytes < 256) {
						return ret;
					}
				} else {
					this.socketChannel.close();
				}
			} catch (Exception e) {

				bRead = false;
				Log.getInstance().errorLog("读取终端数据失败:" + e.getMessage(), e);

				// Log.getInstance().outLog(e.getMessage() + "====Delete "+
				// this.getParseBase().getDeviceSN());
				try {
					socket.socket().close();

				} catch (IOException ex) {
					Log.getInstance().errorLog("socket.socket()关闭链路异常", e);
				}
				try {
					socket.close();

					key.cancel();
				} catch (IOException ex) {
					Log.getInstance().errorLog("socket关闭链路异常", e);
				}
				if (this.getParseBase() != null) {
					GPRSThreadList.getInstance().remove2(
							this.getParseBase().getDeviceSN());
				}
				break;
			}
		}
		// Log.getInstance().outLog("接收字节数为："+ret.length+ " str="+new
		// String(ret));
		return ret;
	}

	// 把接收到的数据按照协议的标识头尾来进行切分,分成多行.
	// 注意：禁止标识尾有多个标识符号!
	public synchronized String[] getSocketLines(String hex) {
		String[] ret = null;
		try {
			String tmpstart = this.getHexStart();
			String tmpend = this.getHexEnd();
			if (tmpstart != null && tmpstart.split(",").length > 1) {
				String[] tmplines = tmpstart.split(",");
				for (int i = 0; i < tmplines.length; i++) {
					String tmpline = tmplines[i];

					if (tmpline == null || tmpline.trim().length() == 0)
						continue;
					ret = getsplitLines(hex, tmpline, tmpend);
					// Log.getInstance().outLog("切分指令："+ret+"tmpline:"+tmpline);
					if (ret != null)
						break;
				}
			} else {
				ret = getsplitLines(hex, tmpstart, tmpend);
			}
			// Log.getInstance().outLog("分割成多行数据成功!一共:" + ret.length + "行数据.");
		} catch (Exception ex) {
			String errinfo = "分割数据出现错误:" + ex.getMessage();
			errinfo += "数据:" + hex + "标识头:" + this.getHexStart() + "标识尾:"
					+ this.getHexEnd();
			Log.getInstance().outLog(errinfo);
			Log.getInstance().errorLog(errinfo, ex);
			String[] shytdata = new String[1];
			shytdata[0] = hex;
			return shytdata;
		}
		return ret;
	}

	// 切分函数
	private synchronized String[] getsplitLines(String hex, String hexstart,
			String hexend) {
		if (hex == null || (hex.length() % 2 != 0))
			return null;
		if (hexstart == null && hexend == null)
			return null;
		if (hexstart.trim().length() <= 0 && hexend.trim().length() <= 0)
			return null;

		String cData = addSpaceToHexstring(hex);
		String cStartData = addSpaceToHexstring(hexstart);
		String cEndData = addSpaceToHexstring(hexend);
		String[] rettmp = null;
		String strTmp = "";

		int f = -1;
		if (hexstart != null && hexend != null && hexstart.trim().length() > 0
				&& hexend.trim().length() > 0) {
			// 包头包尾,删除包头前和包尾后的数据
			int m = cData.indexOf(cStartData);
			int n = cData.lastIndexOf(cEndData);
			if (m == -1 || n == -1)
				return null;

			cData = cData.substring(m, cData.lastIndexOf(cEndData)
					+ cEndData.length());
			if (hexstart.equals("3032") && hexend.equals("23")) {
				rettmp = cData.split(cEndData);
			} else {
				rettmp = cData.split(cStartData);
			}

			f = 0;
		} else if (hexstart != null && hexstart.trim().length() > 0) {

			if (hexstart.equals("24")) { // 针对四维位置特殊指令解析
				int count = hex.length() / 64;
				rettmp = new String[count];
				for (int i = 0; i < count; i++) {
					rettmp[i] = hex.substring(i * 64, (i + 1) * 64);
				}
				return rettmp;
			}

			// 包头,删除包头前的数据
			int m = cData.indexOf(cStartData);
			if (m == -1)
				return null;
			cData = cData.substring(cData.indexOf(cStartData), cData.length());
			rettmp = cData.split(cStartData);
			f = 1;
		} else if (hexend != null && hexend.trim().length() > 0) {
			// 包尾,删除包尾后的数据

			int n = cData.lastIndexOf(cEndData);
			if (n == -1)
				return null;

			cData = cData.substring(0, cData.lastIndexOf(cEndData)
					+ cEndData.length());
			rettmp = cData.split(cEndData);
			f = 2;
		}

		for (int i = 0; i < rettmp.length; i++) {
			String tmp = rettmp[i];

			if (tmp == null || tmp.trim().length() == 0)
				continue;
			if (hexstart.equals("3032") && hexend.equals("23") && f == 0) {
				tmp = tmp + cEndData;
			} if (hexstart.equals("7e") && hexend.equals("7e") && f == 0) {
				tmp = cStartData + tmp + cEndData;
			}  else if (f == 0 || f == 1) {
				tmp = cStartData + tmp;
			}

			if (f == 2)
				tmp = tmp + cEndData;
			if (f == 0) {
				if (tmp.startsWith(cStartData) && tmp.endsWith(cEndData)) {
					strTmp += tmp + ",";

				}
			} else if (f == 1) {
				if (tmp.startsWith(cStartData)) {
					strTmp += tmp + ",";
				}
			} else if (f == 2) {
				if (tmp.endsWith(cEndData)) {
					strTmp += tmp + ",";
				}
			}
		}

		if (strTmp.trim().length() <= 0) {
			return null;
		}
		strTmp = strTmp.substring(0, strTmp.length() - 1);
		strTmp = strTmp.replaceAll(" ", "");
		return strTmp.split(",");
	}

	// 把十六进制度的字符串每个字节之间与' ' 隔开。
	private synchronized String addSpaceToHexstring(String hex) {
		if (hex == null || hex.trim().length() == 0 || (hex.length() % 2 != 0)) {
			return null;
		}
		char c = ' ';
		char[] cs = hex.toCharArray();
		CharBuffer cb = java.nio.CharBuffer.wrap(cs);
		int size = cs.length * 2;
		CharBuffer result = CharBuffer.allocate(size);
		for (int i = 0; i < hex.length(); i++) {
			result.put(cb.get(i));
			if (i > 0 && (i % 2 == 1)) {
				result.put(c);
			}
		}
		result.flip();
		return result.toString();
	}

	public static void main(String[] args) {
		GprsSocketTcpChannel tl = new GprsSocketTcpChannel(null);
		tl.setHexStart("24");
		String org = "2490906186040541241608093958842000116179818e000000fffffbffff0055"
				+ "2490906186040541291608093958842000116179818e000000fffffbffff0056"
				+ "2490906186040541341608093958842000116179818e000000fffffbffff0057"
				+ "2490906186040541391608093958842000116179818e000000fffffbffff0058"
				+ "2490906186040541441608093958842000116179818e000000fffffbffff0059"
				+ "2490906186040541491608093958842000116179818e000000fffffbffff005a"
				+ "2490906186040541541608093958842000116179818e000000fffffbffff005b"
				+ "2490906186040541591608093958842000116179818e000000fffffbffff005c"
				+ "2490906186040542041608093958842000116179818e000000fffffbffff005d"
				+ "2490906186040542091608093958842000116179818e000000fffffbffff005e"
				+ "2490906186040542141608093958842000116179818e000000fffffbffff005f"
				+ "2490906186040542191608093958842000116179818e000000fffffbffff0060"
				+ "2490906186040542241608093958842000116179818e000000fffffbffff0061"
				+ "2490906186040542291608093958842000116179818e000000fffffbffff0062"
				+ "2490906186040542341608093958842000116179818e000000fffffbffff0063"
				+ "2490906186040542391608093958842000116179818e000000fffffbffff0064";
		tl.getsplitLines(org, "24", null);
	}

}
