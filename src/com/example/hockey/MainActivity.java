package com.example.hockey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		

  		setContentView(R.layout.activity_main);
  		setHomeScreenContent();
	}
	
		//ホーム画面
	  private void setHomeScreenContent() {  
//	    	Log.d("haitta","setHomeScreenContent onClick");
		    Button nextButton = (Button) findViewById(R.id.button01);
		    nextButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		//   	Log.d("haitta","setHomeScreenContent onClick");
				Intent intent = new Intent(MainActivity.this, ConnectMenuActivity.class);
				startActivity(intent);
		      }
		    });
	  }

	  @Override
	  protected void onActivityResult(int requestCode, int ResultCode, Intent date){
		  
	  }	  

	  //汎用トースト
	public void Log(String string){
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	  }
}
