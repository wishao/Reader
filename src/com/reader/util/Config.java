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
	public static final String HTTP_READER_UPDATE = HTTP_PATH + "reader!update";
	public static final String HTTP_BOOK_UPDATE = HTTP_PATH
			+ "book!allByClient";
	public static final String HTTP_RECORD_READ = HTTP_PATH + "book!read";
	public static final String HTTP_RECORD_SELECT = HTTP_PATH
			+ "record!allByClient";
	public static final String HTTP_RECORD_ADD = HTTP_PATH + "record!add";
	public static final String HTTP_RECORD_SELECT_ID = HTTP_PATH
			+ "record!getById";
	public static final String HTTP_RECORD_UPDATE_READ = HTTP_PATH
			+ "record!updateByReader";

}
