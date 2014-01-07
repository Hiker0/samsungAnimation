package com.example.samsunganimation.cameramode;

import com.example.samsunganimation.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ModeItem extends RelativeLayout {
	private ImageView imageview;
	private TextView textview;
	public int dy = 0,Index = -1;
	
	public ModeItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public ModeItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		this.setLayoutParams(lp);
		
		imageview=(ImageView) this.findViewById(R.id.image);
		textview =(TextView) this.findViewById(R.id.text);
	}
	
	public void setImage(int resId){
		imageview.setImageResource(resId);
	}
	
	public void setText(String text){
		textview.setText(text);
		
	}
	
}
