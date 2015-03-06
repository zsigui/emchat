package com.ijob.hx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

public class HXUtils {
	private static final String USER_REGEX = "[a-zA-Z0-9_\\-./]*";
	private static final String HTTP_URL_REGEX = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
	private static final String APPKEY_REGEX = "^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+";
	
	/**
	 * 判断是否匹配用户
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isMatchUser(String input) {
		return Pattern.matches(HXUtils.USER_REGEX, input);
	}
	
	/**
	 * 判断URL地址是否符合规范
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isMatchUrl(String input) {
		return Pattern.matches(HXUtils.HTTP_URL_REGEX, input);
	}
	
	/**
	 * 判断是否匹配APPkey
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isMatchAPPKey(String input) {
		return Pattern.matches(HXUtils.APPKEY_REGEX, input);
	}

	/**
	 * 读取配置文件
	 * 
	 * @return
	 */
	public static Properties getProperties() {
		Properties p = new Properties();
		try {
			InputStream is = HXUtils.class.getClassLoader().getResourceAsStream("HXRestAPIConfig.properties");
			p.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
}
