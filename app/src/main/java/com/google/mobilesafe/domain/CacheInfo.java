package com.google.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class CacheInfo implements Comparable<CacheInfo>{
	public Drawable icon;
	public long cacheSize;
	public String appName;
	public String appPackageName;

	@Override
	public int compareTo(CacheInfo another) {
		return (int) (another.cacheSize - this.cacheSize);
	}
	
	
}
