package com.sosgps.wzt.directl.device.dopod;

import com.sosgps.wzt.directl.idirectl.TerminalControlAdaptor;
import com.sosgps.wzt.directl.idirectl.TerminalParam;

 

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Beijing Mapabc Co., Ltd.</p>
 *
 * @author 终端产品部 黄山项目组
 * @version 1.0
 */
public class DopodControl extends TerminalControlAdaptor {
    /**
     *
     * @param tparam TerminalParam
     */
    public DopodControl( TerminalParam tparam) {
        this.terminalParam = tparam;
    }

    /**
     * 文字调度信息  msg：文字信息
     * SDGPS,设备号,3,信息
     * @param tel
     * @return
     */
    public String SendMessage(String msg){
        StringBuffer buffer = new StringBuffer();
         buffer.append("SDGPS,");
         buffer.append(this.terminalParam.getSeriesNo());
         buffer.append(",3,");
         buffer.append(msg);
         buffer.append("\r\n");

         return buffer.toString();
     }
}
