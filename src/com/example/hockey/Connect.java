package com.example.hockey;

import android.bluetooth.BluetoothSocket;

public class Connect {
	private boolean Serverflag = false;
	private boolean Clientflag = false;
	BluetoothSocket socket = null;
	private int step_count;
	public Connect(){
	}
	public void Init(){
		Serverflag = false;
		Clientflag = false;
		socket = null;
		
		step_count = 0;
	}
	public void setSocket(BluetoothSocket socket){
		this.socket = socket;
	}
	public void setServet(){ Serverflag = true; }
	public void setClient(){ Clientflag = true; }
	
	public boolean checkSocket(){
		if( socket != null ) return true;
		return false;
	}
	
	//最初に呼び出す
	public void FastCall(){
		if( Serverflag ){//サーバーなら
			//受信結果を見る
			
			//受信結果が真になるまで待つ ( step_count より ↑なら )
			
			//受信開始
		}
		if( Clientflag ){//クライアントなら
		}
	}
	//最後に呼び出す
	public void LastCall(){
		if( Serverflag ){//サーバーなら
			//状況を送信
		}
		if( Clientflag ){//クライアントなら
			//受信結果を見る
			
			//受信結果が真になるまで待つ( step_count = 0のときのみ飛ばす )
			
			//状況を送信
			
			//受信を開始
		}
		step_count++;
	}
}
