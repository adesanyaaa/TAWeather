package cn.edu.bit.cs.fragments;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.edu.bit.cs.taweather.CurrentWeather;
import cn.edu.bit.cs.taweather.NetTaskManager;
import cn.edu.bit.cs.taweather.PicSourceManager;
import cn.edu.bit.cs.taweather.R;
import cn.edu.bit.cs.taweather.WeatherInfo;
import cn.edu.bit.cs.taweather.WeatherQueryManager;
import cn.edu.bit.cs.taweather.NetTaskManager.HttpLinkTest;
import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ForcastWeatherFragment extends Fragment {

	private final String TAG = "ForcastWeatherFragment";
	private List<WeatherInfo> forcastWeatherInfo = null;
	private CurrentWeather currentWeatherInfo = null;
	private List<Integer> weatherPic = new ArrayList<Integer>();
	private List<Integer> weatherText = new ArrayList<Integer>();
	private List<Integer> weatherTextCondition = new ArrayList<Integer>();
	private String cityName;
	private SharedPreferences historyInfo;
	private SharedPreferences.Editor historyEditor;
	NetTaskManager ntm;
	NetTaskManager ntm2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_frocast,
				container, false);

		// history weather info
		SharedPreferences sharedInfo = getActivity().getSharedPreferences(
				"sharedWeatherInfo", Activity.MODE_PRIVATE);
		cityName = sharedInfo.getString("cityName", "");
		historyInfo = getActivity().getSharedPreferences("historyInfo",
				Activity.MODE_PRIVATE);
		String historyForcastJson = historyInfo.getString("historyForcastJSON",
				"");
		String hsitoryCurrentJson = historyInfo.getString("historyCurrentJSON",
				"");
		if (!cityName.isEmpty()) {
			if (!historyForcastJson.isEmpty()) {
				ForcastQueryAction(cityName, historyForcastJson);
			}

			if (!hsitoryCurrentJson.isEmpty())
				CurrentQueryAction(cityName, hsitoryCurrentJson);

			if (forcastWeatherInfo != null && currentWeatherInfo != null) {
				SetResources(view);
			}
		}

		// initialize refresh button
		ImageButton buttonRefresh = (ImageButton) view
				.findViewById(R.id.buttonRefresh);

		buttonRefresh.setOnLongClickListener(new OnLongClickListener() {

			// avoid refreshing frequently
			long lastClickTime;
			final int intervalTime = 3000;

			public boolean isFastDoubleClick() {
				long time = System.currentTimeMillis();
				long timeD = time - lastClickTime;
				if (0 < timeD && timeD < intervalTime) {
					return true;
				}
				lastClickTime = time;
				return false;
			}

			@Override
			public boolean onLongClick(View arg0) {
				// avoid refreshing frequently
				if (isFastDoubleClick()) {
					Toast.makeText(getActivity(), "为了节省您的流量，请勿频繁刷新哦~",
							Toast.LENGTH_SHORT).show();
					return true;
				} else {
					Toast.makeText(getActivity(), "更新天气中，请稍后...",
							Toast.LENGTH_LONG).show();
				}

				Vibrator vib = (Vibrator) getActivity().getSystemService(
						Service.VIBRATOR_SERVICE);
				final int vibrateTime = 300;
				long[] pattern = { 0, vibrateTime};
				vib.vibrate(pattern, -1);

				// test connecting to server
				ntm = new NetTaskManager(getActivity());
				final String urlTest = "http://api.openweathermap.org/data/2.5/weather?q=beijing";
				final int delay = 3000;
				boolean isNetworakOk = ntm.ConnectionTest(urlTest, delay);
				if (!isNetworakOk) {
					Toast.makeText(getActivity(), "网络状态不佳，稍后重试",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				SharedPreferences sharedInfo = getActivity()
						.getSharedPreferences("sharedWeatherInfo",
								Activity.MODE_PRIVATE);
				cityName = sharedInfo.getString("cityName", "");
				if (cityName.isEmpty()) {
					Toast.makeText(getActivity(), "还没有选择你所在的城市哦，向右划吧",
							Toast.LENGTH_SHORT).show();
				} else {

					// ForcastQueryAction(cityName, "");
					// CurrentQueryAction(cityName, "");
					ExecutorService pool = Executors.newFixedThreadPool(2);
					Callable<Void> forcastCallable = new ForcastQuery(cityName,
							"");
					Callable<Void> currentCallable = new CurrentQuery(cityName,
							"");
					Future<Void> f1 = pool.submit(forcastCallable);
					Future<Void> f2 = pool.submit(currentCallable);
					try {
						f1.get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						f2.get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SetResources(view);
					Toast.makeText(getActivity(), "天气信息已更新", Toast.LENGTH_SHORT)
							.show();
				}
				return false;
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	private void SetResources(View view) {
		// initialize resources
		PicSourceManager psm = new PicSourceManager();
		psm.initMap();
		initSource();

		// show current date
		TextView timeText = (TextView) view.findViewById(R.id.textViewTime);
		timeText.setText(GetCurrentTime() + "\n" + "当前城市:" + cityName);

		// show forcasst weather picture and description
		int i = 0;
		for (i = 0; i < forcastWeatherInfo.size(); i++) {
			ImageView image = (ImageView) view.findViewById(weatherPic.get(i));
			image.setImageResource(psm.getForcastWeatherPic(forcastWeatherInfo
					.get(i).getWeatherConditionCode()));
			TextView text = (TextView) view.findViewById(weatherText.get(i));
			text.setText(forcastWeatherInfo.get(i).getTempLow() + "°" + "~"
					+ forcastWeatherInfo.get(i).getTempHigh() + "°");
			TextView textCon = (TextView) view
					.findViewById(weatherTextCondition.get(i));
			textCon.setText(forcastWeatherInfo.get(i).getWeatherCondition());

		}

		// show current weather picture and description
		ImageView imageCurrent = (ImageView) view
				.findViewById(R.id.ImageViewWeather);
		imageCurrent.setImageResource(psm
				.getCurrentWeatherPic(currentWeatherInfo
						.getWeatherConditionCode()));
		TextView textInfo = (TextView) view.findViewById(R.id.textViewInfo);
		textInfo.setText(currentWeatherInfo.getTempNow() + "°" + ","
				+ currentWeatherInfo.getWeatherCondition() + "\n" + "风速:"
				+ currentWeatherInfo.getWind() + "级" + "\n" + "湿度:"
				+ currentWeatherInfo.getHumidity() + "%");
	}

	public class ForcastQuery implements Callable<Void> {

		String cityName;
		String jsonWeather;

		ForcastQuery(String cityName, String jsonWeather) {
			this.cityName = cityName;
			this.jsonWeather = jsonWeather;
		}

		public Void call() throws Exception {
			WeatherQueryManager wqm = new WeatherQueryManager(getActivity());
			if (jsonWeather.isEmpty()) {
				try {
					jsonWeather = wqm.QueryCityWeather(cityName);
				} catch (Exception e) {
					Log.i(TAG, e.toString());
				}
				// save history forcast info, so we do not to refresh the
				// fragment
				// all the time
				historyInfo = getActivity().getSharedPreferences("historyInfo",
						Activity.MODE_PRIVATE);
				historyEditor = historyInfo.edit();
				historyEditor.putString("historyForcastJSON", jsonWeather);
				historyEditor.commit();
			}
			if (jsonWeather != null && !jsonWeather.isEmpty())
				forcastWeatherInfo = wqm.ParseJsonForecast(jsonWeather);
			return null;
		}

	};

	public class CurrentQuery implements Callable<Void> {

		String cityName;
		String jsonWeather;

		CurrentQuery(String cityName, String jsonWeather) {
			this.cityName = cityName;
			this.jsonWeather = jsonWeather;
		}

		public Void call() throws Exception {
			WeatherQueryManager wqm = new WeatherQueryManager(getActivity());
			if (jsonWeather.isEmpty()) {
				jsonWeather = wqm.UpdateCurrentWeather(cityName);

				// save history current weather info
				historyInfo = getActivity().getSharedPreferences("historyInfo",
						Activity.MODE_PRIVATE);
				historyEditor = historyInfo.edit();
				historyEditor.putString("historyCurrentJSON", jsonWeather);
				historyEditor.commit();
			}
			if (jsonWeather != null && !jsonWeather.isEmpty())
				currentWeatherInfo = wqm.ParseJsonCurrent(jsonWeather);
			return null;
		}

	};

	// Query forcast weather info
	private void ForcastQueryAction(String cityName, String jsonWeather) {
		WeatherQueryManager wqm = new WeatherQueryManager(getActivity());
		if (jsonWeather.isEmpty()) {
			try {
				jsonWeather = wqm.QueryCityWeather(cityName);
			} catch (Exception e) {
				Log.i(TAG, e.toString());
			}
			// save history forcast info, so we do not to refresh the fragment
			// all the time
			historyInfo = getActivity().getSharedPreferences("historyInfo",
					Activity.MODE_PRIVATE);
			historyEditor = historyInfo.edit();
			historyEditor.putString("historyForcastJSON", jsonWeather);
			historyEditor.commit();
		}
		if (jsonWeather != null && !jsonWeather.isEmpty())
			forcastWeatherInfo = wqm.ParseJsonForecast(jsonWeather);

	}

	// Query current weather info
	private void CurrentQueryAction(String cityName, String jsonWeather) {
		WeatherQueryManager wqm = new WeatherQueryManager(getActivity());
		if (jsonWeather.isEmpty()) {
			jsonWeather = wqm.UpdateCurrentWeather(cityName);

			// save history current weather info
			historyInfo = getActivity().getSharedPreferences("historyInfo",
					Activity.MODE_PRIVATE);
			historyEditor = historyInfo.edit();
			historyEditor.putString("historyCurrentJSON", jsonWeather);
			historyEditor.commit();
		}
		if (jsonWeather != null && !jsonWeather.isEmpty())
			currentWeatherInfo = wqm.ParseJsonCurrent(jsonWeather);
	}

	// get current date
	private String GetCurrentTime() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String mYear = String.valueOf(c.get(Calendar.YEAR));
		String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
		String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "日";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		return mYear + "年" + mMonth + "月" + mDay + "日" + "/星期" + mWay;
	}

	// init .png resources
	public void initSource() {
		weatherPic.add(R.id.ImageViewForcast1);
		weatherPic.add(R.id.ImageViewForcast2);
		weatherPic.add(R.id.ImageViewForcast3);
		weatherPic.add(R.id.ImageViewForcast4);

		weatherText.add(R.id.textViewFoacast1);
		weatherText.add(R.id.textViewFoacast2);
		weatherText.add(R.id.textViewFoacast3);
		weatherText.add(R.id.textViewFoacast4);

		weatherTextCondition.add(R.id.textViewFoacastCon1);
		weatherTextCondition.add(R.id.textViewFoacastCon2);
		weatherTextCondition.add(R.id.textViewFoacastCon3);
		weatherTextCondition.add(R.id.textViewFoacastCon4);
	}
}
