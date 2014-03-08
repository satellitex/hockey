package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class MalletPointFrame extends Task{
	
	boolean viewflag;
	private Paint paint = new Paint();	
	private Bitmap fraimg;
	private Resources res = App.getInstance().getResources();
	private Rect src;
	private Rect dst;
	
	public MalletPointFrame(){
		fraimg = BitmapFactory.decodeResource(res, R.drawable.waku);
		src = new Rect(0,0,fraimg.getWidth(),fraimg.getHeight());
		dst = new Rect(0,0,fraimg.getWidth(),fraimg.getHeight());
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
		c.drawBitmap(fraimg, src, dst, paint);
	}	

}
