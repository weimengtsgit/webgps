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
 * @author �ն˲�Ʒ�� ��ɽ��Ŀ��
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
     * ���ֵ�����Ϣ  msg��������Ϣ
     * SDGPS,�豸��,3,��Ϣ
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
