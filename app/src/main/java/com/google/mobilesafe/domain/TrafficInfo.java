package com.google.mobilesafe.domain;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer.TrackInfo;

public class TrafficInfo implements Comparable<TrafficInfo> {

	public String appname;
	/**
	 * 下载流量
	 */
	public long rcv;
	/**
	 * 上传流量
	 */
	public long snd;

	public Drawable icon;

	@Override
	public int hashCode() {

		return super.hashCode();
	}

	@Override
	public int compareTo(TrafficInfo another) {
		return (int) ((another.snd + another.rcv) - (this.rcv + this.snd));
	}

}
