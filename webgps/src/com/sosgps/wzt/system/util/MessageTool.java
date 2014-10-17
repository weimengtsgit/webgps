/**
 * 
 */
package com.sosgps.wzt.system.util;

import org.sos.helper.SpringHelper;

//import com.sosgps.wzt.sms.service.SmsService;

/**
 * @author xiaojun.luan
 *
 */
public class MessageTool {
	public static boolean sendMessage(String content,String phoneNumber){
		System.out.println("·¢ËÍ¶ÌÐÅ£º'"+content+"' to "+phoneNumber);
		//redo
//		SmsService smsService=(SmsService)SpringHelper.getBean("SmsServiceImpl");
//		boolean b=smsService.sendSms(phoneNumber, content);
		boolean b = false;
		return b;
	}
}
