package com.example.samsunganimation.cameramode;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.samsunganimation.R;

public class CameraModeShow extends Activity {
	final static String TAG = "CameraModeShow";
	
    private static final int[] MODE_ICONS = new int[]{
        R.drawable.mode_storyshot,
        R.drawable.mode_rich_tone,
        R.drawable.mode_beauty_face,
        R.drawable.mode_panorama,
        R.drawable.mode_3d_photo,
        R.drawable.mode_night,
        R.drawable.mode_best_face,
        R.drawable.mode_aqua,
        R.drawable.mode_continuous,
    };
	
    private static final int[] MODE_TEXT = new int[]{
        R.string.mode_photo_title,
        R.string.mode_hdr_title,
        R.string.mode_facebeauty_title,
        R.string.mode_panorama_title,
        R.string.mode_mav_title,
        R.string.mode_asd_title,
        R.string.mode_smile_title,
        R.string.mode_best_shot_title,
        R.string.mode_ev_bracket_shot_title,
    };
	
    
	int deree = 0;
	OrientationEventListener lis;
	int orientationHistory = OrientationEventListener.ORIENTATION_UNKNOWN;
	private TextView infoView  ;
	private ImageButton  mViewBack,mViewAuto;
	

	private View.OnClickListener mClickListener  = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == mViewBack){
				
			}
			
			if(v == mViewAuto){
				
			}
		}
		
	};
	
	
	private TarWidget.TarWidgetListener clickCallback = new TarWidget.TarWidgetListener(){

		@Override
		public void onItemClick(int index) {
			// TODO Auto-generated method stub
			
		}

	        
	};
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.cameramode_show);
		  

		// TODO Auto-generated method stub
		final TarWidget wiget = (TarWidget) this.findViewById(R.id.picker);
		wiget.initialItems(MODE_ICONS, MODE_TEXT);
		wiget.focusItem(2);
		wiget.setListener(clickCallback);
        mViewBack = (ImageButton) this.findViewById(R.id.back);
        mViewAuto = (ImageButton) this.findViewById(R.id.auto);
        infoView  = (TextView)this.findViewById(R.id.info);
        mViewAuto.setOnClickListener(mClickListener);
        mViewBack.setOnClickListener(mClickListener);
        infoView.setText("asdfasdfasdf sadfgdfgdfghfgh rthgrfgh");
        updateInfo();
//		LinearLayout root = (LinearLayout) this.findViewById(R.id.picker);
//		final TarWidget wiget = new TarWidget(this);
//		wiget.initialItems(MODE_ICONS_NORMAL, MODE_TEXT);
//		//final Button button = (Button) this.findViewById(R.id.add);
//		
//		float height = this.getResources().getDimension(R.dimen.modelist_height);
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,(int) height);
//		wiget.setLayoutParams(lp);
//		root.addView(wiget);
		super.onStart();
		
//		button.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				wiget.onTipChanged(1);
//				deree+=1;
//				button.setText(""+deree);
//			}
//		});
		lis = new OrientationEventListener(this){

			@Override
			public void onOrientationChanged(int orientation) {
				// TODO Auto-generated method stub
				int orient = roundOrientation(orientation,orientationHistory);
				if(orientationHistory != orient){
					orientationHistory = orient;
					wiget.onOrientationChanged(-orientationHistory);
				}
				updateInfo();
			}
			
		};
		
	
	}
	
	private void updateInfo(){
		switch(orientationHistory){
			case 0:
			{
				infoView.setBackgroundResource(R.drawable.description_bubble_popup_01);
				
				LayoutParams params = new LayoutParams(500, 200);
				params.topMargin = 450;
				params.leftMargin = 110;
				infoView.setLayoutParams(params);
				infoView.setPadding(20, 20, 20, 20);
				break;
			}
			case 90:
			{
				infoView.setBackgroundResource(R.drawable.description_bubble_popup_04);
				LayoutParams params = new LayoutParams(500, 200);
				params.topMargin = 250;
				params.leftMargin = 110;
				infoView.setLayoutParams(params);
				infoView.setPadding(40, 20, 20, 20);
				break;
			}
			case 270:
			{  
				infoView.setBackgroundResource(R.drawable.description_bubble_popup_03);
				infoView.setBackgroundResource(R.drawable.description_bubble_popup_04);
				LayoutParams params = new LayoutParams(500, 200);
				params.topMargin = 250;
				params.leftMargin = 110;
				infoView.setLayoutParams(params);
				infoView.setPadding(40, 20, 20, 20);
				
				break;
			}
			case 180:
			{
				infoView.setBackgroundResource(R.drawable.description_bubble_popup_02);
				LayoutParams params = new LayoutParams(500, 200);
				params.topMargin = 450;
				params.leftMargin = 110;
				infoView.setLayoutParams(params);
				infoView.setPadding(20, 10, 20, 40);
				break;
			}
		}
		infoView.setRotation(-orientationHistory);
	}
	
    public static int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation = false;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + 5);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        
        Log.d(TAG,"orientationHistory=" +orientationHistory);
        return orientationHistory;
    }
    
    public  int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                break;
        }
        return 0;
    }
    
	@Override
	protected void onStart() {
		super.onStart();
		lis.enable();}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		lis.disable();
	}
	
	
	
	
}
