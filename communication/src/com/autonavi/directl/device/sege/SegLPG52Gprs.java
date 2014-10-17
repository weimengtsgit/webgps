package com.autonavi.directl.device.sege;

import com.autonavi.directl.idirectl.Alarm;
import com.autonavi.directl.idirectl.Locator;
import com.autonavi.directl.idirectl.Terminal;
import com.autonavi.directl.idirectl.TerminalControl;
import com.autonavi.directl.idirectl.TerminalQuery;
import com.autonavi.directl.idirectl.TerminalSetting;

 

public class SegLPG52Gprs extends Terminal {
    public SegLPG52Gprs() {
    }

    public String sentMsg(String content) {
        return "";
    }

    public Alarm createAlarm() {
        return new SegLPG52Alarm(this.terminalParam);
    }

    public Locator createLocator() {
        return new SegLPG52Locator(this.terminalParam);
    }

    public TerminalControl createTerminalControl() {
        return new SegLPG52Control(this.terminalParam);
    }

    public TerminalQuery createTerminalQuery() {
        return new SegLPG52Query(this.terminalParam);
    }

    public TerminalSetting createTerminalSetting() {
        return new SegLPG52TerminalSetting(this.terminalParam);
    }

    public boolean isSupport(String functionName) {
        return false;
    }

    public void showTerminalInfo() {
    }
}
