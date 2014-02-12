package com.example.hockey;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class RatioAdjustment {
	private static MainActivity main;
	
	private static int rleft,rright;
	private static int goaly;
	private static int mleftspace;
	private static int lifegaugesize;
	
	private static int fsize;
	private static int fcenterx,fcentery;
	
	private static int width,height;
	
	public static void init(MainActivity m){
		main = m;
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
		return height;
	}
	
	//フィールド上の中心座標X
	public static int FieldCenterX(){
		return fcenterx;
	}
	
	//フィールド上の中心座標Y
	public static int FieldCenterY(){
		return fcentery;
	}	
}
