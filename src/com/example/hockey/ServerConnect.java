package com.example.hockey;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class ServerConnect extends Thread{
    //サーバー側の処理
    //UUID：Bluetoothプロファイル毎に決められた値
    private final BluetoothServerSocket servSock;
    static BluetoothAdapter myServerAdapter;
    //UUIDの生成
    public final UUID TECHBOOSTER_BTSAMPLE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    private Connect connect;
    
    private ServerActivity parent;
    final Handler handler;
    
    private boolean loopflag;
    
	public ServerConnect(ServerActivity p,Connect c, BluetoothAdapter btAdapter,Handler handle){
        //各種初期化
		loopflag=true;
		handler = handle;
		parent = p;
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
        while(loopflag){
            try{
                //クライアント側からの接続要求待ち。ソケットが返される。
                receivedSocket = servSock.accept();
            }catch(IOException e){
            	e.printStackTrace();
	        	handler.sendEmptyMessage(0);
                break;
            }
 
            if(receivedSocket != null){
                //ソケットを受け取れていた(接続完了時)の処理
                //RwClassにmanageSocketを移す
            	try {
					servSock.close();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
            	connect.setSocket(receivedSocket);
    	        connect.setServet();
	        	handler.sendEmptyMessage(1);
            	break;
            } else {
//	        	Log.d("ok","count final");
//	        	KeyEvent key = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK);
	        	handler.sendEmptyMessage(0);
	        	parent.Log("接続タイムアウト");
//	        	Log.d("ok","count final2");
	        	break;
	        }
        }
        if( !loopflag ){
	        try {
				servSock.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
        }
        Log.d("koko","koko server connect owari");
        return;
	}
	
	public void cancel(){
        Log.d("koko","koko ServerConnectCancel");
		loopflag = false;
		/*try {
			servSock.close();
		} catch( IOException e) { }
		*/
	}

}
