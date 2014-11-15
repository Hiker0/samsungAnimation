package com.example.samsunganimation.cameramode;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.samsunganimation.R;


public class ModeListWiget extends LinearLayout implements ModeBase,View.OnKeyListener {
	
	private static final String TAG = "ModeListWiget";
	int count;
	int[] Images;
	int[] Texts;
	Context mContext ;
	ListView listView;
	ModeAdapter modeAdapter;
	ModeListener mlistener;
	int curFocus = 0;
	
	public ModeListWiget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void initialItems(int[] images, int[] texts) {
		// TODO Auto-generated method stub
		Images = images;
		Texts = texts;
		count = Math.min(images.length, Texts.length);
		listView =(ListView) this.findViewById(R.id.list);
		modeAdapter = new ModeAdapter();
		listView.setAdapter(modeAdapter);
		

		float width = mContext.getResources().getDimension(R.dimen.modelist_item_length);
		ViewGroup.MarginLayoutParams params = (MarginLayoutParams) listView.getLayoutParams();//new ViewGroup.MarginLayoutParams(mDrawable.getIntrinsicWidth(),LayoutParams.MATCH_PARENT);
		params.width = (int) width;
		updateViewLayout(listView, params);
		
	}

	@Override
	public void focusItem(int index) {
		// TODO Auto-generated method stub
		curFocus = index;
		modeAdapter.dofocus(index);
	}

	@Override
	public void enableItem(int index) {
		// TODO Auto-generated method stub

		//ModeItem view = (ModeItem) listView.getItemAtPosition(index);
		//view.doEnabled(true);
	}
	@Override
	public void disableItem(int index) {
		// TODO Auto-generated method stub

		//ModeItem view = (ModeItem) listView.getItemAtPosition(index);
		//view.doEnabled(true);
	}

	public void setListener(ModeListener listener){
		mlistener = listener;
	}

	@Override
	public void onOrientationChanged(float degree) {
		// TODO Auto-generated method stub

	}
	


	
	public class ModeAdapter extends BaseAdapter {
		
		int foucus = 0;
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return count;
		}
		
		public void dofocus(int index) {
			// TODO Auto-generated method stub
			foucus = index;
			this.notifyDataSetInvalidated();			
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ModeItem view;
			Log.d(TAG, "position="+position);
			if(convertView == null){

				LayoutInflater mInflater =  LayoutInflater.from(mContext);
				
				view = (ModeItem) mInflater.inflate(R.layout.modelistitem, null);
				
			}else{
				view = (ModeItem) convertView;
				
			}
			if(foucus == position){
				view.doFocus(true);
			}else{
				view.doFocus(false);
			}
			
			view.Num = position;
			view.setImage(Images[position]);
			view.setText(Texts[position]);
			
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ModeItem view = (ModeItem)v;
					focusItem(view.Num);
					mlistener.onItemClick(view.Num);
					
				}
			});
			//convertView = new View(mContext);
			return view;
		}

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
			if(mlistener != null){
				mlistener.onItemClick(curFocus);
			}
			return true;
		}
		return false;
	}

	

}
