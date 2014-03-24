package com.example.hockey;

import java.util.concurrent.TimeUnit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FpsController extends Task {
	
	private static long ONE_SEC_TO_NANO = TimeUnit.SECONDS.toNanos(1L);
	private static long ONE_MILLI_TO_NANO = TimeUnit.MILLISECONDS.toNanos(1L);

    private long _startTime = 0;            //測定開始時刻
    private int  _cnt = 0;                  //カウンタ
    private Paint _paint = new Paint();     //paint情報
    private float _fps;                     //fps
    private final static int N = 60;        //平均を取るサンプル数
    private final static int FONT_SIZE = 20;//フォントサイズ
    
    private long oneCycle;
    private long pretime;
    
    private final double FPS_C = 30.0;
    
    public FpsController(){
            _paint.setColor(Color.BLUE);    //フォントの色を青に設定
            _paint.setTextSize(FONT_SIZE);  //フォントサイズを設定
            oneCycle = (long)(Math.floor((double)ONE_SEC_TO_NANO/FPS_C));
            pretime = System.nanoTime();
    }
    
    @Override
    public boolean onUpdate(){
            if( _cnt == 0 ){ //1フレーム目なら時刻を記憶
                    _startTime = System.currentTimeMillis();
            }
            if( _cnt == N ){ //60フレーム目なら平均を計算する
                    long t = System.currentTimeMillis();
                    _fps = 1000.f/((t-_startTime)/(float)N);
                    _cnt = 0;
                    _startTime = t;
            }
            
            long sleeptime = System.nanoTime() - pretime;
            if( sleeptime < ONE_MILLI_TO_NANO) {
            	sleeptime = ONE_MILLI_TO_NANO;
            }
          //  Log.d("sleep","sleep... cycle = " + oneCycle +"  time = "+sleeptime);
            if( sleeptime < oneCycle ){
       //         Log.d("sleep","sleep in ..."+Integer.toString((int)(oneCycle - sleeptime)));
            	try {
					TimeUnit.NANOSECONDS.sleep( oneCycle - sleeptime );
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
            }
            
            	pretime = System.nanoTime();
            
            _cnt++;
            return true;
    }

    @Override
    public void onDraw(Canvas c ){
            c.drawText(String.format("%.1f", _fps), 0, FONT_SIZE-2, _paint);
    }


}
