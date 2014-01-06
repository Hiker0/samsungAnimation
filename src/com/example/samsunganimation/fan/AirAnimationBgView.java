package com.example.samsunganimation.fan;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.samsunganimation.R;

public class AirAnimationBgView extends FrameLayout {
	
		final static String TAG = "AirAnimationBgView";
		private int CENTER_IMAGE_DURATION = 600;
		private int DESCRIPTION_DURATION = 150;
		private int FOCUS_DURATION = 200;
		private int MENU_ICON_DURATION = 120;
		private int MENU_ICON_INTERVAL = 50;
		private int START_DELAY = 0;
	  
		private Context mContext = null;
		protected AirButtonGlobalMenuBgImageView bgView = null;
		protected FrameLayout contentView = null;
		protected ImageView selectView = null;
		protected ImageView centerView = null;
		protected ImageView shadowView = null;
		protected ImageView actionMemo = null;
		protected ImageView scrapBooker = null;
		protected ImageView screenWrite = null;
		protected ImageView sFinder = null;
		protected ImageView penWindow = null;
		protected ImageView[] targetViews;
		protected LinearLayout actionLabelParent = null;
		protected TextView actionLabel = null;
		protected SoundPool sound=null;
		private static int sCloseSoundId=-1;
		private static int sOpenSoundId = -1;
		private int mStreamSoundId = -1;
		private ObjectAnimator mMoveFocusAnimation = new ObjectAnimator();
		Handler mHandler = new Handler();
		AirMenuListener mListener;
		
	    private static int BIG_CIRCLE_RADIUS = 185;
	    private static int SUB_CIRCLE_RADIUS = 80;
	    //screen size
	    int screenWidth;
	    int screenHeight;
	    int statusBarHeight;
	    int touchSlop;
	    //location of each view
	    int lableLeft;
	    int lableTop;
	    int currentPosition = 0;
	    // touch point
	    private float mLastX;
	    private float mLastY;
	    int touchX;
	    int touchY;
	    int contentRadiu;
	    //set below
	    static final long SLIDE_TIME = 300;
	    static final int CUSTOM_LOCATION_X = 300;
	    static final int CUSTOM_LOCATION_Y = 800;
	    int lastIndex = -1;
	    int currentIndex = lastIndex;
	    boolean isPrepareToDrag;
	    boolean isMovingAcross;
	    boolean walkAcrossCircle;
	    boolean isInCircle;
	    private LongTouchRunnable mLongTouchRunnable;
	    static final int[] actionStringId = new int[] { R.string.select1, R.string.select2,
            R.string.select3, R.string.select4, R.string.select5 };

	class LongTouchRunnable implements Runnable {
	        @Override
	        public void run() {
	        		playClose();	
	        }
	 }
	    
		
	interface AirMenuListener{
		
		void onTrigger(int index);
		void onMove(float dx, float dy);
		void onClose();
		
	}

