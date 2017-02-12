package com.google.mobilesafe.utils;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-2-21 下午6:09:13
 * 
 * 描 述 ：
 * 		加密解密工具
 * 
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class Crypto {
	/**
	 * Encrypt plain string and encode to Base64
	 * 
	 * @param seed
	 * @param plain
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String seed, String plain) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] encrypted = encrypt(rawKey, plain.getBytes());
		return Base64.encodeToString(encrypted, Base64.DEFAULT);
	}

	/**
	 * Decrypt Base64 encoded encrypted string
	 * 
	 * @param seed
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String seed, String encrypted)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = Base64.decode(encrypted.getBytes(), Base64.DEFAULT);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(seed);
		keygen.init(128, random); // 192 and 256 bits may not be available
		SecretKey key = keygen.generateKey();
		byte[] raw = key.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] plain) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] encrypted = cipher.doFinal(plain);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted)
			throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}
}