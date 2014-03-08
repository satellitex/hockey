package com.example.hockey;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	
	private GameMgr _gameMgr;
	
	
	//現在何本指で押してるか
	private int now_input_count = 0;
	
	private Thread _thread;
	
	public GameSurfaceView(Context context,Connect c) {
		super(context);
		Log.d("koko","koko In GameSurfaceview()");
		_gameMgr = new GameMgr(c);
		getHolder().addCallback(this);
	}
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //解像度情報変更通知 
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
		Log.d("koko","koko surfaceCreated()01");
            _thread = new Thread(this);             //別スレッドでメインループを作る
    		Log.d("koko","koko surfaceCreated()");
            _thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    		Log.d("koko","koko surface destroy");
            _thread = null;
    }
    @Override
    public void run() {
    		_gameMgr.StartRead();
    		while (_thread!=null) { //メインループ
    				Log.d("koko","koko run start");
            		_gameMgr.onUpdate();
                    onDraw(getHolder());
                    Log.d("koko","koko run end");
            }
    }
    

	public boolean onTouchEventEx(MotionEvent event){
		
		
		 if( _gameMgr.isEnd() ){
				if( _gameMgr.issinEnd() ){
					_gameMgr.setReset();
				}
		 } else if( _gameMgr.isStart() ){
			int count = event.getPointerCount();		
			int malletflag = 0;
			
			Log.d("touch","touch count = "+count);
			while( _gameMgr.isMalletUpdate() != 0 );
			_gameMgr.startMalletUpdate();
			
			//タッチした分だけマレットを追加
			if( (now_input_count <= count) ){
				for(int i=0;i<count;i++){
					//Log.d("touch","touch id = "+ i +" X="+event.getX(i)+"Y="+event.getY(i));
					if( event.getX(i) > RatioAdjustment.RefLeft() && event.getY(i) > RatioAdjustment.MalletR() ){
						malletflag |= _gameMgr.AddMallet(new Circle(event.getX(i),event.getY(i),RatioAdjustment.MalletR()));
					}
				}
			}
			
			String binary = Integer.toBinaryString(malletflag);
			Log.d("touch","touch flag = "+binary);
			
			//マレットを削除
			_gameMgr.setMalletState(malletflag);
			_gameMgr.endMalletupdate();
		} else {
			Ready r = _gameMgr.getReady();
			r.setOk();
		}
		
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
