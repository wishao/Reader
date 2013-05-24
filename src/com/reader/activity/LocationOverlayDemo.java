package com.reader.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.utils.CoordinateConver;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.reader.R;
import com.reader.impl.UserHelper;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.HttpUtils;

public class LocationOverlayDemo extends Activity {
	double cLat;
	double cLon;
	// 存放overlayitem
	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	// 存放overlay图片
	public List<Drawable> res = new ArrayList<Drawable>();
	static MapView mMapView = null;

	private MapController mMapController = null;

	public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public NotifyLister mNotifyer = null;

	Button testUpdateButton = null;

	EditText indexText = null;
	MyLocationOverlay myLocationOverlay = null;
	int index = 0;
	LocationData locData = null;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			/*
			 * Toast.makeText(LocationOverlayDemo.this, "msg:" + msg.what,
			 * Toast.LENGTH_SHORT).show();
			 */
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locationoverlay);

		mMapView = (MapView) findViewById(R.id.bmapView);

		mMapController = mMapView.getController();

		initMapView();

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		// 位置提醒相关代码
		mNotifyer = new NotifyLister();
		mNotifyer.SetNotifyLocation(42.03249652949337, 113.3129895882556, 3000,
				"bd09ll");// 4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
		mLocClient.registerNotify(mNotifyer);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);

		mMapView.displayZoomControls(true);
		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(LocationOverlayDemo.this, title,
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager,
				mMapListener);
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();

		myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();

		cLat = locData.latitude;
		cLon = locData.longitude;
		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(LocationOverlayDemo.this, title,
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		res.add(getResources().getDrawable(R.drawable.icon_marka));
		res.add(getResources().getDrawable(R.drawable.icon_markb));
		res.add(getResources().getDrawable(R.drawable.icon_markc));
		res.add(getResources().getDrawable(R.drawable.icon_markd));
		res.add(getResources().getDrawable(R.drawable.icon_marke));
		res.add(getResources().getDrawable(R.drawable.icon_markf));
		res.add(getResources().getDrawable(R.drawable.icon_markg));
		res.add(getResources().getDrawable(R.drawable.icon_markh));
		res.add(getResources().getDrawable(R.drawable.icon_marki));
		// overlay 数量
		int iSize = 20;
		double pi = 3.1415926;
		// overlay半径
		double r = 0.5;
		// 准备overlay 数据
		for (int i = 0; i < iSize; i++) {
			int lat = (int) (cLat + r * Math.cos(2 * i * pi / iSize));
			int lon = (int) (cLon + r * Math.sin(2 * i * pi / iSize));
			OverlayItem item = new OverlayItem(new GeoPoint(lat, lon), "item"
					+ i, "item" + i);
			item.setMarker(res.get(i % (res.size())));
			mGeoList.add(item);
		}

		testUpdateButton = (Button) findViewById(R.id.button1);
		OnClickListener clickListener = new OnClickListener() {
			public void onClick(View v) {
				testUpdateClick();
				mLocClient.start();
				UserHelper helper = new UserHelper(getApplicationContext());
				User user = helper.findUser();
				String address = locData.latitude + "," + locData.longitude;
				user.setAddress(address);
				helper.updateUser(user);
				String path = Config.HTTP_UPDATE_ADDRESS;
				String params = "id=" + user.getId() + "&address="
						+ user.getAddress();
				JSONObject result = HttpUtils.getJsonByPost(path, params);
				mLocClient.stop();

				Toast.makeText(LocationOverlayDemo.this, address,
						Toast.LENGTH_SHORT).show();

			}
		};
		testUpdateButton.setOnClickListener(clickListener);

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	public void testUpdateClick() {
		mLocClient.requestLocation();
	}

	private void initMapView() {
		mMapView.setLongClickable(true);
		// mMapController.setMapClickEnable(true);
		// mMapView.setSatellite(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			locData.direction = 2.0f;
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			Log.d("loctest",
					String.format("before: lat: %f lon: %f",
							location.getLatitude(), location.getLongitude()));
			myLocationOverlay.setData(locData);
			mMapView.refresh();
			mMapController
					.animateTo(new GeoPoint((int) (locData.latitude * 1e6),
							(int) (locData.longitude * 1e6)), mHandler
							.obtainMessage(1));

			cLat = locData.latitude;
			cLon = locData.longitude;
			mMapListener = new MKMapViewListener() {
				
				@Override
				public void onMapMoveFinish() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onClickMapPoi(MapPoi mapPoiInfo) {
					// TODO Auto-generated method stub
					String title = "";
					if (mapPoiInfo != null){
						title = mapPoiInfo.strText;
						Toast.makeText(LocationOverlayDemo.this,title,Toast.LENGTH_SHORT).show();
					}
				}
			};
			res.add(getResources().getDrawable(R.drawable.icon_marka));
			res.add(getResources().getDrawable(R.drawable.icon_markb));
			res.add(getResources().getDrawable(R.drawable.icon_markc));
			res.add(getResources().getDrawable(R.drawable.icon_markd));
			res.add(getResources().getDrawable(R.drawable.icon_marke));
			res.add(getResources().getDrawable(R.drawable.icon_markf));
			res.add(getResources().getDrawable(R.drawable.icon_markg));
			res.add(getResources().getDrawable(R.drawable.icon_markh));
			res.add(getResources().getDrawable(R.drawable.icon_marki));
			// overlay 数量
			int iSize = 20;
			double pi = 3.1415926;
			// overlay半径
			int r = 50000;
			// 准备overlay 数据
			for (int i = 0; i < iSize; i++) {
				int lat = (int) (cLat + r * Math.cos(2 * i * pi / iSize));
				int lon = (int) (cLon + r * Math.sin(2 * i * pi / iSize));
				OverlayItem item = new OverlayItem(new GeoPoint(lat, lon),
						"item" + i, "item" + i);
				item.setMarker(res.get(i % (res.size())));
				mGeoList.add(item);
			}

			mLocClient.stop();
			UserHelper helper = new UserHelper(getApplicationContext());
			User user = helper.findUser();
			String address = locData.latitude + "," + locData.longitude;
			user.setAddress(address);
			helper.updateUser(user);
			String path = Config.HTTP_UPDATE_ADDRESS;
			String params = "id=" + user.getId() + "&address="
					+ user.getAddress();
			JSONObject result = HttpUtils.getJsonByPost(path, params);

			Toast.makeText(LocationOverlayDemo.this, address,
					Toast.LENGTH_SHORT).show();

		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}
}
