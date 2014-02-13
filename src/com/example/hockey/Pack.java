package com.example.hockey;

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
	
	public Pack(GameMgr p){
		parent = p;
		packimg = BitmapFactory.decodeResource(res, R.drawable.pack);
		cir = new Circle(RatioAdjustment.FieldCenterX(),RatioAdjustment.GoalY(),packimg.getWidth()/2f);
		vec = new Vec(3f,-1f);
		
		src = new Rect(0,0,packimg.getWidth(),packimg.getHeight());
		int dx = cir.getDrawX(),dy = cir.getDrawY();
		dst = new Rect(dx,dy,dx+packimg.getWidth(),dy+packimg.getHeight());
		
		refl = RatioAdjustment.RefLeft();
		refr = RatioAdjustment.RefRight();
	}
	
	@Override
	public boolean onUpdate() {
		Circle nc = new Circle(  cir.getX()+vec.getX(), cir.getY()+vec.getY(), cir.getR() );
		
		//自分のマレットとの接触判定
		int n=parent.CountMallet();
		for(int i=0;i<n;i++){
			Mallet m = parent.GetMallet(i);
			if( m.getSurvival() ){
				Circle mc = m.getCircle();
				if( GeneralCalc.CheckCircleInCircle(nc, mc) ){
					float rad = GeneralCalc.CircleToCircleAngle(mc, nc );
					vec.setF(vec.getF(), rad);
					vec.addF(GeneralCalc.CircleInCircleSize(nc, mc)/2f);
					nc.setX(nc.getX()+vec.getX());
					nc.setY(nc.getY()+vec.getY());
					break;
				}
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
		Log.d("dx dy","dx = "+dx+" dy = "+dy+" angle = " + GeneralCalc.RadToDegree(vec.getAngle()));
		dst.set(dx,dy,dx+packimg.getWidth(),dy+packimg.getHeight());
		
		//向きに合わせて回転
		matrix.setRotate(GeneralCalc.RadToDegree(vec.getAngle()));
		zimg = Bitmap.createBitmap(packimg,0,0,packimg.getWidth(),packimg.getHeight(),matrix,true);
		src.set(0,0,zimg.getWidth(),zimg.getHeight());
		return true;
	}
	
	@Override	
	public void onDraw(Canvas c){
		c.drawBitmap(zimg, src, dst, paint);
	}	

}
