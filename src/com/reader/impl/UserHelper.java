package com.reader.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import com.reader.util.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author yuanweinan
 * 
 */
public class UserHelper {

	private Database dbHelper;
	private SQLiteDatabase db;
	Date date = null;

	public UserHelper(Context context) {
		dbHelper = new Database(context);
	}

	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {
		dbHelper.getWritableDatabase().close();
	}
	public void insertSong(String songname, boolean downloaded,
			Date downloadtime, String urlstr, String filepath, int position1,
			int position2, int position3, int position4, int position5,
			int filesize) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"INSERT INTO download(songname,downloaded,downloadtime,urlstr,filepath,position1,position2,position3,position4,position5,filesize)VALUES(?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { songname, downloaded, downloadtime, urlstr,
						filepath, position1, position2, position3, position4,
						position5, filesize });
		db.close();
	}

	/**
	 * 判断当前下载是否已经存在于数据库
	 * 
	 * @param urlstr
	 * @return
	 */
	public boolean isFind(String urlstr) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * from download where urlstr = ?;",
				new String[] { urlstr });
		System.out.println();
		if (cursor.moveToFirst()) {// 当cursor为空时回返回false
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * 由于下载地址是唯一的，所以根据下载地址和歌曲名删除指定行
	 * 
	 * @param songname
	 * @param urlstr
	 */
	public void deleteSong(String urlstr) {
		db = dbHelper.getWritableDatabase();
		db.execSQL("DELETE FROM download where urlstr=?;",
				new String[] { urlstr });
		db.close();
	}

	/**
	 * 更新downloaded的值 i=0表示正下载，i=1表示已下载
	 * 
	 * @param urlstr
	 */
	public void modifyDownloaded(String urlstr, int i) {
		db = dbHelper.getWritableDatabase();
		// ContentValues values = new ContentValues();
		// values.put("downloaded", true);
		// db.update("download", values,
		// "SELECT downloaded from download where urlstr=?;",
		// new String[] { urlstr });
		System.out.println("viewReceiver");
		db.execSQL("update download set downloaded=? where urlstr = ?;",
				new Object[] { i, urlstr });
		System.out.println("viewReceiver");
		db.close();

	}

	/**
	 * 对传近来的list按照date顺序进行重新排序
	 * 
	 * @param list
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> sortList(
			ArrayList<HashMap<String, Object>> list) {
		HashMap<String, Object> map = new HashMap<String, Object>(); // 使用data来存取数据
		ArrayList<HashMap<String, Object>> newlist = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> newmap = new HashMap<String, Object>(); // 使用data来存取数据
		for (int i = 0; i < list.size(); i++) {
			map = list.get(i);
			Date date = (Date) map.get("downloadtime");// 得到一个date的值

			for (int j = 1; j < list.size(); j++) {
				newmap = list.get(j);
				Date newdate = (Date) newmap.get("downloadtime");// 得到一个date的值
				if (newdate.compareTo(date) < 0) { // newdate小于date将返回负数
					map = newmap; // 保证map当前最小
				}
				newlist.add(map);
			}
		}
		return newlist;

	}

	/**
	 * 返回所有未下载完歌曲的列表，并按时间顺序进行了排序
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> downloadingList() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		db = dbHelper.getWritableDatabase();
		// 查询返回所有downloaded为false的行
		Cursor cursor = db
				.rawQuery(
						"SELECT songname,downloadtime,urlstr,filepath,position1,position2,position3,position4,position5,filesize from download where downloaded = 0;",
						null);
		if (!cursor.moveToFirst()) {
			System.out.println("cursor为空");
		} else {

			do {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("songname",
						cursor.getString(cursor.getColumnIndex("songname")));
				map.put("urlstr",
						cursor.getString(cursor.getColumnIndex("urlstr")));
				map.put("downloadtime", cursor.getColumnIndex("downloadtime"));
				map.put("position1",
						cursor.getInt(cursor.getColumnIndex("position1")));
				map.put("position2",
						cursor.getInt(cursor.getColumnIndex("position2")));
				map.put("position3",
						cursor.getInt(cursor.getColumnIndex("position3")));
				map.put("position4",
						cursor.getInt(cursor.getColumnIndex("position4")));
				map.put("position5",
						cursor.getInt(cursor.getColumnIndex("position5")));
				map.put("filesize",
						cursor.getInt(cursor.getColumnIndex("filesize")));
				list.add(map);
				// System.out.println("list == " + list);
			} while (cursor.moveToNext());
			// list = sortList(list); // 排序
			cursor.close();
		}
		db.close();
		return list;
	}

	/**
	 * 返回所有下载完歌曲的列表，并按时间顺序进行了排序
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> downloadedList() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		db = dbHelper.getWritableDatabase();
		// 查询返回所有downloaded为false的行
		Cursor cursor = db
				.rawQuery(
						"SELECT songname,downloadtime,urlstr,filepath,position1,position2,position3,position4,position5,filesize from download where downloaded = 1;",
						null);
		if (!cursor.moveToFirst()) {
			System.out.println("cursor为空");
		} else {
			do {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("songname",
						cursor.getString(cursor.getColumnIndex("songname")));
				map.put("urlstr",
						cursor.getString(cursor.getColumnIndex("urlstr")));
				map.put("downloadtime", cursor.getColumnIndex("downloadtime"));
				map.put("position1",
						cursor.getInt(cursor.getColumnIndex("position1")));
				map.put("position2",
						cursor.getInt(cursor.getColumnIndex("position2")));
				map.put("position3",
						cursor.getInt(cursor.getColumnIndex("position3")));
				map.put("position4",
						cursor.getInt(cursor.getColumnIndex("position4")));
				map.put("position5",
						cursor.getInt(cursor.getColumnIndex("position5")));
				map.put("filesize",
						cursor.getInt(cursor.getColumnIndex("filesize")));
				list.add(map);
			} while (cursor.moveToNext());
			// list = sortList(list); // 排序
			cursor.close();
		}
		db.close();
		return list;
	}

	/**
	 * 存入更新各歌曲相应断点的位置
	 * 
	 * @param position1
	 * @param position2
	 * @param position3
	 * @param position4
	 * @param position5
	 */
	public void insertPosition(String urlstr, int position1, int position2,
			int position3, int position4, int position5) {
		db = dbHelper.getWritableDatabase();
		// ContentValues values = new ContentValues();
		// values.put("position1", position1);
		// values.put("position2", position2);
		// values.put("position3", position3);
		// values.put("position4", position4);
		// values.put("position5", position5);
		// db.update(
		// "download",
		// values,
		// "SELECT position1,position2,position3,position4,position5 from download where urlstr=?;",
		// new String[] { urlstr });
		db.execSQL(
				"UPDATE download set position1 = ?,position2=?,position3=?,position4=?,position5=? where urlstr=?;",
				new Object[] { position1, position2, position3, position4,
						position5, urlstr });
		db.close();

	}

	/**
	 * 插入filesize
	 * 
	 * @param urlstr
	 * @param filesize
	 */
	public void insertFilesize(String urlstr, int filesize) {
		db = dbHelper.getWritableDatabase();
		db.execSQL("UPDATE download set filesize = ?;",
				new Object[] { filesize });
		db.close();
	}

	/**
	 * 返回特定歌曲的断点
	 * 
	 * @param songname
	 * @param urlstr
	 * @return
	 */
	public HashMap<String, Object> getPosition(String songname, String urlstr) {
		HashMap<String, Object> map = new HashMap<String, Object>(); // 使用data来存取数据
		db = dbHelper.getWritableDatabase();
		// Cursor cursor = db
		// .rawQuery(
		// "SELECT position1,position2,position3,position4,position5 from download where urlstr = "
		// + urlstr + ";", null);
		Cursor cursor = db
				.rawQuery(
						"SELECT position1,position2,position3,position4,position5,filesize from download where urlstr = ?;",
						new String[] { urlstr });
		System.out.println(cursor.moveToFirst());
		int filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
		int blockSize = filesize / 5 + 1;
		if (cursor.getInt(cursor.getColumnIndex("position1")) == 0)
			map.put("position1", 0);
		if (cursor.getInt(cursor.getColumnIndex("position2")) == 0)
			map.put("position2", blockSize);
		if (cursor.getInt(cursor.getColumnIndex("position3")) == 0)
			map.put("position3", blockSize * 2);
		if (cursor.getInt(cursor.getColumnIndex("position4")) == 0)
			map.put("position4", blockSize * 3);
		if (cursor.getInt(cursor.getColumnIndex("position5")) == 0)
			map.put("position5", blockSize * 4);
		cursor.close();
		db.close();
		return map;
	}

	/**
	 * 返回当前下载了多少
	 * 
	 * @param urlstr
	 * @return
	 */
	public int getCurrentSize(String urlstr) {
		db = dbHelper.getWritableDatabase();
		int currentsize = 0;
		int i;
		Cursor cursor = db
				.rawQuery(
						"SELECT position1,position2,position3,position4,position5,filesize from download where urlstr =?;",
						new String[] { urlstr });
		if (cursor.moveToFirst()) {
			int filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
			int blocksize = filesize / 5 + 1;
			// currentsize = cursor.getInt(cursor.getColumnIndex("position1"))
			// + cursor.getInt(cursor.getColumnIndex("position2"))
			// + cursor.getInt(cursor.getColumnIndex("position3"))
			// + cursor.getInt(cursor.getColumnIndex("position4"))
			// + cursor.getInt(cursor.getColumnIndex("position5"))
			// - blocksize * (1 + 2 + 3);
			if ((i = cursor.getInt(cursor.getColumnIndex("position1"))) != 0) {
				currentsize += i;
			}
			if ((i = cursor.getInt(cursor.getColumnIndex("position2"))) != 0) {
				currentsize += (i - blocksize);
			}
			if ((i = cursor.getInt(cursor.getColumnIndex("position3"))) != 0) {
				currentsize += (i - blocksize * 2);
			}
			if ((i = cursor.getInt(cursor.getColumnIndex("position4"))) != 0) {
				currentsize += (i - blocksize * 3);
			}
			if ((i = cursor.getInt(cursor.getColumnIndex("position5"))) != 0) {
				currentsize += (i - blocksize * 4);
			}

		} else {
			currentsize = 0;
		}
		cursor.close();
		db.close();
		return currentsize;
	}

	/**
	 * 删除歌曲列表 i=1删除已下载列表 i=0删除未下载列表
	 * 
	 * @param i
	 */
	public void deleteList(int i) {
		db = dbHelper.getWritableDatabase();
		db.execSQL("DELETE FROM download where downloaded = ?;",
				new Object[] { i });
		db.close();
	}

	/**
	 * 改变下载队列
	 * 
	 * @param fromId
	 * @param toId
	 */
	public void shiftRow(int fromId, int toId) {
		db = dbHelper.getWritableDatabase();
		db.execSQL("update download set download_id=? where download_id=?;",
				new Object[] { -1, fromId });
		db.execSQL("update download set download_id=? where download_id=?;",
				new Object[] { fromId, toId });
		db.execSQL("update download set download_id=? where download_id=?;",
				new Object[] { toId, -1 });
		db.close();
	}
}
