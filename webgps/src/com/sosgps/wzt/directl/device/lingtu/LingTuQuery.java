package com.sosgps.wzt.directl.device.lingtu;

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
public class LingTuQuery extends TerminalQueryAdaptor {
    public LingTuQuery(TerminalParam terminalParam) {
        this.terminalParam = terminalParam;
    }

    /**
     *查询GPRS连接参数
     * @return String
     * @todo Implement this com.sosgps.directl.idirectl.TerminalQuery method
     */
    public String getStateParam() {
       
       
        return null;
    }

}
