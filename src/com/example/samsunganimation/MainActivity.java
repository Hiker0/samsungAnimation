package com.example.samsunganimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.samsunganimation.fan.AirAnimationShow;


public class MainActivity extends ListActivity {
	
	
	
	private ArrayList<Map<String, String>> mData = new ArrayList<Map<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initList();
		
		MapAdapter adapter= new MapAdapter(this,mData);
		setListAdapter(adapter);
	}
	
	void initList(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", "AirAnimation");
		map.put("class", AirAnimationShow.class.getName());
		mData.add(map);
	}
	
	
	
	class MapAdapter extends BaseAdapter{
		
		private ArrayList<Map<String, String>> mdata ;
		private Context mContext;
		private LayoutInflater mInflater;
		public MapAdapter(Context context, ArrayList<Map<String, String>>  data){
			mContext = context;
			mdata = data;
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mdata.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mdata.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			  return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			Map map = mdata.get(position);
			String text = (String) map.get("title");
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.mainmenu_item, null);
				TextView tv = (TextView) convertView.findViewById(R.id.text);
				if(tv != null){
					tv.setText(text);
				}
				
			}else{
				TextView tv = (TextView) convertView.findViewById(R.id.text);
				if(tv != null){
					tv.setText(text);
				}
			}
			
			final String  classname = (String) map.get("class");
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClassName(mContext, classname);
					mContext.startActivity(intent);
				}
			});
			
			return convertView;
		}
		
	}
	
	
}
