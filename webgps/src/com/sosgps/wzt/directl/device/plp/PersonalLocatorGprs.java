/**
 * 
 */
package com.sosgps.wzt.directl.device.plp;

import com.sosgps.wzt.directl.idirectl.Alarm;
import com.sosgps.wzt.directl.idirectl.Locator;
import com.sosgps.wzt.directl.idirectl.Terminal;
import com.sosgps.wzt.directl.idirectl.TerminalControl;
import com.sosgps.wzt.directl.idirectl.TerminalQuery;
import com.sosgps.wzt.directl.idirectl.TerminalSetting;

/**
 * @author shiguang.zhou
 *
 */
public class PersonalLocatorGprs extends Terminal {

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createAlarm()
	 */
	@Override
	public Alarm createAlarm() {
		// TODO Auto-generated method stub
		return new PersonalLocatorAlarm(this.terminalParam);
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createLocator()
	 */
	@Override
	public Locator createLocator() {
		// TODO Auto-generated method stub
		return new PersonalLocatorLocator(this.terminalParam);
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createTerminalControl()
	 */
	@Override
	public TerminalControl createTerminalControl() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createTerminalQuery()
	 */
	@Override
	public TerminalQuery createTerminalQuery() {
		// TODO Auto-generated method stub
		return new PersonalLocatorQuery(this.terminalParam);
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createTerminalSetting()
	 */
	@Override
	public TerminalSetting createTerminalSetting() {
		// TODO Auto-generated method stub
		return new PersonalLocatorSetting(this.terminalParam);
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#isSupport(java.lang.String)
	 */
	@Override
	public boolean isSupport(String functionName) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#sentMsg(java.lang.String)
	 */
	@Override
	public String sentMsg(String content) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#showTerminalInfo()
	 */
	@Override
	public void showTerminalInfo() {
		// TODO Auto-generated method stub

	}

}
