package com.example.hockey;

import java.io.File;
import java.util.UUID;

import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.ViewGroup;

public class App extends android.app.Application {

	public static final String NAME = "YourAppName";
	public static final String TAG = "YourAppName";
	public static final int MP = ViewGroup.LayoutParams.FILL_PARENT;
	public static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	// コンテキスト
	private static App instance;
	public App() {
		instance = this;
	}
	public static App getInstance() {
		return instance;
	}

	// ログ出力用
	public static void Logd(Object obj, String str) {
		android.util.Log.d(App.TAG,
				String.format("@%s - %s", obj.toString(), str));
	}
	public static void Loge(Object obj, Exception e) {
		e.printStackTrace();
		android.util.Log.e(App.TAG,
				String.format("@%s - %s", obj.toString(), e.toString()));
	}

	// ファイルシステム
	public static File getStorageDir() {
		File file = Environment.getExternalStorageDirectory();
		return new File(file, App.NAME);
	}
	public static boolean isStorageAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	public static String makeUUIDString() {
		return UUID.randomUUID().toString();
	}

	// アプリ設定の取得
	public SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}

	// DPとPXの相互変換
	public int dp2px(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density);
	}
	public int px2dp(int px) {
		return (int) (px / getResources().getDisplayMetrics().density);
	}
	
}