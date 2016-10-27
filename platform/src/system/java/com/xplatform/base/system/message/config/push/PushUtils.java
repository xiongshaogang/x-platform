package com.xplatform.base.system.message.config.push;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;

import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.util.pdf.HttpUtils;
import com.xplatform.base.platform.common.def.ConfigConst;
import com.xplatform.base.system.message.config.push.android.AndroidBroadcast;
import com.xplatform.base.system.message.config.push.android.AndroidCustomizedcast;
import com.xplatform.base.system.message.config.push.android.AndroidFilecast;
import com.xplatform.base.system.message.config.push.android.AndroidGroupcast;
import com.xplatform.base.system.message.config.push.android.AndroidUnicast;
import com.xplatform.base.system.message.config.push.ios.IOSBroadcast;
import com.xplatform.base.system.message.config.push.ios.IOSCustomizedcast;
import com.xplatform.base.system.message.config.push.ios.IOSFilecast;
import com.xplatform.base.system.message.config.push.ios.IOSGroupcast;
import com.xplatform.base.system.message.config.push.ios.IOSUnicast;


public class PushUtils {
	// The host
	protected static final String host = "http://msg.umeng.com";
	// The upload path
	protected static final String uploadPath = "/upload";
	// The post path
	protected static final String postPath = "/api/send";
		
//	public static String android_appkey = "55b30e40e0f55a0bd6007c29";
//	public static String android_appMasterSecret = "ibmvjjz3aphnapt6zdgqdbinma92misv";
//	public static String ios_appkey = "55b44661e0f55a489b00487f";
//	public static String ios_appMasterSecret = "zbjatq8b8ph0d0n6wcnhgrfk1kskrjhc";
	public static String android_appkey = "55f69b1467e58efea200064a";
	public static String android_appMasterSecret = "spe906chh8pwykauwu3selfnktvrovjw";
	public static String ios_appkey = "55f77f3567e58eeea5000fac";
	public static String ios_appMasterSecret = "nlwkwnmfbaqfom42zyzzow7bcjowontw";
	public String timestamp = null;

	public PushUtils() {
	}

