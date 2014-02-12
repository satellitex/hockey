package com.example.hockey;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private GameSurfaceView _game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		RatioAdjustment.init(this);
		
		_game = new GameSurfaceView(this);
		setContentView( _game );
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		Log.d("touch","Activity event");
		_game.onTouchEventEx(event);
		return super.onTouchEvent(event);
	}
}
