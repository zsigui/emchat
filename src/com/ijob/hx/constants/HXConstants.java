package com.ijob.hx.constants;

import com.ijob.hx.utils.HXUtils;

public class HXConstants {
	
	public static String API_HTTP_SCHEMA = "https";
	public static String API_SERVER_HOST = HXUtils.getProperties().getProperty(
			"API_SERVER_HOST");
	// appkey = org_name#app_name
	public static String APPKEY = HXUtils.getProperties().getProperty("APPKEY");
	public static String APP_CLIENT_ID = HXUtils.getProperties().getProperty(
			"APP_CLIENT_ID");
	public static String ORG_NAME = APPKEY.split("#")[0];
	public static String APP_NAME = APPKEY.split("#")[1];
	public static String APP_CLIENT_SECRET = HXUtils.getProperties()
			.getProperty("APP_CLIENT_SECRET");
	public static String ORG_ADMIN_USERNAME = HXUtils.getProperties()
			.getProperty("ORG_ADMIN_USERNAME");
	public static String ORG_ADMIN_PASSWORD = HXUtils.getProperties()
			.getProperty("ORG_ADMIN_PASSWORD");
	public static String DEFAULT_PASSWORD = "kaor234_3kire";

	/** USER_ROLE_APPADMIN value: appAdmin */
	public static String USER_ROLE_APPADMIN = "appAdmin";
}
