package cn.edu.bit.cs.fragments;

import cn.edu.bit.cs.taweather.CityQueryManager;
import cn.edu.bit.cs.taweather.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsFragment extends ListFragment implements
		OnItemClickListener {

	private ListFragmentCallback callback;
	private final String TAG = "SettingsFragment";
	private String[] cities;
	private final String[] provinces = { "北京", "上海", "天津", "重庆", "黑龙江", "吉林",
			"辽宁", "内蒙古", "河北", "山西", "陕西", "山东", "新疆", "西藏", "青海", "甘肃", "宁夏",
			"河南", "江苏", "湖北", "浙江", "安徽", "福建", "江西", "湖南", "贵州", "四川", "广东",
			"云南", "广西", "海南", "香港", "澳门", "台湾" };

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callback = (ListFragmentCallback) activity;
		} catch (Exception e) {

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, provinces));
	}

	//when the province list clicked
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// callback.SettingsCallback(position);
		ListView lv = (ListView) getActivity().findViewById(
				R.id.android_listChild);
		try {
			CityQueryManager cqm = new CityQueryManager(getActivity());
			cities = cqm.QueryCityNames(position);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		lv.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, cities));
		lv.setOnItemClickListener((OnItemClickListener) this);
	}

	//when the child list clicked, it pass the selected item to the MainActivity
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		TextView textview = (TextView)getActivity().findViewById(R.id.android_curretnCity);
		textview.setText(arg0.getItemAtPosition(position).toString());
		callback.listCallback(arg0.getItemAtPosition(position).toString());
		Log.i(TAG, arg0.getItemAtPosition(position).toString());
	}

	//use this interface to pass the selected city to MainActivity
	public interface ListFragmentCallback {
		public void listCallback(String city);
	}
}
