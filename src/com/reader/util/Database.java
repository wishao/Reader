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
		db.execSQL("DROP TABLE IF EXISTS t_user;");
		db.execSQL("CREATE TABLE t_user(ARCHAR(36) NOT NULL COMMENT 'Id',name VARCHAR(20) NOT NULL COMMENT '用户用户名',password VARCHAR(32) NOT NULL COMMENT '密码',create_time DATETIME NOT NULL COMMENT '创建时间',address VARCHAR(20) COMMENT '最近地址',signature TEXT(100) COMMENT '签名档',update_time DATETIME NOT NULL COMMENT '更新时间',status TINYINT(4) NOT NULL COMMENT '是否有效',PRIMARY KEY (id))ENGINE=INNODB DEFAULT CHARSET=UTF8 COMMENT='用户表';");
		db.execSQL("DROP TABLE IF EXISTS t_book;");
		db.execSQL("CREATE TABLE t_book(id VARCHAR(36) NOT NULL COMMENT 'Id',name TEXT(100) NOT NULL COMMENT '书名',author VARCHAR(20) COMMENT '作者',content VARCHAR(100) NOT NULL COMMENT '内容文本链接',create_time DATETIME NOT NULL COMMENT '创建时间',update_time DATETIME NOT NULL COMMENT '更新时间',recommend TEXT(100) COMMENT '简介',cover VARCHAR(100) COMMENT '封面链接',reader INT NOT NULL COMMENT '阅读人数',focus INT NOT NULL COMMENT '关注人数',catalog TEXT(100) COMMENT '目录',score INT NOT NULL COMMENT '平均得分',status TINYINT(4) NOT NULL COMMENT '是否有效',PRIMARY KEY (id))ENGINE=INNODB DEFAULT CHARSET=UTF8 COMMENT='书本表';");
		db.execSQL("DROP TABLE IF EXISTS t_record;");
		db.execSQL("CREATE TABLE t_record(id VARCHAR(36) NOT NULL COMMENT 'Id',user_id VARCHAR(36) NOT NULL COMMENT '用户Id',book_id VARCHAR(36) NOT NULL COMMENT '书本Id',record INT NOT NULL COMMENT '阅读记录',evaluation TEXT(100) COMMENT '点评',score INT COMMENT '评分',create_time DATETIME NOT NULL COMMENT '创建时间',share TINYINT(1) NOT NULL COMMENT '是否分享',PRIMARY KEY (id))ENGINE=INNODB DEFAULT CHARSET=UTF8 COMMENT='阅读记录表';");
		db.execSQL("DROP TABLE IF EXISTS t_reader;");
		db.execSQL("CREATE TABLE t_reader(id VARCHAR(36) NOT NULL COMMENT 'Id',user_id VARCHAR(36) NOT NULL COMMENT '用户Id',font VARCHAR(20) NOT NULL COMMENT '字体大小',background_color VARCHAR(10) NOT NULL COMMENT '背景颜色',font_color VARCHAR(10) NOT NULL COMMENT '字体颜色',create_time DATETIME NOT NULL COMMENT '创建时间',PRIMARY KEY (id))ENGINE=INNODB DEFAULT CHARSET=UTF8 COMMENT='阅读器设置表';");
		db.execSQL("DROP TABLE IF EXISTS t_contact;");
		db.execSQL("CREATE TABLE t_contact(id VARCHAR(36) NOT NULL COMMENT 'Id',send_user_id VARCHAR(36) NOT NULL COMMENT '发送方Id',receive_user_id VARCHAR(36) NOT NULL COMMENT '接收方Id',content TEXT(100) COMMENT '内容',create_time DATETIME NOT NULL COMMENT '时间',PRIMARY KEY (id))ENGINE=INNODB DEFAULT CHARSET=UTF8 COMMENT='聊天记录表';");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);

	}

}
