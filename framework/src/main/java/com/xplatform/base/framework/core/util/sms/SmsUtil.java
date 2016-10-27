package com.xplatform.base.framework.core.util.sms;

public class SmsUtil {
	private static JXTSmsClient client = new JXTSmsClient();

	public static String send(String mobiles, String content) {
		return client.sendSms("spreadingwind", "abc123", mobiles, content, "");
	}
}
