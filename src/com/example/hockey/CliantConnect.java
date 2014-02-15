package com.example.hockey;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

public class CliantConnect {
	public class BluetoothClientThread extends Thread {
	    //クライアント側の処理
	    private final BluetoothSocket clientSocket;
	    //UUIDの生成
	    public final UUID TECHBOOSTER_BTSAMPLE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	    private BluetoothAdapter myClientAdapter;
	    public String myNumber;
	    
	    Connect connect;
	 
	    //コンストラクタ定義
	    public BluetoothClientThread(Connect c, BluetoothDevice device, BluetoothAdapter btAdapter){
	        //各種初期化
	        BluetoothSocket tmpSock = null;
	        myClientAdapter = btAdapter;
	        connect = c;
	        
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
		        	break;
		        }
	        }
	        
	        connect.setSocket(clientSocket);
	        connect.setClient();
	        
	    }
	 
	    public void cancel() {
	            try {
	                clientSocket.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	      }
	}
}
