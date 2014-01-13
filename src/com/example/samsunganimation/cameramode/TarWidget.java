package com.example.samsunganimation.cameramode;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.example.samsunganimation.R;

public class TarWidget extends ViewGroup implements View.OnTouchListener {
	
	final String TAG = "TarWidget";
	private ArrayList<ModeItem> viewlist;
	private Context mContext;
	private SoundPool sound = null;
	private int mSoundId = 0;
	final static int TIP_NUM = 20;
    private int curFocus =2;
	
	int pointX[]= {102,132,198,358,520,582,620};
	int pointY[]= {417,366,273,186,273,366,417};
	float SCALE[]={0.5f,0.65f,0.8f,1.0f,0.8f,0.65f,0.5f};
	float orentationDegree = 0;
	
	int lastTip = 10;
	int current = 0;
	int itemNum = 10;
	int layoutType = 0;
	
	TarWidgetListener mlistener;
	
	private Handler handler = new Handler();
	
	
	public interface TarWidgetListener{
		
		void onItemClick(int index);	
	}
	

	public TarWidget(Context context){
		super(context);
		
		
		mContext = context;
		
		this.setOnTouchListener(this);
		//handler.postDelayed(runnable, 10);
	}

	public TarWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		this.setOnTouchListener(this);
	}
	

	
	public void  initialItems(int[] images,int[] texts){
		LayoutInflater mInflater =  LayoutInflater.from(mContext);
		viewlist = new ArrayList<ModeItem>();
		
		for(int i=0;i< images.length;i++){
		
			ModeItem itemview = (ModeItem) mInflater.inflate(R.layout.modeitem, null);
			itemview.setImage(images[i]);
			
			Resources res = mContext.getResources();
			String st = res.getString(texts[i]);
			itemview.setText(st);
			
			
			itemview.Num = i;
			itemview.setRotation(orentationDegree);
			
		    this.addView(itemview);	
		    viewlist.add(itemview);
		}
		
		if(viewlist.size() < 5){
			layoutType = 1;
		}
	}
	
	public void enableItem(int index){
		viewlist.get(index).doEnabled(true);
	}
	
	public void disableItem(int index){
		viewlist.get(index).doEnabled(false);
	}
	
	public void focusItem(int index){
		curFocus = index;
		int num = viewlist.size();
		if(num > 0){
			for(int i = 0;i < num;i++){
				if(index == i){
					viewlist.get(i).doFocus(true);
				}else{
					viewlist.get(i).doFocus(false);
				}
			}
		}
		
	}
	
	public void setListener(TarWidgetListener listener){
		mlistener = listener;
	}
	
	public void onOrientationChanged(float degree){
		if(viewlist == null){
			return;
		}
		int num = viewlist.size();
		ModeItem itemview;
		for(int i=0; i < num; i++){
			itemview = viewlist.get(i);
			itemview.setRotation(degree);
		}
	}
	
	
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();

	}
	
     @Override
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         
         for (int index = 0; index < getChildCount(); index++) {
             final View child = getChildAt(index);
             // measure
             child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
         }
 
         super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

	 

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		sound = new SoundPool(1, 1, 0);
		mSoundId =sound.load(mContext, R.raw.swipe, 1);
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		sound.release();
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
			locatePosition();
		}
	
	
	private void  onTipChanged(int dtip){
		
		int curTip = lastTip + dtip%TIP_NUM;
		int num = viewlist.size();
		ModeItem itemview;
	
		
		if(curTip < 0){
			itemview= viewlist.get((current+5)%num);
			itemview.setVisibility(View.VISIBLE);
			itemview= viewlist.get(current);
			itemview.setVisibility(View.INVISIBLE);
			
			current += 1;
			lastTip = curTip + TIP_NUM;
			current = current % num;
			

			playSound();
			
		}else if((curTip >= TIP_NUM)){
			
			current -= 1;
			lastTip = curTip - TIP_NUM;
			current = (current+num) % num;
			
			itemview= viewlist.get((current+5)%num);
			itemview.setVisibility(View.INVISIBLE);
			itemview= viewlist.get(current);
			itemview.setVisibility(View.VISIBLE);
			
			playSound();
		}else{
			lastTip = curTip;
		}
		
		
		
		this.requestLayout();
	}
	
	private void playSound(){
		if(sound != null){
			sound.play(mSoundId,  1.0F, 1.0F, 1, 0, 1.0F);
		}
	}
	
	private void locateItemPosition(int index){
		ModeItem itemview;
		float mscale;
		int dx,dy;
		int num = viewlist.size();
		int dheight,dwidth;
	
		Log.d(TAG, "lastTip="+lastTip);
		itemview= viewlist.get((current+index)%num);
		int total = lastTip+index*TIP_NUM;
		int level = (total+10)/TIP_NUM;
		int tip = (total+10)%TIP_NUM;
		
		
		Log.d(TAG, "level="+level);
		mscale =SCALE[level]+ (SCALE[level+1]-SCALE[level])/TIP_NUM*tip;
		dx = pointX[level]+ (pointX[level+1]-pointX[level])/TIP_NUM*tip;
		dy = pointY[level]+ (pointY[level+1]-pointY[level])/TIP_NUM*tip;

		
		itemview.dy = dy;
		
		itemview.setScaleX(mscale);
		itemview.setScaleY(mscale);
		dheight= (int) (itemview.getMeasuredHeight()*0.5);
		dwidth = (int) (itemview.getMeasuredWidth()*0.5);
		
		itemview.layout(dx- dwidth, dy - dheight, dx + dwidth, dy + dheight);

	}
	
	private void setFront(){
		ModeItem itemview;
		ModeItem itemview1;
		int num = viewlist.size();
		
		itemview= viewlist.get((current+1)%num);
		itemview1= viewlist.get((current+3)%num);
		if(itemview.dy < itemview1.dy){
			this.bringChildToFront(itemview1);
			this.bringChildToFront(itemview);
		}else{
			this.bringChildToFront(itemview);
			this.bringChildToFront(itemview1);
		}
		itemview= viewlist.get((current+2)%num);
		this.bringChildToFront(itemview);
	}
	private void locatePosition(){
		if(viewlist == null){
			return;
		}
		locateItemPosition(0);
		locateItemPosition(1);
		locateItemPosition(2);
		locateItemPosition(3);
		locateItemPosition(4);
		setFront();

	}
	
	private void reLocate(){
		if(lastTip !=  10){
			lastTip = 10;
			locateItemPosition(0);
			locateItemPosition(1);
			locateItemPosition(2);
			locateItemPosition(3);
			locateItemPosition(4);
		}
	}

	
	private float lastX = 0;
	private int flag = FLAG_NONE;
	final static int FLAG_NONE = 0;
	final static int FLAG_CLICK = 1;
	final static int FLAG_EMOVE = 2;
	final static int FLAG_MOVE = 3;
	final static int CUSTOM_LONGPRESS = 200;
	private ModeItem clickItem;
	
	private VelocityTracker mVelocityTracker = null;
	
	private ModeItem getClickItem(float x, float y){
		
		ModeItem itemview;
	
		int num = viewlist.size();
		
		itemview= viewlist.get((current+2)%num);
		if(x > itemview.getLeft()
				&& x < itemview.getRight()
				&& y > itemview.getTop()
				&& y < itemview.getBottom())
		{
			return itemview;
		}
		
		itemview= viewlist.get((current+1)%num);
		if(x > itemview.getLeft()
				&& x < itemview.getRight()
				&& y > itemview.getTop()
				&& y < itemview.getBottom())
		{
			return itemview;
		}
		
		itemview= viewlist.get((current+3)%num);
		if(x > itemview.getLeft()
				&& x < itemview.getRight()
				&& y > itemview.getTop()
				&& y < itemview.getBottom())
		{
			return itemview;
		}
		
		itemview= viewlist.get(current);
		if(x > itemview.getLeft()
				&& x < itemview.getRight()
				&& y > itemview.getTop()
				&& y < itemview.getBottom())
		{
			return itemview;
		}
		
		itemview= viewlist.get((current+4)%num);
		if(x > itemview.getLeft()
				&& x < itemview.getRight()
				&& y > itemview.getTop()
				&& y < itemview.getBottom())
		{
			return itemview;
		}
		
		return null;
	}
	
	Runnable longRunnable= new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			flag = FLAG_EMOVE;
		}
		
	};
	
	class PopRunnable implements Runnable{
		int tipTime,mTip;
		public PopRunnable(int tiptime, int tip){
			tipTime= tiptime;
			mTip = tip;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			onTipChanged(mTip);
			tipTime -=1;
			if(tipTime > 0){
				handler.post(this);
			}else{
				reLocate();
				flag = FLAG_NONE;
				clickItem = null;
			}
		}
	}
	private void doClick(ModeItem itemview){
		int num = viewlist.size();
		int click = itemview.Num - current;
		if(click < 0){
			click += num;
		}
		if(click == 2){
					
			itemview= viewlist.get(curFocus);
			itemview.doFocus(false);
			
			itemview= viewlist.get((current+2)%num);
			itemview.doFocus(true);
			
			curFocus = itemview.Num;
			mlistener.onItemClick(curFocus);
		}else{
			playPop(click);
		}
	}
	
	
	private void playPop(int click){
		int totalTip = (2-click)*20;
		
		if(totalTip > 0){
			handler.post(new PopRunnable(totalTip/5,5));
		}else{
			handler.post(new PopRunnable((-totalTip)/5,-5));
		}
				
	}
	
	private int  pressVelocityTracker(int velocityX){
		if(Math.abs(velocityX) < 100){
			return 0;
		}
		
		int totalTip = velocityX /8;
		
		if(totalTip > 0){
			handler.post(new PopRunnable(totalTip/7,7));
		}else{
			handler.post(new PopRunnable((-totalTip)/7,-7));
		}
		
		flag = FLAG_NONE;
		return 1;
	}

	
	@SuppressLint("Recycle")
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		float x = event.getX();
		float y = event.getY();
		if(mVelocityTracker == null){
			mVelocityTracker=VelocityTracker.obtain();
		}
		 mVelocityTracker.addMovement(event);
		 
		switch(event.getAction()){
		
			case MotionEvent.ACTION_DOWN:
				lastX = x;
				clickItem = getClickItem(x,y);
				
				if( clickItem != null){
					clickItem.doFocus(true);
					flag = FLAG_CLICK;
					handler.postDelayed(longRunnable,CUSTOM_LONGPRESS);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(flag ==  FLAG_NONE){
					break;
				}
				
				int tip = (int) ((x-lastX)/5);
				Log.d(TAG,"ACTION_MOVE x="+x + ", lastX ="+lastX);
				
				if( tip != 0){
					onTipChanged(tip);
					flag = FLAG_MOVE;
					handler.removeCallbacks(longRunnable);
					lastX = x;
				}
				
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if(flag == FLAG_NONE || clickItem == null){
					flag = FLAG_NONE;
					clickItem = null;
					break;
				}
				
				mVelocityTracker.computeCurrentVelocity(100, ViewConfiguration.getMaximumFlingVelocity());
	            int velocityX = (int) mVelocityTracker.getXVelocity();
	            int tracked =  pressVelocityTracker(velocityX);
				
	            
				if(flag == FLAG_CLICK){
					doClick(clickItem);				
				}else if(tracked == 0){
					reLocate();
				}
				
				if(clickItem.Num != curFocus){
					clickItem.doFocus(false);
				}
				
				flag = FLAG_NONE;
				clickItem = null;
				
		        if (mVelocityTracker != null) {
		            mVelocityTracker.recycle();
		            mVelocityTracker = null;
		        }
				break;
			}
	
			return true;
	}
    
	
}
