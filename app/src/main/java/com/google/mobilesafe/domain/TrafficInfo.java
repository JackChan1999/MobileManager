package com.google.mobilesafe.domain;

import android.graphics.drawable.Drawable;
/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：MobileSafe
 * Package_Name：com.google.mobilesafe
 * Version：1.0
 * time：2016/2/15 22:32
 * des ：手机卫士
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
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
