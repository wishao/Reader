package com.reader.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.reader.model.User;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginListener implements OnClickListener {
	private Context context;
	private User user;

	public LoginListener(Context context, User user) {
		this.context = context;
		this.user = user;
	}

	@Override
	public void onClick(View v) {
		String httpUrl = "http://localhost:8080/LBS-Reader/LBS-Reader/admin!login";
		HttpPost request = new HttpPost(httpUrl);
		HttpClient httpClient = new DefaultHttpClient();
		// 传递参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("loginName", user.getName()));
		params.add(new BasicNameValuePair("loginPassword", user.getPassword()));
		HttpResponse response;
		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			request.setEntity(entity);
			response = httpClient.execute(request);

			// 如果返回状态为200，获得返回的结果
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = EntityUtils.toString(response.getEntity());
				System.out.println("response:" + str);
				if (str.trim().equals("success")) {
					// 用户登录成功
					System.out.println("登录成功！");
					// Intent intent = new
					// Intent(GossipActivity.this,GossiplistActivity.class);
					// context.startActivity(intent);
				} else {
					// 用户登录失败
					System.out.println("登录失败！");
				}
			} else {
				System.out.println("连接失败！");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
