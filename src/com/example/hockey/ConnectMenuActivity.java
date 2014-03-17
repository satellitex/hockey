package com.example.hockey;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class ConnectMenuActivity extends Activity {
    private final int REQUEST_ENABLE_BLUETOOTH = 10;	
    final private int requestcode = 1001;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);
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
		  
		//Bluetooth設定画面へ
	    ImageButton BluetoothButton = (ImageButton) findViewById(R.id.imagebutton1);
	    BluetoothButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
	    	Intent intent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
	  		startActivity(intent);
	    	}
	    });

	    //接続先一覧へ
	    ImageButton NextButton = (ImageButton) findViewById(R.id.imagebutton2);
	    NextButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
			Intent intent = new Intent(ConnectMenuActivity.this,ConnectClientActivity.class);
			startActivityForResult(intent, 0);
	      }
	    });  
	    
	    
	    //接続待機へ
	    ImageButton WaitButton = (ImageButton) findViewById(R.id.imagebutton3);
	    WaitButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
	    	Intent intent = new Intent(ConnectMenuActivity.this,ServerActivity.class);
	    	startActivityForResult(intent,requestcode);
	      }
	    });
	}
	
  @Override
  protected void onActivityResult(int requestCode, int ResultCode, Intent date){
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(ResultCode == Activity.RESULT_OK){
                //BluetoothがONにされた場合の処理
                Toast.makeText(this,"BluetoothをONにしてもらえました。",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"BluetoothがONにしてもらえませんでした。",Toast.LENGTH_LONG).show();
              finish();
            }
        } else if ( requestCode == requestcode ){
			if( ResultCode == Activity.RESULT_OK ){
				String str = date.getStringExtra("wlcount");
				Intent intent = new Intent(ConnectMenuActivity.this,ResultActivity.class);
	            intent.putExtra("wlcount",str);
	            startActivity(intent);
			}      	
        }
  }	  	
	  //汎用トースト
	public void Log(String string){
	        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	  }
}
