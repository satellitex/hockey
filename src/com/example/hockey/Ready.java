package com.example.hockey;

import java.util.Scanner;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Ready extends Task {
	
	private Paint paint = new Paint();	
	private Matrix matrix = new Matrix();
	private Bitmap img1;
	private Bitmap img2;
	private Bitmap imgy;
	private Bitmap imgh;
	private Bitmap maku;
	private Resources res = App.getInstance().getResources();
	private Rect src;
	private Rect dst1;
	private Rect dst2;
	private Rect srcm;
	private Rect dstm;
	
	private boolean ok_flag;
	private boolean yoi_flag;
	private boolean hazime_flag;
	private int ok_count;
	
	private final int sx,sy;
	
	public Ready(){
		ok_flag=false;
		ok_count = 0;
		img1 = BitmapFactory.decodeResource(res, R.drawable.wait_bar);
		img2 = BitmapFactory.decodeResource(res, R.drawable.wait_ok);
		imgy = BitmapFactory.decodeResource(res, R.drawable.wait_yoi);
		imgh = BitmapFactory.decodeResource(res, R.drawable.wait_hazime);
		maku = BitmapFactory.decodeResource(res, R.drawable.usukuro);

		sx = 0;
		sy = RatioAdjustment.Height()/2 - img1.getHeight()/2;
		
		src = new Rect(0,0,img1.getWidth(),img1.getHeight());
		dst1 = new Rect(sx,sy,sx+img1.getWidth(),sy+img1.getHeight());
		dst2 = new Rect(sx+img1.getWidth(),sy,sx+img1.getWidth()*2,sy+img1.getHeight());

		dstm = new Rect(0,0,RatioAdjustment.Width(),RatioAdjustment.Height());
		srcm= new Rect(0,0,maku.getWidth(),maku.getHeight());
	}
	
	public void setOk(){ ok_flag = true; }
	public void setYoi(){ if( ok_flag ) yoi_flag= true; }
	public void setHa(){ if( yoi_flag )hazime_flag = true; }
	
	public boolean isOk(){ return ok_flag; }
	public boolean isOkOk(){ if( ok_count == 20 ) return true; return false; }
	public boolean isYoi(){ return yoi_flag; }
	public boolean isHa(){ return hazime_flag; }	
	
	@Override
	public boolean onUpdate() {
		if( ok_flag ){
			if( ok_count < 20 ){
				ok_count++;
				float minus = (float)Math.sin(GeneralCalc.DegreeToRad((float)ok_count*4.5f))*(float)RatioAdjustment.Width();
				dst1.set(dst1.left-(int)minus, dst1.top, dst1.right-(int)minus, dst1.bottom);
				dst2.set(dst2.left-(int)minus, dst2.top, dst2.right-(int)minus, dst2.bottom);
			} else {
				dst2.set(sx,sy,sx+img1.getWidth(),sy+img1.getHeight());
				dst1.set(sx+img1.getWidth(),sy,sx+img1.getWidth()*2,sy+img1.getHeight());
			}
		}
		if( yoi_flag ){
			if( ok_count < 40 ){
				ok_count++;
				int tc = ok_count-20;
				float minus = (float)Math.sin(GeneralCalc.DegreeToRad((float)tc*4.5f))*(float)RatioAdjustment.Width();
				dst1.set(dst1.left-(int)minus, dst1.top, dst1.right-(int)minus, dst1.bottom);
				dst2.set(dst2.left-(int)minus, dst2.top, dst2.right-(int)minus, dst2.bottom);
			} else if( ok_count <70 ){
				ok_count++;
				dst1.set(sx,sy,sx+img1.getWidth(),sy+img1.getHeight());
				dst2.set(sx+img1.getWidth(),sy,sx+img1.getWidth()*2,sy+img1.getHeight());
			} else {
				setHa();
			}
		}
		if( hazime_flag ){
			if( ok_count < 80 ){
				ok_count++;
				int tc = ok_count-70;
				float minus = (float)Math.sin(GeneralCalc.DegreeToRad((float)tc*9f))*(float)RatioAdjustment.Width();
				dst1.set(dst1.left-(int)minus, dst1.top, dst1.right-(int)minus, dst1.bottom);
				dst2.set(dst2.left-(int)minus, dst2.top, dst2.right-(int)minus, dst2.bottom);
			} else if( ok_count < 90 ){
				dst2.set(sx,sy,sx+img1.getWidth(),sy+img1.getHeight());
				dst1.set(sx+img1.getWidth(),sy,sx+img1.getWidth()*2,sy+img1.getHeight());
				ok_count++;
			} else if( ok_count < 120 ){
				ok_count++;
				int tc = ok_count-90;
				float minus = (float)Math.sin(GeneralCalc.DegreeToRad((float)tc*9f))*(float)RatioAdjustment.Width();
				dst2.set(dst2.left-(int)minus, dst2.top, dst2.right-(int)minus, dst2.bottom);
			} else {
				dst2.set(sx+img1.getWidth(),sy,sx+img1.getWidth()*2,sy+img1.getHeight());
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onDraw(Canvas c){
		c.drawBitmap(maku, srcm, dstm, paint);
		if( hazime_flag ){
			c.drawBitmap(imgy, src, dst1, paint);
			c.drawBitmap(imgh, src, dst2, paint);
		} else if( yoi_flag ){
			c.drawBitmap(imgy, src, dst1, paint);
			c.drawBitmap(img2, src, dst2, paint);
		} else if( ok_flag ){
			c.drawBitmap(img1, src, dst1, paint);
			c.drawBitmap(img2, src, dst2, paint);
		} else {
			c.drawBitmap(img1, src, dst1, paint);
		}
	}
}
