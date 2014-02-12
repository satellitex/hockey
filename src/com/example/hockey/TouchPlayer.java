package com.example.hockey;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

public class TouchPlayer extends Task{
	
	private GameMgr mgr;
	
	private int now_input_count;
	
	TouchPlayer(GameMgr _mgr){
		mgr = _mgr;
		now_input_count = 0;
	}
	
	@Override
	public boolean onUpdate() {
		return true;
	}
	
	@Override
	public void onDraw(Canvas c){
	}

}
