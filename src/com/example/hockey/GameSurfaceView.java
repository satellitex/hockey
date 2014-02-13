package com.example.hockey;

import java.lang.Thread;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	
	private GameMgr _gameMgr = new GameMgr();
	
	//現在何本指で押してるか
	private int now_input_count = 0;
	
	private Thread _thread;
	
	public GameSurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //解像度情報変更通知 
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
            _thread = new Thread(this);             //別スレッドでメインループを作る
            _thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            _thread = null;
    }
    @Override
    public void run() {
    		while (_thread!=null) { //メインループ
            		_gameMgr.onUpdate();
                    onDraw(getHolder());
            }
    }
    

	public boolean onTouchEventEx(MotionEvent event){
		
		

		int count = event.getPointerCount();
		int malletflag = 0;
		
		Log.d("touch","touch count = "+count);
		
		//タッチした分だけマレットを追加
		if( (now_input_count <= count) ){
			for(int i=0;i<count;i++){
				//Log.d("touch","touch id = "+ i +" X="+event.getX(i)+"Y="+event.getY(i));
				if( event.getX(i) > RatioAdjustment.RefLeft() ){
					malletflag |= _gameMgr.AddMallet(new Mallet(event.getX(i),event.getY(i)));
				}
			}
		}
		
		String binary = Integer.toBinaryString(malletflag);
		Log.d("touch","touch flag = "+binary);
		
		//マレットを削除
		_gameMgr.KillMallet(malletflag);
		
		return super.onTouchEvent(event);
	}
	

    private void onDraw(SurfaceHolder holder) {
            Canvas c = holder.lockCanvas();
            if(c == null){
                          return;
            }
            //ゲームの描画処理
            _gameMgr.onDraw(c);
            
            holder.unlockCanvasAndPost(c);
    }


}
