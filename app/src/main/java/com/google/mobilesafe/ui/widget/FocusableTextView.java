package com.google.mobilesafe.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class FocusableTextView extends TextView {

	public FocusableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		setEllipsize(TruncateAt.MARQUEE);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setSingleLine();
		setMarqueeRepeatLimit(-1);
		
	}
	
	public FocusableTextView(Context context) {
		super(context);
		
	}
	
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if (focused) {
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (hasWindowFocus) {
			
			super.onWindowFocusChanged(hasWindowFocus);
		}
	}
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}

}
