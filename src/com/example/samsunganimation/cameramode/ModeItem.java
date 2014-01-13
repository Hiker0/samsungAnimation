package com.example.samsunganimation.cameramode;

import com.example.samsunganimation.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ModeItem extends RelativeLayout {
	private ImageView imageview,focusView,enableView;
	private TextView textview;
	private boolean focused = false,  enabled = true;
	public int dy = 0,Num = -1;
	
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
		
		focusView = (ImageView) this.findViewById(R.id.focus);
		imageview = (ImageView) this.findViewById(R.id.image);
		textview =(TextView) this.findViewById(R.id.text);
		enableView = (ImageView) this.findViewById(R.id.disable);
		
		doFocus(focused);
		doEnabled(enabled);
	}
	
	public void setImage(int resId){
		imageview.setImageResource(resId);
	}
	
	public void setText(String text){
		textview.setText(text);
		
	}
	public void doFocus(boolean focus){
		focused = focus;
		if(focus){
			focusView.setVisibility(View.VISIBLE);
		}else{
			focusView.setVisibility(View.INVISIBLE);
		}
	}
	
	public void doEnabled(boolean enable){
		enabled = enable;
		if(enable){
			enableView.setVisibility(View.INVISIBLE );
		}else{
			enableView.setVisibility(View.VISIBLE);
		}
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
}
