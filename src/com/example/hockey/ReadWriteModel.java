package com.example.hockey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android.bluetooth.BluetoothSocket;

public class ReadWriteModel extends Thread {
	public static InputStream in;
	public static OutputStream out;
	
	BluetoothSocket socket;
	
	public boolean writef=false;
	public String str;
	
	Connect parent;
	
	public ReadWriteModel(BluetoothSocket socket,Connect p,boolean wf){
		writef = wf;
		parent = p;
		this.socket = socket;
	}
	
	public void set(String str){
		this.str = str;
	}
	
	public void write(byte[] buf){
		try {
			out.write(buf);
		} catch( IOException e ){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		
		while ( true ){
			try {
				in = socket.getInputStream();
				out = socket.getOutputStream();
			} catch( IOException e ){
				e.printStackTrace();
			}
			if( writef ){
				try {
					write(str.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			} else {
				int tmpBuf = 0;
				byte[] buf = new byte[1024];
				try {
					tmpBuf = in.read(buf);
				} catch (IOException e ){
					e.printStackTrace();
				}
				if( tmpBuf != 0 ){
					try{
						parent.recieveString(new String(buf,"UTF-8"));
					} catch( UnsupportedEncodingException e) {
						e.getStackTrace();
					}
					break;
				}
			}
		}
	}

	public void cansel(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
