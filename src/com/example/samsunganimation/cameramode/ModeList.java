package com.example.samsunganimation.cameramode;

import java.util.ArrayList;
import java.lang.Math;

import com.example.samsunganimation.R;

import android.content.Context;
import android.graphics.Canvas;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ModeList extends ViewGroup {
	
	private ArrayList<Item> list;
	private ArrayList<ModeItem> viewlist;
	private Context mContext;
	private SoundPool sound = null;
	private int mSoundId = 0;
	
	int pointX[]= {0,100,200,300,400};
	int pointY[]= {200,100,0,100,200};
	float scale[]={0.65f,0.8f,1.0f,0.8f,0.65f};
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
	
	int lastDegree = 0;
	int current = 0;
	int itemNum = 10;
	
	private Handler handler = new Handler();
	private Runnable runnable=  new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			onDegreeChanged(2);
			handler.postDelayed(runnable, 10);
		}
		
	};
	public ModeList(Context context){
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
		
		//handler.postDelayed(runnable, 10);
	}

	public ModeList(Context context, AttributeSet attrs) {
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
			locatePosition(lastDegree);
		}
	
	
	private void playSound(){
		if(sound != null){
			sound.play(mSoundId,  1.0F, 1.0F, 1, 0, 1.0F);
		}
	}
	public void onDegreeChanged(int dDegree){
		
		int curDegree = lastDegree + dDegree;
		int num = viewlist.size();
		ModeItem itemview;
		
		if((curDegree)/36 > (lastDegree)/36){
			
			if(current == 0){
				current = num-1;
			}else{
				current -= 1;
			}
			
			itemview= viewlist.get((current+5)%num);
			itemview.setVisibility(View.INVISIBLE);
			itemview= viewlist.get(current);
			itemview.setVisibility(View.VISIBLE);
			playSound();
			
		}else if((curDegree)/36 < (lastDegree)/36){
			
			itemview= viewlist.get((current+5)%num);
			itemview.setVisibility(View.INVISIBLE);
			itemview= viewlist.get(current);
			itemview.setVisibility(View.VISIBLE);
			
			if(current == num-1){
				current = 0;
			}else{
				current += 1;
			}
			
			playSound();
			
		}
		
		
		if(curDegree>360 || curDegree < 0){
			
			curDegree = (curDegree+360) % 360;
		}
		lastDegree = curDegree;
		//locatePosition(curDegree);
		this.requestLayout();
	}
	
	
	private void locateItemPositionCircle(final int curDegree, int index){
		ModeItem itemview;
		int degree;
		double zRatio,xRatio,yRatio;
		float scale;
		int dx,dy;
		int num = viewlist.size();
		int dheight,dwidth;
		double rudians = 0;
		
		itemview= viewlist.get((current+index)%num);
		degree = curDegree % 36+36*index;
		rudians= degree*PAI/180;
		zRatio  =  Math.sin(rudians);
		xRatio  =  Math.cos(rudians);
		yRatio  =  zRatio * zRatio * ratio;
		
		
		scale = (float) (0.5f + 0.5f * (zRatio));
		itemview.setScaleX(scale);
		itemview.setScaleY(scale);
		dheight= (int) (itemview.getMeasuredHeight()*0.5);
		dwidth = (int) (itemview.getMeasuredWidth()*0.5);
		
		dx = (int) (CENTER_X - RADIUS * xRatio);
		dy = (int) (CENTER_Y - RADIUS * yRatio);
		itemview.dy = dy;
		itemview.layout(dx- dwidth, dy - dheight, dx + dwidth, dy + dheight);
		
	}
	
	private void locateItemPositionTrangle(final int curDegree, int index){
		ModeItem itemview;
		int degree;
		double zRatio,xRatio,yRatio;
		float scale;
		int dx,dy;
		int num = viewlist.size();
		int dheight,dwidth;
		
		itemview= viewlist.get((current+index)%num);
		degree = curDegree % 36+36*index;
		if(degree < 90){
			
			zRatio = degree/90.0f;
			xRatio  =  1 - zRatio;
			yRatio  =  zRatio * ratio;
		}else{                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
			zRatio = (180 - degree)/90.0f;
			xRatio  =  zRatio-1;
			yRatio  =  zRatio * ratio;
		}
		
		
		scale = (float) (0.5f + 0.5f * (zRatio));
		itemview.setScaleX(scale);
		itemview.setScaleY(scale);
		dheight= (int) (itemview.getMeasuredHeight()*0.5);
		dwidth = (int) (itemview.getMeasuredWidth()*0.5);
		
		dx = (int) (CENTER_X - RADIUS * xRatio);
		dy = (int) (CENTER_Y - RADIUS * yRatio);
		itemview.dy = dy;
		itemview.layout(dx- dwidth, dy - dheight, dx + dwidth, dy + dheight);

	}
	
	private void setFront(){
		ModeItem itemview;
		ModeItem itemview1;
		int num = viewlist.size();
		
		itemview= viewlist.get((current+1)%num);
		itemview1= viewlist.get((current+3)%num);
		if(itemview.dy > itemview1.dy){
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
		locateItemPositionCircle(curDegree,0);
		locateItemPositionCircle(curDegree,1);
		locateItemPositionCircle(curDegree,2);
		locateItemPositionCircle(curDegree,3);
		locateItemPositionCircle(curDegree,4);
		setFront();

	}
	
}
