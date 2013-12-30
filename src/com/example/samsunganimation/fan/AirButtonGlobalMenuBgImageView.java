package com.example.samsunganimation.fan;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.samsunganimation.R;

public class AirButtonGlobalMenuBgImageView extends ImageView {

	private Context mContext = null;
	private final int ANIMATION_DURATION = 600;
	private final float ARC_BASE_ANGLE = 80.0F;
	private final float ARC_MAX_ANGLE = -223.0F;
	private final String TAG = "AirButtonGlobalMenuBgImageView";
	private ObjectAnimator mArcAnimator = null;
	private Path mArcPath = null;
	private float mCurrentAnimatingArc = ARC_MAX_ANGLE;
	private RectF mImageBoundary = null;
	private float mImageCenterX = 0.0F;
	private float mImageCenterY = 0.0F;

	public AirButtonGlobalMenuBgImageView(Context context) {
		super(context);
		mContext = context;
		super.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mArcAnimator = new ObjectAnimator();
		mArcAnimator.setTarget(this);
		mArcAnimator.setPropertyName("animationArc");
	}

	public AirButtonGlobalMenuBgImageView(Context context, AttributeSet attrs) {

		super(context, attrs);
		// TODO Auto-generated constructor stub

		mContext = context;
		super.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		super.setImageResource(R.drawable.airbutton_global_bg);
		mArcAnimator = new ObjectAnimator();
		mArcAnimator.setTarget(this);
		mArcAnimator.setPropertyName("animationArc");
	}

	public AirButtonGlobalMenuBgImageView(Context context, AttributeSet attrs,
			int x) {

		super(context, attrs, x);
		// TODO Auto-generated constructor stub

		mContext = context;
		super.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		super.setImageResource(R.drawable.airbutton_global_bg);
		mArcAnimator = new ObjectAnimator();
		mArcAnimator.setTarget(this);
		mArcAnimator.setPropertyName("animationArc");
	}

	private void initVariables() {
		if (mImageBoundary == null) {
			mImageBoundary = new RectF(-100.0F, -100.0F,
					100 + super.getWidth(), 100 + super.getWidth());
		}
		float f = super.getWidth() / 2;
		mImageCenterY = f;
		mImageCenterX = f;
		mArcPath = new Path();
		Log.v("airbutton",
				"AirButtonGlobalMenuBgImageView initVariables mImageCenterY="
						+ mImageCenterY + ", mImageCenterX="
						+ mImageCenterX);
		Log.v("airbutton",
				"AirButtonGlobalMenuBgImageView initVariables super.getWidth()="
						+ super.getWidth());
	}

	protected void onDraw(Canvas paramCanvas) {
		if ((mImageBoundary == null) || (mArcPath == null)) {
			return;
		}

		mArcPath.reset();
		mArcPath.moveTo(mImageCenterX, mImageCenterY);
		mArcPath.arcTo(mImageBoundary, ARC_BASE_ANGLE,
				mCurrentAnimatingArc);
		mArcPath.close();
		paramCanvas.clipPath(mArcPath, Region.Op.DIFFERENCE);
		super.onDraw(paramCanvas);

	}

	public void setAnimationArc(float paramFloat) {
		mCurrentAnimatingArc = paramFloat;
		super.invalidate();
	}

	public void startCloseAnimation(int paramInt,
			Animator.AnimatorListener paramAnimatorListener) {
		initVariables();
		Log.v("airbutton", "startCloseAnimation");
		if (mArcAnimator.isRunning())
			mArcAnimator.cancel();
		mArcAnimator.setStartDelay(paramInt);
		mArcAnimator.setDuration(ANIMATION_DURATION);
		ObjectAnimator localObjectAnimator = mArcAnimator;
		float[] arrayOfFloat = new float[2];
		arrayOfFloat[0] = 0.0F;
		arrayOfFloat[1] = -303.0F;
		localObjectAnimator.setFloatValues(arrayOfFloat);
		mArcAnimator.removeAllListeners();
		if (paramAnimatorListener != null) {
			mArcAnimator.addListener(paramAnimatorListener);
		}
		mArcAnimator.start();
	}

	public void startOpenAnimation() {
		initVariables();
		if (mArcAnimator.isRunning())
			mArcAnimator.cancel();
		mArcAnimator.setStartDelay(0L);
		mArcAnimator.setDuration(ANIMATION_DURATION);
		ObjectAnimator localObjectAnimator = mArcAnimator;
		float[] arrayOfFloat = new float[2];
		arrayOfFloat[0] = -303.0F;
		arrayOfFloat[1] = 0.0F;
		localObjectAnimator.setFloatValues(arrayOfFloat);
		mArcAnimator.removeAllListeners();
		mArcAnimator.start();
	}
}

/*
 * Location: D:\testapk\decode\dex2jar-0.0.9.9\Samsung_dex2jar.jar Qualified
 * Name: android.samsung.android.airbutton.view.AirButtonGlobalMenuBgImageView
 * JD-Core Version: 0.5.4
 */