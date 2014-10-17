package com.sosgps.wzt.directl.device.sege;

import java.nio.*;

import com.sosgps.wzt.directl.idirectl.TerminalParam;
import com.sosgps.wzt.directl.idirectl.TerminalQueryAdaptor;
 

/**
 * <p>Title: GPS网关</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: www.sosgps.com</p>
 *
 * @author musicjiang@sohu.com
 * @version 1.0
 */
public class SegLPG52Query extends TerminalQueryAdaptor {
    public SegLPG52Query(TerminalParam terminalParam) {
        this.terminalParam = terminalParam;
    }
    String centerSn = "01";//中心号
	String zuoSn = "001"; //坐席号

    /**
     *查询GPRS连接参数
     * @return String
     * @todo Implement this com.sosgps.directl.idirectl.TerminalQuery method
     */
    public String getStateParam() {
        String content = "(QPM,GPS)";
        int size = 14 + content.length();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.put((byte) 0x5B);
        buffer.put((byte) 0x47);
        buffer.put("0100100001".getBytes());
        buffer.put((byte) content.length());
        buffer.put(content.getBytes());
        buffer.put((byte) 0x5D);
        String queryCmd = new String(buffer.array());
        return queryCmd;
    }

}
