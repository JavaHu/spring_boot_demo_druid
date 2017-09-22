package com.springboot.demo.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public class CipherUtil {

	private static Key key;

	private static byte[] desIv = { 113, -127, (byte) 0xab, 040, -128, 026, 0x17, 81 };

	private static AlgorithmParameterSpec aps;

	static {
		try {
			DESKeySpec keySpec = new DESKeySpec("12345678".getBytes());
			aps = new IvParameterSpec(desIv);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
			key = factory.generateSecret(keySpec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 加密
	 * 
	 * @param s
	 * @return
	 */
	public static String encode(String s) {
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, aps);
			byte[] res = cipher.doFinal(s.getBytes());
			return HexBin.encode(res);
 		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public static String decode(String hex) {
		try {
 			byte[] b = HexBin.decode(hex);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, aps);
			byte[] res = cipher.doFinal(b);
			return new String(res);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String...args){
		System.out.println(CipherUtil.encode("gamehu"));
		System.out.println(CipherUtil.decode("D6D0D8FD11A930A7"));
	}
}
