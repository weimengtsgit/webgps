package com.sosgps.wzt.directl.device.sjhx;

import com.sosgps.wzt.directl.idirectl.Alarm;
import com.sosgps.wzt.directl.idirectl.Locator;
import com.sosgps.wzt.directl.idirectl.Terminal;
import com.sosgps.wzt.directl.idirectl.TerminalControl;
import com.sosgps.wzt.directl.idirectl.TerminalQuery;
import com.sosgps.wzt.directl.idirectl.TerminalSetting;

public class SjhxGPRS extends Terminal {
	public Alarm createAlarm() {
		return null;
	}

	public Locator createLocator() {
		return null;
	}

	public TerminalControl createTerminalControl() {
		return new SjhxControl(this.terminalParam);
	}

	public TerminalQuery createTerminalQuery() {
		return null;
	}

	public TerminalSetting createTerminalSetting() {
		return new SjhxSetting(this.terminalParam);
	}

	public boolean isSupport(String functionName) {
		return false;
	}

	public String sentMsg(String content) {
		return null;
	}

	public void showTerminalInfo() {
	}
}