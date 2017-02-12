package com.google.mobilesafe.domain;

public class ContactInfo1 implements Comparable<ContactInfo1> {
	public String name;
	public String pinyin;
	public String phone;
	public long id;

	@Override
	public int compareTo(ContactInfo1 another) {
		return this.pinyin.compareTo(another.pinyin);
	}

}
