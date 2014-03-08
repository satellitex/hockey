package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameResult extends Task {
	private Paint paint = new Paint();	
	private Bitmap imgw;
	private Bitmap imgl;
	private Bitmap maku;
	private Resources res = App.getInstance().getResources();
	private Rect src;
	private Rect dst;
	private Rect srcm;
	private Rect dstm;	
	private final int sx,sy;
	
	private boolean winflag;
	private boolean loseflag;
	
	private int count;
	
	public GameResult(){
		
		imgw = BitmapFactory.decodeResource(res, R.drawable.winner);
		imgl = BitmapFactory.decodeResource(res, R.drawable.loser);
		maku = BitmapFactory.decodeResource(res, R.drawable.usukuro);
		
		winflag=false;
		loseflag=false;
		count = 0;

		sx = 0;
		sy = (int)RatioAdjustment.MalletR();
		
		src = new Rect(0,0,imgw.getWidth(),imgw.getHeight());
		dst = new Rect(sx,sy,sx+imgw.getWidth(),sy+imgw.getHeight());

		dstm = new Rect(0,0,RatioAdjustment.Width(),RatioAdjustment.Height());
		srcm= new Rect(0,0,maku.getWidth(),maku.getHeight());
	}
	public void init(){
		winflag=false;
		loseflag=false;		
		count = 0;
	}
	public void setWin(){
		winflag=true;
	}
	public void setLose(){
		loseflag=true;
	}
	public boolean isWin(){ return winflag; }
	public boolean isLose(){ return loseflag; }
	
	@Override
	public boolean onUpdate() {
		count++;
		if( count > 30 ){
			return true;
		}
		return false;
	}
	
	@Override
	public void onDraw(Canvas c){
		c.drawBitmap(maku, srcm, dstm, paint);
		if( loseflag ){
			c.drawBitmap(imgl, src, dst, paint);
		}
		if( winflag ){
			c.drawBitmap(imgw, src, dst, paint);
		}
	}
}
