package com.example.hockey;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class HowToPlayActivity extends FragmentActivity {
	  ViewPager viewPager;

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.howtoplay0);
	    viewPager = (ViewPager) findViewById(R.id.pager);
	    viewPager.setAdapter(
	      new HowToPlayPagerAdapter(
	        getSupportFragmentManager()));
	  }
}
