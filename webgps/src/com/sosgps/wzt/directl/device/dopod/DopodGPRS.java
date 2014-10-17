package com.sosgps.wzt.directl.device.dopod;

import com.sosgps.wzt.directl.idirectl.Alarm;
import com.sosgps.wzt.directl.idirectl.Locator;
import com.sosgps.wzt.directl.idirectl.Terminal;
import com.sosgps.wzt.directl.idirectl.TerminalControl;
import com.sosgps.wzt.directl.idirectl.TerminalQuery;
import com.sosgps.wzt.directl.idirectl.TerminalSetting;

 

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
public class DopodGPRS extends Terminal {
    public DopodGPRS() {
    }

    /**
     * createAlarm
     *
     * @return Alarm
     * @todo Implement this com.sosgps.directl.idirectl.Terminal method
     */
    public Alarm createAlarm() {
        return new DopodAlarm(this.terminalParam);
    }

    /**
     * createLocator
     *
     * @return Locator
     * @todo Implement this com.sosgps.directl.idirectl.Terminal method
     */
    public Locator createLocator() {
        return new DopodLocator(this.terminalParam);
    }

    /**
     * createTerminalControl
     *
     * @return TerminalControl
     * @todo Implement this com.sosgps.directl.idirectl.Terminal method
     */
    public TerminalControl createTerminalControl() {
        return new DopodControl(this.terminalParam);
    }

    /**
     * createTerminalQuery
     *
     * @return TerminalQuery
     * @todo Implement this com.sosgps.directl.idirectl.Terminal method
     */
    public TerminalQuery createTerminalQuery() {
        return new DopodQuery(this.terminalParam);
    }

    /**
     * createTerminalSetting
     *
     * @return TerminalSetting
     * @todo Implement this com.sosgps.directl.idirectl.Terminal method
     */
    public TerminalSetting createTerminalSetting() {
        return new DopodTerminalSetting(this.terminalParam);
    }

    /**
     * isSupport
     *
     * @param functionName String
     * @return boolean
     * @todo Implement this com.sosgps.directl.idirectl.Terminal method
     */
    public boolean isSupport(String functionName) {
        return false;
    }

    /**
     * sentMsg
     *
     * @param content String
     * @return String
     * @todo Implement this com.sosgps.directl.idirectl.Terminal method
     */
    public String sentMsg(String content) {
        return "";
    }

    /**
     * showTerminalInfo
     *
     * @todo Implement this com.sosgps.directl.idirectl.Terminal method
     */
    public void showTerminalInfo() {
    }
}
