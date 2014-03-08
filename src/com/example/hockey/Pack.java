package com.example.hockey;

import java.util.LinkedList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Pack extends Task{
	
	private GameMgr parent;
	Circle cir;
	Vec vec;

	private Paint paint = new Paint();	
	private Matrix matrix = new Matrix();
	private Bitmap packimg;
	private Bitmap zimg;
	private Resources res = App.getInstance().getResources();
	private Rect src;
	private Rect dst;
	
	private float refl,refr;
	
	LinkedList<EnemyMallet> _enemy;
	
	private boolean clientflag;
	
	private float tmpAngle;
	
	private int check_mallet_state;//チェックする自マレットのビット列
	
	public Pack(GameMgr p,LinkedList<EnemyMallet> enemy){
		_enemy = enemy;
		parent = p;
		packimg = BitmapFactory.decodeResource(res, R.drawable.pack);
		cir = new Circle(RatioAdjustment.FieldCenterX(),RatioAdjustment.FieldCenterY(),packimg.getWidth()/2f);

		if( p.getConnect().isClient() ){ 
			vec = new Vec(8f,-10f);
		} else {
			vec = new Vec(-8f,10f);
		}
		
		src = new Rect(0,0,packimg.getWidth(),packimg.getHeight());
		int dx = cir.getDrawX(),dy = cir.getDrawY();
		dst = new Rect(dx,dy,dx+packimg.getWidth(),dy+packimg.getHeight());
		
		refl = RatioAdjustment.RefLeft();
		refr = RatioAdjustment.RefRight();
		
		clientflag = false;
	}
	
	public void init(){
		check_mallet_state = 0;
		cir.set(0,0,cir.getR());
		if( parent.getConnect().isClient() ){ 
			vec = new Vec(8f,-10f);
		} else {
			vec = new Vec(-8f,10f);
		}		
	}
	
	public void rmEnemy(){
		_enemy.clear();
	}
	public void addEnemy(int id,float x,float y,float c){
		_enemy.add(new EnemyMallet(x,y,c));
	}
	
	public void set(float x,float y,float vx,float vy){
		vec.set(-vx, -vy);
		
		float angle = GeneralCalc.RadToDegree(vec.getAngle());
		Log.d("set","koko set ( "+x+", "+y+", "+angle);
		
		cir.set( x, y, cir.getR() );
		dst.set(cir.getDrawX(),cir.getDrawY(),cir.getDrawX()+packimg.getWidth(),cir.getDrawY()+packimg.getHeight());

		//向きに合わせて回転
		matrix.setRotate(angle);
		zimg = Bitmap.createBitmap(packimg,0,0,packimg.getWidth(),packimg.getHeight(),matrix,true);
		src.set(0,0,zimg.getWidth(),zimg.getHeight());
		clientflag = true;
	}
	public void setMalletState(int st){ check_mallet_state = st; }
	
	public float getHx(){ return RatioAdjustment.getHX(cir.getX()); }
	public float getHy(){ return RatioAdjustment.getHY(cir.getY()); }
	public float getAn(){ return tmpAngle; }
	public Vec getVec(){ return vec; }
	
	@Override
	public boolean onUpdate() {
		
		if( !clientflag ){
		
			Circle nc = new Circle(  cir.getX()+vec.getX(), cir.getY()+vec.getY(), cir.getR() );
			
			//自分のマレットとの接触判定
			int n=parent.CountMallet();
			for(int i=0;i<n;i++){
				if( ( check_mallet_state & (1<<i) ) > 0 ){
					Mallet m = parent.GetMallet(i);
					Circle mc = m.getCircle();
					if( GeneralCalc.CheckCircleInCircle(nc, mc) ){
						float rad = GeneralCalc.CircleToCircleAngle(mc, nc );
						vec.setF(GeneralCalc.CircleInCircleSize(nc, mc),rad);
	//						Log.d("setF","mc:: ("+mc.getX()+","+mc.getY()+") nc:: ("+nc.getX()+","+nc.getY()+") angle = "+GeneralCalc.RadToDegree(rad));
						nc.setX(nc.getX()+vec.getX());
						nc.setY(nc.getY()+vec.getY());
						break;
					}
				}
			}
			
			//敵のマレットとの接触判定
			for(int i=0;i<_enemy.size();i++){
				Circle mc = _enemy.get(i).getCircle();
				if( GeneralCalc.CheckCircleInCircle(nc, mc) ){
					float rad = GeneralCalc.CircleToCircleAngle(mc, nc );
					vec.setF(GeneralCalc.CircleInCircleSize(nc, mc),rad);
					Log.d("setF","mc:: ("+mc.getX()+","+mc.getY()+") nc:: ("+nc.getX()+","+nc.getY()+") angle = "+GeneralCalc.RadToDegree(rad));
					nc.setX(nc.getX()+vec.getX());
					nc.setY(nc.getY()+vec.getY());
					break;
				}
			}
	
			//左端チェック
			if( GeneralCalc.CheckCircleRefLeft(nc) ){
				nc.setX( 2*refl-(nc.getX()-nc.getR()) );
				vec.revX();
			}
			//右端チェック
			if( GeneralCalc.CheckCircleRefRight(nc) ){
				nc.setX( 2*refr - (nc.getX()+nc.getR()) );
				vec.revX();
			}
			
			cir.set( nc );
			
			int dx = cir.getDrawX(),dy = cir.getDrawY();
//			Log.d("dx dy","dx = "+dx+" dy = "+dy+" angle = " + GeneralCalc.RadToDegree(vec.getAngle()));
			dst.set(dx,dy,dx+packimg.getWidth(),dy+packimg.getHeight());
			
			//向きに合わせて回転
			tmpAngle = GeneralCalc.RadToDegree(vec.getAngle());
			matrix.setRotate(tmpAngle);
			zimg = Bitmap.createBitmap(packimg,0,0,packimg.getWidth(),packimg.getHeight(),matrix,true);
			src.set(0,0,zimg.getWidth(),zimg.getHeight());
		}
		
		if( cir.getY() > RatioAdjustment.GoalY() ){
			parent.gameEnd();
			parent.setLose();
		}
		
		clientflag = false;
		check_mallet_state = 0;
		return true;
	}
	
	@Override	
	public void onDraw(Canvas c){
		c.drawBitmap(zimg, src, dst, paint);
	}	

}
