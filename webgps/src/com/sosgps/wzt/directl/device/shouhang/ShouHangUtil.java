/**
 * 
 */
package com.sosgps.wzt.directl.device.shouhang;

import java.nio.ByteBuffer;

import com.sosgps.wzt.directl.Tools;

/**
 * @author shiguang.zhou
 * 
 */
public class ShouHangUtil {

	public static String crtCmdByte(String deviceids, String businessType,
			String dataType, String hexdata) {
		String ret = "";

		String head = "5346";
		String end = "af";
		while (deviceids.length() < 16) {
			deviceids += "f";
		}

		ret = "10ffff" + deviceids + businessType + dataType
				+ Tools.int2Hexstring(hexdata.length() / 2, 4) + hexdata;
		byte code = getVerfyCode(Tools.fromHexString(ret));
		ret = head + ret + Tools.bytesToHexString(new byte[] { code }) + end;
		return ret;

	}

	public static byte getVerfyCode(byte[] br) {

		int sum = 0;
		for (int i = 0; i < br.length; i++) {
			sum += br[i] & 0xFF;
		}

		return (byte) sum;
	}
	
	public static void main(String args[]){
		//ÉãÏñÍ·²éÑ¯
		//String ret = crtCmdByte("1309241251302030", "06","01", "");//534610ffff13092412513020300601000038af
		//×¥ÅÄÍ¼Ïñ
		String ret = crtCmdByte("1309241251302030", "06","03", "01");//534610ffff130924125130203006030001013caf
		
		System.out.println(ret);
		
	}

}
