/**
 * 
 */
package com.autonavi.directl.device.xwrj;

import com.autonavi.directl.idirectl.Alarm;
import com.autonavi.directl.idirectl.Locator;
import com.autonavi.directl.idirectl.Terminal;
import com.autonavi.directl.idirectl.TerminalControl;
import com.autonavi.directl.idirectl.TerminalQuery;
import com.autonavi.directl.idirectl.TerminalSetting;

/**
 * @author asia-auto
 * 星网锐捷终端
 */
public class XWRJGPRS extends Terminal {

	/* （非 Javadoc）
	 * @see com.autonavi.directl.idirectl.Terminal#createAlarm()
	 */
	@Override
	public Alarm createAlarm() {
		// TODO 自动生成方法存根
		return new XWRJAlarm(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.autonavi.directl.idirectl.Terminal#createLocator()
	 */
	@Override
	public Locator createLocator() {
		// TODO 自动生成方法存根
		return new XWRJLocator(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.autonavi.directl.idirectl.Terminal#createTerminalControl()
	 */
	@Override
	public TerminalControl createTerminalControl() {
		// TODO 自动生成方法存根
		return new XWRJControl(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.autonavi.directl.idirectl.Terminal#createTerminalQuery()
	 */
	@Override
	public TerminalQuery createTerminalQuery() {
		// TODO 自动生成方法存根
		return new XWRJQuery(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.autonavi.directl.idirectl.Terminal#createTerminalSetting()
	 */
	@Override
	public TerminalSetting createTerminalSetting() {
		// TODO 自动生成方法存根
		return new XWRJSetting(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.autonavi.directl.idirectl.Terminal#isSupport(java.lang.String)
	 */
	@Override
	public boolean isSupport(String functionName) {
		// TODO 自动生成方法存根
		return false;
	}

	/* （非 Javadoc）
	 * @see com.autonavi.directl.idirectl.Terminal#sentMsg(java.lang.String)
	 */
	@Override
	public String sentMsg(String content) {
		// TODO 自动生成方法存根
		return null;
	}

	/* （非 Javadoc）
	 * @see com.autonavi.directl.idirectl.Terminal#showTerminalInfo()
	 */
	@Override
	public void showTerminalInfo() {
		// TODO 自动生成方法存根

	}

}
