package com.example.samsunganimation.fan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.samsunganimation.R;

public class AirAnimationShow extends Activity {

	AirAnimationBgView mAirAnimationBgView=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.floatmenu_test);
		
		mAirAnimationBgView = (AirAnimationBgView) this.findViewById(R.id.air_animate);
		
		Button open = (Button)this.findViewById(R.id.air_start);
		open.setSoundEffectsEnabled(false);
		if(open != null){
			open.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mAirAnimationBgView.playOpen();
				}
			});
		}
		
		
		
		Button close = (Button)this.findViewById(R.id.air_stop);
		close.setSoundEffectsEnabled(false);
		if(close!=null){
			close.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mAirAnimationBgView.playClose();
				}
			});
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mAirAnimationBgView.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mAirAnimationBgView.playOpen();
			}
			
		});
	}
}
