package cn.edu.bit.cs.taweather;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class CityQueryManager extends SQLiteAssetHelper {
	private static final String DATABASE_NAME = "db_weather.db";
	private static final int DATABASE_VERSION = 1;
	private final String TAG = "CityQueryManager";
	
	public CityQueryManager(Context mContext){
		super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//Query city names by province name
	public String[] QueryCityNames(int ProvinceId)
	{
		SQLiteDatabase db = null;
		SQLiteQueryBuilder qb = null;
		
		try{
			db = getReadableDatabase();
			qb = new SQLiteQueryBuilder();
		}catch(Exception e){
			Log.e(TAG, e.toString());
		}
		

		String [] sqlSelect = {"name"};  
		String sqlTables = "citys";
		String sqlCondition = "province_id =";
		sqlCondition += ProvinceId;

		qb.setTables(sqlTables);
		Cursor cursor = qb.query(db, sqlSelect, sqlCondition, null,
				null, null, null);
		cursor.moveToFirst();

		String[] str = new String[cursor.getCount()];
		int i = 0;
		int index = 0;
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext(), i++)
		{
			index = cursor.getColumnIndex("name");
			str[i] = cursor.getString(index);
		}
		cursor.close();
		db.close();
	    return str;
	}
}
