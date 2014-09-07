package cn.edu.bit.cs.taweather;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public final class PicSourceManager {

	private final String TAG = "PicSourceManager";
	private Map<Integer, Integer> picMapForacast;
	private Map<Integer, Integer> picMapCurrent;
	private Integer[] Thunderstorm = {200, 201, 202, 210, 211, 212, 221, 230, 231, 232};
	private Integer[] Drizzle = {300, 301, 302, 310, 311, 312, 313, 314, 321};
	private Integer[] Rain = {500, 501, 502, 503, 504, 511, 520, 521, 522, 531};
	private Integer[] Snow = {600, 601, 602, 611, 612, 615, 616, 620, 621, 622};
	private Integer[] Atmosphere = {701, 711, 721, 731, 741, 751, 761, 762, 771, 781};
	private Integer[] Clouds = {800, 801, 802, 803, 804};
	
	public final void initMapForcast(){
		picMapForacast = new HashMap<Integer, Integer>();
		for(int i1 = 0; i1 < Thunderstorm.length; i1++){
			picMapForacast.put(Thunderstorm[i1], R.drawable._11db);
		}
		
		for(int i2 = 0; i2 < Drizzle.length; i2++){
			picMapForacast.put(Drizzle[i2], R.drawable._09db);
		}
		
		for(int i3 = 0; i3 < Rain.length; i3++){
			picMapForacast.put(Rain[i3], R.drawable._09db);
		}
		
		for(int i4 = 0; i4 < Snow.length; i4++){
			picMapForacast.put(Snow[i4], R.drawable._13db);
		}
		
		for(int i5 = 0; i5 < Atmosphere.length; i5++){
			picMapForacast.put(Atmosphere[i5], R.drawable._50db);
		}
		
		picMapForacast.put(Clouds[0], R.drawable._01db);
		picMapForacast.put(Clouds[1], R.drawable._02db);
		picMapForacast.put(Clouds[2], R.drawable._03db);
		picMapForacast.put(Clouds[3], R.drawable._03db);
		picMapForacast.put(Clouds[4], R.drawable._04db);
	}
	
	public final void initMapCurrent(){
		picMapCurrent = new HashMap<Integer, Integer>();
		for(int i1 = 0; i1 < Thunderstorm.length; i1++){
			picMapCurrent.put(Thunderstorm[i1], R.drawable._11dbb);
		}
		
		for(int i2 = 0; i2 < Drizzle.length; i2++){
			picMapCurrent.put(Drizzle[i2], R.drawable._09dbb);
		}
		
		for(int i3 = 0; i3 < Rain.length; i3++){
				picMapCurrent.put(Rain[i3], R.drawable._09dbb);
		}
		
		for(int i4 = 0; i4 < Snow.length; i4++){
			picMapCurrent.put(Snow[i4], R.drawable._13dbb);
		}
		
		for(int i5 = 0; i5 < Atmosphere.length; i5++){
			picMapCurrent.put(Atmosphere[i5], R.drawable._50dbb);
		}
		
		picMapCurrent.put(Clouds[0], R.drawable._01dbb);
		picMapCurrent.put(Clouds[1], R.drawable._02dbb);
		picMapCurrent.put(Clouds[2], R.drawable._03dbb);
		picMapCurrent.put(Clouds[3], R.drawable._03dbb);
		picMapCurrent.put(Clouds[4], R.drawable._04dbb);
	}
	
	public final void initMap()
	{
		initMapForcast();
		initMapCurrent();
	}
	
	public final int getForcastWeatherPic(int weatherCode)
	{
		Log.i(TAG,((Integer)weatherCode).toString());
		Log.i(TAG, picMapForacast.get(weatherCode).toString());
		return picMapForacast.get((Integer)weatherCode);
	}
	
	public final int getCurrentWeatherPic(int weatherCode){
		return picMapCurrent.get(weatherCode);
	}
}
