package com.example.hockey;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity{
	private int wc,lc;
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("koko","koko ResultActivity00");
		super.onCreate(savedInstanceState);
		Log.d("koko","koko ResultActivity01");
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
  		setContentView(R.layout.result);

		Log.d("koko","koko ResultActivity03");
		Intent intent = getIntent();
        String item =intent.getStringExtra("wlcount"); 
		Scanner str = new Scanner(item);
		wc = str.nextInt(); lc = str.nextInt();
        TextView textw = (TextView)findViewById(R.id.wincount);
        textw.setText(Integer.toString(wc));
        TextView textl = (TextView)findViewById(R.id.losecount);
        textl.setText(Integer.toString(lc));
		Log.d("koko","koko ResultActivity04");
  		setHomeScreenContent();
		Log.d("koko","koko ResultActivity05");
	}
	
		//ホーム画面
	  private void setHomeScreenContent() {  
		    ImageButton nextButton = (ImageButton) findViewById(R.id.twitterbutton);
		    nextButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	String tweet = new String();
		    	try {
		    	    tweet = "https://twitter.com/intent/tweet?text="
		    	        + URLEncoder.encode(wc+"勝 "+lc+"敗 でした", "UTF-8")
		    	        + "+"
		    	        + URLEncoder.encode("#タグ", "UTF-8")
		    	        + "&url="
		    	        + URLEncoder.encode("http://tnko.com/", "UTF-8");
		    	} catch (UnsupportedEncodingException e) {
		    	    e.printStackTrace();
		    	}
		    	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweet));
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
