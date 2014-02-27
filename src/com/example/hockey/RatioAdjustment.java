package com.example.hockey;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class RatioAdjustment {
	
	private static int rleft,rright;
	private static int goaly;
	private static int mleftspace;
	private static int lifegaugesize;
	
	private static int fsize;
	private static int fcenterx,fcentery;
	
	private static int width,height;
	
	//マレットの直径
	private static float diameter;
	
	public static void init(Activity main){
		WindowManager wm = (WindowManager)main.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		width = disp.getWidth();
		height = disp.getHeight();
		
		Log.d("Ratio","ratio ( "+width+" , "+height+" ) ");
		
		rleft = width/9;
		mleftspace = width/144;
		lifegaugesize = rleft - mleftspace*2;
		rright = width;
		goaly = height - height/10;
		
		fsize = (width-rleft);
		fcenterx = (width + rleft)/2;
		fcentery = 0;
		
		Bitmap mainimg = BitmapFactory.decodeResource(App.getInstance().getResources(), R.drawable.mallet_main);
		diameter = mainimg.getWidth();
	}
	
	//純粋な横幅
	public static int Width(){
		return width;
	}
	
	//純粋な縦幅
	public static int Height(){
		return height;
	}
	
	//←の反射壁のX座標
	public static int RefLeft(){
		return rleft;
	}
	//→の反射壁のX座標
	public static int RefRight(){
		return rright;
	}
	
	//ゴールのY座標
	public static int GoalY(){
		return goaly;
	}
	
	//左端からライフまでの隙間の大きさ
	public static int MaxLeftSpace(){
		return mleftspace;
	}
	
	//ライフのサイズ
	public static int LifeGaugeSize(){
		return lifegaugesize;
	}

	//フィールドの横幅
	public static int FieldSizeX(){
		return fsize;
	}
	//フィールド縦幅
	public static int FieldSizeY(){
		return height*2;
	}
	
	//フィールド上の中心座標X
	public static int FieldCenterX(){
		return fcenterx;
	}
	
	//フィールド上の中心座標Y
	public static int FieldCenterY(){
		return fcentery;
	}
	
	//マレットの直径を取得
	public static float MalletD(){
		return diameter;
	}
	//マレットの半径を取得
	public static float MalletR(){
		return diameter/2f;
	}
	
	//比率座標を求めるX
	public static float getHX(float x){
		return (x - (float)rleft)/(float)FieldSizeX();
	}
	//比率座標を求めるY
	public static float getHY(float y){
		return ((float)height-y) / (float)FieldSizeY();
	}
	
	//比率座標を戻すX
	public static float changeHX(float x){
		return (1f-x)*(float)FieldSizeX()+(float)rleft;
	}
	//比率座標を戻すY
	public static float changeHY(float y){
		return (1f-y)*(float)FieldSizeY()+height;
	}
	//比率半径を返す
	public static float changeBR(float b){
		return (diameter + diameter*b)/2f;
	}
}
