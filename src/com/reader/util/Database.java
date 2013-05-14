package com.reader.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	private final static String DATABASE_FILE_NAME = "lbsreader.db";
	private final static int VERSION = 1;

	public Database(Context context) {
		super(context, DATABASE_FILE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL("create table if not exists download ([download_id] INTEGER PRIMARY KEY AUTOINCREMENT, [downloaded] BOOLEAN,  [downloadtime] DATE, [songname] TEXT, [urlstr] TEXT, [filepath] TEXT, [position1] INTEGER, [position2] INTEGER, [position3] INTEGER, [position4] INTEGER, [position5] INTEGER, [filesize] INTEGER);");
		db.execSQL("CREATE TABLE t_user (id VARCHAR(36) NOT NULL,name VARCHAR(20) NOT NULL ,password VARCHAR(32) NOT NULL ,create_time DATETIME NOT NULL,address VARCHAR(20) ,signature TEXT(100),update_time DATETIME NOT NULL,status TINYINT(4) NOT NULL,PRIMARY KEY (id));");
		db.execSQL("CREATE TABLE t_book(id VARCHAR(36) NOT NULL,name TEXT(100) NOT NULL,author VARCHAR(20),content VARCHAR(100) NOT NULL,text TEXT NOT NULL,create_time DATETIME NOT NULL,update_time DATETIME NOT NULL,recommend TEXT(100),cover VARCHAR(100),reader INT NOT NULL,focus INT NOT NULL,catalog TEXT(100),score INT NOT NULL,status TINYINT(4) NOT NULL,PRIMARY KEY (id));");
		db.execSQL("CREATE TABLE t_record(id VARCHAR(36) NOT NULL,user_id VARCHAR(36) NOT NULL,book_id VARCHAR(36) NOT NULL,record INT NOT NULL,evaluation TEXT(100),score INT,create_time DATETIME NOT NULL,share TINYINT(1) NOT NULL,PRIMARY KEY (id));");
		db.execSQL("CREATE TABLE t_reader(id VARCHAR(36) NOT NULL,user_id VARCHAR(36) NOT NULL,font VARCHAR(20) NOT NULL,background_color VARCHAR(10) NOT NULL,font_color VARCHAR(10) NOT NULL,create_time DATETIME NOT NULL,PRIMARY KEY (id));");
		db.execSQL("CREATE TABLE t_contact(id VARCHAR(36) NOT NULL,send_user_id VARCHAR(36) NOT NULL,receive_user_id VARCHAR(36) NOT NULL,content TEXT(100),create_time DATETIME NOT NULL,PRIMARY KEY (id));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS t_user;");
		db.execSQL("DROP TABLE IF EXISTS t_book;");
		db.execSQL("DROP TABLE IF EXISTS t_record;");
		db.execSQL("DROP TABLE IF EXISTS t_reader;");
		db.execSQL("DROP TABLE IF EXISTS t_contact;");
		onCreate(db);

	}

}
