package com.example.hockey;

import android.util.Log;

public class Vec {
	private float vx,vy;	//速度
	
	Vec(){
		vx = vy = 0;
	}
	
	Vec( float _vx, float _vy){
		vx = _vx;
		vy = _vy;
	}
	
	public void set ( float _vx, float _vy){
		vx = _vx;
		vy = _vy;
	}
	
	public void setF(float f,float rad){
		vx = f*(float)Math.cos(rad);
		vy = f*(float)Math.sin(rad);
		Log.d("vec","f = "+f+"  angle = "+GeneralCalc.RadToDegree(rad)+" vec ("+vx+","+vy+")");
	}
	public void addF(float f){
		float a = this.getAngle();
		vx += f*(float)Math.cos(a);
		vy += f*(float)Math.sin(a);
	}
	public float getF(){
		return (float)Math.hypot(vx, vy);
	}
	
	public float getX(){
		return vx;
	}
	public void revX(){
		vx*=-1;
	}
	public void setX(float x){
		vx = x;
	}
	public float getY(){
		return vy;
	}
	public void revY(){
		vy*=-1;
	}
	public void setY(float y){
		vy = y;
	}
	
	public float getAngle(){
		return (float)Math.atan2(vy,vx);
	}
	
	public float getSpeed(){
		return (float)Math.sqrt( vx * vx + vy * vy );
	}
}
