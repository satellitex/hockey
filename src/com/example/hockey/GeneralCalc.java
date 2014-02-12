package com.example.hockey;

import android.util.Log;

public class GeneralCalc {
	public static boolean CheckCircleInPoint(Circle c,float sx,float sy){
		if( Math.hypot(sx-c.getX(), sy-c.getY()) <= c.getR() ){
			return true;
		}
		return false;
	}
	public static boolean CheckCircleInCircle(Circle c1,Circle c2){
		if( Math.hypot(c1.getX() - c2.getX(), c1.getY() - c2.getY()) < c1.getR() + c2.getR() ){
			return true;
		}
		Log.d("CircleIn","CircleIn  c1.x = "+c1.getX()+ ", c1.y = "+c1.getY()+", c1.R="+c1.getR()+"  c1.x = "+c1.getX()+ ", c1.y = "+c1.getY()+", c1.R="+c1.getR());
		return false;
	}
	public static boolean CheckCircleRefLeft(Circle c){
		if( RatioAdjustment.RefLeft() > c.getX()-c.getR() ){
			return true;
		}
		return false;
	}
	public static boolean CheckCircleRefRight(Circle c){
		if( RatioAdjustment.RefRight() < c.getX()+c.getR() ){
			return true;
		}
		return false;
	}
	public static float RadToDegree(float rad){
		return rad * 360/(2*(float)Math.PI);
	}
}
