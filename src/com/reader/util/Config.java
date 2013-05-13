package com.reader.util;

public class Config {
	public static final String HTTP_IP = "192.168.1.102";
	public static final String HTTP_PATH = "http://" + HTTP_IP
			+ ":8080/LBS-Reader/";
	public static final String HTTP_LOGIN = HTTP_PATH + "user!login";
	public static final String HTTP_REGISTER = HTTP_PATH + "user!addFromClient";
}
