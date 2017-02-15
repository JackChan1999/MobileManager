package com.google.mobilesafe.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import org.json.JSONArray;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
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
public class StringUtils {
	private final String TAG = "StringUtils";
	public final static String	UTF_8	= "utf-8";
	public static final String MPLUG86 = "+86";
	private static final String hex = "0123456789ABCDEF";
	private static final String encoding = "UTF-8";
	private static final String phoneFiled86_1 = "+86";
	private static final String phoneFiled86_2 = "\\+86";
	private static final String phoneFiled86_3 = "0086";
	private static final String phoneFiled86_4 = "86";
	private static final String phoneFiled17951 = "17951";
	private static final String phoneFiled12593 = "12593";
	public static final String phoneFiled10193 = "10193";
	public static final String phoneFiled12520 = "12520";
	public static final String phoneFiled17908 = "17908";
	public static final String phoneFiled17909 = "17909";
	public static final String phoneFiled17911 = "17911";
	public static final String phoneFiled179110 = "179110";

	public static byte[] MD5(byte[] bArr) {
		MessageDigest instance;
		try {
			instance = MessageDigest.getInstance("MD5");
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage());
			instance = null;
		}
		if (instance == null) {
			return null;
		}
		instance.update(bArr);
		return instance.digest();
	}

	public static List<String> arryToList(JSONArray jSONArray) {
		if (jSONArray != null) {
			try {
				if (jSONArray.length() > 0) {
					List<String> arrayList = new ArrayList();
					for (int i = 0; i < jSONArray.length(); i++) {
						arrayList.add(jSONArray.get(i).toString());
					}
					return arrayList;
				}
			} catch (Throwable e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return null;
	}

	public static byte[] compressGZip(byte[] bArr) {
		byte[] toByteArray;
		Throwable th;
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
			gZIPOutputStream.write(bArr);
			gZIPOutputStream.finish();
			gZIPOutputStream.close();
			toByteArray = byteArrayOutputStream.toByteArray();
			try {
				byteArrayOutputStream.close();
			} catch (Throwable th2) {
				th = th2;
				Log.e(TAG, th2.getMessage());
				return toByteArray;
			}
		} catch (Throwable th3) {
			Throwable th4 = th3;
			toByteArray = null;
			th = th4;
			Log.e(TAG, th3.getMessage());
			return toByteArray;
		}
		return toByteArray;
	}

	public static String decode(String str) {
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream(str.length() / 2);
			int i = 0;
			while (i < str.length()) {
				byteArrayOutputStream.write((hex.indexOf(str.charAt(i)) << 4) | hex.indexOf(str.charAt(i + 1)));
				i += 2;
			}
			String code = new String(byteArrayOutputStream.toByteArray(), encoding);
			return code;
		}catch (UnsupportedEncodingException e1){
			e1.printStackTrace();
			return null;
		}finally {
			try {
				byteArrayOutputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static String encode(String str) {
		try {
			byte[] bytes = str.getBytes(encoding);
			StringBuilder stringBuilder = new StringBuilder(bytes.length << 1);
			for (int i = 0; i < bytes.length; i++) {
				stringBuilder.append(hex.charAt((bytes[i] & 240) >> 4));
				stringBuilder.append(hex.charAt(bytes[i] & 15));
			}
			return stringBuilder.toString();
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
	}

	public static String getFileMD5(String str) {
		byte[] bArr = new byte[1024];
		try {
			InputStream fileInputStream = new FileInputStream(str);
			MessageDigest instance = MessageDigest.getInstance("MD5");
			while (true) {
				int read = fileInputStream.read(bArr);
				if (read <= 0) {
					fileInputStream.close();
					return getMD5(instance.digest());
				}
				instance.update(bArr, 0, read);
			}
		} catch (Throwable th) {
			return null;
		}
	}

	public static long getLongByString(String str) {
		long j = -1;
		try {
			if (!isNull(str)) {
				j = Long.parseLong(str);
			}
		} catch (Throwable th) {
			Log.e(TAG, th.getMessage());
		}
		return j;
	}

	public static String getMD5(String str) {
		return getMD5(str.getBytes());
	}

	public static String getMD5(byte[] bArr) {
		return bytesToHexString(MD5(bArr));
	}

	public static String getNoNullString(String str) {
		return str == null ? "" : str.trim();
	}

	public static String getPhoneNumberNo86(String str) {
		if (isNull(str)) {
			return str;
		}
		str = str.replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
		return str.startsWith("+86") ? str.replaceFirst(phoneFiled86_2, "") : str.startsWith(phoneFiled86_3) ? str.replaceFirst(phoneFiled86_3, "") : str.startsWith(phoneFiled86_4) ? str.replaceFirst(phoneFiled86_4, "") : (!str.startsWith(phoneFiled17951) || str.length() <= 10) ? (!str.startsWith(phoneFiled12593) || str.length() <= 10) ? (!str.startsWith(phoneFiled12520) || str.length() <= 10) ? str : str.replaceFirst(phoneFiled12520, "") : str.replaceFirst(phoneFiled12593, "") : str.replaceFirst(phoneFiled17951, "");
	}

	public static String getSubString(String str) {
		return (isNull(str) || str.length() < 7) ? str : str.substring(0, 7);
	}

	public static String getTwoDigitType(String str) {
		return isNull(str) ? "" : str.length() < 2 ? "0" + str : str.length() > 2 ? "99" : str;
	}

	public static String getValueByKey(Map<String, String> map, String str) {
		return (map == null || map.isEmpty() || isNull(str)) ? "" : (String) map.get(str);
	}

	public static String handlerAssemble2(String str) {
		int i = 0;
		try {
			byte[] bytes = str.getBytes(encoding);
			int length = bytes.length;
			int i2 = 0;
			while (i < length) {
				bytes[i2] = Byte.valueOf(new StringBuilder(String.valueOf(bytes[i] - ((i2 + 1) % 3))).toString()).byteValue();
				i2++;
				i++;
			}
			return new String(bytes);
		} catch (Throwable th) {
			Log.e(TAG, th.getMessage());
			return "";
		}
	}

	public static boolean isNull(String str) {
		return str == null || str.trim().length() == 0 || str.trim().equalsIgnoreCase("null");
	}

	public static boolean isNull2(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean isNumber(String str) {
		if (isNull(str)) {
			return false;
		}
		return Pattern.compile("[0-9]*").matcher(getPhoneNumberNo86(str)).matches();
	}

	public static boolean isPhoneNumber(String str) {
		return isNull(str) ? false : sj(getPhoneNumberNo86(str));
	}

	public static String[] jsonArryToArray(JSONArray jSONArray) {
		if (jSONArray == null || jSONArray.length() <= 0) {
			return null;
		}
		String[] strArr = new String[jSONArray.length()];
		for (int i = 0; i < jSONArray.length(); i++) {
			try {
				strArr[i] = jSONArray.getString(i);
			} catch (Throwable e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return strArr;
	}

	public static String replaceBlank(String str) {
		return isNull(str) ? null : str.replaceAll("\\s", "");
	}

	public static boolean sj(String str) {
		return (str == null || str.length() != 11 || "13800138000".equals(str)) ? false : str.startsWith("13") || str.startsWith("14") || str.startsWith("15") || str.startsWith("18");
	}

	public static Document stringConvertXML(String str, String str2) {
		ByteArrayInputStream byteArrayInputStream = null;
		Document document = null;
		if (!isNull(str)) {
			try {
				if (str.indexOf("?>") != -1) {
					str = str.substring(str.indexOf("?>") + 2);
				}
				StringBuilder stringBuilder = new StringBuilder(str2);
				stringBuilder.append(str);
				DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
				byteArrayInputStream = new ByteArrayInputStream(stringBuilder.toString().getBytes("utf-8"));
				document = newInstance.newDocumentBuilder().parse(byteArrayInputStream);
				return document;
			} catch (Throwable th5) {
				IOUtils.closeQuietly(byteArrayInputStream);
			}
		}
		return document;
	}

	public static byte[] uncompressGZip(byte[] bArr) {
		ByteArrayInputStream byteArrayInputStream = null;
		GZIPInputStream gZIPInputStream = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(bArr);
			gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
			byte[] bArr2 = new byte[1024];
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			while (true) {
				int read = gZIPInputStream.read(bArr2, 0, 1024);
				if (read == -1) {
					bArr2 = byteArrayOutputStream.toByteArray();
					byteArrayOutputStream.flush();
					byteArrayOutputStream.close();
					gZIPInputStream.close();
					byteArrayInputStream.close();
					return bArr2;
				}
				byteArrayOutputStream.write(bArr2, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**
	 * 返回一个高亮spannable
	 * 
	 * @param content
	 *            文本内容
	 * @param color
	 *            高亮颜色
	 * @param start
	 *            起始位置
	 * @param end
	 *            结束位置
	 * @return 高亮spannable
	 */
	public static CharSequence getHighLightText(String content, int color, int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 获取链接样式的字符串，即字符串下面有下划线
	 * 
	 * @param resId
	 *            文字资源
	 * @return 返回链接样式的字符串
	 */
	public static Spanned getHtmlStyleString(int resId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<StorageVolume href=\"\"><u><b>").append(UIUtils.getString(resId)).append(" </b></u></StorageVolume>");
		return Html.fromHtml(sb.toString());
	}

	/** 格式化文件大小，不保留末尾的0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** 格式化文件大小，保留末尾的0，达到长度一致 */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)，保留一位小数
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)，个位四舍五入
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)，保留两位小数
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)，保留一位小数
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)，个位四舍五入
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
		}
		return size;
	}

	/**MD5加密url等字符串*/
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	/**字节数组转成16进制字符串*/
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/*public static String bytesToHexString(byte[] bArr) {
		int i = 0;
		if (bArr == null) {
			return null;
		}
		String str = "0123456789abcdef";
		char[] cArr = new char[(bArr.length * 2)];
		int i2 = 0;
		while (i2 < bArr.length) {
			cArr[i] = str.charAt((bArr[i2] >> 4) & 15);
			i++;
			cArr[i] = str.charAt(bArr[i2] & 15);
			i2++;
			i++;
		}
		return String.valueOf(cArr);
	}*/
}
