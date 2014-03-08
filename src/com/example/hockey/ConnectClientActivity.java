package com.example.hockey;


import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ConnectClientActivity extends Activity {
    BluetoothAdapter mBtAdapter;
    ArrayAdapter<String> pairedDeviceAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.connectdevice);
		
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		Log.d("setuzoku","intent oks");
		//接続履歴のあるデバイスを取得
		pairedDeviceAdapter = new ArrayAdapter<String>(this, R.layout.paireddevicelist);
		//BluetoothAdapterから、接続履歴のあるデバイスの情報を取得
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
		if(pairedDevices.size() > 0){
		    //接続履歴のあるデバイスが存在する
		    for(BluetoothDevice device : pairedDevices){
		        //接続履歴のあるデバイスの情報を順に取得してアダプタに詰める
		        //getName()・・・デバイス名取得メソッド
		        //getAddress()・・・デバイスのMACアドレス取得メソッド
		        pairedDeviceAdapter.add(device.getName() + "\n" + device.getAddress());
		    }
		    ListView deviceList = (ListView)findViewById(R.id.paireddevicelist);
		    deviceList.setAdapter(pairedDeviceAdapter);
		    deviceList.setOnItemClickListener(new MyClickAdapter());
		}				
	}
    class MyClickAdapter implements OnItemClickListener {
	        @Override
	        public void onItemClick(AdapterView<?> adapter,View view, int position, long id) {
	        	Log.d("onItemClick","onItemClick0");
	            ListView listView = (ListView) adapter;
	            String item = (String) listView.getItemAtPosition(position);
	            
	            Intent intent = new Intent(ConnectClientActivity.this,ClientActivity.class);
	            intent.putExtra("survername",item);
	            startActivity(intent);
	        	Log.d("onItemClick","onItemClick1");
	        }
	  }
	  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	}
	
	//汎用トースト
	public void Log(String string){
	        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	  }	
}
