package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class NumberView {
	private Paint paint = new Paint();	
	private Bitmap imgs;
	private Bitmap imgwl;
	private Resources res = App.getInstance().getResources();
	private Rect srcs[];
	private Rect dst[];
	private Rect srcwl[];
	private final int wids,widwl,height;
	private String wc,lc;
	public NumberView(){
		imgs = BitmapFactory.decodeResource(res, R.drawable.number);
		imgwl = BitmapFactory.decodeResource(res, R.drawable.winlose);

		wids = imgs.getWidth()/10;
		widwl = imgwl.getWidth()/2;
		height = imgs.getHeight();
		
		wc=lc=null;
		
		srcs=new Rect[10];
		srcwl=new Rect[2];
		dst=new Rect[20];
		for(int i=0;i<20;i++){
			dst[i]=new Rect(0,0,0,0);
		}
		int sxi = imgs.getWidth()/10;
		for(int i=0;i<10;i++){
			srcs[i] = new Rect(i*sxi,0,(i+1)*sxi,imgs.getHeight());
		}
		srcwl[0] = new Rect(0,0,imgwl.getWidth()/2,imgwl.getHeight());
		srcwl[1] = new Rect(imgwl.getWidth()/2,0,imgwl.getWidth(),imgwl.getHeight());
	}
	
	public void set(int drawy,String winc,String losec){
		if( winc == wc && losec == lc ) return;
		wc = winc;
		lc = losec;
		int sx = RatioAdjustment.Width()/2 -wc.length()*wids;
		int cc=0;
		for(int i=0;i<wc.length();i++){
			dst[cc++] = new Rect(sx,drawy,sx+wids,drawy+height);
			sx+=wids;
		}
		dst[cc++] = new Rect(sx-widwl,drawy+height/3,sx,drawy+height/3+height);
		sx=RatioAdjustment.Width()/2;
		for(int i=0;i<lc.length();i++){
			dst[cc++] = new Rect(sx,drawy,sx+wids,drawy+height);
			sx+=wids;
		}
		dst[cc++] = new Rect(sx-widwl,drawy+height/3,sx,drawy+height/3+height);
	}
	public void draw(Canvas c){
		if( wc == null || lc == null ) return;
		Log.d("koko","koko wc = "+wc+" lc = "+lc);
		int cc = 0;
		for(int i=0;i<wc.length();i++){
			Log.d("koko","koko WC integer = "+Integer.valueOf(wc.substring(i, i+1)));
			c.drawBitmap(imgs, srcs[Integer.valueOf(wc.substring(i, i+1))], dst[cc++], paint);
		}
		c.drawBitmap(imgwl, srcwl[0], dst[cc++], paint);
		for(int i=0;i<lc.length();i++){
			Log.d("koko","koko LC integer = "+Integer.valueOf(lc.substring(i, i+1)));
			c.drawBitmap(imgs, srcs[Integer.valueOf(lc.substring(i, i+1))], dst[cc++], paint);
		}
		c.drawBitmap(imgwl, srcwl[1], dst[cc++], paint);
	}
}
