package com.example.hockey;

import java.util.Scanner;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Connect {
	private boolean Serverflag = false;
	private boolean Clientflag = false;
	BluetoothSocket socket = null;
	private int step_count;
	
	private ReadWriteModel read;
	private ReadWriteModel write;
	
	private Scanner rcvstr;
	private String sendstr;
	private boolean rcvflag;

	private boolean firstflag;
	public Connect(){
	}
	public void Init(){
		Log.d("init","connect init-0");
		Serverflag = false;
		Clientflag = false;
		Log.d("init","connect init0");
		socket = null;
		Log.d("init","connect init1");
		
		step_count = 0;
		rcvflag=false;
		firstflag=true;
	}
	public void setSocket(BluetoothSocket socket){
		this.socket = socket;
		read = new ReadWriteModel(socket,this,false);
		write = new ReadWriteModel(socket,this,true);
	}
	public void setServet(){ Serverflag = true; }
	public void setClient(){ Clientflag = true; }
	
	public boolean checkSocket(){
		if( socket != null ) return true;
		return false;
	}
	public boolean isServer(){ return Serverflag; }
	public boolean isClient(){ return Clientflag; }
	
	public void recieveString(String str){
		rcvstr = new Scanner(str);
		int sn = rcvstr.nextInt();
		Log.d("koko","koko rcvstr count : "+sn);
		if( (Clientflag && step_count == sn) ||
			(Serverflag && step_count< sn) ){
			rcvflag = true;
		}
	}
	public void sendString(String str){
		sendstr += " "+str;
	}
	
	public void StartRead(){
		read.start();
		write.set("-1");
		write.start();
	}
	
	//最初に呼び出す
	public Scanner FastCall(){
		Log.d("koko","koko FastCall 0 count+"+step_count);
		Scanner ret = null;
		if( Serverflag ){//サーバーなら
			//受信結果が真になるまで待つ ( step_count より ↑なら )
			while( rcvflag );
			rcvflag=false;
			ret = rcvstr;
			step_count++;
			sendstr = String.format("%d", step_count);
			
			//受信開始
//			read.start();
		}
		if( Clientflag ){//クライアントなら
			sendstr = String.format("%d", step_count+1);
			step_count++;
		}
		Log.d("koko","koko FastCall 1 count+"+step_count);
		return ret;
	}
	//最後に呼び出す
	public Scanner LastCall(){
		Log.d("koko","koko LastCall 0");
		Scanner ret = null;
		if( Serverflag ){//サーバーなら
			//状況を送信
			write.set(sendstr);
	//		write.start();
		}
		if( Clientflag ){//クライアントなら
			//受信結果を見る
			//受信結果が真になるまで待つ( step_count = 0のときのみ飛ばす )
			if( firstflag == false ){
				while( rcvflag );
				rcvflag=false;
				ret = rcvstr;
			} else {
				firstflag=true;
			}
			Log.d("koko","koko Client tyukan");
			//受信開始
		//	read.start();
			Log.d("koko","koko Client tyukan2");
			
			//状況を送信
			write.set(sendstr);
			//write.start();
		}
		Log.d("koko","koko LastCall 1");
		return ret;
	}
}
