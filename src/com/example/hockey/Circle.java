package com.example.hockey;

public class Circle {
	private float x,y;	//中心座標
	private float r;		//半径
	
	public Circle(){
		x = y = r = 0;
	}
	
	public Circle( float _x, float _y, float _r ){
		x = _x;
		y = _y;
		r = _r;
	}
	
	public Circle( Circle c ){
		x = c.getX();
		y = c.getY();
		r = c.getR();
	}
	
	public void set (float _x, float _y, float _r ){
		x = _x;
		y = _y;
		r = _r;
	}
	public void set( Circle c ){
		x = c.getX();
		y = c.getY();
		r = c.getR();
	}
	
	public float getX(){
		return x;
	}
	public int getDrawX(){
		return (int)(x-r);
	}
	public float getRatioX(){
		return RatioAdjustment.FieldSizeX()/(x-RatioAdjustment.RefLeft());
	}
	public void setX(float x){
		this.x = x;
	}
	public float getY(){
		return y;
	}
	public int getDrawY(){
		return (int)(y-r-10);
	}
	public float getRatioY(){
		return RatioAdjustment.FieldSizeY()/y;
	}
	public void setY(float y){
		this.y = y;
	}
	public float getR(){
		return r;
	}
	public void setR(float r){
		this.r = r;
	}
}
