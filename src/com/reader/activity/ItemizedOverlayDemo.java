package com.reader.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.reader.R;
import com.reader.impl.UserHelper;
import com.reader.model.User;
import com.reader.util.Config;
import com.reader.util.Constant;
import com.reader.util.HttpUtils;

/**
 * 在一个圆周上添加自定义overlay.
 */
public class ItemizedOverlayDemo extends Activity {

	final static String TAG = "MainActivty";
	static MapView mMapView = null;
	public NotifyLister mNotifyer = null;
	LocationClient mLocClient;
	public MyLocationListenner1 myListener = new MyLocationListenner1();
	private MapController mMapController = null;
	LocationData locData = null;
	MyLocationOverlay myLocationOverlay = null;
	public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;

	Button testItemButton = null;
	Button removeItemButton = null;
	Button removeAllItemButton = null;
	EditText indexText = null;
	int index = 0;

	/**
	 * 圆心经纬度坐标
	 */
	int cLat = 23048985;// 39909230 ;
	int cLon = 113394257;// 116397428 ;
	// 存放overlayitem
	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	// 存放overlay图片
	public List<Drawable> res = new ArrayList<Drawable>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemizedoverlay);
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
		mMapView.getController().setZoom(13);
		mMapView.getController().enableClick(true);
		mMapView.displayZoomControls(true);
		testItemButton = (Button) findViewById(R.id.button1);
		removeItemButton = (Button) findViewById(R.id.button2);
		removeAllItemButton = (Button) findViewById(R.id.button3);
		OnClickListener clickListener = new OnClickListener() {
			public void onClick(View v) {
				testItemClick();
			}
		};
		OnClickListener clickListener1 = new OnClickListener() {
			public void onClick(View v) {
				testUpdateClick();
			}
		};
		OnClickListener removeAllListener = new OnClickListener() {
			public void onClick(View v) {
				testRemoveAllItemClick();
			}
		};
		testItemButton.setOnClickListener(clickListener);
		removeItemButton.setOnClickListener(clickListener1);
		removeAllItemButton.setOnClickListener(removeAllListener);
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
					Toast.makeText(ItemizedOverlayDemo.this, title,
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager,
				mMapListener);
		res.add(getResources().getDrawable(R.drawable.icon_marki));
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager,
				mMapListener);
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();

		myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();
		UserHelper helper = new UserHelper(getApplicationContext());
		User user = helper.findUser();
		String path = Config.HTTP_USER_MAP;
		String params = "address=" + user.getAddress();
		JSONObject result = HttpUtils.getJsonByPost(path, params);
		JSONArray rows = new JSONArray();
		List<User> userList = new ArrayList<User>();
		int lat, lon;
		try {
			rows = result.getJSONArray("rows");
			int count = result.getInt("total");
			for (int i = 0; i < count; i++) {
				user = new User();
				user.setId(((JSONObject) rows.get(i)).getString("id"));
				user.setName(((JSONObject) rows.get(i)).getString("name"));
				user.setSignature(((JSONObject) rows.get(i))
						.getString("signature"));
				user.setAddress(((JSONObject) rows.get(i)).getString("address"));
				userList.add(user);
				lat = Integer.parseInt(user.getAddress().substring(0,
						user.getAddress().indexOf(",")));
				lon = Integer.parseInt(user.getAddress().substring(
						user.getAddress().indexOf(",") + 1,
						user.getAddress().length()));
				OverlayItem item = new OverlayItem(new GeoPoint(lat, lon),
						"item" + i, "item" + i);
				item.setMarker(res.get(0));
				mGeoList.add(item);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * // overlay 数量 int iSize = 20; double pi = 3.1415926; // overlay半径 int
		 * r = Constant.MAP_OVERLAY; // 准备overlay 数据 for (int i = 0; i < iSize;
		 * i++) { int lat = (int) (cLat + r * Math.cos(2 * i * pi / iSize)); int
		 * lon = (int) (cLon + r * Math.sin(2 * i * pi / iSize)); OverlayItem
		 * item = new OverlayItem(new GeoPoint(lat, lon), "item" + i, "item" +
		 * i); item.setMarker(res.get(0)); mGeoList.add(item); }
		 */
	}

	public void testUpdateClick() {
		mLocClient.start();
		mLocClient.requestLocation();
		// mLocClient.stop();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		};
	};

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner1 implements BDLocationListener {
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

			mLocClient.stop();
			cLat = (int) (locData.latitude * 1e6);
			cLon = (int) (locData.longitude * 1e6);
			UserHelper helper = new UserHelper(getApplicationContext());
			User user = helper.findUser();
			String address = cLat + "," + cLon;
			user.setAddress(address);
			helper.updateUser(user);
			String path = Config.HTTP_UPDATE_ADDRESS;
			String params = "id=" + user.getId() + "&address="
					+ user.getAddress();
			JSONObject result = HttpUtils.getJsonByPost(path, params);

			Toast.makeText(ItemizedOverlayDemo.this, address,
					Toast.LENGTH_SHORT).show();

		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
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

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		super.onDestroy();
	}

	private void initMapView() {
		mMapView.setLongClickable(true);
		// mMapController.setMapClickEnable(true);
		// mMapView.setSatellite(false);
	}

	public void testRemoveAllItemClick() {
		mMapView.getOverlays().clear();
		mMapView.refresh();
		mMapView.getOverlays().add(myLocationOverlay);
		mMapView.refresh();
	}

	public void testRemoveItemClick() {
		int n = (int) (Math.random() * (mGeoList.size() - 1));
		Drawable marker = ItemizedOverlayDemo.this.getResources().getDrawable(
				R.drawable.icon_marka);
		mMapView.getOverlays().clear();

		OverlayTest ov = new OverlayTest(marker, this);
		for (int i = 0; i < mGeoList.size(); i++) {
			if (i != n)
				ov.addItem(mGeoList.get(i));
		}
		mMapView.getOverlays().add(ov);

		mMapView.refresh();
		mMapView.getController().setCenter(new GeoPoint(cLat, cLon));
	}

	public void testItemClick() {
		Drawable marker = ItemizedOverlayDemo.this.getResources().getDrawable(
				R.drawable.icon_marka);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(myLocationOverlay);
		mMapView.refresh();
		OverlayTest ov = new OverlayTest(marker, this);
		for (OverlayItem item : mGeoList) {
			ov.addItem(item);
		}
		mMapView.getOverlays().add(ov);
		mMapView.refresh();
		mMapView.getController().setCenter(new GeoPoint(cLat, cLon));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

class OverlayTest extends ItemizedOverlay<OverlayItem> {
	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	private Context mContext = null;
	static PopupOverlay pop = null;

	public OverlayTest(Drawable marker, Context context) {
		super(marker);
		this.mContext = context;
		pop = new PopupOverlay(ItemizedOverlayDemo.mMapView,
				new PopupClickListener() {

					@Override
					public void onClickedPopup() {
						Log.d("hjtest  ", "clickpop");
					}
				});
		populate();

	}

	protected boolean onTap(int index) {
		Drawable marker = this.mContext.getResources().getDrawable(
				R.drawable.pop); // 得到需要标在地图上的资源
		BitmapDrawable bd = (BitmapDrawable) marker;
		Bitmap popbitmap = bd.getBitmap();
		pop.showPopup(popbitmap, mGeoList.get(index).getPoint(), 32);
		// int latspan = this.getLatSpanE6();
		// int lonspan = this.getLonSpanE6();
		Toast.makeText(this.mContext, mGeoList.get(index).getTitle(),
				Toast.LENGTH_SHORT).show();
		super.onTap(index);
		return false;
	}

	public boolean onTap(GeoPoint pt, MapView mapView) {
		if (pop != null) {
			pop.hidePop();
		}
		super.onTap(pt, mapView);
		return false;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mGeoList.get(i);
	}

	@Override
	public int size() {
		return mGeoList.size();
	}

	public void addItem(OverlayItem item) {
		mGeoList.add(item);
		populate();
	}

	public void removeItem(int index) {
		mGeoList.remove(index);
		populate();
	}

}
