package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Mallet extends Task {
	private final static float SIZE = 50f;	//初期サイズ
	private Circle cir = null;				//マレットの円
	private Paint paint = new Paint();
	private Bitmap mainimg;
	private float main_resize;
	private float resize_counter = 0;
	private float bairitu=0;
	private Resources res = App.getInstance().getResources();
	
	private boolean survival = true;
	private int killcount = 0;

	private Rect src,dst;
	
	public Mallet(float x,float y){
		cir = new Circle( x, y, SIZE );
		mainimg = BitmapFactory.decodeResource(res, R.drawable.mallet_main);
		src = new Rect(0,0,mainimg.getWidth(),mainimg.getHeight());
		dst = new Rect(0,0,mainimg.getWidth(),mainimg.getHeight());
		main_resize = src.width();
		survival = true;
		killcount = 0;
		
		Log.d("Mallet","Mallet zahyo ( "+x+", "+y+" )");
		
		this.onUpdate();
	}
	
	@Override
	public boolean onUpdate(){
		
		//拡大率
		//削除時徐々に小さく
		if( killcount > 4 ){
			main_resize -= mainimg.getWidth()/10f;
			if( main_resize < 0 ){
				main_resize = 0;
			}
		} else {//通常時徐々に大きく
			resize_counter+=0.01f;
			if( resize_counter >= Math.PI/2.0f ){
				resize_counter = (float)Math.PI/2f;
			}
			bairitu = (float)Math.sin(resize_counter)*0.3f;
			main_resize = mainimg.getWidth() + mainimg.getWidth()*bairitu;
		}
		
		cir.set(cir.getX(), cir.getY(), main_resize/2f);
		
		
		dst.set(cir.getDrawX(), cir.getDrawY(), cir.getDrawX()+(int)main_resize, cir.getDrawY()+(int)main_resize);
		
//		Log.d("resize","resize : "+main_resize);
		
		boolean ok = true;
		
		if( !survival ){
			killcount++;
			if( killcount > 14 ){
				ok = false;
			}
		} else {
			killcount=0;
			survival=false;
		}
		
		return ok;
	}
	
	@Override
	public void onDraw( Canvas c ){
		c.drawBitmap(mainimg, src, dst, paint);
	}
	
	
	//削除する
	public void kill(){
		survival = false;
	}
	
	//生かす
	public void revive(){
		survival = true;
	}
	
	//生きてる？(true) or 死んでる？(false)
	public boolean getSurvival(){
		return survival;
	}
	
	public float getX(){
		return cir.getX();
	}
	public float getY(){
		return cir.getY();
	}
	public Circle getCircle(){
		return cir;
	}
}
