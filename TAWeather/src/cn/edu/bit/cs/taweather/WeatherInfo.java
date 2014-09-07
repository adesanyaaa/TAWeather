package cn.edu.bit.cs.taweather;

public class WeatherInfo {
	private String tempNow;
	private String tempLow;
	private String tempHigh;
	private String weatherCondition;
	private int weatherConditionCode;
	
	
	public String getTempNow() {
		return tempNow;
	}
	public void setTempNow(String tempNow) {
		this.tempNow = tempNow;
	}
	public String getTempLow() {
		return tempLow;
	}
	public void setTempLow(String tempLow) {
		this.tempLow = tempLow;
	}
	public String getTempHigh() {
		return tempHigh;
	}
	public void setTempHigh(String tempHigh) {
		this.tempHigh = tempHigh;
	}
	
	public String getWeatherCondition() {
		return weatherCondition;
	}
	public void setWeatherCondition(String weatherCondition) {
		this.weatherCondition = weatherCondition;
	}
	public void setWeatherConditionCode(int weatherConditionCode)
	{
		this.weatherConditionCode = weatherConditionCode;
	}
	public int getWeatherConditionCode()
	{
		return weatherConditionCode;
	}
}
