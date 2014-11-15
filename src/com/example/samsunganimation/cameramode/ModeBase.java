package com.example.samsunganimation.cameramode;

public interface ModeBase {

	public interface ModeListener{
		
		void onItemClick(int index);	
	}

	abstract public void  initialItems(int[] images,int[] texts);
	abstract public void focusItem(int index);
	abstract public void enableItem(int index);
	abstract public void setListener(ModeListener listener);
	abstract public void onOrientationChanged(float degree);
	abstract public void disableItem(int index);
}
