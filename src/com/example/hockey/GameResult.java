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
	private Bitmap imgf;
	private Bitmap maku;
	private Resources res = App.getInstance().getResources();
	private Rect src;
	private Rect dst;
	private Rect srcm;
	private Rect dstm;
	private Rect srcf;
	private Rect dstf;
	private final int sx,sy;
	
	private NumberView nums;
	
	private boolean winflag;
	private boolean loseflag;
	
	private int count;
	
	private int wincount;
	private int losecount;
	
	public GameResult(){
		nums = new NumberView();
		
		imgw = BitmapFactory.decodeResource(res, R.drawable.winner);
		imgl = BitmapFactory.decodeResource(res, R.drawable.loser);
		imgf = BitmapFactory.decodeResource(res, R.drawable.gameendbutton);
		maku = BitmapFactory.decodeResource(res, R.drawable.usukuro);
		
		winflag=false;
		loseflag=false;
		count = 0;
		wincount = 0;

		sx = 0;
		sy = (int)RatioAdjustment.MalletR();
		
		src = new Rect(0,0,imgw.getWidth(),imgw.getHeight());
		dst = new Rect(sx,sy,sx+imgw.getWidth(),sy+imgw.getHeight());

		dstm = new Rect(0,0,RatioAdjustment.Width(),RatioAdjustment.Height());
		srcm= new Rect(0,0,maku.getWidth(),maku.getHeight());

		dstf = new Rect(0,0,imgf.getWidth(),imgf.getHeight());
		srcf= new Rect(0,0,imgf.getWidth(),imgf.getHeight());
	}
	public void init(){
		winflag=false;
		loseflag=false;		
		count = 0;
	}
	public void setWin(){
		if( !winflag ) wincount++;
		winflag=true;
	}
	public void setLose(){
		if( !loseflag ) losecount++;
		loseflag=true;
	}
	public boolean isWin(){ return winflag; }
	public boolean isLose(){ return loseflag; }
	
	public int getWin(){ return wincount; }
	public int getLose(){ return losecount; }
	
	@Override
	public boolean onUpdate() {
		count++;
		nums.set(sy+imgw.getHeight(), Integer.toString(wincount), Integer.toString(losecount));
		if( count > 30 ){
			return true;
		}
		return false;
	}
	
	@Override
	public void onDraw(Canvas c){
		c.drawBitmap(maku, srcm, dstm, paint);
		c.drawBitmap(imgf, srcf, dstf, paint);
		if( loseflag ){
			c.drawBitmap(imgl, src, dst, paint);
		}
		if( winflag ){
			c.drawBitmap(imgw, src, dst, paint);
		}
		nums.draw(c);
	}
}
