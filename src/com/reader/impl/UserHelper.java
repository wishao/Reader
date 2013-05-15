package com.reader.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

import com.reader.model.User;
import com.reader.util.Constant;
import com.reader.util.Database;
import com.reader.util.MD5Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserHelper {

	private Database dbHelper;
	private SQLiteDatabase db;
	private Context context;
	Date date = null;

	public UserHelper(Context context) {
		this.context = context;
		dbHelper = new Database(context);
	}

	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {
		dbHelper.getWritableDatabase().close();
	}

	public void insertUser(User user) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"insert into t_user (id,name,password,create_time,address,signature,update_time,status) values (?,?,?,?,?,?,?,?);",
				new Object[] { user.getId(), user.getName(),
						user.getPassword(), user.getCreateTime(),
						user.getAddress(), user.getSignature(),
						user.getUpdateTime(), user.getStatus() });
		db.close();
	}

	/**
	 * 离线登录
	 * 
	 * @param urlstr
	 * @return
	 */
	public User login(User user) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from t_user where name=? and password=?;",
				new String[] { user.getName(),
						MD5Util.getMD5(user.getPassword()) });
		cursor.moveToFirst();
		User resultUser = new User();
		if (!cursor.isAfterLast()) {
			try {
				resultUser.setId(cursor.getString(0));
				resultUser.setName(cursor.getString(1));
				resultUser.setPassword(cursor.getString(2));
				resultUser.setCreateTime(new Timestamp((Constant.sf).parse(
						cursor.getString(3)).getTime()));
				resultUser.setAddress(cursor.getString(4));
				resultUser.setSignature(cursor.getString(5));
				resultUser.setUpdateTime(new Timestamp((Constant.sf).parse(
						cursor.getString(6)).getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cursor.close();
			db.close();
			return resultUser;
		} else {
			return null;
		}

	}

	/**
	 * 删除之前的用户
	 * 
	 */
	public void deleteUser() {
		db = dbHelper.getWritableDatabase();
		db.execSQL("DELETE FROM t_user;");
		db.close();
	}

	/**
	 * 更新用户
	 * 
	 * @param urlstr
	 */
	public void updateUser(User user) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"update t_user set id=?,name=?,password=?,create_time=?,address=?,signature=?,update_time=?,status=?;",
				new Object[] { user.getId(), user.getName(),
						MD5Util.getMD5(user.getPassword()),
						user.getCreateTime(), user.getAddress(),
						user.getSignature(), user.getUpdateTime(),
						user.getStatus() });
		db.close();

	}

	/**
	 * 查找用户
	 * 
	 * @param urlstr
	 */
	public User findUser() {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from t_user;", new String[] {});
		cursor.moveToFirst();
		User user = new User();
		while (!cursor.isAfterLast()) {
			try {
				user.setId(cursor.getString(0));
				user.setName(cursor.getString(1));
				user.setPassword(cursor.getString(2));
				user.setCreateTime(new Timestamp((Constant.sf).parse(
						cursor.getString(3)).getTime()));
				user.setAddress(cursor.getString(4));
				user.setSignature(cursor.getString(5));
				user.setUpdateTime(new Timestamp((Constant.sf).parse(
						cursor.getString(6)).getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cursor.close();
			db.close();
			return user;
		}
		cursor.close();
		db.close();
		return null;
	}
}