	public static AjaxJson sendAndroidBroadcast(AndroidBroadcast androidBroadcast) throws Exception {
		androidBroadcast.setAppMasterSecret(android_appMasterSecret);
		androidBroadcast.setPredefinedKeyValue("appkey", android_appkey);
		androidBroadcast.setPredefinedKeyValue("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000)));
		androidBroadcast.setPredefinedKeyValue("production_mode", ConfigConst.production_mode);
		// Set customized fields
		// broadcast.setExtraField("test", "helloworld");
		return androidBroadcast.send();
	}

	public static AjaxJson sendAndroidUnicast(AndroidUnicast androidUnicast) throws Exception {
		androidUnicast.setAppMasterSecret(android_appMasterSecret);
		androidUnicast.setPredefinedKeyValue("appkey", android_appkey);
		androidUnicast.setPredefinedKeyValue("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000)));
		androidUnicast.setPredefinedKeyValue("production_mode", ConfigConst.production_mode);
		return androidUnicast.send();
	}

	public void sendAndroidGroupcast() throws Exception {
		AndroidGroupcast groupcast = new AndroidGroupcast();
		groupcast.setAppMasterSecret(android_appMasterSecret);
		groupcast.setPredefinedKeyValue("appkey", android_appkey);
		groupcast.setPredefinedKeyValue("timestamp", this.timestamp);
		/*
		 * TODO Construct the filter condition: "where": { "and": [ {"tag":"test"}, {"tag":"Test"} ] }
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		JSONObject TestTag = new JSONObject();
		testTag.put("tag", "test");
		TestTag.put("tag", "Test");
		tagArray.add(testTag);
		tagArray.add(TestTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());

		groupcast.setPredefinedKeyValue("filter", filterJson);
		groupcast.setPredefinedKeyValue("ticker", "Android groupcast ticker");
		groupcast.setPredefinedKeyValue("title", "中文的title");
		groupcast.setPredefinedKeyValue("text", "Android groupcast text");
		groupcast.setPredefinedKeyValue("after_open", "go_app");
		groupcast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		groupcast.setPredefinedKeyValue("production_mode", ConfigConst.production_mode);
		groupcast.send();
	}

	public static AjaxJson sendAndroidCustomizedcast(AndroidCustomizedcast androidCustomizedcast) throws Exception {
		androidCustomizedcast.setAppMasterSecret(android_appMasterSecret);
		androidCustomizedcast.setPredefinedKeyValue("appkey", android_appkey);
		androidCustomizedcast.setPredefinedKeyValue("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000)));
		androidCustomizedcast.setPredefinedKeyValue("production_mode", ConfigConst.production_mode);
		return androidCustomizedcast.send();
	}

	public void sendAndroidFilecast() throws Exception {
		AndroidFilecast filecast = new AndroidFilecast();
		filecast.setAppMasterSecret(android_appMasterSecret);
		filecast.setPredefinedKeyValue("appkey", android_appkey);
		filecast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
		filecast.uploadContents("aa" + "\n" + "bb");
		filecast.setPredefinedKeyValue("ticker", "Android filecast ticker");
		filecast.setPredefinedKeyValue("title", "中文的title");
		filecast.setPredefinedKeyValue("text", "Android filecast text");
		filecast.setPredefinedKeyValue("after_open", "go_app");
		filecast.setPredefinedKeyValue("display_type", "notification");
		filecast.send();
	}

	public static AjaxJson sendIOSBroadcast(IOSBroadcast iosBroadcast) throws Exception {
		iosBroadcast.setAppMasterSecret(ios_appMasterSecret);
		iosBroadcast.setPredefinedKeyValue("appkey", ios_appkey);
		iosBroadcast.setPredefinedKeyValue("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000)));
		iosBroadcast.setPredefinedKeyValue("badge", 0);
		iosBroadcast.setPredefinedKeyValue("sound", "chime");
		iosBroadcast.setPredefinedKeyValue("production_mode", ConfigConst.production_mode);
		// Set customized fields
		// iosBroadcast.setCustomizedField("test", "helloworld");
		return iosBroadcast.send();
	}

	public static AjaxJson sendIOSUnicast(IOSUnicast iosUnicast) throws Exception {
		iosUnicast.setAppMasterSecret(ios_appMasterSecret);
		iosUnicast.setPredefinedKeyValue("appkey", ios_appkey);
		iosUnicast.setPredefinedKeyValue("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000)));
		iosUnicast.setPredefinedKeyValue("badge", 0);
		iosUnicast.setPredefinedKeyValue("sound", "chime");
		iosUnicast.setPredefinedKeyValue("production_mode", ConfigConst.production_mode);
		return iosUnicast.send();
	}

	public void sendIOSGroupcast() throws Exception {
		IOSGroupcast groupcast = new IOSGroupcast();
		groupcast.setAppMasterSecret(ios_appMasterSecret);
		groupcast.setPredefinedKeyValue("appkey", ios_appkey);
		groupcast.setPredefinedKeyValue("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000)));
		/*
		 * TODO Construct the filter condition: "where": { "and": [ {"tag":"iostest"} ] }
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		testTag.put("tag", "iostest");
		tagArray.add(testTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());

		// Set filter condition into rootJson
		groupcast.setPredefinedKeyValue("filter", filterJson);
		groupcast.setPredefinedKeyValue("alert", "IOS 组播测试");
		groupcast.setPredefinedKeyValue("badge", 0);
		groupcast.setPredefinedKeyValue("sound", "chime");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		groupcast.setPredefinedKeyValue("production_mode", ConfigConst.production_mode);
		groupcast.send();
	}

	public static AjaxJson sendIOSCustomizedcast(IOSCustomizedcast iosCustomizedcast) throws Exception {
		iosCustomizedcast.setAppMasterSecret(ios_appMasterSecret);
		iosCustomizedcast.setPredefinedKeyValue("appkey", ios_appkey);
		iosCustomizedcast.setPredefinedKeyValue("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000)));
		iosCustomizedcast.setPredefinedKeyValue("badge", 0);
		iosCustomizedcast.setPredefinedKeyValue("sound", "chime");
		iosCustomizedcast.setPredefinedKeyValue("production_mode", ConfigConst.production_mode);
		return iosCustomizedcast.send();
	}

	public void sendIOSFilecast() throws Exception {
		IOSFilecast filecast = new IOSFilecast();
		filecast.setAppMasterSecret(ios_appMasterSecret);
		filecast.setPredefinedKeyValue("appkey", ios_appkey);
		filecast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
		filecast.uploadContents("aa" + "\n" + "bb");
		filecast.setPredefinedKeyValue("alert", "IOS 文件播测试");
		filecast.setPredefinedKeyValue("badge", 0);
		filecast.setPredefinedKeyValue("sound", "chime");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		filecast.setPredefinedKeyValue("production_mode", ConfigConst.production_mode);
		filecast.send();
	}
	
	public static AjaxJson sendByAndroidJson(String paramJson) throws Exception {
		AjaxJson j = new AjaxJson();
		String url = host + postPath;

		String sign = DigestUtils.md5Hex(("POST" + url + paramJson +  ConfigConst.android_appMasterSecret).getBytes("utf8"));
		url = url + "?sign=" + sign;

		String result = HttpUtils.requestByString(url, paramJson);
		j.setInfo(result);
		return j;
	}
	
	public static AjaxJson sendByIOSJson(String paramJson) throws Exception {
		AjaxJson j = new AjaxJson();
		String url = host + postPath;

		String sign = DigestUtils.md5Hex(("POST" + url + paramJson + ConfigConst.ios_appMasterSecret).getBytes("utf8"));
		url = url + "?sign=" + sign;

		String result = HttpUtils.requestByString(url, paramJson);
		j.setInfo(result);
		return j;
	}

	public static void main(String[] args) {
		// TODO set your appkey and master secret here
		// PutshUtils demo = new PushUtils("55b30e40e0f55a0bd6007c29", "ibmvjjz3aphnapt6zdgqdbinma92misv");
		try {
			// demo.sendAndroidUnicast();
			/*
			 * TODO these methods are all available, just fill in some fields and do the test
			 * demo.sendAndroidBroadcast(); demo.sendAndroidGroupcast(); demo.sendAndroidCustomizedcast();
			 * demo.sendAndroidFilecast();
			 * 
			 * demo.sendIOSBroadcast(); demo.sendIOSUnicast(); demo.sendIOSGroupcast(); demo.sendIOSCustomizedcast();
			 * demo.sendIOSFilecast();
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
