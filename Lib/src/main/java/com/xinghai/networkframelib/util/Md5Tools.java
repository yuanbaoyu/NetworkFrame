package com.xinghai.networkframelib.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Md5Tools
{

	private Md5Tools()
	{
	}

	public static String toMd5(byte abyte0[], boolean flag)
	{
		String res = "";
		
		try{
			MessageDigest messagedigest;
			messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.reset();
			messagedigest.update(abyte0);
			res = toHexString(messagedigest.digest(), "", flag);
			
		}catch(NoSuchAlgorithmException e){
			
		}finally{
			
		}
		
		return res;
	}

	public static String toHexString(byte abyte0[], String s, boolean flag)
	{
		StringBuilder stringbuilder = new StringBuilder();
		byte abyte1[] = abyte0;
		int i = abyte1.length;
		for (int j = 0; j < i; j++)
		{
			byte byte0 = abyte1[j];
			String s1 = Integer.toHexString(0xff & byte0);
			if (flag)
				s1 = s1.toUpperCase();
			if (s1.length() == 1)
				stringbuilder.append("0");
			stringbuilder.append(s1).append(s);
		}

		return stringbuilder.toString();
	}
}