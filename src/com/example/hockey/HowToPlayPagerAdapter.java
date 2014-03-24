package com.example.hockey;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HowToPlayPagerAdapter  extends FragmentStatePagerAdapter {
	public HowToPlayPagerAdapter(FragmentManager fm) {
		    super(fm);
	}
	
	@Override
	public Fragment getItem(int i) {
	    switch(i){
	    case 0:
	      return new HowToPlayFragment01();
	    case 1:
	      return new HowToPlayFragment02();
	    case 2:
	      return new HowToPlayFragment03();
	    case 3:
	      return new HowToPlayFragment04();
	    case 4:
	      return new HowToPlayFragment05();
	    case 5:
	      return new HowToPlayFragment06();
	    case 6:
	      return new HowToPlayFragment07();
	    case 7:
	      return new HowToPlayFragment08();
	    case 8:
	      return new HowToPlayFragment09();
	    default:
	      return new HowToPlayFragment01();	     
	    }
	 }

	  @Override
	  public int getCount() {
	    return 9;
	  }

	  @Override
	  public CharSequence getPageTitle(int position) {
		position++;
	    return "其の " + position;
	  }
}
