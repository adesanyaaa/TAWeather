package cn.edu.bit.cs.taweather;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import cn.edu.bit.cs.fragments.ForcastWeatherFragment;
import cn.edu.bit.cs.fragments.SettingsFragment;
import cn.edu.bit.cs.fragments.SettingsFragment.ListFragmentCallback;

public class MainActivity extends FragmentActivity implements
		ListFragmentCallback {

	private String currentCity;
	private ViewPager viewPager;// page
	private List<Fragment> views;// pages
	private Fragment viewForcastWeather;
	private Fragment viewSettings;
	private final String TAG = "MainActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViewPager();
		
	}
	
	// initialize fragments
	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		views = new ArrayList<Fragment>();

		viewSettings = new SettingsFragment();
		viewForcastWeather = new ForcastWeatherFragment();

		views.add(viewSettings);
		views.add(viewForcastWeather);

		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(1);
		viewPager.setOnPageChangeListener(new PageChangeLisener());
	}

	class ViewPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> views;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);

			views = MainActivity.this.views;
		}

		@Override
		public Fragment getItem(int arg0) {

			return views.get(arg0);
		}

		@Override
		public int getCount() {

			return views.size();
		}

	}

	class PageChangeLisener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Get current city's name from SettingsFragment
	@Override
	public void listCallback(String city) {
		
		currentCity = city.substring(city.indexOf(".") + 1);
		Log.i(TAG, currentCity);
		try {

			SharedPreferences sharedInfo = getSharedPreferences(
					"sharedWeatherInfo", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedInfo.edit();
			editor.putString("cityName", currentCity);
			editor.commit();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

}
