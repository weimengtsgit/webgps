package com.sosgps.wzt.orm;

public class SmsAccounts extends AbstractSmsAccounts implements
		java.io.Serializable {
	private static final long serialVersionUID = 1L;
	 
	public SmsAccounts(){
	 }
	 public SmsAccounts(String entCode, Integer smsAvailable, Integer sms_sent_count,
			 Integer sms_total) {
			super(entCode, smsAvailable, sms_sent_count, sms_total);
	 }
}
