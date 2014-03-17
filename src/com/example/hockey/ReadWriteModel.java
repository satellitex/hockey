package com.example.hockey;

import java.io.BufferedReader;
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
	
	private boolean loopflag=true;
	Connect parent;
	
	public ReadWriteModel(BluetoothSocket socket,Connect p,boolean wf){
		writef = wf;
		writeok=false;
		parent = p;
		this.socket = socket;
		
		in = null;
		out = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		Log.d("loop","koko Read Write Model");
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
		
		if( writef ){
			while ( loopflag ){
				if( writeok ){
					tmpStr+="\n";
					str = tmpStr;
/*
					try {
						out = socket.getOutputStream();
					} catch( IOException e ){
						e.printStackTrace();
						continue;
					}
*/
					try {
						write(str.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
					writeok = false;
				}
			}
		} else {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String rstr;
			try {
				Log.d("in","in loop");
				while( loopflag ){
					if ( (rstr = br.readLine()) != null ){
						Log.d("in","in loop message :"+rstr);
						parent.recieveString(rstr);
					}
				}
				Log.d("in","in loop end");
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

		}
	}

	public void cansel(){
		loopflag=false;
	}
}
