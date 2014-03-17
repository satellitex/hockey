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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ServerActivity extends Activity {
	private GameSurfaceView _game;
	
    BluetoothAdapter mBtAdapter;
    ArrayAdapter<String> pairedDeviceAdapter;
    
    public Connect connect;
    private ServerConnect server;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //スリープさせない
		RatioAdjustment.init(this);
		
		connect = new Connect();
		//ゲーム戻るハンドラー
        final Handler handler2 = new Handler(){
        	public void handleMessage( Message msg ){
                Log.d("koko","koko in Hudler00");
        		Intent intent = new Intent();
        		intent.putExtra("wlcount", (String)msg.obj );
                Log.d("koko","koko in Hudler01");
        		setResult( Activity.RESULT_OK, intent );
                Log.d("koko","koko in Hudler02");
        		finish();
                Log.d("koko","koko in Hudler03");
        	}
        };
		_game = new GameSurfaceView(this,connect,handler2);
		
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
    				finish();
    			} else {//ゲーム画面へ遷移
    				//終了処理を記述
    				setScreenContent(0);
    			}
    		}
    	};
    	
    	server= new ServerConnect(this,connect,mBtAdapter,handler);
    	Log("接続待機中");
    	server.start();

	}
	
	@Override
	public void onPause(){
		super.onPause();
		connect.cansel();
		server.cancel();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d("koko","koko onDdestroy server");
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
