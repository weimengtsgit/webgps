package com.sosgps.wzt.directl.device.lingtu;

import com.sosgps.wzt.directl.idirectl.Alarm;
import com.sosgps.wzt.directl.idirectl.Locator;
import com.sosgps.wzt.directl.idirectl.Terminal;
import com.sosgps.wzt.directl.idirectl.TerminalControl;
import com.sosgps.wzt.directl.idirectl.TerminalQuery;
import com.sosgps.wzt.directl.idirectl.TerminalSetting;

 

public class LingTuGprs extends Terminal {
    public LingTuGprs() {
    }

    public String sentMsg(String content) {
        return "";
    }

    public Alarm createAlarm() {
        return new LingTuAlarm(this.terminalParam);
    }

    public Locator createLocator() {
        return new LingTuLocator(this.terminalParam);
    }

    public TerminalControl createTerminalControl() {
        return new LingTuControl(this.terminalParam);
    }

    public TerminalQuery createTerminalQuery() {
        return new LingTuQuery(this.terminalParam);
    }

    public TerminalSetting createTerminalSetting() {
        return new LingTuSetting(this.terminalParam);
    }

    public boolean isSupport(String functionName) {
        return false;
    }

    public void showTerminalInfo() {
    }
}
