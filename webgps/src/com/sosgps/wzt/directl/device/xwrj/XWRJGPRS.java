/**
 * 
 */
package com.sosgps.wzt.directl.device.xwrj;

import com.sosgps.wzt.directl.idirectl.Alarm;
import com.sosgps.wzt.directl.idirectl.Locator;
import com.sosgps.wzt.directl.idirectl.Terminal;
import com.sosgps.wzt.directl.idirectl.TerminalControl;
import com.sosgps.wzt.directl.idirectl.TerminalQuery;
import com.sosgps.wzt.directl.idirectl.TerminalSetting;

/**
 * @author asia-auto
 * 星网锐捷终端
 */
public class XWRJGPRS extends Terminal {

	/* （非 Javadoc）
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createAlarm()
	 */
	@Override
	public Alarm createAlarm() {
		// TODO 自动生成方法存根
		return new XWRJAlarm(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createLocator()
	 */
	@Override
	public Locator createLocator() {
		// TODO 自动生成方法存根
		return new XWRJLocator(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createTerminalControl()
	 */
	@Override
	public TerminalControl createTerminalControl() {
		// TODO 自动生成方法存根
		return new XWRJControl(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createTerminalQuery()
	 */
	@Override
	public TerminalQuery createTerminalQuery() {
		// TODO 自动生成方法存根
		return new XWRJQuery(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#createTerminalSetting()
	 */
	@Override
	public TerminalSetting createTerminalSetting() {
		// TODO 自动生成方法存根
		return new XWRJSetting(this.terminalParam);
	}

	/* （非 Javadoc）
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#isSupport(java.lang.String)
	 */
	@Override
	public boolean isSupport(String functionName) {
		// TODO 自动生成方法存根
		return false;
	}

	/* （非 Javadoc）
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#sentMsg(java.lang.String)
	 */
	@Override
	public String sentMsg(String content) {
		// TODO 自动生成方法存根
		return null;
	}

	/* （非 Javadoc）
	 * @see com.sosgps.wzt.directl.idirectl.Terminal#showTerminalInfo()
	 */
	@Override
	public void showTerminalInfo() {
		// TODO 自动生成方法存根

	}

}
