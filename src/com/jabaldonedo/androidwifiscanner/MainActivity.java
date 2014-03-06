package com.jabaldonedo.androidwifiscanner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private WifiData mWifiData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mWifiData = null;
		
		// set receiver
		MainActivityReceiver mReceiver = new MainActivityReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(Constants.APP_NAME));

		// launch WiFi service
		Intent intent = new Intent(this, WifiService.class);
		startService(intent);

		// set layout
		setContentView(R.layout.activity_main);
		plotData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public void plotData() {
		if (mWifiData == null) {
			TextView noDataView = new TextView(this);
			noDataView.setText(getResources().getString(R.string.wifi_no_data));
			noDataView.setGravity(Gravity.CENTER_HORIZONTAL);
			noDataView.setPadding(0, 50, 0, 0);
			noDataView.setTextSize(18);
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.scanningResultBlock);
			linearLayout.removeAllViews();
			linearLayout.addView(noDataView);
		} else {
			
		}
	}
	
	public class MainActivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

		}

	}
}
