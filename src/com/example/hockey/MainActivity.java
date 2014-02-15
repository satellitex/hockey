package com.example.hockey;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private GameSurfaceView _game;
	
    private final int REQUEST_ENABLE_BLUETOOTH = 10;	
    BluetoothAdapter mBtAdapter;
    private final int REQUESTCODE_BLUETOOTH_ON = 10;
    ArrayAdapter<String> pairedDeviceAdapter;
    
    private boolean ServerFlag=false;
    private boolean CliantFlag =false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		RatioAdjustment.init(this);
		
		_game = new GameSurfaceView(this);
//		setContentView( _game );
		setScreenContent(1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		Log.d("touch","Activity event");
		_game.onTouchEventEx(event);
		return super.onTouchEvent(event);
	}
	
	private void setScreenContent(int screenId) {  
		  	switch ( screenId ){
		  	case 0:
		  		setContentView(_game);
		  		break;
		  	case 1:
		  		setContentView(R.layout.activity_main);
		  		setHomeScreenContent();
		  		break;
		  	case 2:
		  		setContentView(R.layout.connect);
		  		setConnectScreenContent();
		  		break;
		  	case 3:
		  		setContentView(R.layout.connectdevice);
		  		setConnectDeviceScreenContent();
		  		break;
		  	}
	}
	
		//ホーム画面
	  private void setHomeScreenContent() {  
//	    	Log.d("haitta","setHomeScreenContent onClick");
		    Button nextButton = (Button) findViewById(R.id.button01);
		    nextButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		//   	Log.d("haitta","setHomeScreenContent onClick");
		        setScreenContent(2);
		      }
		    });
	  }

	  //通信コネクト画面
	  private void setConnectScreenContent() {  
			  BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
			  if(!Bt.equals(null)){
				  Toast.makeText(this, "Bluetoothがサポートされてます。", Toast.LENGTH_LONG).show();
			  }else{
				  Toast.makeText(this, "Bluetoothがサポートされていません。", Toast.LENGTH_LONG).show();
			      finish();
			  }
			  
			  boolean btEnable = Bt.isEnabled();
			  if(btEnable == true){
			     //BluetoothがONだった場合の処理
			  }else{
			      //OFFだった場合、ONにすることを促すダイアログを表示する画面に遷移
			      Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			      startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
			  }
			  
			  Log.d("blue","connect1");
		    ImageButton BluetoothButton = (ImageButton) findViewById(R.id.imagebutton1);
			  Log.d("blue","connect1-2");
		    BluetoothButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
//		        setScreenContent(2);
		    	Intent intent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
		  		startActivity(intent);
		    	}
		    });
		    
			  Log.d("blue","connect2");
		    ImageButton NextButton = (ImageButton) findViewById(R.id.imagebutton2);
		    NextButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        setScreenContent(3);
		      }
		    });  
			  Log.d("blue","connect3");

	  }
	  
	  //通信コネクト画面２
	  private void setConnectDeviceScreenContent(){
		  
		  mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		  
		  //接続履歴のあるデバイスを取得
		  pairedDeviceAdapter = new ArrayAdapter(this, R.layout.paireddevicelist);
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
		  }		

	  }
	  
	  
	  @Override
	  protected void onActivityResult(int requestCode, int ResultCode, Intent date){
	        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
	            if(ResultCode == Activity.RESULT_OK){
	                //BluetoothがONにされた場合の処理
	                Toast.makeText(this,"BluetoothをONにしてもらえました。",Toast.LENGTH_LONG).show();
					 setScreenContent(3);
	            }else{
	                Toast.makeText(this,"BluetoothがONにしてもらえませんでした。",Toast.LENGTH_LONG).show();
                    finish();
	            }
	        }
	  }	  
	  
	  
	  
	  
	  //汎用トースト
	public void Log(String string){
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	  }
}
