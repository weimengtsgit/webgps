/**
 * 
 */
package com.autonavi.directl.device.plp;

import com.autonavi.directl.idirectl.Alarm;
import com.autonavi.directl.idirectl.Locator;
import com.autonavi.directl.idirectl.Terminal;
import com.autonavi.directl.idirectl.TerminalControl;
import com.autonavi.directl.idirectl.TerminalQuery;
import com.autonavi.directl.idirectl.TerminalSetting;

/**
 * @author shiguang.zhou
 *
 */
public class PersonalLocatorGprs extends Terminal {

	/* (non-Javadoc)
	 * @see com.autonavi.directl.idirectl.Terminal#createAlarm()
	 */
	@Override
	public Alarm createAlarm() {
		// TODO Auto-generated method stub
		return new PersonalLocatorAlarm(this.terminalParam);
	}

	/* (non-Javadoc)
	 * @see com.autonavi.directl.idirectl.Terminal#createLocator()
	 */
	@Override
	public Locator createLocator() {
		// TODO Auto-generated method stub
		return new PersonalLocatorLocator(this.terminalParam);
	}

	/* (non-Javadoc)
	 * @see com.autonavi.directl.idirectl.Terminal#createTerminalControl()
	 */
	@Override
	public TerminalControl createTerminalControl() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.autonavi.directl.idirectl.Terminal#createTerminalQuery()
	 */
	@Override
	public TerminalQuery createTerminalQuery() {
		// TODO Auto-generated method stub
		return new PersonalLocatorQuery(this.terminalParam);
	}

	/* (non-Javadoc)
	 * @see com.autonavi.directl.idirectl.Terminal#createTerminalSetting()
	 */
	@Override
	public TerminalSetting createTerminalSetting() {
		// TODO Auto-generated method stub
		return new PersonalLocatorSetting(this.terminalParam);
	}

	/* (non-Javadoc)
	 * @see com.autonavi.directl.idirectl.Terminal#isSupport(java.lang.String)
	 */
	@Override
	public boolean isSupport(String functionName) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.autonavi.directl.idirectl.Terminal#sentMsg(java.lang.String)
	 */
	@Override
	public String sentMsg(String content) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.autonavi.directl.idirectl.Terminal#showTerminalInfo()
	 */
	@Override
	public void showTerminalInfo() {
		// TODO Auto-generated method stub

	}

}
