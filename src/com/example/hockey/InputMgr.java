package com.example.hockey;

import java.util.InputMismatchException;
import java.util.Scanner;

import android.util.Log;

public class InputMgr{
	
	private GameMgr parent;
	
	public InputMgr(GameMgr p){
		parent = p;
	}
	
	public void onUpdate(Scanner s){
		Log.d("koko","koko in InputMgr()");
		int kind;
		try{
			kind = s.nextInt();
		} catch( InputMismatchException e ){
			return;
		}
		Log.d("koko","koko kind is "+kind);
		switch ( kind ){
		case 0:	//スタート前
			int state = s.nextInt();
			Log.d("koko","koko state is "+state);
			if( state == 4 ){
				parent.gameStart();
			} else if( state == 1 ){
				parent.getReady().setYoi();
			}
			break;
		case 1:	//サーバー側
			break;
		case 2:	//クライアント側
			break;
		}
	}
}
