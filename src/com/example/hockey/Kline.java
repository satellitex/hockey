package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Kline extends Task{
	boolean viewflag;
	private Paint paint = new Paint();	
	private Bitmap img;
	private Bitmap imgzone;
	private Resources res = App.getInstance().getResources();
	private Rect src;
	private Rect dst;
	private Rect srcz;
	private Rect dstz;
	
	public Kline(){
		int sx = RatioAdjustment.RefLeft(),sy = RatioAdjustment.GoalY();
		
		img = BitmapFactory.decodeResource(res, R.drawable.klineb);
		imgzone = BitmapFactory.decodeResource(res, R.drawable.dontouch);	
		src = new Rect(0,0,img.getWidth(),img.getHeight());
		dst = new Rect(sx,sy-img.getHeight()*2,sx+img.getWidth(),sy-img.getHeight());
		srcz = new Rect(0,0,imgzone.getWidth(),imgzone.getHeight());
		dstz = new Rect(sx,0,sx+imgzone.getWidth(),imgzone.getHeight());
		viewflag = true;
	}

	public void ViewOn(){
		viewflag=true;
	}
	public void ViewOff(){
		viewflag=false;
	}
	
	@Override
	public boolean onUpdate() {
		return true;
	}
	
	@Override
	public void onDraw(Canvas c){
		c.drawBitmap(imgzone, srcz, dstz, paint);
		c.drawBitmap(img, src, dst, paint);
	}

}
