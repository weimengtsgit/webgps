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
 * ��������ն�
 */
public class XWRJGPRS extends Terminal {

	/* ���� Javadoc��
	 * @see com.autonavi.directl.idirectl.Terminal#createAlarm()
	 */
	@Override
	public Alarm createAlarm() {
		// TODO �Զ����ɷ������
		return new XWRJAlarm(this.terminalParam);
	}

	/* ���� Javadoc��
	 * @see com.autonavi.directl.idirectl.Terminal#createLocator()
	 */
	@Override
	public Locator createLocator() {
		// TODO �Զ����ɷ������
		return new XWRJLocator(this.terminalParam);
	}

	/* ���� Javadoc��
	 * @see com.autonavi.directl.idirectl.Terminal#createTerminalControl()
	 */
	@Override
	public TerminalControl createTerminalControl() {
		// TODO �Զ����ɷ������
		return new XWRJControl(this.terminalParam);
	}

	/* ���� Javadoc��
	 * @see com.autonavi.directl.idirectl.Terminal#createTerminalQuery()
	 */
	@Override
	public TerminalQuery createTerminalQuery() {
		// TODO �Զ����ɷ������
		return new XWRJQuery(this.terminalParam);
	}

	/* ���� Javadoc��
	 * @see com.autonavi.directl.idirectl.Terminal#createTerminalSetting()
	 */
	@Override
	public TerminalSetting createTerminalSetting() {
		// TODO �Զ����ɷ������
		return new XWRJSetting(this.terminalParam);
	}

	/* ���� Javadoc��
	 * @see com.autonavi.directl.idirectl.Terminal#isSupport(java.lang.String)
	 */
	@Override
	public boolean isSupport(String functionName) {
		// TODO �Զ����ɷ������
		return false;
	}

	/* ���� Javadoc��
	 * @see com.autonavi.directl.idirectl.Terminal#sentMsg(java.lang.String)
	 */
	@Override
	public String sentMsg(String content) {
		// TODO �Զ����ɷ������
		return null;
	}

	/* ���� Javadoc��
	 * @see com.autonavi.directl.idirectl.Terminal#showTerminalInfo()
	 */
	@Override
	public void showTerminalInfo() {
		// TODO �Զ����ɷ������

	}

}
