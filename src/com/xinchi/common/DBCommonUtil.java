package com.xinchi.common;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;


public class DBCommonUtil {

	public static final String STRING_ALL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	
	/**
	 * 生成主键20位
	 */
	public static String genPk(){
//		String pk=UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		byte[] bytes = new byte[16];
		for (int i=0; i<bytes.length; i++) {
			String sub = uuid.substring(2*i, 2*i+1);
			bytes[i] = (byte) (128 - Integer.parseInt(sub, 16));
		}
		return Base64.encodeBase64URLSafeString(bytes);
	}

	public static String[] genPks(int num) {
		String[] pks = new String[num];
		for(int i=0;i<pks.length;i++){
			pks[i] =genPk();
		}
		return pks;
	}
	
	public static void main(String[] args) {
		System.out.println(genPk());
	}
	
}
