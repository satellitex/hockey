package com.example.hockey;

public class Status {
	private float hp;
	private int barrier;
	
	public Status(){
		hp = 5f;
		barrier = 5;		
	}
	
	private void CalcBarrier(){
		if( hp <= 0f ){
			barrier = 0;
		}
		for(int i=barrier;i<5;i++){
			if( (int)hp >= i+1 ){
				barrier = i+1;
			}
		}
	}
	
	public void setHp(float h){ hp = h; CalcBarrier(); }
	public void setBarrier(int b){ barrier = b; }
	public void DecreaseHp(float h){ 
		hp-=h;
		if( hp < 0f ) {
			hp = 0f;
		}
		CalcBarrier();
	}
	public void HealHp(float h){
		if(barrier >= hp ){
			hp+=h;
		} else {
			hp+=h/3f;
		}
		if( hp > 5f ){
			hp = 5f;
		}
		CalcBarrier();
	}
	public float getHp() { return hp; }
	public float getBarrier() { return barrier; }
	
}
