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
	private String tmpRcvstr;

	private String sendstr;
	private boolean rcvflag;

	private boolean firstflag;
	
	private boolean send_flag;
	
	public Connect(){
		read = null;
		write = null;
	}
	public void cansel(){
		if( write != null ){
			write.cansel();
		}
		if( read != null ){
			read.cansel();
		}
	}
	public void Init(){
		Log.d("init","connect init-0");
		Serverflag = false;
		Clientflag = false;
		socket = null;
		
		step_count = 0;
		rcvflag=false;
		firstflag=true;
		send_flag=false;
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
	
	public void startSend(){ send_flag=true; }
	
	public void recieveString(String str){
		tmpRcvstr = str;
		rcvflag = true;
	}
	
	private void recieve(){
		if(rcvflag){
			step_count++;
			rcvflag=false;
			rcvstr = new Scanner(tmpRcvstr);
			int sn = rcvstr.nextInt();
			if( sn == -1 ) rcvstr=null;
//			if( Clientflag )Log.d("koko","koko rcvstr Client");
//			if( Serverflag )Log.d("koko","koko rcvstr Server");
//			Log.d("koko","koko rcvstr count : "+sn+" step_count = "+step_count);
		} else {
			rcvstr = null;
		}
		
	}
	
	public void sendString(String str){
		sendstr += str+" ";
	}
	
	public void StartRead(){
		read.start();
		write.set(new String("-1 "));
		write.start();
	}
	
	//最初に呼び出す
	public Scanner FastCall(){
	//	Log.d("koko","koko FastCall 0 count+"+step_count);
		Scanner ret = null;
		if( Serverflag ){//サーバーなら
	//		Log.d("koko","koko Server");
			//受信結果が真になるまで待つ ( step_count より ↑なら )
			recieve();
			ret = rcvstr;
			sendstr = String.format("%d ", step_count);
			
			//受信開始
		}
		if( Clientflag ){//クライアントなら
		//	Log.d("koko","koko Client");
			sendstr = String.format("%d ", step_count+1);
		}
		//Log.d("koko","koko FastCall 1 count+"+step_count);
		return ret;
	}
	//最後に呼び出す
	public Scanner LastCall(){
	//	Log.d("koko","koko LastCall 0");
		Scanner ret = null;
		if( Serverflag ){//サーバーなら
			//状況を送信
			if( send_flag ) write.set(sendstr);
			send_flag=false;
		}
		if( Clientflag ){//クライアントなら
			//受信結果を見る
			//受信結果が真になるまで待つ( step_count = 0のときのみ飛ばす )
			if( firstflag == false ){
				recieve();
				ret = rcvstr;
			} else {
				step_count++;
				firstflag=false;
			}
			//受信開始
			
			//状況を送信
			if( send_flag ) write.set(sendstr);
			send_flag=false;
		}
	//	Log.d("koko","koko LastCall 1");
		return ret;
	}
}
