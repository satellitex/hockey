package com.example.hockey;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ClientActivity extends Activity {

	private GameSurfaceView _game;
	
	BluetoothAdapter mBtAdapter;
	ArrayAdapter<String> pairedDeviceAdapter;

    public Connect connect;
    
    public ClientConnect client;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		RatioAdjustment.init(this);
		
  		setContentView(R.layout.connect);

		connect = new Connect();
		_game = new GameSurfaceView(this,connect);
		
		setScreenContent(1);
//  		setContentView(R.layout.clientwait);

		Intent intent = getIntent();
        String item =intent.getStringExtra("survername"); 
		
        TextView text = (TextView)findViewById(R.id.servername);
        text.setText(item);
 
        //Log(item);
        Log.d("onItemClick","onItemClick1 "+item);
        
        
    	//ゲーム画面へ遷移するハンドル
    	final Handler handler=  new Handler() {
    		public void handleMessage(Message msg) {
    			if( msg.equals(0) ){//戻る
    				finish();
    			} else {//ゲーム画面へ遷移
    				//終了処理を記述
    				setScreenContent(0);
    			}
    		}
    	};
    	
    	
    	//BluetoothAdapterから、接続履歴のあるデバイスの情報を取得
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
  		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
  		
  		if(pairedDevices.size() > 0){
  			//接続履歴のあるデバイスが存在する
  			Log.d("onItemClick","onItemClick2");
  			for(BluetoothDevice device : pairedDevices){
  				Log.d("onItemClick","onItemClick3 ");
  				//接続履歴のあるデバイスの情報を順に取得してアダプタに詰める
  				//getName()・・・デバイス名取得メソッド
  				//getAddress()・・・デバイスのMACアドレス取得メソッド
  				String str = device.getName() + "\n" + device.getAddress();
  				Log.d("device","onItemClick333 "+str);
  				if( str.equals(item) ){
  					Log("device.getNam()と接続中\n");
  					connect.Init();
	  		        Log.d("onItemClick","onItemClick4.0");
  		    		client = new ClientConnect(this,connect,device,mBtAdapter,handler);
	  		        Log.d("onItemClick","onItemClick4.1");
  		    		client.start();
  		        	Log.d("onItemClick","onItemClick4.2");
  		        }
  			}
  		}
  		
	}
	
	@Override
	public void onDestroy(){
		Log.d("koko","koko onDdestroy client");
		connect.cansel();
		client.cancel();
	}	
	
	@Override
	public void onPause(){
		connect.cansel();
		client.cancel();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		Log.d("touch","Activity event");
		_game.onTouchEventEx(event);
		return super.onTouchEvent(event);
	}	
	
	public void setScreenContent(int screenId) {  
	  	switch ( screenId ){
	  	case 0:
	  		setContentView(_game);
	  		break;
	  	case 1:
	  		setContentView(R.layout.clientwait);
	  		break;
	  	}
	}
	  @Override
	  protected void onActivityResult(int requestCode, int ResultCode, Intent date){
	  }	  	
	  
	  //汎用トースト
	public void Log(String string){
          Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	  }	
}
