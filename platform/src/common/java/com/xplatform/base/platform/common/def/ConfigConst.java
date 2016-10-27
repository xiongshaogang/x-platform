package com.xplatform.base.platform.common.def;

import com.xplatform.base.framework.core.util.PropertiesUtil;

public interface ConfigConst {
	public static PropertiesUtil sysUtil = new PropertiesUtil("sysConfig.properties");
	String production_mode = sysUtil.readProperty("production_mode"); // 测试&正式环境推送

	String mail_host = sysUtil.readProperty("mail.smtp.host"); // 邮箱host
	String mail_from = sysUtil.readProperty("mail.from"); // 邮箱默认发送者邮箱地址
	String mail_username = sysUtil.readProperty("mail.username"); // 邮箱默认登录名
	String mail_password = sysUtil.readProperty("mail.smtp.password"); // 邮箱默认发送密码
	String mail_auth = sysUtil.readProperty("mail.smtp.auth"); // 邮箱权限
	String mail_port = sysUtil.readProperty("mail.smtp.port"); // 邮箱端口
	String mail_protocal = sysUtil.readProperty("mail.smtp.protocal"); // 邮箱协议类型

	Boolean pushSwitch = Boolean.parseBoolean(sysUtil.readProperty("pushSwitch")); // 推送启用开关

	Boolean smsUsername = Boolean.parseBoolean(sysUtil.readProperty("smsUsername")); // 短信启用开关
	Boolean smsPassword = Boolean.parseBoolean(sysUtil.readProperty("smsPassword")); // 推送启用开关
	Boolean smsSwitch = Boolean.parseBoolean(sysUtil.readProperty("smsSwitch")); // 短信启用开关

	String geetest_captcha_id = sysUtil.readProperty("geetest_captcha_id");//极验验证captcha_id
	String geetest_private_key = sysUtil.readProperty("geetest_private_key");//极验验证private_key
	
	String serverUrl=sysUtil.readProperty("serverUrl");//后台服务器域名
	String attachRequest=sysUtil.readProperty("attachRequest");//后台服务器获取附件的请求
	String attachImgRequest=sysUtil.readProperty("attachImgRequest");//后台服务器获取附件的请求
	String attachThumbnailRequest=sysUtil.readProperty("attachThumbnailRequest");//缩略图获取请求
	
	String tablePrefix = sysUtil.readProperty("tablePrefix");// 用户自定义应用产生物理表前缀
	/******************** 环信相关 begin*******************/
	// API_HTTP_SCHEMA
	public static String API_HTTP_SCHEMA = "https";
	// API_SERVER_HOST
	public static String API_SERVER_HOST = sysUtil.readProperty("API_SERVER_HOST");
	// APPKEY
	public static String APPKEY = sysUtil.readProperty("APPKEY");
	// APP_CLIENT_ID
	public static String APP_CLIENT_ID = sysUtil.readProperty("APP_CLIENT_ID");
	// APP_CLIENT_SECRET
	public static String APP_CLIENT_SECRET = sysUtil.readProperty("APP_CLIENT_SECRET");
	// DEFAULT_PASSWORD
	public static String DEFAULT_PASSWORD = "123456";
	
	public static String USER_ROLE_APPADMIN = "appAdmin";
	/******************** 环信相关 end*******************/
	
	/******************** TypeEntity begin*******************/
	public static String flowFormDefaultTypeId = "-2";
	public static String WorkflowDefaultTypeId = "-3";
	/******************** TypeEntity end*******************/
	
	public static int pageRows = 10;
	
	public static String DEFAULT_URL = "appFormTableController.do?commonFormEdit";  //FlowFormEntity里设置的默认的url
	
	public static String LOCALHOST_URL = "http://120.24.160.161:8080/attachController.do?downloadFile&aId=";
	
	public static String DOMAIN_NAME = "http://app.saifamc.com";  //正式库的域名
	
	/******************** 友盟相关  begin*******************/
	public static String android_appkey = sysUtil.readProperty("android_appkey");
	public static String android_appMasterSecret = sysUtil.readProperty("android_appMasterSecret");
	public static String ios_appkey = sysUtil.readProperty("ios_appkey");
	public static String ios_appMasterSecret = sysUtil.readProperty("ios_appMasterSecret");
	/******************** 友盟相关 end*******************/
}
