package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class MalletPoint extends Task {
	
	private GameMgr parent;
	
	private Paint paint = new Paint();	
	private Matrix matrix = new Matrix();
	private Bitmap suke;
	private Bitmap suke2;
	private Bitmap suke3n;
	private Bitmap suke3r;
	private Bitmap suke4n;
	private Bitmap suke4r;	
	private Resources res = App.getInstance().getResources();
	
	private Status st;
	
	private int stx,sty;//一番ひだりうえのX座標、Y座標
	private int may;//間
	
	
	private Rect srcs,src[],dst[],dstc[];//元画像、表示部位
	
	private final static float DesHp=0.01f;
	private static int SIZE;
	
	public MalletPoint(GameMgr g,Status s){
		
		st = s;
		
		parent = g;
		
		suke  = BitmapFactory.decodeResource(res, R.drawable.suke);
		suke2 = BitmapFactory.decodeResource(res, R.drawable.suke2);
		suke3n = BitmapFactory.decodeResource(res, R.drawable.suke3n);
		suke3r = BitmapFactory.decodeResource(res, R.drawable.suke3r);
		
		srcs = new Rect(0,0,suke.getWidth(),suke.getHeight());
		src = new Rect[5];
		dst = new Rect[5];
		dstc = new Rect[5];
		for(int i=0;i<5;i++){
			src[i] = new Rect(0,0,suke.getWidth(),suke.getHeight());
			dst[i] = new Rect(0,0,suke.getWidth(),suke.getHeight());
			dstc[i] = new Rect(0,0,suke.getWidth(),suke.getHeight());
		}
		
		SIZE = RatioAdjustment.LifeGaugeSize();
		
		stx = RatioAdjustment.MaxLeftSpace();
		sty = RatioAdjustment.MaxLeftSpace()*2;
		may = SIZE;
	}
	
	
		
	@Override
	public boolean onUpdate(){
		
		//マレット消費分だけ体力を減らす
		if( parent.CountMallet() > 0 ){
			st.DecreaseHp(parent.CountMallet()*DesHp);		
		} else {//つかってなければ回復
			st.HealHp(DesHp);
		}

		
		Log.d("hp","hitpoint = "+st.getHp());
		
		//表示すべき体力の調整
		boolean viewok = false;
		int x=stx,y=sty;
		for(int i=5;i>0;i--){
			int id = i-1;
			if( !viewok ){
				if( st.getHp() >= (float)i ){
					src[id].set(0,0,suke.getWidth(),suke.getHeight());
					dstc[id].set(x,y,x+SIZE,y+SIZE);
					viewok = true;
				} else {
					float zr = (float)i-st.getHp();
//					Log.d("zr","hpid= "+id+" zr = "+zr);
					if( zr >= 1f ){
						src[id].set(0,0,0,0);
						dstc[id].set(x,y,x+SIZE,y+SIZE);
					} else {
						float zrr = (float)suke.getHeight()*zr;
//						Log.d("zr","zrr = "+(int)zrr);
						src[id].set(0,(int)zrr,suke.getWidth(),suke.getHeight());
						dstc[id].set(x,y+(int)(zr*(float)SIZE),x+SIZE,y+SIZE);
						viewok=true;
					}
				}
			} else {
				src[id].set(0,0,suke.getWidth(),suke.getHeight());
				dstc[id].set(x,y,x+SIZE,y+SIZE);
			}
			dst[id].set(x,y,x+SIZE,y+SIZE);
			y+=may;
		}
		
		//ギザギザ回転
		matrix.postRotate((float)90);
		suke4r = Bitmap.createBitmap(suke3r,0,0,suke3r.getWidth(),suke3r.getHeight(),matrix,true);
		suke4n = Bitmap.createBitmap(suke3n,0,0,suke3n.getWidth(),suke3n.getHeight(),matrix,true);				
		return true;
	}
	
	@Override
	public void onDraw( Canvas c ){
		for(int i=0;i<5;i++){
			if( st.getBarrier() <= i ){
				c.drawBitmap(suke4r, src[i], dstc[i], paint);				
			} else {
				c.drawBitmap(suke4n, src[i], dstc[i], paint);
			}
			c.drawBitmap(suke, srcs, dst[i], paint);
			
//			Log.d("suke2","suke2 ( "+src[i].left+" , "+src[i].top+" , "+src[i].right+" , "+src[i].bottom);
			
			c.drawBitmap(suke2, src[i], dstc[i], paint);
		}
		
	}
}
