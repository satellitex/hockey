package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Ready extends Task {
	
	private Paint paint = new Paint();	
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
	
	private final int sx,sy,sx2,sx3;
	
	private final int p1=10,p2=p1*2,p3=p2+p1/2,p4=p3+p1,p5=p4+p1/2,p6=p5+p1;
	private final float pk = 90f/(float)p1;
	
	public Ready(){
		
		ok_flag=false;
		yoi_flag=false;
		hazime_flag=false;
		ok_count = 0;
		img1 = BitmapFactory.decodeResource(res, R.drawable.wait_bar);
		img2 = BitmapFactory.decodeResource(res, R.drawable.wait_ok);
		imgy = BitmapFactory.decodeResource(res, R.drawable.wait_yoi);
		imgh = BitmapFactory.decodeResource(res, R.drawable.wait_hazime);
		maku = BitmapFactory.decodeResource(res, R.drawable.usukuro);

		sx = 0;
		sx2 = img1.getWidth();
		sx3 = sx2*2;
		sy = RatioAdjustment.Height()/2 - img1.getHeight()/2;
		
		src = new Rect(0,0,img1.getWidth(),img1.getHeight());
		dst1 = new Rect(sx,sy,sx+img1.getWidth(),sy+img1.getHeight());
		dst2 = new Rect(sx+img1.getWidth(),sy,sx+img1.getWidth()*2,sy+img1.getHeight());

		dstm = new Rect(0,0,RatioAdjustment.Width(),RatioAdjustment.Height());
		srcm= new Rect(0,0,maku.getWidth(),maku.getHeight());
	}
	
	public void init(){
		ok_flag=false;
		yoi_flag=false;
		hazime_flag=false;
		ok_count = 0;		
	}
	
	public void setOk(){ ok_flag = true; }
	public void setYoi(){ if( ok_flag ) yoi_flag= true; }
	public void setHa(){ if( yoi_flag )hazime_flag = true; }
	
	public boolean isOk(){ return ok_flag; }
	public boolean isOkOk(){ if( ok_count >= p1 ) return true; return false; }
	public boolean isYoi(){ return yoi_flag; }
	public boolean isHa(){ return hazime_flag; }	
	public boolean isHaOK(){ if( ok_count >= p6 ) return true; return false; }  
	
	@Override
	public boolean onUpdate() {
		if( ok_flag ){
			if( ok_count < p1 ){
				ok_count++;
				float minus = (float)Math.sin(GeneralCalc.DegreeToRad((float)ok_count*pk))*(float)RatioAdjustment.Width();
				dst1.set(sx-(int)minus, dst1.top, sx2-(int)minus, dst1.bottom);
				dst2.set(sx2-(int)minus, dst2.top, sx3-(int)minus, dst2.bottom);
			} else {
				dst2.set(sx,sy,sx2,sy+img1.getHeight());
				dst1.set(sx2,sy,sx3,sy+img1.getHeight());
			}
		}
		if( yoi_flag ){
			if( ok_count < p2 ){
				ok_count++;
				int tc = ok_count-p1;
				float minus = (float)Math.sin(GeneralCalc.DegreeToRad((float)tc*pk))*(float)RatioAdjustment.Width();
				dst2.set(sx-(int)minus, dst1.top, sx2-(int)minus, dst1.bottom);
				dst1.set(sx2-(int)minus, dst2.top, sx3-(int)minus, dst2.bottom);
			} else if( ok_count <p3 ){
				ok_count++;
				dst1.set(sx,sy,sx2,sy+img1.getHeight());
				dst2.set(sx2,sy,sx3,sy+img1.getHeight());
			} else {
				setHa();
			}
		}
		if( hazime_flag ){
			if( ok_count < p4 ){
				ok_count++;
				int tc = ok_count-p3;
				float minus = (float)Math.sin(GeneralCalc.DegreeToRad((float)tc*pk))*(float)RatioAdjustment.Width();
				dst1.set(sx-(int)minus, dst1.top, sx2-(int)minus, dst1.bottom);
				dst2.set(sx2-(int)minus, dst2.top, sx3-(int)minus, dst2.bottom);
			} else if( ok_count < p5 ){
				ok_count++;
				dst2.set(sx,sy,sx2,sy+img1.getHeight());
				dst1.set(sx2,sy,sx3,sy+img1.getHeight());
			} else if( ok_count < p6 ){
				ok_count++;
				int tc = ok_count-p5;
				float minus = (float)Math.sin(GeneralCalc.DegreeToRad((float)tc*pk))*(float)RatioAdjustment.Width();
				dst2.set(sx-(int)minus, dst1.top, sx2-(int)minus, dst1.bottom);
			} else {
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
