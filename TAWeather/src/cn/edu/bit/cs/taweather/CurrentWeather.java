package cn.edu.bit.cs.taweather;

public class CurrentWeather extends WeatherInfo{
	private String cityName;
	private String cityID;
	private String cityLat;//经度
	private String cityLng;//维度
	private String presure;//气压
	private String humidity;
	private String infoTime;
	private String sunRise;
	private String sunSet;
	private String wind;
	
	public String getSunRise() {
		return sunRise;
	}
	public void setSunRise(String sunRise) {
		this.sunRise = sunRise;
	}
	public String getSunSet() {
		return sunSet;
	}
	public void setSunSet(String sunSet) {
		this.sunSet = sunSet;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getCityLat() {
		return cityLat;
	}
	public void setCityLat(String cityLat) {
		this.cityLat = cityLat;
	}
	public String getCityLng() {
		return cityLng;
	}
	public void setCityLng(String cityLng) {
		this.cityLng = cityLng;
	}
	public String getPresure() {
		return presure;
	}
	public void setPresure(String presure) {
		this.presure = presure;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getInfoTime() {
		return infoTime;
	}
	public void setInfoTime(String infoTime) {
		this.infoTime = infoTime;
	}
}
