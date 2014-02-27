package com.example.hockey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ReadWriteModel extends Thread {
	public static InputStream in;
	public static OutputStream out;
	
	BluetoothSocket socket;
	
	public boolean writef=false;
	public boolean writeok;
	private String str;
	private String tmpStr;
	
	Connect parent;
	
	public ReadWriteModel(BluetoothSocket socket,Connect p,boolean wf){
		writef = wf;
		writeok=false;
		parent = p;
		this.socket = socket;
		
		in = null;
		out = null;
	}
	
	public void set(String str){
		tmpStr = new String(str);
		writeok=true;
	}
	
	public void write(byte[] buf){
		try {
			String rstr = new String(buf,"UTF-8");
			Log.d("koko","koko write rstr = "+rstr +" num="+buf.length);
			out.write(buf, 0, buf.length);
		} catch( IOException e ){
			e.printStackTrace();
		}
	}
	
    private String read() throws IOException {
        byte[] buf = new byte[1024];
        int numRead = in.read(buf,0,buf.length);
        if( numRead == 0 ){
        	Log.d("ret","koko read 0");
        	return null;
        }
        String ret = new String(buf,"utf-8");
        Log.d("ret","koko read ret = "+ret);
        return ret;
    }	
    
	@Override
	public void run(){
		
		while ( true ){
			if( writef ){
				if( writeok ){
					str = tmpStr;
					try {
						out = socket.getOutputStream();
					} catch( IOException e ){
						e.printStackTrace();
					}
					try {
						//Log.d("str","koko write str = "+str);
						write(str.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO 自動生成された catch ブロック
			            Log.e("TAG", "temp sockets not created", e);
					}
					writeok = false;
				}
			} else {
				try {
					in = socket.getInputStream();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
		            Log.e("TAG", "temp sockets not created", e);
				}
				String rstr = null;
				try {
					rstr = read();
				} catch (IOException e ){
					e.printStackTrace();
				}
				if( rstr != null && !rstr.isEmpty() ){
				Log.d("str","koko read...OK ");
						parent.recieveString(rstr);
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
