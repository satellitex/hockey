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
		int N;
		float x,y,b,vx,vy;
		Pack pack;
		switch ( kind ){
		case 0:	//スタート前
			int state = s.nextInt();
			Ready ready = parent.getReady();
//			Log.d("koko","koko state is "+state);
			if( state == 4 ){
				if( parent.getConnect().isClient() && ready.isHaOK() ){
					parent.gameStart();
				}
			} else if( state >= 1 ){
				if( !parent.isStart() && !parent.isEnd() &&  parent.getReady().isOkOk() ){
					parent.getReady().setYoi();
				}
			}
			break;
		case 1:	//クライアント側からくるデータ
			parent.gameStart();
			N = s.nextInt();
			pack = parent.getPack();
			pack.rmEnemy();
			for(int i=0;i<N;i++){
				x = s.nextFloat();
				y = s.nextFloat();
				b = s.nextFloat();
				pack.addEnemy(i, RatioAdjustment.changeHX(x) , RatioAdjustment.changeHY(y), b);
			}
			break;
		case 2:	//サーバー側からくるデータ
			N = s.nextInt();
			pack = parent.getPack();
			pack.rmEnemy();
			for(int i=0;i<N;i++){
				x = s.nextFloat();
				y = s.nextFloat();
				b = s.nextFloat();
				pack.addEnemy(i, RatioAdjustment.changeHX(x) , RatioAdjustment.changeHY(y), b);
			}
			x = s.nextFloat();
			y = s.nextFloat();
			vx = s.nextFloat();
			vy = s.nextFloat();
			parent.getPack().set(RatioAdjustment.changeHX(x),RatioAdjustment.changeHY(y), vx, vy);
			break;
		case 3://結果を渡される
			N = s.nextInt();
			Log.d("koko","koko input N = "+N);
			if( !parent.isEnd() && parent.isStart()){
				Log.d("koko","koko input accept");
				parent.gameEnd();
				if( N == 0 ){
					parent.setWin();
					Log.d("koko","koko input accept1");
				} else {
					parent.setLose();
					Log.d("koko","koko input accept2");
				}
			}
			break;
		case 4://改訂版
			if( !parent.isStart() ) {
				parent.gameStart();
			}
			x = s.nextFloat();
			y = s.nextFloat();
			vx = s.nextFloat();
			vy = s.nextFloat();
			parent.getPack().set(RatioAdjustment.changeHX(x),RatioAdjustment.changeHY(y), vx, vy);
		}
	}
}
