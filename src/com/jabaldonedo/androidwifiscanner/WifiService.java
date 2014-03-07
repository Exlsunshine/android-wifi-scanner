package com.jabaldonedo.androidwifiscanner;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class WifiService extends Service {
	
	private final String TAG = "WifiService";

	private WifiManager mWifiManager;
	private ScheduledFuture<?> scheduleReaderHandle;
	private ScheduledExecutorService mScheduler;
	private WifiData mWifiData;

	private long initialDelay = 100;
	private long periodReader = 5 * 1000;

	@Override
	public void onCreate() {
		mWifiData = new WifiData();
		mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		mScheduler = Executors.newScheduledThreadPool(2);

		scheduleReaderHandle = mScheduler.scheduleAtFixedRate(new ScheduleReader(), initialDelay, periodReader,
				TimeUnit.MILLISECONDS);
	}

	@Override
	public void onDestroy() {
		scheduleReaderHandle.cancel(true);
		mScheduler.shutdown();
		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	class ScheduleReader implements Runnable {
		@Override
		public void run() {
			if (mWifiManager.isWifiEnabled()) {
				List<ScanResult> mResults = mWifiManager.getScanResults();
				Log.d(TAG, "New scan result: (" + mResults.size() + ") networks found");
				mWifiData.addNetworks(mResults);
				
				Intent intent = new Intent(Constants.INTENT_FILTER);
				intent.putExtra(Constants.WIFI_DATA, mWifiData);
				LocalBroadcastManager.getInstance(WifiService.this).sendBroadcast(intent);
			} else {

			}
		}
	}
}
