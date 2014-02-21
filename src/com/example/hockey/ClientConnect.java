package com.example.hockey;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class ClientConnect extends Thread{
    //クライアント側の処理
    private final BluetoothSocket clientSocket;
    //UUIDの生成
    public final UUID TECHBOOSTER_BTSAMPLE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter myClientAdapter;
    public String myNumber;
    private ClientActivity parent;
    
    private int count;
    
    Connect connect;
    
    final Handler handler;
 
    //コンストラクタ定義
    public ClientConnect(ClientActivity p,Connect c, BluetoothDevice device, BluetoothAdapter btAdapter,Handler handle){
        //各種初期化
    	handler = handle;
    	parent = p;
        BluetoothSocket tmpSock = null;
        myClientAdapter = btAdapter;
        connect = c;
    	Log.d("ok","onItemClick syo");
    	count = 0;
        
        try{
            //自デバイスのBluetoothクライアントソケットの取得
            tmpSock = device.createRfcommSocketToServiceRecord(TECHBOOSTER_BTSAMPLE_UUID);
        }catch(IOException e){
            e.printStackTrace();
        }
        clientSocket = tmpSock;
    }
 
    public void run(){
        //接続要求を出す前に、検索処理を中断する。
        if(myClientAdapter.isDiscovering()){
            myClientAdapter.cancelDiscovery();
        }
 
        while ( true ){

        	count++;
        	Log.d("count","client count "+count);
	        try{
	            //サーバー側に接続要求
	            clientSocket.connect();
	        }catch(IOException e){
	             try {
	                 clientSocket.close();
	             } catch (IOException closeException) {
	                 e.printStackTrace();
	             }
	        }
	        if(clientSocket.isConnected()){
	        	Log.d("ok","onItemClick final");
	        	connect.setSocket(clientSocket);
	        	connect.setClient();
	        	handler.sendEmptyMessage(1);
	        	break;
	        } else if( count > 24000 ){
//	        	Log.d("ok","count final");
//	        	KeyEvent key = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK);
	        	handler.sendEmptyMessage(0);
	        	parent.Log("接続タイムアウト");
//	        	Log.d("ok","count final2");
	        	break;
	        }
        }
    }
 
    public void cancel() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
      }
}