	public AirAnimationBgView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		Log.d(TAG,"AirAnimationBgView");
		initData();
	}
	
	public AirAnimationBgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Log.d(TAG,"AirAnimationBgView");
		mContext = context;
		initData();
	
	}
	
	public void setAirMenuListener(AirMenuListener listener){
		mListener = listener;
	}



	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		loadViews();
		//initViews();
		Log.d(TAG,"onFinishInflate");
	
	}
	
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		initSound();
		Log.d(TAG,"onAttachedToWindow");
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		sound.release();
		Log.d(TAG,"onDetachedFromWindow");
	}
	
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
    	int mRawX = (int) event.getRawX();
        int mRawY = (int) event.getRawY();
        touchX = (int) event.getX();
        touchY = (int) event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        
        case MotionEvent.ACTION_DOWN:
            contentRadiu = contentView.getWidth() / 2;
            isInCircle = (touchX - BIG_CIRCLE_RADIUS) * (touchX - BIG_CIRCLE_RADIUS) + (touchY - BIG_CIRCLE_RADIUS)
                    * (touchY - BIG_CIRCLE_RADIUS) <= SUB_CIRCLE_RADIUS * SUB_CIRCLE_RADIUS;
            whichSectionFingerisAt(mRawX, mRawY, contentRadiu, (int) event.getX(), (int) event.getY(), true, isInCircle);
            break;
        case MotionEvent.ACTION_MOVE:
            whichSectionFingerisAt(mRawX, mRawY, contentRadiu, touchX, touchY, false, isInCircle);
            break;
        case MotionEvent.ACTION_UP:
            if (currentIndex != -1 && lastIndex != -1 && currentIndex == lastIndex && !isMovingAcross && !walkAcrossCircle) {
            	if(mListener != null){
            		mListener.onTrigger(currentIndex);
            	}
            }
            isPrepareToDrag = false;
            isMovingAcross = false;
            walkAcrossCircle = false;
            currentIndex = lastIndex = -1;
            mHandler.removeCallbacks(mLongTouchRunnable);
            break;
        case MotionEvent.ACTION_CANCEL:
            isPrepareToDrag = false;
            isMovingAcross = false;
            walkAcrossCircle = false;
            currentIndex = lastIndex = -1;
            mHandler.removeCallbacks(mLongTouchRunnable);
            break;
        default:
            break;
        }
        return true;
	}


    void whichSectionFingerisAt(int rawX, int rawY, int R, int touchX, int touchY, boolean isDown, boolean inCircle) {
        if (isDown && inCircle) {
            isPrepareToDrag = true;
            mLastX = rawX;
            mLastY = rawY;
            mHandler.postDelayed(mLongTouchRunnable, ViewConfiguration.getLongPressTimeout());
            return;
        } else if (!isDown && isPrepareToDrag) {
            if ((Math.abs((rawX - mLastX)) > touchSlop || Math.abs((rawY - mLastY)) > touchSlop)) {
                mHandler.removeCallbacks(mLongTouchRunnable);
                mListener.onMove(rawX - mLastX, rawY - mLastY);
            }
            return;
        }
        double pi = Math.PI;
        int deltaHeight = Math.abs(R - touchY);
        int deltaWidth = Math.abs(R - touchX);
        double radius = Math.atan((1.0f * deltaHeight / deltaWidth));
        boolean isInSector = deltaHeight * deltaHeight + deltaWidth * deltaWidth < R * R;
        boolean leftPart = radius > 0 && touchX < R && touchY < R && isInSector;
        boolean rightTopPart = radius > 0 && touchX > R && touchY < R;

        boolean rightBottomPart = radius > 0 && touchX > R && touchY > R;
        if (radius < pi / 4 && leftPart) {
            currentPosition = 0;
        } else if (radius > pi / 4 && radius < pi / 2 && leftPart) {
            currentPosition = 1;
        } else if (rightTopPart) {
            if (radius > pi / 4 && radius < pi / 2 && isInSector) {
                currentPosition = 2;
            } else if (radius < pi / 4) {
                currentPosition = 3;
            }
        } else if (rightBottomPart) {
            if (radius < pi / 4 && isInSector) {
                currentPosition = 4;
            }
        } else if (!isDown) {
            walkAcrossCircle = true;
        }
        if (currentPosition >= 0) {
            if (isDown) {
                lastIndex = targetViews[currentPosition].getId();
            } else {
                currentIndex = targetViews[currentPosition].getId();
            }
        }
        if (currentIndex != -1 && lastIndex != -1 && lastIndex != currentIndex) {
            isMovingAcross = true;
        }
        actionLabel.setText(getResources().getString(actionStringId[currentPosition]));
        playSelect(currentPosition);

    }

    
    private void initData(){
    	
    	final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        touchSlop = configuration.getScaledTouchSlop();	
        mLongTouchRunnable = new LongTouchRunnable();
    }
	private void initSound(){
		sound = new SoundPool(1, 1, 0);
		sOpenSoundId=sound.load(mContext, R.raw.airbutton_open, 1);
		sCloseSoundId=sound.load(mContext, R.raw.airbutton_close, 1);

	}

	
	private void loadViews(){
		
		LayoutInflater mInflater =  LayoutInflater.from(mContext);;
		contentView = (FrameLayout)mInflater.inflate(R.layout.airanimation, null);
		
		actionMemo = (ImageView) contentView.findViewById(R.id.action_memo);
		scrapBooker = (ImageView) contentView.findViewById(R.id.scrap_booker);
		screenWrite = (ImageView) contentView.findViewById(R.id.screen_write);
		sFinder = (ImageView) contentView.findViewById(R.id.s_finder);
		penWindow = (ImageView) contentView.findViewById(R.id.pen_window);
		actionLabelParent = (LinearLayout) contentView.findViewById(R.id.circle_layout);
		actionLabel = (TextView) contentView.findViewById(R.id.sector_title);
		//mAirAnimationBgView =(AirAnimationBgView)parent.findViewById(R.id.air_animate);
		targetViews = new ImageView[] { actionMemo, scrapBooker, screenWrite, sFinder, penWindow };
		
		bgView = (AirButtonGlobalMenuBgImageView) contentView.findViewById(R.id.air_bg);
		selectView=(ImageView) contentView.findViewById(R.id.air_select);
		centerView = (ImageView) contentView.findViewById(R.id.air_center);
		shadowView =(ImageView) contentView.findViewById(R.id.air_shadow);
		this.addView(contentView);
		
		//BIG_CIRCLE_RADIUS = bgView.getHeight()/2;
		//SUB_CIRCLE_RADIUS = centerView.getHeight()/2;
	}
	
	void playOpenSound(){
		sound.stop(this.mStreamSoundId);
		mStreamSoundId = sound.play(sOpenSoundId, 1.0F, 1.0F, 1, 0, 1.0F);
		
	}
	
	void playCloseSound(){
		sound.stop(this.mStreamSoundId);
		mStreamSoundId = sound.play(sCloseSoundId, 1.0F, 1.0F, 1, 0, 1.0F);
	}
	
	public void setSelect(int resid){
		selectView.setBackgroundResource(resid);
	}
	
	public void playSelect(int select) {

		if(select < 0){
			selectView.setVisibility(View.GONE);
			return;
		}
		float rotationFloat = -2.7F;
		switch (select) {
		case -1:
			break;
		case 0:
			rotationFloat = -2.7F;
			break;
		case 1:
			rotationFloat = 40.299999F;
			break;
		case 2:
			rotationFloat = 83.800003F;
			break;
		case 3:
			rotationFloat = 126.0F;
			break;
		case 4:
			rotationFloat = 168.8F;
			break;
		default:
			break;
		}
		selectView.setRotation(rotationFloat);
		selectView.setVisibility(View.VISIBLE);
	}
	public void playOpen(){
		Log.d(TAG,"playOpen");
		contentView.setVisibility(View.VISIBLE);
		
		startBgOpenAnimation();
		
		final LinearLayout localImageView = actionLabelParent;
		mHandler.postDelayed(new Runnable(){
				@Override
				public void run() {
				      localImageView.setVisibility(View.VISIBLE);
					}
				}, START_DELAY + 4 * this.MENU_ICON_INTERVAL);
				
		
		 View labelView = actionLabel;
		 float[] arrayOfFloat7 = new float[2];
		 arrayOfFloat7[0] = 0.0F;
		 arrayOfFloat7[1] = 1.0F;
		 ObjectAnimator localObjectAnimator7 = ObjectAnimator.ofFloat(labelView, "alpha", arrayOfFloat7);
		 localObjectAnimator7.setStartDelay(START_DELAY + MENU_ICON_INTERVAL);
		 localObjectAnimator7.setDuration(this.MENU_ICON_DURATION);
		 localObjectAnimator7.start();
		 
		 startMenuOpenAnimation();
		  
		
	}
	
	public void playClose(){
		Log.d(TAG,"playClose");
		
		View localImageView = actionLabel;
		float[] arrayOfFloat = new float[2];
		arrayOfFloat[0] = 1.0F;
		arrayOfFloat[1] = 0.0F;
		
		ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(localImageView, "alpha", arrayOfFloat);
		localObjectAnimator.setStartDelay(4*MENU_ICON_INTERVAL);
		localObjectAnimator.setDuration(this.MENU_ICON_DURATION);
		localObjectAnimator.start();
		
		startBgCloseAnimation();
		startMenuCloseAnimation();
		
	}
	
	private void startMenuOpenAnimation(){
		
		for (int i = 0; i < 5; ++i)
	    {
		      final View localImageView = targetViews[i];
		      mHandler.postDelayed(new Runnable(){
					@Override
					public void run() {
					      localImageView.setVisibility(View.VISIBLE);
						}
					}, START_DELAY + i * this.MENU_ICON_INTERVAL);
				
		      float[] arrayOfFloat7 = new float[2];
		      arrayOfFloat7[0] = 0.0F;
		      arrayOfFloat7[1] = 1.0F;
		      ObjectAnimator localObjectAnimator7 = ObjectAnimator.ofFloat(localImageView, "alpha", arrayOfFloat7);
		      localObjectAnimator7.setStartDelay(START_DELAY + i * this.MENU_ICON_INTERVAL);
		      localObjectAnimator7.setDuration(this.MENU_ICON_DURATION);
		      localObjectAnimator7.start();
	    }
		
	}
	
	private void startMenuCloseAnimation(){
		for (int i = 4; i >= 0; --i)
	    {
	      View localImageView7 = targetViews[i];
	      float[] arrayOfFloat7 = new float[2];
	      arrayOfFloat7[0] = 1.0F;
	      arrayOfFloat7[1] = 0.0F;
	      ObjectAnimator localObjectAnimator7 = ObjectAnimator.ofFloat(localImageView7, "alpha", arrayOfFloat7);
	      localObjectAnimator7.setStartDelay((4 - i) * this.MENU_ICON_INTERVAL);
	      localObjectAnimator7.setDuration(this.MENU_ICON_DURATION);
	      localObjectAnimator7.start();
	    }
	}
	
	private void startBgOpenAnimation(){
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				playOpenSound();
			}
		});
		
		selectView.setBackground(null);
		bgView.startOpenAnimation();
		
		float[] arrayOfFloat1 = new float[2];
	    arrayOfFloat1[0] = 0.0F;
	    arrayOfFloat1[1] = 1.0F;
	    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(shadowView, "scaleX", arrayOfFloat1);

	    float[] arrayOfFloat2 = new float[2];
	    arrayOfFloat2[0] = 0.0F;
	    arrayOfFloat2[1] = 1.0F;
	    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(shadowView, "scaleY", arrayOfFloat2);

	    float[] arrayOfFloat3 = new float[2];
	    arrayOfFloat3[0] = 0.0F;
	    arrayOfFloat3[1] = 1.0F;
	    ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(shadowView, "alpha", arrayOfFloat3);
	    
	    AnimatorSet localAnimatorSet1 = new AnimatorSet();
	    Animator[] arrayOfAnimator1 = new Animator[3];
	    arrayOfAnimator1[0] = localObjectAnimator1;
	    arrayOfAnimator1[1] = localObjectAnimator2;
	    arrayOfAnimator1[2] = localObjectAnimator3;
	    localAnimatorSet1.playTogether(arrayOfAnimator1);
	    localAnimatorSet1.setStartDelay(this.START_DELAY);
	    localAnimatorSet1.setDuration(this.CENTER_IMAGE_DURATION);
	   // localAnimatorSet1.addListener(this.mStartAnimationListener);
	    localAnimatorSet1.start();
	    shadowView.setVisibility(View.VISIBLE);
	    

	    float[] arrayOfFloat4 = new float[2];
	    arrayOfFloat4[0] = 0.0F;
	    arrayOfFloat4[1] = 1.0F;
	    ObjectAnimator localObjectAnimator4 = ObjectAnimator.ofFloat(centerView, "scaleX", arrayOfFloat4);
	    float[] arrayOfFloat5 = new float[2];
	    arrayOfFloat5[0] = 0.0F;
	    arrayOfFloat5[1] = 1.0F;
	    ObjectAnimator localObjectAnimator5 = ObjectAnimator.ofFloat(centerView, "scaleY", arrayOfFloat5);
	    float[] arrayOfFloat6 = new float[2];
	    arrayOfFloat6[0] = 0.0F;
	    arrayOfFloat6[1] = 1.0F;
	    ObjectAnimator localObjectAnimator6 = ObjectAnimator.ofFloat(centerView, "alpha", arrayOfFloat6);
	    AnimatorSet localAnimatorSet2 = new AnimatorSet();
	    Animator[] arrayOfAnimator2 = new Animator[3];
	    arrayOfAnimator2[0] = localObjectAnimator4;
	    arrayOfAnimator2[1] = localObjectAnimator5;
	    arrayOfAnimator2[2] = localObjectAnimator6;
	    localAnimatorSet2.playTogether(arrayOfAnimator2);
	    localAnimatorSet2.setStartDelay(this.START_DELAY);
	    localAnimatorSet2.setDuration(this.CENTER_IMAGE_DURATION);
	    //localAnimatorSet2.addListener(this.mStartAnimationListener);
	    localAnimatorSet2.start();
	    centerView.setVisibility(View.VISIBLE);
	
	}
	
	private void startBgCloseAnimation(){
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				playCloseSound();
			}
		});
			
		selectView.setBackground(null);
	
	    float[] arrayOfFloat1 = new float[2];
	    arrayOfFloat1[0] = 1.0F;
	    arrayOfFloat1[1] = 0.0F;
	    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(shadowView, "scaleX", arrayOfFloat1);

	    float[] arrayOfFloat2 = new float[2];
	    arrayOfFloat2[0] = 1.0F;
	    arrayOfFloat2[1] = 0.0F;
	    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(shadowView, "scaleY", arrayOfFloat2);

	    float[] arrayOfFloat3 = new float[2];
	    arrayOfFloat3[0] = 1.0F;
	    arrayOfFloat3[1] = 0.0F;
	    ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(shadowView, "alpha", arrayOfFloat3);
	    
	    AnimatorSet localAnimatorSet1 = new AnimatorSet();
	    Animator[] arrayOfAnimator1 = new Animator[3];
	    arrayOfAnimator1[0] = localObjectAnimator1;
	    arrayOfAnimator1[1] = localObjectAnimator2;
	    arrayOfAnimator1[2] = localObjectAnimator3;
	    localAnimatorSet1.playTogether(arrayOfAnimator1);
	    localAnimatorSet1.setDuration(this.CENTER_IMAGE_DURATION);
	    localAnimatorSet1.start();

	    float[] arrayOfFloat4 = new float[2];
	    arrayOfFloat4[0] = 1.0F;
	    arrayOfFloat4[1] = 0.0F;
	    ObjectAnimator localObjectAnimator4 = ObjectAnimator.ofFloat(centerView, "scaleX", arrayOfFloat4);

	    float[] arrayOfFloat5 = new float[2];
	    arrayOfFloat5[0] = 1.0F;
	    arrayOfFloat5[1] = 0.0F;
	    ObjectAnimator localObjectAnimator5 = ObjectAnimator.ofFloat(centerView, "scaleY", arrayOfFloat5);

	    float[] arrayOfFloat6 = new float[2];
	    arrayOfFloat6[0] = 1.0F;
	    arrayOfFloat6[1] = 0.0F;
	    ObjectAnimator localObjectAnimator6 = ObjectAnimator.ofFloat(centerView, "alpha", arrayOfFloat6);
	    
	    AnimatorSet localAnimatorSet2 = new AnimatorSet();
	    Animator[] arrayOfAnimator2 = new Animator[3];
	    arrayOfAnimator2[0] = localObjectAnimator4;
	    arrayOfAnimator2[1] = localObjectAnimator5;
	    arrayOfAnimator2[2] = localObjectAnimator6;
	    localAnimatorSet2.playTogether(arrayOfAnimator2);
	    localAnimatorSet2.setDuration(this.CENTER_IMAGE_DURATION);
	    localAnimatorSet2.start();
	    
	    Animator.AnimatorListener listener = new  Animator.AnimatorListener(){

				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void onAnimationEnd(Animator animation) {
					// TODO Auto-generated method stub
					contentView.setVisibility(View.INVISIBLE);
					if(mListener != null){
						mListener.onClose();
					}
				}
	
				@Override
				public void onAnimationRepeat(Animator animation) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void onAnimationStart(Animator animation) {
					// TODO Auto-generated method stub
					
				}
		    	
		    };
			bgView.startCloseAnimation(this.START_DELAY / 2,listener );
	}
}
