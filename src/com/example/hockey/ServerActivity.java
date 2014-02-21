package com.example.hockey;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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

public class ServerActivity extends Activity {
	private GameSurfaceView _game;
	
    private final int REQUEST_ENABLE_BLUETOOTH = 10;	
    BluetoothAdapter mBtAdapter;
    private final int REQUESTCODE_BLUETOOTH_ON = 10;
    private final int SHOW_CONNECT_MENU = 0;
    ArrayAdapter<String> pairedDeviceAdapter;
    
    public Connect connect;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		connect = new Connect();
		_game = new GameSurfaceView(this,connect);
		
		setScreenContent(1);
		
    	mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    	String name = mBtAdapter.getName();
    	String addres = mBtAdapter.getAddress();
    	
    	TextView text= (TextView)findViewById(R.id.servername);
    	text.setText("あなたの名前は "+name+"\nアドレスは "+addres+" です。");
    	
    	Log.d("connecet","server init0");
    	connect.Init();
    	
    	//ゲーム画面へ遷移するハンドル
    	final Handler handler=  new Handler() {
    		public void handleMessage(Message msg) {
    			if( msg.equals(0) ){//戻る
//    				finish();
    			} else {//ゲーム画面へ遷移
    				//終了処理を記述
    				setScreenContent(0);
    			}
    		}
    	};
    	
    	ServerConnect server= new ServerConnect(this,connect,mBtAdapter,handler);
    	Log("接続待機中");
    	server.start();

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
	  		Log.d("ok","koko server set _game");
	  		setContentView(_game);
	  		Log.d("ok","koko server set _game_end");
	  		break;
	  	case 1:
	  		setContentView(R.layout.serverwait);
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