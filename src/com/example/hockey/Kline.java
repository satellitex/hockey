package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class Kline extends Task{
	boolean viewflag;
	private Paint paint = new Paint();	
	private Matrix matrix = new Matrix();
	private Bitmap img;
	private Resources res = App.getInstance().getResources();
	private Rect src;
	private Rect dst;
	
	public Kline(){
		int sx = RatioAdjustment.RefLeft(),sy = RatioAdjustment.GoalY();
		
		img = BitmapFactory.decodeResource(res, R.drawable.klineb);
		src = new Rect(0,0,img.getWidth(),img.getHeight());
		dst = new Rect(sx,sy,sx+img.getWidth(),sy+img.getHeight());
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
		c.drawBitmap(img, src, dst, paint);
	}	

}
