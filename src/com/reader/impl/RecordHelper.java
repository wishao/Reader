package com.reader.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.text.ParseException;

import com.reader.model.Book;
import com.reader.model.Record;
import com.reader.model.User;
import com.reader.util.Constant;
import com.reader.util.Database;
import com.reader.util.MD5Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecordHelper {

	private Database dbHelper;
	private SQLiteDatabase db;
	Date date = null;

	public RecordHelper(Context context) {
		dbHelper = new Database(context);
	}

	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {
		dbHelper.getWritableDatabase().close();
	}

	public void insertRecord(Record record) {
		String currenttime = Constant.sf.format(new Date());
		Timestamp createtime = Timestamp.valueOf(currenttime);
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"insert into t_record (id,user_id,book_id,record,evaluation,score,create_time,share) values (?,?,?,?,?,?,?,?);",
				new Object[] { record.getId(), record.getUser().getId(),
						record.getBook().getId(), record.getRecord(),
						record.getEvaluation(), record.getScore(), createtime,
						record.getShare() });
		db.close();
	}

	/**
	 * 更新用户
	 * 
	 * @param urlstr
	 */
	public void updateRecord(Record record) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"update t_record set user_id=?,book_id=?,record=?,evaluation=?,score=?,share=? where id=?;",
				new Object[] { record.getUser().getId(),
						record.getBook().getId(), record.getRecord(),
						record.getEvaluation(), record.getScore(),
						record.getShare(), record.getId() });
		db.close();

	}

	/**
	 * 查找记录
	 * 
	 * @param urlstr
	 */
	public Record findById(String id) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from t_record where id=?;",
				new String[] { id });
		cursor.moveToFirst();
		Record record = new Record();
		while (!cursor.isAfterLast()) {
			try {
				record.setId(cursor.getString(0));
				User user = new User();
				user.setId(cursor.getString(1));
				record.setUser(user);
				Book book = new Book();
				book.setId(cursor.getString(2));
				record.setBook(book);
				record.setRecord(cursor.getInt(3));
				record.setEvaluation(cursor.getString(4));
				record.setScore(cursor.getInt(5));
				record.setCreateTime(new Timestamp((Constant.sf).parse(
						cursor.getString(6)).getTime()));
				record.setShare(new Byte(cursor.getString(7)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cursor.close();
			db.close();
			return record;
		}
		cursor.close();
		db.close();
		return null;
	}

	/**
	 * 查找记录
	 * 
	 * @param urlstr
	 */
	public List<Record> findByUserId(String userId) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from t_record where user_id=?;",
				new String[] { userId });
		cursor.moveToFirst();
		List<Record> rl = new ArrayList<Record>();
		Record record = new Record();
		while (!cursor.isAfterLast()) {
			try {
				record = new Record();
				record.setId(cursor.getString(0));
				User user = new User();
				user.setId(cursor.getString(1));
				record.setUser(user);
				Book book = new Book();
				book.setId(cursor.getString(2));
				record.setBook(book);
				record.setRecord(cursor.getInt(3));
				record.setEvaluation(cursor.getString(4));
				record.setScore(cursor.getInt(5));
				record.setCreateTime(new Timestamp((Constant.sf).parse(
						cursor.getString(6)).getTime()));
				record.setShare(new Byte(cursor.getString(7)));
				rl.add(record);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cursor.close();
			db.close();
			return rl;
		}
		cursor.close();
		db.close();
		return null;
	}
}
