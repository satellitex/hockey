package com.example.hockey;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class ServerConnect extends Thread{
    //サーバー側の処理
    //UUID：Bluetoothプロファイル毎に決められた値
    private final BluetoothServerSocket servSock;
    static BluetoothAdapter myServerAdapter;
    //UUIDの生成
    public final UUID TECHBOOSTER_BTSAMPLE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    private Connect connect;
    
	public ServerConnect(Connect c, BluetoothAdapter btAdapter ){
        //各種初期化
		connect = c;
        BluetoothServerSocket tmpServSock = null;
        myServerAdapter = btAdapter;
        try{
            //自デバイスのBluetoothサーバーソケットの取得
             tmpServSock = myServerAdapter.listenUsingRfcommWithServiceRecord("BlueToothSample03", TECHBOOSTER_BTSAMPLE_UUID);
        }catch(IOException e){
            e.printStackTrace();
        }
        servSock = tmpServSock;		
	}
	
	@Override
	public void run(){
        BluetoothSocket receivedSocket = null;
        while(true){
            try{
                //クライアント側からの接続要求待ち。ソケットが返される。
                receivedSocket = servSock.accept();
            }catch(IOException e){
                break;
            }
 
            if(receivedSocket != null){
                //ソケットを受け取れていた(接続完了時)の処理
                //RwClassにmanageSocketを移す
            	connect.setSocket(receivedSocket);
    	        connect.setServet();
            	break;
            }
        }
	}
	
	public void cansel(){
		try {
			servSock.close();
		} catch( IOException e) { }
	}

}
