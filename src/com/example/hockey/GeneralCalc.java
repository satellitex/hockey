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
	public static float PointToPointAngle(float x1,float y1,float x2,float y2){
		return (float)Math.atan2(y2-y1, x2-x1);
	}
	public static float CircleToCircleAngle(Circle c1,Circle c2){
		return PointToPointAngle( c1.getX(), c1.getY(), c2.getX(), c2.getY() );
	}
	public static float CircleInCircleSize(Circle c1,Circle c2){//円と円の重なってる距離
		return c1.getR()+c2.getR() - (float)Math.hypot(c2.getX()-c1.getX(),c2.getY()-c1.getY());
	}
	public static float RadToDegree(float rad){
		return rad * 360/(2*(float)Math.PI);
	}
}
