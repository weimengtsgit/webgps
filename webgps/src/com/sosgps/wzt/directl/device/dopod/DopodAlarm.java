package com.sosgps.wzt.directl.device.dopod;

import com.sosgps.wzt.directl.idirectl.AlarmAdapter;
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
public class DopodAlarm extends AlarmAdapter {
    public DopodAlarm() {
    }

    public DopodAlarm( TerminalParam tparam) {
        this.terminalParam = tparam;
    }

}
