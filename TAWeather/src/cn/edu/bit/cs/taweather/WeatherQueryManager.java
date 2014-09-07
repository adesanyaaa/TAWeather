package cn.edu.bit.cs.taweather;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherQueryManager {

	private final String TAG = "WeatherQueryManager";
	private final int forecastDays = 4;
	private Context mContext;
	private final String appID = "a7fb1b760b418b4eeddee7df6e7d5bcc";

	public WeatherQueryManager(Context context) {
		mContext = context;
	}

	// get forecast Weather
	public String QueryCityWeather(String cityName) {
		final String weatherServiceURL;
		weatherServiceURL = "http://api.openweathermap.org/data/2.5/forecast/daily?cnt=4&q="
				+ cityName + "&lang=zh_cn&APPID=" + appID;
		String weatherDays = null;

		try {
			NetTaskManager ntm = new NetTaskManager(mContext);
			weatherDays = ntm.execute(weatherServiceURL).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		Log.i(TAG, weatherDays);

		return weatherDays;
	}

	// get current weather
	public String UpdateCurrentWeather(String cityName) {
		String currentWeather = null;
		String weatherServiceURL;
		weatherServiceURL = "http://api.openweathermap.org/data/2.5/weather?q="
				+ cityName + "&lang=zh_cn&APPID=" + appID;

		try {
			NetTaskManager ntm = new NetTaskManager(mContext);
			currentWeather = ntm.execute(weatherServiceURL).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return currentWeather;
	}

	// parse forecast Json information
	public List<WeatherInfo> ParseJsonForecast(String WeatherJson) {

		List<WeatherInfo> weatherDays = new ArrayList<WeatherInfo>(forecastDays);

		try {
			JSONArray jsonArray = new JSONObject(WeatherJson)
					.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				WeatherInfo day = new WeatherInfo();
				JSONObject jsonObjtenp = jsonObj.getJSONObject("temp");
				DecimalFormat format = new DecimalFormat("#0.0");
				day.setTempHigh((format.format((Double) (jsonObjtenp
						.getDouble("max") - 273.15))).toString());
				day.setTempLow((format.format((Double) (jsonObjtenp
						.getDouble("min") - 273.15))).toString());
				day.setWeatherCondition(((JSONObject) (jsonObj
						.getJSONArray("weather").get(0)))
						.getString("description"));
				day.setWeatherConditionCode(((JSONObject) (jsonObj
						.getJSONArray("weather").get(0))).getInt("id"));
				weatherDays.add(day);
			}
			/*
			 * for(int j = 0; j < 4; j++) { WeatherInfo wi = weatherDays.get(j);
			 * Log.i(TAG, wi.GetTempLow()); Log.i(TAG, wi.GetTempHigh());
			 * Log.i(TAG, wi.GetWeatherCondition()); }
			 */
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}

		return weatherDays;
	}

	// parse current weather json information
	public CurrentWeather ParseJsonCurrent(String WeatherJson) {
		CurrentWeather currentWeather = new CurrentWeather();
		try {
			JSONObject jsonObj = new JSONObject(WeatherJson);
			JSONArray jsonArrayWeather = jsonObj.getJSONArray("weather");
			currentWeather.setWeatherCondition(((JSONObject) (jsonArrayWeather
					.get(0))).getString("description"));
			currentWeather
					.setWeatherConditionCode(((JSONObject) (jsonArrayWeather
							.get(0))).getInt("id"));
			JSONObject jsonObjTemp = jsonObj.getJSONObject("main");
			DecimalFormat format = new DecimalFormat("#0.0");
			currentWeather.setTempHigh((format.format((Double) (jsonObjTemp
					.getDouble("temp_max") - 273.15))).toString());
			currentWeather.setTempLow((format.format((Double) (jsonObjTemp
					.getDouble("temp_min") - 273.15))).toString());
			currentWeather.setTempNow((format.format((Double) (jsonObjTemp
					.getDouble("temp") - 273.15)).toString()));
			currentWeather.setHumidity(jsonObjTemp.getString("humidity"));

			JSONObject jsonObjWind = jsonObj.getJSONObject("wind");
			currentWeather.setWind(jsonObjWind.getString("speed"));

			currentWeather.setInfoTime(jsonObj.getString("dt"));

		} catch (JSONException e) {

			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
		return currentWeather;
	}
}
