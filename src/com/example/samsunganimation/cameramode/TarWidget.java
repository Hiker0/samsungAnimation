package com.example.samsunganimation.cameramode;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsunganimation.R;

public class TarWidget extends ViewGroup implements View.OnTouchListener {
	
	private ArrayList<Item> list;
	private ArrayList<ModeItem> viewlist;
	private Context mContext;
	private SoundPool sound = null;
	private int mSoundId = 0;
	final static int TIP_NUM = 20;
	
	int pointX[]= {102,132,198,358,520,582,620};
	int pointY[]= {417,366,273,186,273,366,417};
	float SCALE[]={0.5f,0.65f,0.8f,1.0f,0.8f,0.65f,0.5f};
	
	
	int[] imageids={R.drawable.mode_3d_photo,
			R.drawable.mode_animated_scene,
			R.drawable.mode_aqua,
			R.drawable.mode_auto,
			R.drawable.mode_beauty_face,
			R.drawable.mode_best_face,
			R.drawable.mode_best_photo,
			R.drawable.mode_continuous,
			R.drawable.mode_dramashot,
			R.drawable.mode_eraser,
			R.drawable.mode_focus,
			R.drawable.mode_golfshot,};
	
	final static int RADIUS = 260;
	final static float ratio = 0.8f;
	final static double PAI = 3.1415926f;
	int CENTER_X = 360;
	int CENTER_Y = 380;
	
	int lastTip = 10;
	int current = 0;
	int itemNum = 10;
	
	private Handler handler = new Handler();
	private Runnable runnable=  new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			onTipChanged(2);
			handler.postDelayed(runnable,0);
		}
		
	};
	public TarWidget(Context context){
		super(context);
		
		setWillNotDraw(false);
		
		mContext = context;
		viewlist = new ArrayList<ModeItem>();
		
		initList();
		
		LayoutInflater mInflater =  LayoutInflater.from(mContext);
		
		for(int i=0;i<list.size();i++){
		
			ModeItem itemview = (ModeItem) mInflater.inflate(R.layout.modeitem, null);
			Item item = list.get(i);
			itemview.setImage(item.imageId);
			itemview.setText(item.text);
			//emview.setVisibility(View.INVISIBLE);
			viewlist.add(itemview);
			
		    this.addView(itemview);	
		}
		
		handler.postDelayed(runnable, 10);
	}

	public TarWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mContext = context;
	}
	
	public class Item{
		int imageId;
		String text;
		
		public Item(int imageid, String string){
			imageId = imageid; 
			text = string;
		}		
		
	}
	
	private void initList(){
		
		list = new ArrayList<Item>();
		for(int i=0; i<10; i++){
			Item item = new Item(imageids[i],Integer.toString(i));
			list.add(item);	
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
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//super.onDraw(canvas);
		//ModeItem itemview = viewlist.get(0);
		
		//itemview.draw(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
			locatePosition(lastTip);
		}
	
	
	private void playSound(){
		if(sound != null){
			sound.play(mSoundId,  1.0F, 1.0F, 1, 0, 1.0F);
		}
	}
	public void onTipChanged(int dtip){
		
		int curTip = lastTip + dtip;
		int num = viewlist.size();
		ModeItem itemview;
		
		if(curTip < 0){
			itemview= viewlist.get((current+5)%num);
			itemview.setVisibility(View.VISIBLE);
			itemview.Index = -1;
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
	
	
	private void locateItemPosition(int index){
		ModeItem itemview;
		float mscale;
		int dx,dy;
		int num = viewlist.size();
		int dheight,dwidth;
	
		
		itemview= viewlist.get((current+index)%num);
		int total = lastTip+index*TIP_NUM;
		int level = (total+10)/TIP_NUM;
		int tip = (total+10)%TIP_NUM;
		
		
		
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
	private void locatePosition(final int curDegree){
		locateItemPosition(0);
		locateItemPosition(1);
		locateItemPosition(2);
		locateItemPosition(3);
		locateItemPosition(4);
		setFront();

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
