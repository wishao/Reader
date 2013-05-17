package com.reader.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.reader.model.Reader;
import com.reader.model.User;
import com.reader.util.Constant;
import com.reader.util.Database;
import com.reader.util.MD5Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReaderHelper {

	private Database dbHelper;
	private SQLiteDatabase db;
	Date date = null;

	public ReaderHelper(Context context) {
		dbHelper = new Database(context);
	}

	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {
		dbHelper.getWritableDatabase().close();
	}

	public void insertReader(Reader reader) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"insert into t_reader (id,user_id,font,background_color,font_color,create_time) values (?,?,?,?,?,?);",
				new Object[] { reader.getId(), reader.getUser().getId(),
						reader.getFont(), reader.getBackgroundColor(),
						reader.getFontColor(), reader.getCreateTime() });
		db.close();
	}

	/**
	 * 离线登录
	 * 
	 * @param urlstr
	 * @return
	 */
	@SuppressWarnings("null")
	public User login(User user) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select id,name,password,create_time,address,signature,update_time,status from t_user where name=? and password=?;",
						new String[] { user.getName(),
								MD5Util.getMD5(user.getPassword()) });
		cursor.moveToFirst();
		user = null;
		if (!cursor.isAfterLast()) {
			try {
				user.setId(cursor.getString(cursor.getColumnIndex("id")));
				user.setName(cursor.getString(cursor.getColumnIndex("name")));
				user.setPassword(cursor.getString(cursor
						.getColumnIndex("password")));
				user.setCreateTime(new Timestamp((Constant.sf).parse(
						cursor.getString(cursor.getColumnIndex("create_time")))
						.getTime()));
				user.setAddress(cursor.getString(cursor
						.getColumnIndex("address")));
				user.setSignature(cursor.getString(cursor
						.getColumnIndex("signature")));
				user.setUpdateTime(new Timestamp((Constant.sf).parse(
						cursor.getString(cursor.getColumnIndex("update_time")))
						.getTime()));
				user.setStatus(new Byte(cursor.getString(cursor
						.getColumnIndex("status"))));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cursor.close();
		db.close();
		return user;
	}

	/**
	 * 删除之前的数据
	 * 
	 */
	public void deleteReader() {
		db = dbHelper.getWritableDatabase();
		db.execSQL("DELETE FROM t_reader;");
		db.close();
	}

	/**
	 * 更新阅读器
	 * 
	 * @param urlstr
	 */
	public void updateReader(Reader reader) {
		String currenttime = Constant.sf.format(new Date());
		Timestamp createtime = Timestamp.valueOf(currenttime);
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"update t_reader set id=?,user_id=?,font=?,background_color=?,font_color=?,create_time=?;",
				new Object[] { reader.getId(), reader.getUser().getId(),
						reader.getFont(), reader.getBackgroundColor(),
						reader.getFontColor(), createtime });
		db.close();

	}

	/**
	 * 查找阅读器
	 * 
	 * @param urlstr
	 */
	public Reader findReader() {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from t_reader;", new String[] {});
		cursor.moveToFirst();
		Reader reader = new Reader();
		while (!cursor.isAfterLast()) {
			try {
				reader.setId(cursor.getString(0));
				User user = new User();
				user.setId(cursor.getString(1));
				reader.setUser(user);
				reader.setFont(cursor.getString(2));
				reader.setBackgroundColor(cursor.getString(3));
				reader.setFontColor(cursor.getString(4));
				reader.setCreateTime(new Timestamp((Constant.sf).parse(
						cursor.getString(5)).getTime()));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cursor.close();
			db.close();
			return reader;
		}
		cursor.close();
		db.close();
		return null;
	}
}
