package com.autonavi.directl.device.dopod;

import com.autonavi.directl.idirectl.Alarm;
import com.autonavi.directl.idirectl.Locator;
import com.autonavi.directl.idirectl.Terminal;
import com.autonavi.directl.idirectl.TerminalControl;
import com.autonavi.directl.idirectl.TerminalQuery;
import com.autonavi.directl.idirectl.TerminalSetting;

 

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
public class DopodGPRS extends Terminal {
    public DopodGPRS() {
    }

    /**
     * createAlarm
     *
     * @return Alarm
     * @todo Implement this com.mapabc.directl.idirectl.Terminal method
     */
    public Alarm createAlarm() {
        return new DopodAlarm(this.terminalParam);
    }

    /**
     * createLocator
     *
     * @return Locator
     * @todo Implement this com.mapabc.directl.idirectl.Terminal method
     */
    public Locator createLocator() {
        return new DopodLocator(this.terminalParam);
    }

    /**
     * createTerminalControl
     *
     * @return TerminalControl
     * @todo Implement this com.mapabc.directl.idirectl.Terminal method
     */
    public TerminalControl createTerminalControl() {
        return new DopodControl(this.terminalParam);
    }

    /**
     * createTerminalQuery
     *
     * @return TerminalQuery
     * @todo Implement this com.mapabc.directl.idirectl.Terminal method
     */
    public TerminalQuery createTerminalQuery() {
        return new DopodQuery(this.terminalParam);
    }

    /**
     * createTerminalSetting
     *
     * @return TerminalSetting
     * @todo Implement this com.mapabc.directl.idirectl.Terminal method
     */
    public TerminalSetting createTerminalSetting() {
        return new DopodTerminalSetting(this.terminalParam);
    }

    /**
     * isSupport
     *
     * @param functionName String
     * @return boolean
     * @todo Implement this com.mapabc.directl.idirectl.Terminal method
     */
    public boolean isSupport(String functionName) {
        return false;
    }

    /**
     * sentMsg
     *
     * @param content String
     * @return String
     * @todo Implement this com.mapabc.directl.idirectl.Terminal method
     */
    public String sentMsg(String content) {
        return "";
    }

    /**
     * showTerminalInfo
     *
     * @todo Implement this com.mapabc.directl.idirectl.Terminal method
     */
    public void showTerminalInfo() {
    }
}
