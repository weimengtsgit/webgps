package com.sosgps.wzt.directl.device.shouhang;

import com.sosgps.wzt.directl.idirectl.Alarm;
import com.sosgps.wzt.directl.idirectl.Locator;
import com.sosgps.wzt.directl.idirectl.Terminal;
import com.sosgps.wzt.directl.idirectl.TerminalControl;
import com.sosgps.wzt.directl.idirectl.TerminalQuery;
import com.sosgps.wzt.directl.idirectl.TerminalSetting;

public class ShouHangGPRS extends Terminal {

	@Override
	public Alarm createAlarm() {
		// TODO Auto-generated method stub
		return new ShouHangAlarm(this.terminalParam);
	}

	@Override
	public Locator createLocator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TerminalControl createTerminalControl() {
		// TODO Auto-generated method stub
		return new ShouHangControl(this.terminalParam);
	}

	@Override
	public TerminalQuery createTerminalQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TerminalSetting createTerminalSetting() {
		return new ShouHangSetting(this.terminalParam);
	}

	@Override
	public boolean isSupport(String functionName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String sentMsg(String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showTerminalInfo() {
		// TODO Auto-generated method stub

	}

}
