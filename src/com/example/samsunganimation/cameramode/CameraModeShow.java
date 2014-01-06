package com.example.samsunganimation.cameramode;

import com.example.samsunganimation.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CameraModeShow extends Activity {
	
	private Handler handler = new Handler();
	int deree = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.cameramode_show);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		LinearLayout root = (LinearLayout) this.findViewById(R.id.root);
		final TarWidget wiget = new TarWidget(this);
		final Button button = (Button) this.findViewById(R.id.add);
		
		float height = this.getResources().getDimension(R.dimen.modelist_height);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,(int) height);
		wiget.setLayoutParams(lp);
		root.addView(wiget);
		super.onStart();
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wiget.onTipChanged(1);
				deree+=1;
				button.setText(""+deree);
			}
		});
		
	}
	
	
}
