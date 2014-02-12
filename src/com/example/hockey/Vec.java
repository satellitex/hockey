package com.example.hockey;

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
