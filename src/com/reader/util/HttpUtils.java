package com.reader.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HttpUtils {

	private static HttpURLConnection httpURLConnection;
	private static URL url;

	public static String getContent(String path) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		String content = null;
		Reader reader = null;
		try {
			url = new URL(path);

			httpURLConnection = (HttpURLConnection) url.openConnection();

			httpURLConnection.setConnectTimeout(3 * 1000);
			httpURLConnection.setReadTimeout(3 * 1000);
			if (httpURLConnection.getResponseCode() == 200) {
				reader = new InputStreamReader(new BufferedInputStream(
						httpURLConnection.getInputStream()), "UTF-8");
				int c;
				StringBuffer sb = new StringBuffer();
				while ((c = reader.read()) != -1) {
					sb.append((char) c);
				}
				content = sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return content;
	}

	public static String getContentByPost(String path, String params) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		String content = null;
		Reader reader = null;
		try {
			url = new URL(path);

			httpURLConnection = (HttpURLConnection) url.openConnection();

			httpURLConnection.setConnectTimeout(3 * 1000);
			httpURLConnection.setReadTimeout(3 * 1000);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.getOutputStream().write(params.getBytes());

			if (httpURLConnection.getResponseCode() == 200) {
				reader = new InputStreamReader(new BufferedInputStream(
						httpURLConnection.getInputStream()), "UTF-8");
				int c;
				StringBuffer sb = new StringBuffer();
				while ((c = reader.read()) != -1) {
					sb.append((char) c);
				}
				content = sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return content;
	}

	public static JSONObject getJsonByPost(String path, String params) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		String content = null;
		Reader reader = null;
		JSONObject jsonObject = null;
		try {
			url = new URL(path);

			httpURLConnection = (HttpURLConnection) url.openConnection();

			httpURLConnection.setConnectTimeout(3 * 1000);
			httpURLConnection.setReadTimeout(3 * 1000);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.getOutputStream().write(params.getBytes());

			if (httpURLConnection.getResponseCode() == 200) {
				reader = new InputStreamReader(new BufferedInputStream(
						httpURLConnection.getInputStream()), "UTF-8");
				int c;
				StringBuffer sb = new StringBuffer();
				while ((c = reader.read()) != -1) {
					sb.append((char) c);
				}
				content = sb.toString();
				jsonObject = new JSONObject(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return jsonObject;
	}

}
