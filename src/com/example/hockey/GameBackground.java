package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameBackground extends Task{
	private Paint paint = new Paint();	
	private Bitmap img;
	private Resources res = App.getInstance().getResources();
	private Rect src;
	private Rect dst;
	
	public GameBackground(){
		
		img = BitmapFactory.decodeResource(res, R.drawable.mainbackground);
		src = new Rect(0,0,img.getWidth(),img.getHeight());
		dst = new Rect(0,0,img.getWidth(),img.getHeight());
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
