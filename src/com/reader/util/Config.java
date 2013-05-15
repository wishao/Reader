package com.reader.util;

public class Config {
	public static final String HTTP_IP = "192.168.0.179";
	public static final String HTTP_PATH = "http://" + HTTP_IP
			+ ":8080/LBS-Reader/";
	public static final String HTTP_LOGIN = HTTP_PATH + "user!login";
	public static final String HTTP_UPDATE_PASSWORD = HTTP_PATH
			+ "user!updatePasswordFromClient";
	public static final String HTTP_REGISTER = HTTP_PATH + "user!addFromClient";

	public static final String HTTP_READER_SELECT = HTTP_PATH
			+ "reader!selectByUser";
}
