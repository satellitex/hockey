package com.example.hockey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class EnemyMallet extends Task{
	private final float SIZE;	//初期サイズ
	private Circle cir = null;				//マレットの円
	private float main_resize;
	private float resize_counter = 0;
	private float bairitu=0;
	
	public EnemyMallet(float x,float y,float c){
		SIZE = RatioAdjustment.MalletD();
		cir = new Circle( x, y, SIZE/2f );
		main_resize = SIZE;
		resize_counter = c;
	}
	
	@Override
	public boolean onUpdate() {		
		//拡大率
		//通常時徐々に大きく
		resize_counter+=0.01f;
		if( resize_counter >= Math.PI/2.0f ){
			resize_counter = (float)Math.PI/2f;
		}
		bairitu = (float)Math.sin(resize_counter)*0.3f;
		main_resize = SIZE + SIZE*bairitu;
		cir.set(cir.getX(), cir.getY(), main_resize/2f);		
		return true;
	}	
	public Circle getCircle(){
		return cir;
	}
}
